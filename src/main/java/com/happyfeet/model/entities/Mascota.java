package com.happyfeet.model.entities;

import com.happyfeet.model.enums.SexoMascota;
import com.happyfeet.model.enums.EstadoVacunacion;
import java.time.LocalDate;

public class Mascota {
    private Integer id;
    private Integer duenoId;
    private String nombre;
    private Integer razaId;
    private LocalDate fechaNacimiento;
    private SexoMascota sexo;
    private String urlFoto;
    private EstadoVacunacion vacunado;

    // Para mostrar información completa (joins)
    private String nombreDueno;
    private String nombreRaza;
    private String nombreEspecie;

    public Mascota(Integer duenoId, String nombre, Integer razaId, LocalDate fechaNacimiento,
                   SexoMascota sexo, String urlFoto, EstadoVacunacion vacunado) {
        this.duenoId = duenoId;
        this.nombre = nombre;
        this.razaId = razaId;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.urlFoto = urlFoto;
        this.vacunado = vacunado;
    }

    public Mascota(Integer id, Integer duenoId, String nombre, Integer razaId, LocalDate fechaNacimiento,
                   SexoMascota sexo, String urlFoto, EstadoVacunacion vacunado) {
        this.id = id;
        this.duenoId = duenoId;
        this.nombre = nombre;
        this.razaId = razaId;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.urlFoto = urlFoto;
        this.vacunado = vacunado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDuenoId() {
        return duenoId;
    }

    public void setDuenoId(Integer duenoId) {
        this.duenoId = duenoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getRazaId() {
        return razaId;
    }

    public void setRazaId(Integer razaId) {
        this.razaId = razaId;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public SexoMascota getSexo() {
        return sexo;
    }

    public void setSexo(SexoMascota sexo) {
        this.sexo = sexo;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public EstadoVacunacion getVacunado() {
        return vacunado;
    }

    public void setVacunado(EstadoVacunacion vacunado) {
        this.vacunado = vacunado;
    }

    public String getNombreDueno() {
        return nombreDueno;
    }

    public void setNombreDueno(String nombreDueno) {
        this.nombreDueno = nombreDueno;
    }

    public String getNombreRaza() {
        return nombreRaza;
    }

    public void setNombreRaza(String nombreRaza) {
        this.nombreRaza = nombreRaza;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", duenoId=" + duenoId +
                ", nombre='" + nombre + '\'' +
                ", razaId=" + razaId +
                ", fechaNacimiento=" + fechaNacimiento +
                ", sexo=" + sexo +
                ", urlFoto='" + urlFoto + '\'' +
                ", vacunado=" + vacunado +
                '}';
    }
}