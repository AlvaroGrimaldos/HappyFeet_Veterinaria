package com.happyfeet.model;

import java.util.List;

public interface IProductoTipoDAO {
    void agregarProductoTipo(ProductoTipo productoTipo);
    List<ProductoTipo> listarTodos();
    ProductoTipo buscarPorId(Integer id);
    void actualizarProductoTipo(ProductoTipo productoTipo);
    void eliminarProductoTipo(Integer id);
}
