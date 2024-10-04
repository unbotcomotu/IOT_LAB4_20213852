package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;
import java.util.List;

public class Temporadas implements Serializable {
    private List<Temporada> seasons;

    public List<Temporada> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Temporada> seasons) {
        this.seasons = seasons;
    }
}
