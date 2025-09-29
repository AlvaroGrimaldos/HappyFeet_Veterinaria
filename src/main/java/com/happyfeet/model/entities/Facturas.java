package com.happyfeet.model.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Facturas {
    private static final Logger log = LogManager.getLogger(Facturas.class);
    private Integer id;
    private Integer duenoId;
    private LocalDate fechaEmision;
    private BigDecimal total;
    private Integer centroVeterinarioId;

    public Facturas(LocalDate fechaEmision, Integer duenoId, BigDecimal total, Integer centroVeterinarioId) {
        this.fechaEmision = fechaEmision;
        this.duenoId = duenoId;
        this.total = total;
        this.centroVeterinarioId = centroVeterinarioId;
    }

    public Facturas(Integer id, LocalDate fechaEmision, Integer duenoId, BigDecimal total, Integer centroVeterinarioId) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.duenoId = duenoId;
        this.total = total;
        this.centroVeterinarioId = centroVeterinarioId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Integer getDuenoId() {
        return duenoId;
    }

    public void setDuenoId(Integer duenoId) {
        this.duenoId = duenoId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getCentroVeterinarioId() {
        return centroVeterinarioId;
    }

    public void setCentroVeterinarioId(Integer centroVeterinarioId) {
        this.centroVeterinarioId = centroVeterinarioId;
    }

    @Override
    public String toString() {
        return "Facturas{" +
                "id=" + id +
                ", duenoId=" + duenoId +
                ", fechaEmision=" + fechaEmision +
                ", total=" + total +
                ", centroVeterinarioId=" + centroVeterinarioId +
                '}';
    }
}
