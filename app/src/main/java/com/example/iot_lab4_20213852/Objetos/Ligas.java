package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;
import java.util.List;

public class Ligas implements Serializable {
    private List<Liga> leagues;
    private List<Liga> countries;

    public List<Liga> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<Liga> leagues) {
        this.leagues = leagues;
    }

    public List<Liga> getCountries() {
        return countries;
    }

    public void setCountries(List<Liga> countries) {
        this.countries = countries;
    }
}
