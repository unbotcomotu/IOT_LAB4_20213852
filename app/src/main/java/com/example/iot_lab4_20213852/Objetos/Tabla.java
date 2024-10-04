package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;
import java.util.List;

public class Tabla implements Serializable {
    private List<Equipo>table;

    public List<Equipo> getTable() {
        return table;
    }

    public void setTable(List<Equipo> table) {
        this.table = table;
    }
}
