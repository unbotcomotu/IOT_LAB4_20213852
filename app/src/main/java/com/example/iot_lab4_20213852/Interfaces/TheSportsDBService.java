package com.example.iot_lab4_20213852.Interfaces;

import com.example.iot_lab4_20213852.Objetos.Eventos;
import com.example.iot_lab4_20213852.Objetos.Ligas;
import com.example.iot_lab4_20213852.Objetos.Tabla;
import com.example.iot_lab4_20213852.Objetos.Temporadas;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheSportsDBService {
    @GET("/api/v1/json/3/all_leagues.php")
    Call<Ligas>listarLigas();

    @GET("/api/v1/json/3/search_all_leagues.php")
    Call<Ligas>listarLigasPorPais(@Query("c")String country);

    @GET("/api/v1/json/3/lookuptable.php")
    Call<Tabla>listarPosiciones(@Query("l")String idLiga, @Query("s")String temporada);

    @GET("/api/v1/json/3/search_all_seasons.php")
    Call<Temporadas>listarTemporadasPorIdLiga(@Query("id")String idLigaStr);

    @GET("/api/v1/json/3/eventsround.php")
    Call<Eventos>listarEventos(@Query("id")String idLigaStr, @Query("r")String rondaStr, @Query("s")String temporada);
}
