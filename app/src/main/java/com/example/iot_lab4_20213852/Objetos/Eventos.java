package com.example.iot_lab4_20213852.Objetos;

import java.io.Serializable;
import java.util.List;

public class Eventos implements Serializable {
    private List<Evento> events;

    public List<Evento> getEvents() {
        return events;
    }

    public void setEvents(List<Evento> events) {
        this.events = events;
    }
}
