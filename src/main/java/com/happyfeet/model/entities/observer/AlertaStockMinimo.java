package com.happyfeet.model.entities.observer;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.ProductoTipoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlertaStockMinimo implements Observer{
    private static final Logger logger = LogManager.getLogger(AlertaStockMinimo.class);
    @Override
    public void update(Inventario inventario) {
        if(inventario.getCantidadStock() < inventario.getStockMinimo()){
            logger.info("ALERTA: STOCK MINIMO DE {}", inventario.getNombreProducto());
        }
    }
}
