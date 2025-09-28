package com.happyfeet.model.entities.creator;

import com.happyfeet.model.entities.Inventario;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class InventarioCreator {
    public abstract Inventario createInventario(String nombreProducto, Integer productoTipoId, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fechaVencimiento, BigDecimal precioVenta);

    public Inventario registrarNuevoInventario(String nombreProducto, Integer productoTipoId, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        Inventario nuevoInventario = createInventario(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
        System.out.println("Se ha creado el inventario con los datos: " + nuevoInventario);
        return nuevoInventario;
    }
}
