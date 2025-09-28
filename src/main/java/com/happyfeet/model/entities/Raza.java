// Raza.java
package com.happyfeet.model.entities;

public class Raza {
    private Integer id;
    private Integer especieId;
    private String nombre;
    private String nombreEspecie; // Para joins

    public Raza(Integer especieId, String nombre) {
        this.especieId = especieId;
        this.nombre = nombre;
    }

    public Raza(Integer id, Integer especieId, String nombre) {
        this.id = id;
        this.especieId = especieId;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEspecieId() {
        return especieId;
    }

    public void setEspecieId(Integer especieId) {
        this.especieId = especieId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    @Override
    public String toString() {
        return "Raza{" +
                "id=" + id +
                ", especieId=" + especieId +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

