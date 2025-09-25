package com.happyfeet.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Inventario {
    private Integer id;
    private String nombreProducto;
    private ProductoTipo productoTipo;
    private String descripcion;
    private String fabricante;
    private String lote;
    private Integer cantidadStock;
    private Integer stockMinimo;
    private LocalDate fechaVencimiento;
    private BigDecimal precioVenta;

    private Inventario(String nombreProducto, ProductoTipo productoTipo, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        this.nombreProducto = nombreProducto;
        this.productoTipo = productoTipo;
        this.fabricante = fabricante;
        this.descripcion = descripcion;
        this.lote = lote;
        this.stockMinimo = stockMinimo;
        this.cantidadStock = cantidadStock;
        this.fechaVencimiento = fechaVencimiento;
        this.precioVenta = precioVenta;
    }

    public Inventario(String nombreProducto, Integer id, String descripcion, ProductoTipo productoTipo, String lote, String fabricante, Integer cantidadStock, Integer stockMinimo, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        this.nombreProducto = nombreProducto;
        this.id = id;
        this.descripcion = descripcion;
        this.productoTipo = productoTipo;
        this.lote = lote;
        this.fabricante = fabricante;
        this.cantidadStock = cantidadStock;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
        this.precioVenta = precioVenta;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public ProductoTipo getProductoTipo() {
        return productoTipo;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public String getLote() {
        return lote;
    }

    public String getFabricante() {
        return fabricante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setProductoTipo(ProductoTipo productoTipo) {
        this.productoTipo = productoTipo;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", productoTipo=" + getTipo() +
                ", descripcion='" + descripcion + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", lote='" + lote + '\'' +
                ", cantidadStock=" + cantidadStock +
                ", stockMinimo=" + stockMinimo +
                ", fechaVencimiento=" + fechaVencimiento +
                ", precioVenta=" + precioVenta +
                '}';
    }
}
