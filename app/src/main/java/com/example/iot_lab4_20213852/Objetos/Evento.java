package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;

public class Evento implements Serializable {
    private String strEvent;
    private Integer intRound;
    private String strHomeTeam;
    private String strAwayTeam;
    private Integer intHomeScore;
    private Integer intAwayScore;
    private String strLeagueBadge;
    private String dateEvent;
    private Integer intSpectators;
    private Integer posicion;

    public String getStrEvent() {
        return strEvent;
    }

    public void setStrEvent(String strEvent) {
        this.strEvent = strEvent;
    }

    public Integer getIntRound() {
        return intRound;
    }

    public void setIntRound(Integer intRound) {
        this.intRound = intRound;
    }

    public String getStrHomeTeam() {
        return strHomeTeam;
    }

    public void setStrHomeTeam(String strHomeTeam) {
        this.strHomeTeam = strHomeTeam;
    }

    public String getStrAwayTeam() {
        return strAwayTeam;
    }

    public void setStrAwayTeam(String strAwayTeam) {
        this.strAwayTeam = strAwayTeam;
    }

    public Integer getIntHomeScore() {
        return intHomeScore;
    }

    public void setIntHomeScore(Integer intHomeScore) {
        this.intHomeScore = intHomeScore;
    }

    public Integer getIntAwayScore() {
        return intAwayScore;
    }

    public void setIntAwayScore(Integer intAwayScore) {
        this.intAwayScore = intAwayScore;
    }

    public String getStrLeagueBadge() {
        return strLeagueBadge;
    }

    public void setStrLeagueBadge(String strLeagueBadge) {
        this.strLeagueBadge = strLeagueBadge;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Integer getIntSpectators() {
        return intSpectators;
    }

    public void setIntSpectators(Integer intSpectators) {
        this.intSpectators = intSpectators;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
}
