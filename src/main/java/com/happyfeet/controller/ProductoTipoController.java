package com.happyfeet.controller;

import com.happyfeet.model.entities.ProductoTipo;
import com.happyfeet.repository.IProductoTipoDAO;
import com.happyfeet.repository.ProductoTipoDAO;
import org.apache.logging.log4j.LogManager;
import java.util.List;

import java.util.logging.Logger;

public class ProductoTipoController {
    private static final Logger logger = (Logger) LogManager.getLogger(ProductoTipoController.class);
    private IProductoTipoDAO productoTipoDAO;

    public ProductoTipoController(IProductoTipoDAO productoTipoDAO) { this.productoTipoDAO = productoTipoDAO; }

    public void agregarProductoTipo(ProductoTipo productoTipo) {
        if(validarProductoTipo(productoTipo)) {
            productoTipoDAO.agregarProductoTipo(productoTipo);
        } else {
            logger.info("Error al agregar Producto Tipo. Datos Invalidos");
        }
    }

    private boolean validarProductoTipo(ProductoTipo productoTipo) {
        if(productoTipo == null) return false;
        if(productoTipo.getNombre() == null || productoTipo.getNombre().isEmpty()) return false;
        return true;
    }

    public void listarTodos() {
        productoTipoDAO.listarTodos();
    }

    public void buscarPorId(Integer id) {
        if(id >= 0) {
            productoTipoDAO.buscarPorId(id);
        } else {
            logger.info("Error. Id no valido, ingrese un id positivo");
        }
    }

    public void actualizarProductoTipo(ProductoTipo productoTipo) {
        if(validarProductoTipo(productoTipo)){
            productoTipoDAO.actualizarProductoTipo(productoTipo);
        } else {
            logger.info("Error al actualizar el Producto Tipo. Datos invalidos");
        }
    }

    public void eliminarProductoTipo(Integer id){
        if(id >= 0) {
            productoTipoDAO.eliminarProductoTipo(id);
        }else {
            logger.info("Error al eliminar el Prodcuto Tipo. Id no valido");
        }
    }
}
