package com.happyfeet.controller;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.IInventarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class InventarioController {
    private static final Logger logger = LogManager.getLogger(InventarioController.class);
    private IInventarioDAO inventarioDAO;

    public InventarioController(IInventarioDAO inventarioDAO) {
        this.inventarioDAO = inventarioDAO;
    }

    public void agregarInventario(Inventario inventario) {
        if (validarInventario(inventario)) {
            try {
                inventarioDAO.agregarInventario(inventario);
                logger.info("Inventario agregado exitosamente: {}", inventario.getNombreProducto());
            } catch (Exception e) {
                logger.error("Error al agregar inventario: {}", e.getMessage(), e);
            }
        } else {
            logger.error("Error al agregar inventario. Datos inválidos para: {}",
                    inventario != null ? inventario.getNombreProducto() : "null");
        }
    }

    private boolean validarInventario(Inventario inventario) {
        if(inventario == null) {
            logger.error("Inventario es null");
            return false;
        }
        if(inventario.getNombreProducto() == null || inventario.getNombreProducto().trim().isEmpty()) {
            logger.error("Nombre del producto es inválido");
            return false;
        }
        if(inventario.getProductoTipoId() == null || inventario.getProductoTipoId() <= 0) {
            logger.error("Producto tipo ID es inválido: {}", inventario.getProductoTipoId());
            return false;
        }
        if(inventario.getFechaVencimiento() == null) {
            logger.error("Fecha de vencimiento es null");
            return false;
        }
        if(inventario.getCantidadStock() == null || inventario.getCantidadStock() < 0) {
            logger.error("Cantidad de stock es inválida: {}", inventario.getCantidadStock());
            return false;
        }
        if(inventario.getDescripcion() == null || inventario.getDescripcion().trim().isEmpty()) {
            logger.error("Descripción es inválida");
            return false;
        }
        if(inventario.getFabricante() == null || inventario.getFabricante().trim().isEmpty()) {
            logger.error("Fabricante es inválido");
            return false;
        }
        if(inventario.getLote() == null || inventario.getLote().trim().isEmpty()) {
            logger.error("Lote es inválido");
            return false;
        }
        if(inventario.getPrecioVenta() == null || inventario.getPrecioVenta().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            logger.error("Precio de venta es inválido: {}", inventario.getPrecioVenta());
            return false;
        }
        if(inventario.getStockMinimo() == null || inventario.getStockMinimo() < 0) {
            logger.error("Stock mínimo es inválido: {}", inventario.getStockMinimo());
            return false;
        }
        return true;
    }

    public List<Inventario> listarTodos() {
        List<Inventario> inventarios = inventarioDAO.listarTodos();
        if(inventarios.isEmpty()) {
            logger.info("No hay inventario registrado");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                    INVENTARIO DE PRODUCTOS                                                       ║");
            System.out.println("╠════╦══════════════════════════════╦═══════════════╦══════════════╦═══════╦════════════╦══════════════╦════════════╦═════════════╣");
            System.out.println("║ ID ║         PRODUCTO             ║     TIPO      ║  FABRICANTE  ║ LOTE  ║   STOCK    ║ STOCK MIN.   ║  F.VENC.   ║   PRECIO    ║");
            System.out.println("╠════╬══════════════════════════════╬═══════════════╬══════════════╬═══════╬════════════╬══════════════╬════════════╬═════════════╣");

            inventarios.forEach(i -> {
                System.out.printf("║ %-2d ║ %-28s ║ %-13s ║ %-12s ║ %-5s ║ %10d ║ %12d ║ %-10s ║ $%,10.2f ║%n",
                        i.getId(),
                        truncarTexto(i.getNombreProducto(), 28),
                        truncarTexto(obtenerNombreTipo(i.getProductoTipoId()), 13),
                        truncarTexto(i.getFabricante(), 12),
                        truncarTexto(i.getLote(), 5),
                        i.getCantidadStock(),
                        i.getStockMinimo(),
                        i.getFechaVencimiento() != null ? i.getFechaVencimiento().toString() : "N/A",
                        i.getPrecioVenta());

                // Alerta de stock bajo
                if (i.getCantidadStock() <= i.getStockMinimo()) {
                    System.out.println("║    ⚠️  ALERTA: Stock bajo o crítico                                                                                                    ║");
                }
            });

            System.out.println("╚════╩══════════════════════════════╩═══════════════╩══════════════╩═══════╩════════════╩══════════════╩════════════╩═════════════╝");
            System.out.println("Total de productos: " + inventarios.size());

            // Estadísticas adicionales
            long stockBajo = inventarios.stream()
                    .filter(i -> i.getCantidadStock() <= i.getStockMinimo())
                    .count();

            if (stockBajo > 0) {
                System.out.println("⚠️  Productos con stock bajo: " + stockBajo);
            }

            logger.info("Se encontraron {} productos en inventario", inventarios.size());
        }
        return inventarios;
    }

    // Método auxiliar para obtener nombre del tipo
    private String obtenerNombreTipo(Integer tipoId) {
        switch(tipoId) {
            case 1: return "Medicamento";
            case 2: return "Vacuna";
            case 3: return "Insumo Médico";
            case 4: return "Alimento";
            default: return "Desconocido";
        }
    }
    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud - 3) + "...";
    }

    public Inventario buscarPorId(Integer id) {
        if(id != null && id > 0) {
            Inventario inventario = inventarioDAO.buscarPorId(id);
            if(inventario != null) {
                System.out.println("Producto encontrado: " + inventario);
                logger.info("Producto encontrado con ID: {}", id);
                return inventario;
            } else {
                logger.warn("No se encontró un producto con id {}", id);
            }
        } else {
            logger.error("Error. ID no válido: {}", id);
        }
        return null;
    }

    public void actualizarInventario(Inventario inventario) {
        if(inventario != null && inventario.getId() != null && inventario.getId() > 0 && validarInventario(inventario)) {
            try {
                inventarioDAO.actualizarInventario(inventario);
                logger.info("Inventario actualizado correctamente: ID {}", inventario.getId());
            } catch (Exception e) {
                logger.error("Error al actualizar el inventario con ID {}: {}", inventario.getId(), e.getMessage(), e);
            }
        } else {
            logger.error("Error al actualizar el inventario. Datos inválidos o ID no válido");
        }
    }

    public void eliminarInventario(Integer id) {
        if(id != null && id > 0) {
            try {
                // Verificar si existe antes de eliminar
                Inventario inventario = inventarioDAO.buscarPorId(id);
                if(inventario != null) {
                    inventarioDAO.eliminarInventario(id);
                    logger.info("Inventario eliminado correctamente: ID {}", id);
                } else {
                    logger.warn("No se puede eliminar. No existe inventario con ID: {}", id);
                }
            } catch (Exception e) {
                logger.error("Error al eliminar el inventario con ID {}: {}", id, e.getMessage(), e);
            }
        } else {
            logger.error("Error. ID no válido para eliminar: {}", id);
        }
    }

    public void agregarStock(Inventario inventario, int cantidad, LocalDate fecha) {
        if(inventario != null && inventario.getId() != null && inventario.getId() > 0 && cantidad > 0 && fecha != null) {
            try {
                inventarioDAO.agregarStock(inventario, cantidad, fecha);
                logger.info("Stock agregado exitosamente para: {}", inventario.getNombreProducto());
            } catch (Exception e) {
                logger.error("Error al agregar stock de {}: {}", inventario.getNombreProducto(), e.getMessage(), e);
            }
        } else {
            logger.error("Error al agregar stock. Parámetros inválidos para: {}",
                    inventario != null ? inventario.getNombreProducto() : "null");
        }
    }

    public void actualizarStockVenta(Inventario inventario, int cantidadVendida) {
        if(inventario != null && inventario.getId() != null && inventario.getId() > 0 && cantidadVendida > 0) {
            try {
                inventarioDAO.actualizarStockVenta(inventario, cantidadVendida);
                logger.info("Stock actualizado por venta exitosamente para: {}", inventario.getNombreProducto());
            } catch (Exception e) {
                logger.error("Error al actualizar stock por venta de {}: {}", inventario.getNombreProducto(), e.getMessage(), e);
            }
        } else {
            logger.error("Error al actualizar stock por venta. Parámetros inválidos");
        }
    }
}