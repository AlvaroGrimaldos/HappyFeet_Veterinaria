package com.happyfeet.repository;

import com.happyfeet.model.entities.Facturas;

import java.util.List;

public interface IFacturasDAO {
    void agregarFactura(Facturas facturas);
    Facturas agregarFacturaYRetornar(Facturas facturas);
    List<Facturas> listarTodos();
    Facturas buscarPorId(Integer id);
    void eliminarFacturas(Integer id);
    void imprimirFacturaPorId(Integer id);
    Facturas buscarUltimaFacturaPorDatos(Integer duenoId, Integer centroVeterinarioId, String fecha);
}
