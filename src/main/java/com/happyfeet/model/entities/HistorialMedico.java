package com.happyfeet.model.entities;

import java.time.LocalDate;

public class HistorialMedico {
    private Integer id;
    private Integer mascotaId;
    private LocalDate fechaEvento;
    private Integer eventoTipoId;
    private String descripcion;
    private String diagnostico;
    private String tratamientoRecomendado;
    private Integer centroVeterinarioId;

    // Para mostrar información completa (joins)
    private String nombreMascota;
    private String nombreDueno;
    private String eventoTipoNombre;
    private String centroVeterinarioNombre;

    public HistorialMedico(Integer mascotaId, LocalDate fechaEvento, Integer eventoTipoId,
                           String descripcion, String diagnostico, String tratamientoRecomendado,
                           Integer centroVeterinarioId) {
        this.mascotaId = mascotaId;
        this.fechaEvento = fechaEvento;
        this.eventoTipoId = eventoTipoId;
        this.descripcion = descripcion;
        this.diagnostico = diagnostico;
        this.tratamientoRecomendado = tratamientoRecomendado;
        this.centroVeterinarioId = centroVeterinarioId;
    }

    public HistorialMedico(Integer id, Integer mascotaId, LocalDate fechaEvento, Integer eventoTipoId,
                           String descripcion, String diagnostico, String tratamientoRecomendado,
                           Integer centroVeterinarioId) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.fechaEvento = fechaEvento;
        this.eventoTipoId = eventoTipoId;
        this.descripcion = descripcion;
        this.diagnostico = diagnostico;
        this.tratamientoRecomendado = tratamientoRecomendado;
        this.centroVeterinarioId = centroVeterinarioId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Integer mascotaId) {
        this.mascotaId = mascotaId;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Integer getEventoTipoId() {
        return eventoTipoId;
    }

    public void setEventoTipoId(Integer eventoTipoId) {
        this.eventoTipoId = eventoTipoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamientoRecomendado() {
        return tratamientoRecomendado;
    }

    public void setTratamientoRecomendado(String tratamientoRecomendado) {
        this.tratamientoRecomendado = tratamientoRecomendado;
    }

    public Integer getCentroVeterinarioId() {
        return centroVeterinarioId;
    }

    public void setCentroVeterinarioId(Integer centroVeterinarioId) {
        this.centroVeterinarioId = centroVeterinarioId;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getNombreDueno() {
        return nombreDueno;
    }

    public void setNombreDueno(String nombreDueno) {
        this.nombreDueno = nombreDueno;
    }

    public String getEventoTipoNombre() {
        return eventoTipoNombre;
    }

    public void setEventoTipoNombre(String eventoTipoNombre) {
        this.eventoTipoNombre = eventoTipoNombre;
    }

    public String getCentroVeterinarioNombre() {
        return centroVeterinarioNombre;
    }

    public void setCentroVeterinarioNombre(String centroVeterinarioNombre) {
        this.centroVeterinarioNombre = centroVeterinarioNombre;
    }

    @Override
    public String toString() {
        return "HistorialMedico{" +
                "id=" + id +
                ", mascotaId=" + mascotaId +
                ", fechaEvento=" + fechaEvento +
                ", eventoTipoId=" + eventoTipoId +
                ", descripcion='" + descripcion + '\'' +
                ", diagnostico='" + diagnostico + '\'' +
                ", tratamientoRecomendado='" + tratamientoRecomendado + '\'' +
                ", centroVeterinarioId=" + centroVeterinarioId +
                '}';
    }
}