package com.example.iot_lab4_20213852.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iot_lab4_20213852.Objetos.Equipo;
import com.example.iot_lab4_20213852.Objetos.Evento;
import com.example.iot_lab4_20213852.Objetos.Liga;
import com.example.iot_lab4_20213852.Objetos.Ligas;

import java.util.List;

public class DataViewModel extends ViewModel {
    private final MutableLiveData<List<Evento>>eventos=new MutableLiveData<>();
    private final MutableLiveData<List<Liga>>ligas=new MutableLiveData<>();
    private final MutableLiveData<List<Equipo>>equipos=new MutableLiveData<>();
    private final MutableLiveData<Integer>contador=new MutableLiveData<>();

    public MutableLiveData<List<Evento>> getEventos() {
        return eventos;
    }

    public MutableLiveData<List<Liga>> getLigas() {
        return ligas;
    }

    public MutableLiveData<List<Equipo>> getEquipos() {
        return equipos;
    }

    public MutableLiveData<Integer> getContador() {
        return contador;
    }
}
