package com.happyfeet.model.entities;

public class EventoTipo {
    private Integer id;
    private String nombre;

    public EventoTipo(String nombre) {
        this.nombre = nombre;
    }

    public EventoTipo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "EventoTipo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}