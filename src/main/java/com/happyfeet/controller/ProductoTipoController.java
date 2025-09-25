package com.happyfeet.controller;

import com.happyfeet.model.entities.ProductoTipo;
import com.happyfeet.repository.IProductoTipoDAO;

public class ProductoTipoController {
    private IProductoTipoDAO productoTipoDAO;

    public ProductoTipoController(IProductoTipoDAO productoTipoDAO) { this.productoTipoDAO = productoTipoDAO; }

    public void agregarProductoTipo(ProductoTipo productoTipo) {}
}
