package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;

public class Equipo implements Serializable {
    private String strTeam;
    private Integer intRank;
    private String strBadge;
    private Integer intWin;
    private Integer intLoss;
    private Integer intDraw;
    private Integer intGoalsFor;
    private Integer intGoalsAgainst;
    private Integer intGoalDifference;

    public String getStrTeam() {
        return strTeam;
    }

    public void setStrTeam(String strTeam) {
        this.strTeam = strTeam;
    }

    public Integer getIntRank() {
        return intRank;
    }

    public void setIntRank(Integer intRank) {
        this.intRank = intRank;
    }

    public String getStrBadge() {
        return strBadge;
    }

    public void setStrBadge(String strBadge) {
        this.strBadge = strBadge;
    }

    public Integer getIntWin() {
        return intWin;
    }

    public void setIntWin(Integer intWin) {
        this.intWin = intWin;
    }

    public Integer getIntLoss() {
        return intLoss;
    }

    public void setIntLoss(Integer intLoss) {
        this.intLoss = intLoss;
    }

    public Integer getIntDraw() {
        return intDraw;
    }

    public void setIntDraw(Integer intDraw) {
        this.intDraw = intDraw;
    }

    public Integer getIntGoalsFor() {
        return intGoalsFor;
    }

    public void setIntGoalsFor(Integer intGoalsFor) {
        this.intGoalsFor = intGoalsFor;
    }

    public Integer getIntGoalsAgainst() {
        return intGoalsAgainst;
    }

    public void setIntGoalsAgainst(Integer intGoalsAgainst) {
        this.intGoalsAgainst = intGoalsAgainst;
    }

    public Integer getIntGoalDifference() {
        return intGoalDifference;
    }

    public void setIntGoalDifference(Integer intGoalsDifference) {
        this.intGoalDifference = intGoalsDifference;
    }
}
