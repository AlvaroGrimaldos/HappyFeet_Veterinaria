package com.happyfeet.model.entities.observer;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.ProductoTipoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class AlertaVencimiento implements Observer{
    private static final Logger logger = LogManager.getLogger(AlertaVencimiento.class);
    @Override
    public void update(Inventario inventario) {
        if(inventario.getFechaVencimiento().isBefore(LocalDate.now().plusDays(5))) {
            logger.info("ALERTA: PRODUCTO  A VENCER EN 5 DIAS: {}", inventario.getNombreProducto());
        }
    }
}
