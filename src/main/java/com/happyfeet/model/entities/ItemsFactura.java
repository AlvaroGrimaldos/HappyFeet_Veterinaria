package com.happyfeet.model.entities;

import java.math.BigDecimal;

public class ItemsFactura {
    private Integer id;
    private Integer facturaId;
    private Integer productoId;
    private String servicioDescripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public ItemsFactura(Integer facturaId, Integer productoId, String servicioDescripcion, BigDecimal subtotal, Integer cantidad, BigDecimal precioUnitario) {
        this.facturaId = facturaId;
        this.productoId = productoId;
        this.servicioDescripcion = servicioDescripcion;
        this.subtotal = subtotal;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public ItemsFactura(Integer id, Integer facturaId, Integer productoId, String servicioDescripcion, Integer cantidad, BigDecimal precioUnitario,  BigDecimal subtotal) {
        this.id = id;
        this.facturaId = facturaId;
        this.productoId = productoId;
        this.precioUnitario = precioUnitario;
        this.servicioDescripcion = servicioDescripcion;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Integer facturaId) {
        this.facturaId = facturaId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getServicioDescripcion() {
        return servicioDescripcion;
    }

    public void setServicioDescripcion(String servicioDescripcion) {
        this.servicioDescripcion = servicioDescripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "ItemsFactura{" +
                "id=" + id +
                ", facturaId=" + facturaId +
                ", productoId=" + productoId +
                ", servicioDescripcion='" + servicioDescripcion + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
