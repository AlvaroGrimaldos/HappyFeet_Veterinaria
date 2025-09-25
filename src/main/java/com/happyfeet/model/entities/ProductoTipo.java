package com.happyfeet.model.entities;

public class ProductoTipo {
    private Integer id;
    private String nombre;

    public ProductoTipo(String nombre) {
        this.nombre = nombre;
    }

    public ProductoTipo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Producto Tipo {" +
                "id = " + id +
                ", nombre = " + nombre +
                "}";
    }
}
