package com.happyfeet.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Vacuna extends Inventario{

    public Vacuna(String nombreProducto, Integer productoTipoId, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        super(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
    }

    @Override
    public String getTipo() {
        return "Vacuna";
    }
}
