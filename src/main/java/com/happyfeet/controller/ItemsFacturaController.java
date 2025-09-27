package com.happyfeet.controller;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.ItemsFactura;
import com.happyfeet.repository.IInventarioDAO;
import com.happyfeet.repository.IItemsFacturaDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ItemsFacturaController {
    private static final Logger logger =  LogManager.getLogger(ProductoTipoController.class);
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
        if(itemsFactura.getProductoId() == null) return false;
        if(itemsFactura.getServicioDescripcion() == null) return false;
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
            itemsFactura.stream().forEach(n -> System.out.println(n));
        }
    }

    public void buscarPorId(Integer id) {
        if(id > 0) {
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
        if(id > 0) {
            itemsFacturaDAO.eliminarItemFactura(id);
            logger.info("Item factura eliminado correctamente");
        }else {
            logger.error("Error a leliminar el item factura {}", id);
        }
    }


}
