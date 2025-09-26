package com.happyfeet.controller;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.IInventarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowFocusListener;
import java.time.LocalDate;
import java.util.List;

public class InventarioController {
    private static final Logger logger = (Logger) LogManager.getLogger(InventarioController.class);
    private IInventarioDAO inventarioDAO;

    public InventarioController(IInventarioDAO inventarioDAO) { this.inventarioDAO = inventarioDAO; }

    public void agregarInventario(Inventario inventario) {
        if (validarInventario(inventario)) {
            inventarioDAO.agregarInventario(inventario);
        } else {
            logger.info("Error al agregar inventario. Datos invalidos");
        }
    }

    private boolean validarInventario(Inventario inventario) {
        if(inventario == null) return false;
        if(inventario.getNombreProducto() == null || inventario.getNombreProducto().isEmpty()) return false;
        if(inventario.getFechaVencimiento() == null) return false;
        if(inventario.getCantidadStock() == null) return false;
        if(inventario.getDescripcion() == null || inventario.getDescripcion().isEmpty()) return false;
        if(inventario.getTipo() == null || inventario.getTipo().isEmpty()) return false;
        if(inventario.getFabricante() == null || inventario.getFabricante().isEmpty()) return false;
        if(inventario.getLote() == null || inventario.getLote().isEmpty()) return false;
        if(inventario.getPrecioVenta() == null) return false;
        if(inventario.getStockMinimo() == null) return false;
        return true;
    }

    public void listarTodos() {
        List<Inventario> inventarios = inventarioDAO.listarTodos();
        if(inventarios.isEmpty()) {
            logger.info("No hay inventario registrado");
        } else {
            inventarios.stream().forEach(n -> System.out.println(n));
        }
    }

    public void buscarPorId(Integer id) {
        if(id > 0) {
            Inventario inventario = inventarioDAO.buscarPorId(id);
            if(inventario != null) {
                System.out.println("Producto encontrado: " + inventario);
            }else {
                logger.info("No se encontro un producto con id {}", id);
            }
        } else {
            logger.info("Error. Id no valido, ingrese un id valido");
        }
    }

    public void actualizarInventario(Inventario inventario) {
        if(inventario.getId() > 0 && validarInventario(inventario)) {
            inventarioDAO.actualizarInventario(inventario);
            logger.info("Inventario actualizado correctamente");
        } else {
            logger.info("Error al actualizar el inventario");
        }
    }

    public void eliminarInventario(Integer id) {
        if(id > 0 ) {
            inventarioDAO.eliminarInventario(id);
            logger.info("Inventario eliminado correctamente");
        }else {
            logger.info("Error al eliminar el inventario de id {}", id);
        }
    }

    public void agregarStock(Inventario inventario, int cantidad, LocalDate fecha) {
        if(inventario.getId() > 0 && cantidad > 0 && fecha != null) {
            inventarioDAO.agregarStock(inventario, cantidad, fecha);
            logger.info("Stock actualizado de {}", inventario.getNombreProducto());
        }else {
            logger.info("Error al agregar stock de {}", inventario.getNombreProducto());
        }
    }
}
