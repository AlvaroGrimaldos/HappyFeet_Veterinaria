package com.happyfeet.model.entities.creator;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.Vacuna;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VacunaCreator extends InventarioCreator{

    @Override
    public Inventario createInventario(String nombreProducto, Integer productoTipoId, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        return new Vacuna(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo,cantidadStock, fechaVencimiento, precioVenta);
    }
}
