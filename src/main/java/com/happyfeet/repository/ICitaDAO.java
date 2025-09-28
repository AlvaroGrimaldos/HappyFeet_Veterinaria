package com.happyfeet.repository;

import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.enums.EstadoCita;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public interface ICitaDAO {
    void agregarCita(Cita cita);
    List<Cita> listarTodas();
    Cita buscarPorId(Integer id);
    List<Cita> buscarPorMascota(Integer mascotaId);
    List<Cita> buscarPorVeterinario(Integer veterinarioId);
    List<Cita> buscarPorEstado(EstadoCita estado);
    List<Cita> buscarPorFecha(LocalDate fecha);
    List<Cita> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    void actualizarCita(Cita cita);
    void eliminarCita(Integer id);
    List<Cita> listarConInformacionCompleta();
    boolean existeConflictoHorario(Integer veterinarioId, LocalDateTime fechaHora, Integer citaId);
}