package com.example.iot_lab4_20213852.Ligas;

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
import com.example.iot_lab4_20213852.Objetos.Liga;
import com.example.iot_lab4_20213852.Objetos.Ligas;
import com.example.iot_lab4_20213852.R;
import com.example.iot_lab4_20213852.ViewModels.DataViewModel;
import com.example.iot_lab4_20213852.databinding.FragmentLigasBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LigasFragment extends Fragment {
    RecyclerViewLigasAdapter adapter;
    TheSportsDBService service =new Retrofit.Builder()
            .baseUrl("https://www.thesportsdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheSportsDBService.class);

    FragmentLigasBinding b;
    List<Liga> ligas;
    DataViewModel vm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b= FragmentLigasBinding.inflate(inflater,container,false);
        vm=new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        if(vm.getLigas().getValue()==null){
            ligas=new ArrayList<>();
            vm.getLigas().setValue(ligas);
        }else {
            ligas=vm.getLigas().getValue();
        }
        b.rvLigas.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter=new RecyclerViewLigasAdapter(ligas);
        b.rvLigas.setAdapter(adapter);

        b.botonBuscar.setOnClickListener(view -> {
            if(b.inputBusqueda.getText()!=null&&!b.inputBusqueda.getText().toString().isBlank()){
                listarLigasPorPais(b.inputBusqueda.getText().toString());
            }else {
                listarLigas();
            }
        });


        return b.getRoot();
    }

    private void listarLigas(){
        service.listarLigas().enqueue(new Callback<Ligas>() {
            @Override
            public void onResponse(Call<Ligas> call, Response<Ligas> response) {
                if(response.isSuccessful()){
                    if(response.body().getLeagues()!=null){
                        ligas.clear();
                        ligas.addAll(response.body().getLeagues());
                        vm.getLigas().setValue(ligas);
                        adapter.notifyDataSetChanged();
                    }else {
                        lanzarToast(2);
                    }
                }else{
                    lanzarToast(1);
                }
            }
            @Override
            public void onFailure(Call<Ligas> call, Throwable t) {
                Log.d("aiudaaa",t.getMessage());
                lanzarToast(1);
            }
        });
    }

    private void listarLigasPorPais(String pais){
        service.listarLigasPorPais(pais).enqueue(new Callback<Ligas>() {
            @Override
            public void onResponse(Call<Ligas> call, Response<Ligas> response) {
                if(response.isSuccessful()){
                    if(response.body().getCountries()!=null){
                        ligas.clear();
                        ligas.addAll(response.body().getCountries());
                        adapter.notifyDataSetChanged();
                    }else {
                        lanzarToast(0);
                    }
                }else if(response.code()==400){
                    lanzarToast(0);
                }else {
                    lanzarToast(1);
                }
            }
            @Override
            public void onFailure(Call<Ligas> call, Throwable t) {
                Log.d("aiudaaa",t.getMessage());
                lanzarToast(1);
            }
        });
    }

    private void lanzarToast(Integer tipo){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());

        switch (tipo){
            case 0:
                Toast.makeText(getContext(),"El país ingresado como parámetro no produjo coincidencias en la búsqueda",Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(getContext(),"Error al realizar la búsqueda",Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(getContext(),"No se encontró ningún resultado",Toast.LENGTH_LONG).show();
                break;
            case 3:
            case 4:
            default:
                Toast.makeText(getContext(),"Error al realizar la búsqueda",Toast.LENGTH_LONG).show();
                break;

        }


    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("ligas",(Serializable) ligas);
        super.onSaveInstanceState(outState);
    }
}