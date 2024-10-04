package com.example.iot_lab4_20213852.Posiciones;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iot_lab4_20213852.Interfaces.TheSportsDBService;
import com.example.iot_lab4_20213852.Ligas.RecyclerViewLigasAdapter;
import com.example.iot_lab4_20213852.Objetos.Equipo;
import com.example.iot_lab4_20213852.Objetos.Liga;
import com.example.iot_lab4_20213852.Objetos.Ligas;
import com.example.iot_lab4_20213852.Objetos.Tabla;
import com.example.iot_lab4_20213852.Objetos.Temporada;
import com.example.iot_lab4_20213852.Objetos.Temporadas;
import com.example.iot_lab4_20213852.R;
import com.example.iot_lab4_20213852.ViewModels.DataViewModel;
import com.example.iot_lab4_20213852.databinding.FragmentPosicionesBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosicionesFragment extends Fragment {
    FragmentPosicionesBinding b;
    RecyclerViewPosicionesAdapter adapter;
    TheSportsDBService service =new Retrofit.Builder()
            .baseUrl("https://www.thesportsdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheSportsDBService.class);
    List<Equipo> equipos;
    DataViewModel vm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b= FragmentPosicionesBinding.inflate(inflater,container,false);
        vm=new ViewModelProvider(this).get(DataViewModel.class);
        if(vm.getEquipos().getValue()==null){
            equipos=new ArrayList<>();
            vm.getEquipos().setValue(equipos);
        }else {
            equipos=vm.getEquipos().getValue();
        }
        b.rvPosiciones.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter=new RecyclerViewPosicionesAdapter(equipos,getContext());
        b.rvPosiciones.setAdapter(adapter);

        b.botonBuscar.setOnClickListener(view -> {
            Boolean idLigaIngresada=b.inputIdLiga.getText()!=null&&!b.inputIdLiga.getText().toString().isBlank();
            Boolean temporadaIngresada=b.inputTemporada.getText()!=null&&!b.inputTemporada.getText().toString().isBlank();
            if(idLigaIngresada&&temporadaIngresada){
                String idLiga=b.inputIdLiga.getText().toString();
                String temporada=b.inputTemporada.getText().toString();
                if(verificarNumeroValido(idLiga)&&verificarTemporadaValida(temporada)){
                    listarPosiciones(idLiga,temporada);
                }else if(!verificarNumeroValido(idLiga)&&!verificarTemporadaValida(temporada)){
                    lanzarDialog(5);
                }else if(!verificarNumeroValido(idLiga)){
                    lanzarDialog(1);
                }else if(!verificarTemporadaValida(temporada)){
                    lanzarDialog(3);
                }

            }else if(!idLigaIngresada&&!temporadaIngresada){
                lanzarDialog(10);
            }else if(!idLigaIngresada){
                lanzarDialog(11);
            }else {
                lanzarDialog(12);
            }
        });

        return b.getRoot();
    }

    private void listarPosiciones(String idLiga,String temporada){
        service.listarPosiciones(idLiga,temporada).enqueue(new Callback<Tabla>() {
            @Override
            public void onResponse(Call<Tabla> call, Response<Tabla> response) {
                if(response.isSuccessful()){
                    if(response.body().getTable()!=null){
                        equipos.clear();
                        equipos.addAll(response.body().getTable());
                        vm.getEquipos().setValue(equipos);
                        adapter.notifyDataSetChanged();
                    }else {
                        verificarExistenciaParametros(idLiga,temporada);
                    }
                }else{
                    verificarExistenciaParametros(idLiga,temporada);
                }
            }
            @Override
            public void onFailure(Call<Tabla> call, Throwable t) {
                Log.d("aiudaaa",t.getMessage());
                verificarExistenciaParametros(idLiga,temporada);
            }
        });
    }

    private void verificarExistenciaParametros(String idLiga,String temporada){
        service.listarLigas().enqueue(new Callback<Ligas>() {
            @Override
            public void onResponse(Call<Ligas> call, Response<Ligas> response) {
                List<Liga>ligas=response.body().getLeagues();
                Boolean idExiste=false;
                for(Liga liga:ligas){
                    if(liga.getIdLeague().toString().equals(idLiga.trim())){
                        idExiste=true;
                    }
                }
                if(!idExiste){
                    lanzarDialog(2);
                }else {
                    service.listarTemporadasPorIdLiga(idLiga).enqueue(new Callback<Temporadas>() {
                        @Override
                        public void onResponse(Call<Temporadas> call, Response<Temporadas> response) {
                            List<Temporada>temporadas=response.body().getSeasons();
                            Boolean temporadaExiste=false;
                            for(Temporada temporadaPorId:temporadas){
                                if(temporadaPorId.getStrSeason().equals(temporada.trim())){
                                    temporadaExiste=true;
                                }
                            }
                            if(!temporadaExiste){
                                lanzarDialog(4);
                            }else {
                                lanzarDialog(0);
                            }
                        }
                        @Override
                        public void onFailure(Call<Temporadas> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<Ligas> call, Throwable t) {
            }
        });
    }

    private Boolean verificarNumeroValido(String numeroStr){
        try {
            Integer numero=Integer.parseInt(numeroStr);
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }

    private Boolean verificarTemporadaValida(String temporada){
        String[]anos=temporada.split("-");
        if(anos.length != 2){
            return false;
        }
        String ano1Str=anos[0];
        String ano2Str=anos[1];

        return verificarNumeroValido(ano1Str)&&verificarNumeroValido(ano2Str)&&(Integer.parseInt(ano2Str)-Integer.parseInt(ano1Str)==1);
    }

    private void lanzarDialog(Integer tipo){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());

        switch (tipo){
            case 0:
                dialogBuilder.setTitle("No se encontraron resultados")
                        .setMessage("No se encontraron resultados para los parámetros ingresados")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 1:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un parámetro de ID numérico")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 2:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un parámetro de ID que exista dentro de una liga")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 3:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un parámetro de temporada correcto. Este tiene que tener el formato \"<año>-<siguiente_año>\"")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 4:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese una temporada que exista para el valor de id de liga ingresado")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 5:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un parámetro de ID numérico y un parámetro de temporada correcto. Este último tiene que tener el formato \"<año>-<siguiente_año>\"")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 10:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga y una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 11:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 12:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            default:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Inténtelo más tarde")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                        })
                        .setNegativeButton("Entendido", (dialog, which) -> {
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("equipos",(Serializable) equipos);
        super.onSaveInstanceState(outState);
    }
}