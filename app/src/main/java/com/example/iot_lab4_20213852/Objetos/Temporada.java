package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;

public class Temporada implements Serializable {
    private String strSeason;

    public String getStrSeason() {
        return strSeason;
    }

    public void setStrSeason(String strSeason) {
        this.strSeason = strSeason;
    }
}
