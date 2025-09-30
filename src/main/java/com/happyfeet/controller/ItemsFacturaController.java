package com.happyfeet.controller;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.ItemsFactura;
import com.happyfeet.repository.IInventarioDAO;
import com.happyfeet.repository.IItemsFacturaDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ItemsFacturaController {
    private static final Logger logger =  LogManager.getLogger(ItemsFacturaController.class);
    private IItemsFacturaDAO itemsFacturaDAO;
    private IInventarioDAO inventarioDAO;

    public ItemsFacturaController(IItemsFacturaDAO itemsFacturaDAO, IInventarioDAO inventarioDAO) {
        this.itemsFacturaDAO = itemsFacturaDAO;
        this.inventarioDAO = inventarioDAO;
    }

    public void agregarItemFactura(ItemsFactura itemsFactura){
        if (validarItemsFactura(itemsFactura)){
            itemsFacturaDAO.agregarItemFactura(itemsFactura);
            Inventario inventario = inventarioDAO.buscarPorId(itemsFactura.getProductoId());
            inventarioDAO.actualizarStockVenta(inventario, itemsFactura.getCantidad());
        }else {
            logger.error("Error al agregar items factura. Datos Invalidos");
        }
    }

    private boolean validarItemsFactura(ItemsFactura itemsFactura){
        if(itemsFactura == null) return false;
        if(itemsFactura.getFacturaId() == null) return false;
        if(itemsFactura.getCantidad() == null) return false;
        if(itemsFactura.getPrecioUnitario() == null) return false;
        if(itemsFactura.getSubtotal() == null) return false;
        return true;
    }

    public void listarTodos(){
        List<ItemsFactura> itemsFactura = itemsFacturaDAO.listarTodos();
        if(itemsFactura.isEmpty()) {
            logger.info("No hay items factura registrados");
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                              ITEMS DE FACTURAS                                                            ║");
            System.out.println("╠════╦═════════════╦══════════════╦═══════════════════════════════╦══════════╦═══════════════╦══════════════════════════════╣");
            System.out.println("║ ID ║ FACTURA ID  ║ PRODUCTO ID  ║      SERVICIO/PRODUCTO        ║ CANTIDAD ║ PRECIO UNIT.  ║          SUBTOTAL            ║");
            System.out.println("╠════╬═════════════╬══════════════╬═══════════════════════════════╬══════════╬═══════════════╬══════════════════════════════╣");

            itemsFactura.forEach(item -> {
                System.out.printf("║ %-2d ║ %11d ║ %12d ║ %-29s ║ %8d ║ $%,11.2f ║ $%,26.2f ║%n",
                        item.getId(),
                        item.getFacturaId(),
                        item.getProductoId() != null ? item.getProductoId() : 0,
                        truncarTexto(item.getServicioDescripcion() != null ? item.getServicioDescripcion() : "N/A", 29),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getSubtotal());
            });

            System.out.println("╚════╩═════════════╩══════════════╩═══════════════════════════════╩══════════╩═══════════════╩══════════════════════════════╝");

            // Estadísticas
            double totalGeneral = itemsFactura.stream()
                    .mapToDouble(i -> i.getSubtotal().doubleValue())
                    .sum();

            int totalItems = itemsFactura.stream()
                    .mapToInt(ItemsFactura::getCantidad)
                    .sum();

            System.out.println("Total de registros: " + itemsFactura.size());
            System.out.println("Total de items vendidos: " + totalItems);
            System.out.printf("Total acumulado: $%,.2f%n", totalGeneral);
        }
    }

    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud - 3) + "...";
    }
    public void buscarPorId(Integer id) {
        if(id > 0 && id != null) {
            ItemsFactura itemsFactura = itemsFacturaDAO.buscarPorId(id);
            if(itemsFactura != null) {
                System.out.println("Item factura encontrado: " + itemsFactura);
            }else {
                logger.info("No se encontro un itemfactura con id {}", id);
            }
        }else {
            logger.error("Error. Id no valido, ingrese un id valido");
        }
    }

    public void eliminarItemFactura(Integer id) {
        if(id > 0 && id != null) {
            itemsFacturaDAO.eliminarItemFactura(id);
            logger.info("Item factura eliminado correctamente");
        }else {
            logger.error("Error a leliminar el item factura {}", id);
        }
    }


}
