package com.happyfeet.repository;

import com.happyfeet.model.entities.Inventario;

import java.time.LocalDate;
import java.util.List;

public interface IInventarioDAO {
    void agregarInventario(Inventario inventario);
    List<Inventario> listarTodos();
    Inventario buscarPorId(Integer id);
    void actualizarInventario(Inventario inventario);
    void eliminarInventario(Integer id);
    void actualizarStockVenta(Inventario i, int cantidadVendida);
    void agregarStock(Inventario i, int cantidad, LocalDate fecha);
}
