package com.happyfeet.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Alimento extends Inventario{

    public Alimento(String nombre, Integer productoTipoId, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        super(nombre, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
    }
    @Override
    public String getTipo() {
        return "Alimento";
    }
}
