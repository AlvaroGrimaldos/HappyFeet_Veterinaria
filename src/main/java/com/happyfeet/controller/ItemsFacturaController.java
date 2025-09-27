package com.happyfeet.controller;

import com.happyfeet.repository.IItemsFacturaDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemsFacturaController {
    private static final Logger logger =  LogManager.getLogger(ProductoTipoController.class);
    private IItemsFacturaDAO itemsFacturaDAO;

    public ItemsFacturaController(IItemsFacturaDAO itemsFacturaDAO) { this.itemsFacturaDAO = itemsFacturaDAO; }


}
