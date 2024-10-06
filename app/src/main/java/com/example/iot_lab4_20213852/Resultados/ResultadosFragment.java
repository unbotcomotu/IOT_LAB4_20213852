package com.example.iot_lab4_20213852.Resultados;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.iot_lab4_20213852.Interfaces.TheSportsDBService;
import com.example.iot_lab4_20213852.Objetos.Equipo;
import com.example.iot_lab4_20213852.Objetos.Evento;
import com.example.iot_lab4_20213852.Objetos.Eventos;
import com.example.iot_lab4_20213852.Objetos.Liga;
import com.example.iot_lab4_20213852.Objetos.Ligas;
import com.example.iot_lab4_20213852.Objetos.Tabla;
import com.example.iot_lab4_20213852.Objetos.Temporada;
import com.example.iot_lab4_20213852.Objetos.Temporadas;
import com.example.iot_lab4_20213852.Posiciones.RecyclerViewPosicionesAdapter;
import com.example.iot_lab4_20213852.R;
import com.example.iot_lab4_20213852.ViewModels.DataViewModel;
import com.example.iot_lab4_20213852.databinding.FragmentResultadosBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultadosFragment extends Fragment implements SensorEventListener {
    FragmentResultadosBinding b;
    RecyclerViewResultadosAdapter adapter;
    TheSportsDBService service =new Retrofit.Builder()
            .baseUrl("https://www.thesportsdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheSportsDBService.class);

    List<Evento> eventos;
    Integer contador;
    SensorManager sensorManager;
    Boolean dialogAbierto=false;
    DataViewModel vm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b=FragmentResultadosBinding.inflate(inflater,container,false);
        vm=new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        if(vm.getEventos().getValue()==null){
            eventos=new ArrayList<>();
            contador=0;
            vm.getEventos().setValue(eventos);
            vm.getContador().setValue(contador);
        }else {
            eventos=vm.getEventos().getValue();
            contador=vm.getContador().getValue();
        }
        b.rvResultados.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter=new RecyclerViewResultadosAdapter(eventos,getContext());
        b.rvResultados.setAdapter(adapter);

        b.botonBuscar.setOnClickListener(view -> {
            Boolean idLigaIngresada=b.inputIdLiga.getText()!=null&&!b.inputIdLiga.getText().toString().isBlank();
            Boolean temporadaIngresada=b.inputSeason.getText()!=null&&!b.inputSeason.getText().toString().isBlank();
            Boolean rondaIngresada=b.inputRonda.getText()!=null&&!b.inputRonda.getText().toString().isBlank();
            if(idLigaIngresada&&temporadaIngresada&&rondaIngresada){
                String idLiga=b.inputIdLiga.getText().toString();
                String temporada=b.inputSeason.getText().toString();
                String ronda=b.inputRonda.getText().toString();
                if(verificarNumeroValido(idLiga)&&verificarTemporadaValida(temporada)&&verificarNumeroValido(ronda)){
                    listarResultados(idLiga,temporada,ronda);
                }else if(!verificarNumeroValido(idLiga)&&!verificarTemporadaValida(temporada)&&!verificarNumeroValido(ronda)){
                    lanzarToast(5);
                }else if(!verificarNumeroValido(ronda)&&!verificarTemporadaValida(temporada)){
                    lanzarToast(6);
                }else if(!verificarNumeroValido(ronda)&&!verificarNumeroValido(idLiga)){
                    lanzarToast(7);
                }else if(!verificarNumeroValido(idLiga)&&!verificarTemporadaValida(temporada)){
                    lanzarToast(9);
                }else if(!verificarNumeroValido(ronda)){
                    lanzarToast(8);
                }else if(!verificarNumeroValido(idLiga)){
                    lanzarToast(1);
                }else if(!verificarTemporadaValida(temporada)){
                    lanzarToast(3);
                }
            }else if(!idLigaIngresada&&!temporadaIngresada&&!rondaIngresada){
                lanzarToast(16);
            }else if(!idLigaIngresada&&!rondaIngresada){
                lanzarToast(14);
            }else if(!rondaIngresada&&!temporadaIngresada){
                lanzarToast(15);
            }else if(!idLigaIngresada&&!temporadaIngresada){
                lanzarToast(10);
            }else if(!idLigaIngresada){
                lanzarToast(11);
            }else if(!temporadaIngresada){
                lanzarToast(12);
            }else {
                lanzarToast(13);
            }
        });
        if(getContext()!=null){
            sensorManager=(SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        }else {
            if(!dialogAbierto){
                lanzarDialog(19);
            }
        }
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sensorManager!=null){
            Sensor mAcc=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if(mAcc!=null){
                sensorManager.registerListener(this,mAcc,SensorManager.SENSOR_DELAY_UI);
            }else {
                if(!dialogAbierto){
                    lanzarDialog(19);
                }
            }
        }else {
            if(!dialogAbierto){
                lanzarDialog(19);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }

    private void listarResultados(String idLiga, String temporada, String ronda){
        service.listarEventos(idLiga,ronda,temporada).enqueue(new Callback<Eventos>() {
            @Override
            public void onResponse(Call<Eventos> call, Response<Eventos> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null&&response.body().getEvents()!=null){
                        List<Evento>nuevaLista=new ArrayList<>();
                        for(Evento evento:response.body().getEvents()){
                            evento.setPosicion(contador);
                            nuevaLista.add(evento);
                        }
                        eventos.addAll(0,nuevaLista);
                        adapter.notifyDataSetChanged();
                        vm.getEventos().setValue(eventos);
                        contador++;
                        vm.getContador().setValue(contador);
                    }else {
                        verificarExistenciaParametros(idLiga,temporada);
                    }
                }else{
                    verificarExistenciaParametros(idLiga,temporada);
                }
            }
            @Override
            public void onFailure(Call<Eventos> call, Throwable t) {
                Log.d("aiudaaa",t.getMessage());
                verificarExistenciaParametros(idLiga,temporada);
            }
        });
    }

    private void verificarExistenciaParametros(String idLiga,String temporada){
        service.listarLigas().enqueue(new Callback<Ligas>() {
            @Override
            public void onResponse(Call<Ligas> call, Response<Ligas> response) {
                List<Liga> ligas=response.body().getLeagues();
                Boolean idExiste=false;
                for(Liga liga:ligas){
                    if(liga.getIdLeague().toString().equals(idLiga.trim())){
                        idExiste=true;
                    }
                }
                if(!idExiste){
                    if(!dialogAbierto){
                        lanzarToast(2);
                    }
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
                                if(!dialogAbierto){
                                    lanzarToast(4);
                                }
                            }else {
                                if(!dialogAbierto){
                                    lanzarToast(0);
                                }
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

        return verificarNumeroValido(ano1Str)&&verificarNumeroValido(ano2Str)&&(Integer.parseInt(ano2Str)-Integer.parseInt(ano1Str)==1)&&Integer.parseInt(ano1Str)>1999&&Integer.parseInt(ano2Str)>2000;
    }

    private void lanzarDialog(Integer tipo){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogAbierto=true;
        switch (tipo){
            case 0:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un valor de ronda que exista para la temporada y el id de liga ingresado")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 1:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un ID numérico")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 2:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un ID que exista dentro de una liga")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 3:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese una temporada correcta (\"<año>-<siguiente_año>\").")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 4:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese una temporada que exista para el valor de id de liga ingresado")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 5:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un ID numérico, una ronda numérica y una temporada correcta (\"<año>-<siguiente_año>\")")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 6:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese una ronda numérica y una temporada correcta (\"<año>-<siguiente_año>\")")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 7:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un ID numérico y una ronda numérica")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 8:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese una ronda numérica")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 9:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Ingrese un ID numérico y una temporada correcta (\"<año>-<siguiente_año>\")")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 10:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga y una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 11:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 12:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 13:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese una ronda")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 14:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga y una ronda")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 15:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese una ronda y una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 16:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga, una ronda y una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 17:
                dialogBuilder.setTitle("Eliminar últimos resultados")
                        .setMessage("¿Desea eliminar los resultados de la última búsqueda realizada?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            eliminarUltimosResultados();
                            dialogAbierto=false;
                        })
                        .setNegativeButton("Cancelar",(dialog,which)->{
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 18:
                dialogBuilder.setTitle("Parámetros vacíos")
                        .setMessage("Ingrese un id de liga, una ronda y una temporada")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            case 19:
                dialogBuilder.setTitle("Eliminar últimos resultados")
                        .setMessage("No existe elementos visibles")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
            default:
                dialogBuilder.setTitle("Error al realizar la búsqueda")
                        .setMessage("Inténtelo más tarde")
                        .setPositiveButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setNegativeButton("Entendido", (dialog, which) -> {
                            dialogAbierto=false;
                        })
                        .setIcon(R.drawable.iconwarning)
                        .show();
                break;
        }
    }

    private void eliminarUltimosResultados(){
        if(!eventos.isEmpty()){
            Integer ultimoIndice=eventos.get(0).getPosicion();
            for(int i=eventos.size()-1;i>=0;i--){
                if(eventos.get(i).getPosicion()==ultimoIndice){
                    eventos.remove(eventos.get(i));
                }
            }
            vm.getEventos().setValue(eventos);
            adapter.notifyDataSetChanged();
        }
    }

    private void lanzarToast(Integer tipo){

        switch (tipo){
            case 0:
                Toast.makeText(getContext(),"Ingrese una ronda que exista para la temporada y el id de liga ingresado",Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(getContext(),"Ingrese un ID numérico",Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(getContext(),"Ingrese un ID que exista dentro de una liga",Toast.LENGTH_LONG).show();
                                break;
            case 3:
                Toast.makeText(getContext(),"Ingrese una temporada correcta (\"<año>-<siguiente_año>\")",Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(getContext(),"Ingrese una temporada que exista para el valor de id de liga ingresado",Toast.LENGTH_LONG).show();
                break;
            case 5:
                Toast.makeText(getContext(),"Ingrese un ID numérico, una ronda numérica y una temporada correcta (\"<año>-<siguiente_año>\")",Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(getContext(),"Ingrese una ronda numérica y una temporada correcta (\"<año>-<siguiente_año>\")",Toast.LENGTH_LONG).show();
                break;
            case 7:
                Toast.makeText(getContext(),"Ingrese un ID numérico y una ronda numérica",Toast.LENGTH_LONG).show();
                break;
            case 8:
                Toast.makeText(getContext(),"Ingrese una ronda numérica",Toast.LENGTH_LONG).show();
                break;
            case 9:
                Toast.makeText(getContext(),"Ingrese un ID numérico y una temporada correcta (\"<año>-<siguiente_año>\")",Toast.LENGTH_LONG).show();
                break;
            case 10:
                Toast.makeText(getContext(),"Ingrese un id de liga y una temporada",Toast.LENGTH_LONG).show();
                break;
            case 11:
                Toast.makeText(getContext(),"Ingrese un id de liga",Toast.LENGTH_LONG).show();
                break;
            case 12:
                Toast.makeText(getContext(),"Ingrese una temporada",Toast.LENGTH_LONG).show();
                break;
            case 13:
                Toast.makeText(getContext(),"Ingrese una ronda",Toast.LENGTH_LONG).show();
                break;
            case 14:
                Toast.makeText(getContext(),"Ingrese un id de liga y una ronda",Toast.LENGTH_LONG).show();
                break;
            case 15:
                Toast.makeText(getContext(),"Ingrese una ronda y una temporada",Toast.LENGTH_LONG).show();
                break;
            case 16:
                Toast.makeText(getContext(),"Ingrese un id de liga, una ronda y una temporada",Toast.LENGTH_LONG).show();
                break;
            case 18:
                Toast.makeText(getContext(),"Ingrese un id de liga, una ronda y una temporada",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(),"Error al realizar la búsqueda",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Integer sensorType=sensorEvent.sensor.getType();
        if(sensorType==Sensor.TYPE_ACCELEROMETER){
            Float xAcc=sensorEvent.values[0];
            Float yAcc=sensorEvent.values[1];
            Float zAcc=sensorEvent.values[2];
            if(Math.sqrt(xAcc*xAcc+yAcc*yAcc+zAcc*zAcc)>20){
                if(eventos.isEmpty()){
                    if(!dialogAbierto){
                        lanzarDialog(19);
                    }
                }else {
                    if(!dialogAbierto){
                        lanzarDialog(17);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}