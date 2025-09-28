package com.happyfeet.service;

import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.entities.Veterinario;
import com.happyfeet.model.enums.EstadoCita;
import com.happyfeet.repository.*;
import com.happyfeet.util.LoggerUtil;
import com.happyfeet.util.ValidadorUtil;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public class CitaService {
    private ICitaDAO citaDAO;
    private IVeterinarioDAO veterinarioDAO;
    private IMascotaDAO mascotaDAO;

    public CitaService() {
        this.citaDAO = new CitaDAO();
        this.veterinarioDAO = new VeterinarioDAO();
        this.mascotaDAO = new MascotaDAO();
    }

    // ================== GESTIÓN DE CITAS ==================

    public boolean programarCita(Cita cita) {
        try {
            if (!validarCita(cita)) {
                return false;
            }

            // Verificar que la mascota exista
            if (mascotaDAO.buscarPorId(cita.getMascotaId()) == null) {
                LoggerUtil.warning("No existe la mascota con ID: " + cita.getMascotaId());
                return false;
            }

            // Verificar que el veterinario exista
            if (veterinarioDAO.buscarPorId(cita.getVeterinarioId()) == null) {
                LoggerUtil.warning("No existe el veterinario con ID: " + cita.getVeterinarioId());
                return false;
            }

            // Verificar conflictos de horario
            if (citaDAO.existeConflictoHorario(cita.getVeterinarioId(), cita.getFechaHora(), null)) {
                LoggerUtil.warning("El veterinario ya tiene una cita programada en ese horario");
                return false;
            }

            // Asegurar que la cita se programa como PROGRAMADA
            cita.setEstado(EstadoCita.PROGRAMADA);

            citaDAO.agregarCita(cita);
            LoggerUtil.operacionExitosa("Programación", "Cita");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Programación", "Cita", e.getMessage());
            return false;
        }
    }

    public boolean actualizarCita(Cita cita) {
        try {
            if (!validarCita(cita) || cita.getId() == null) {
                return false;
            }

            // Verificar que exista
            if (citaDAO.buscarPorId(cita.getId()) == null) {
                LoggerUtil.warning("No existe la cita con ID: " + cita.getId());
                return false;
            }

            // Verificar conflictos de horario (excluyendo esta cita)
            if (citaDAO.existeConflictoHorario(cita.getVeterinarioId(), cita.getFechaHora(), cita.getId())) {
                LoggerUtil.warning("El veterinario ya tiene una cita programada en ese horario");
                return false;
            }

            citaDAO.actualizarCita(cita);
            LoggerUtil.operacionExitosa("Actualización", "Cita");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Actualización", "Cita", e.getMessage());
            return false;
        }
    }

    public boolean cancelarCita(Integer citaId) {
        try {
            Cita cita = citaDAO.buscarPorId(citaId);
            if (cita == null) {
                LoggerUtil.warning("No existe la cita con ID: " + citaId);
                return false;
            }

            if (cita.getEstado() == EstadoCita.FINALIZADA) {
                LoggerUtil.warning("No se puede cancelar una cita ya finalizada");
                return false;
            }

            if (cita.getEstado() == EstadoCita.CANCELADA) {
                LoggerUtil.warning("La cita ya está cancelada");
                return false;
            }

            cita.setEstado(EstadoCita.CANCELADA);
            citaDAO.actualizarCita(cita);
            LoggerUtil.operacionExitosa("Cancelación", "Cita");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Cancelación", "Cita", e.getMessage());
            return false;
        }
    }

    public boolean iniciarCita(Integer citaId) {
        try {
            Cita cita = citaDAO.buscarPorId(citaId);
            if (cita == null) {
                LoggerUtil.warning("No existe la cita con ID: " + citaId);
                return false;
            }

            if (cita.getEstado() != EstadoCita.PROGRAMADA) {
                LoggerUtil.warning("Solo se pueden iniciar citas programadas");
                return false;
            }

            cita.setEstado(EstadoCita.EN_PROCESO);
            citaDAO.actualizarCita(cita);
            LoggerUtil.operacionExitosa("Inicio", "Cita");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Inicio", "Cita", e.getMessage());
            return false;
        }
    }

    public boolean finalizarCita(Integer citaId) {
        try {
            Cita cita = citaDAO.buscarPorId(citaId);
            if (cita == null) {
                LoggerUtil.warning("No existe la cita con ID: " + citaId);
                return false;
            }

            if (cita.getEstado() != EstadoCita.EN_PROCESO) {
                LoggerUtil.warning("Solo se pueden finalizar citas en proceso");
                return false;
            }

            cita.setEstado(EstadoCita.FINALIZADA);
            citaDAO.actualizarCita(cita);
            LoggerUtil.operacionExitosa("Finalización", "Cita");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Finalización", "Cita", e.getMessage());
            return false;
        }
    }

    public boolean eliminarCita(Integer id) {
        try {
            Cita cita = citaDAO.buscarPorId(id);
            if (cita != null && cita.getEstado() == EstadoCita.FINALIZADA) {
                LoggerUtil.warning("No se puede eliminar una cita finalizada");
                return false;
            }

            citaDAO.eliminarCita(id);
            LoggerUtil.operacionExitosa("Eliminación", "Cita");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Eliminación", "Cita", e.getMessage());
            return false;
        }
    }

    // ================== CONSULTAS ==================

    public List<Cita> listarTodasLasCitas() {
        return citaDAO.listarTodas();
    }

    public List<Cita> listarCitasConInfoCompleta() {
        return citaDAO.listarConInformacionCompleta();
    }

    public Cita buscarCitaPorId(Integer id) {
        return citaDAO.buscarPorId(id);
    }

    public List<Cita> buscarCitasPorMascota(Integer mascotaId) {
        return citaDAO.buscarPorMascota(mascotaId);
    }

    public List<Cita> buscarCitasPorVeterinario(Integer veterinarioId) {
        return citaDAO.buscarPorVeterinario(veterinarioId);
    }

    public List<Cita> buscarCitasPorEstado(EstadoCita estado) {
        return citaDAO.buscarPorEstado(estado);
    }

    public List<Cita> buscarCitasPorFecha(LocalDate fecha) {
        return citaDAO.buscarPorFecha(fecha);
    }

    public List<Cita> buscarCitasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return citaDAO.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    // ================== AGENDA Y DISPONIBILIDAD ==================

    public List<Cita> obtenerAgendaVeterinario(Integer veterinarioId, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);

        return citaDAO.buscarPorRangoFechas(inicio, fin).stream()
                .filter(cita -> cita.getVeterinarioId().equals(veterinarioId))
                .filter(cita -> cita.getEstado() != EstadoCita.CANCELADA)
                .toList();
    }

    public List<Cita> obtenerCitasDelDia() {
        return citaDAO.buscarPorFecha(LocalDate.now());
    }

    public List<Cita> obtenerCitasPendientes() {
        return citaDAO.buscarPorEstado(EstadoCita.PROGRAMADA);
    }

    public List<Cita> obtenerCitasEnProceso() {
        return citaDAO.buscarPorEstado(EstadoCita.EN_PROCESO);
    }

    public boolean existeConflictoHorario(Integer veterinarioId, LocalDateTime fechaHora) {
        return citaDAO.existeConflictoHorario(veterinarioId, fechaHora, null);
    }

    // ================== REPORTES ==================

    public String generarReporteAgenda(Integer veterinarioId, LocalDate fecha) {
        Veterinario veterinario = veterinarioDAO.buscarPorId(veterinarioId);
        if (veterinario == null) {
            return "Veterinario no encontrado";
        }

        List<Cita> agenda = obtenerAgendaVeterinario(veterinarioId, fecha);

        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("=== AGENDA DEL DR. %s ===\n", veterinario.getNombreCompleto()));
        reporte.append(String.format("Fecha: %s\n", fecha));
        reporte.append(String.format("Total de citas: %d\n\n", agenda.size()));

        if (agenda.isEmpty()) {
            reporte.append("No hay citas programadas para este día.\n");
        } else {
            agenda.forEach(cita -> {
                reporte.append(String.format("%s | %s | %s | %s\n",
                        cita.getFechaHora().toLocalTime(),
                        cita.getEstado(),
                        cita.getNombreMascota() != null ? cita.getNombreMascota() : "Mascota ID: " + cita.getMascotaId(),
                        cita.getMotivo()));
            });
        }

        return reporte.toString();
    }

    public String generarEstadisticasCitas(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        List<Cita> citas = citaDAO.buscarPorRangoFechas(inicio, fin);

        long programadas = citas.stream().filter(c -> c.getEstado() == EstadoCita.PROGRAMADA).count();
        long enProceso = citas.stream().filter(c -> c.getEstado() == EstadoCita.EN_PROCESO).count();
        long finalizadas = citas.stream().filter(c -> c.getEstado() == EstadoCita.FINALIZADA).count();
        long canceladas = citas.stream().filter(c -> c.getEstado() == EstadoCita.CANCELADA).count();

        return String.format("""
                === ESTADÍSTICAS DE CITAS ===
                Período: %s a %s
                
                Total de citas: %d
                - Programadas: %d
                - En proceso: %d
                - Finalizadas: %d
                - Canceladas: %d
                
                Tasa de finalización: %.1f%%
                """,
                fechaInicio, fechaFin, citas.size(),
                programadas, enProceso, finalizadas, canceladas,
                citas.size() > 0 ? (finalizadas * 100.0 / citas.size()) : 0.0);
    }

    // ================== VALIDACIONES ==================

    private boolean validarCita(Cita cita) {
        if (cita == null) {
            LoggerUtil.warning("La cita no puede ser nula");
            return false;
        }

        if (cita.getMascotaId() == null || cita.getMascotaId() <= 0) {
            LoggerUtil.warning("La mascota es obligatoria");
            return false;
        }

        if (cita.getVeterinarioId() == null || cita.getVeterinarioId() <= 0) {
            LoggerUtil.warning("El veterinario es obligatorio");
            return false;
        }

        if (cita.getFechaHora() == null) {
            LoggerUtil.warning("La fecha y hora son obligatorias");
            return false;
        }

        if (cita.getFechaHora().isBefore(LocalDateTime.now().minusMinutes(30))) {
            LoggerUtil.warning("No se puede programar una cita en el pasado");
            return false;
        }

        if (!ValidadorUtil.esTextoValido(cita.getMotivo(), 5)) {
            LoggerUtil.warning("El motivo debe tener al menos 5 caracteres");
            return false;
        }

        return true;
    }
}