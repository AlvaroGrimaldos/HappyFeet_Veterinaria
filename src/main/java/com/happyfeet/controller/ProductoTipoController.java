package com.happyfeet.controller;

import com.happyfeet.model.entities.ProductoTipo;
import com.happyfeet.repository.IProductoTipoDAO;
import com.happyfeet.repository.ProductoTipoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;



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
        List<ProductoTipo> productos = productoTipoDAO.listarTodos();
        if(productos.isEmpty()) {
            logger.info("No hay productos tipos reegistrados");
        }else {
            productos.stream().forEach(n -> System.out.println(n));
        }
    }

    public void buscarPorId(Integer id) {
        if(id > 0) {
            ProductoTipo producto = productoTipoDAO.buscarPorId(id);
            if(producto != null) {
                System.out.println("Producto tipo encontrado: " + producto);
            } else {
                logger.info("No se encontro un producto tipo con ID: {}", id);
            }

        } else {
            logger.info("Error. Id no valido, ingrese un id positivo");
        }
    }

    public void actualizarProductoTipo(ProductoTipo productoTipo) {
        if(productoTipo.getId() > 0 && !productoTipo.getNombre().isEmpty()){
            productoTipoDAO.actualizarProductoTipo(productoTipo);
            logger.info("Producto tipo actualizado correctamente");
        } else {
            logger.info("Error al actualizar el Producto Tipo. Datos invalidos");
        }
    }

    public void eliminarProductoTipo(Integer id){
        if(id >= 0) {
            productoTipoDAO.eliminarProductoTipo(id);
            logger.info("Producto tipo eliminado correctamente");
        }else {
            logger.info("Error al eliminar el Prodcuto Tipo. Id no valido");
        }
    }
}
