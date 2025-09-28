package com.happyfeet.repository;

import com.happyfeet.model.entities.ItemsFactura;

import java.util.List;

public interface IItemsFacturaDAO {
    void agregarItemFactura(ItemsFactura itemsFactura);
    List<ItemsFactura> listarTodos();
    ItemsFactura buscarPorId(Integer id);
    void eliminarItemFactura(Integer id);
}
