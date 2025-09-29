package com.happyfeet.model.entities;

import com.happyfeet.model.enums.EstadoCita;
import java.time.LocalDateTime;

public class Cita {
    private Integer id;
    private Integer mascotaId;
    private LocalDateTime fechaHora;
    private String motivo;
    private EstadoCita estado;
    private Integer veterinarioId;

    // Para mostrar información completa (joins)
    private String nombreMascota;
    private String nombreDueno;
    private String nombreVeterinario;

    public Cita(Integer mascotaId, LocalDateTime fechaHora, String motivo, EstadoCita estado, Integer veterinarioId) {
        this.mascotaId = mascotaId;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.veterinarioId = veterinarioId;
    }

    public Cita(Integer id, Integer mascotaId, LocalDateTime fechaHora, String motivo, EstadoCita estado, Integer veterinarioId) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.veterinarioId = veterinarioId;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Integer getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Integer veterinarioId) {
        this.veterinarioId = veterinarioId;
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

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", mascotaId=" + mascotaId +
                ", fechaHora=" + fechaHora +
                ", motivo='" + motivo + '\'' +
                ", estado=" + estado +
                ", veterinarioId=" + veterinarioId +
                '}';
    }
}