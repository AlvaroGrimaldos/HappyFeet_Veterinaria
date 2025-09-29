package com.happyfeet.service;

import com.happyfeet.model.entities.HistorialMedico;
import com.happyfeet.model.entities.EventoTipo;
import com.happyfeet.repository.*;
import com.happyfeet.util.LoggerUtil;
import com.happyfeet.util.ValidadorUtil;

import java.time.LocalDate;
import java.util.List;

public class HistorialService {
    private IHistorialMedicoDAO historialDAO;
    private IEventoTipoDAO eventoTipoDAO;
    private IMascotaDAO mascotaDAO;

    public HistorialService() {
        this.historialDAO = new HistorialMedicoDAO();
        this.eventoTipoDAO = new EventoTipoDAO();
        this.mascotaDAO = new MascotaDAO();
    }

    // ================== GESTIÓN DE HISTORIAL ==================

    public boolean registrarEvento(HistorialMedico historial) {
        try {
            if (!validarHistorial(historial)) {
                return false;
            }

            // Verificar que la mascota exista
            if (mascotaDAO.buscarPorId(historial.getMascotaId()) == null) {
                LoggerUtil.warning("No existe la mascota con ID: " + historial.getMascotaId());
                return false;
            }

            // Verificar que el tipo de evento exista
            if (eventoTipoDAO.buscarPorId(historial.getEventoTipoId()) == null) {
                LoggerUtil.warning("No existe el tipo de evento con ID: " + historial.getEventoTipoId());
                return false;
            }

            historialDAO.agregarHistorial(historial);
            LoggerUtil.operacionExitosa("Registro", "Evento médico");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Registro", "Evento médico", e.getMessage());
            return false;
        }
    }

    public boolean actualizarEvento(HistorialMedico historial) {
        try {
            if (!validarHistorial(historial) || historial.getId() == null) {
                return false;
            }

            // Verificar que exista
            if (historialDAO.buscarPorId(historial.getId()) == null) {
                LoggerUtil.warning("No existe el evento médico con ID: " + historial.getId());
                return false;
            }

            historialDAO.actualizarHistorial(historial);
            LoggerUtil.operacionExitosa("Actualización", "Evento médico");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Actualización", "Evento médico", e.getMessage());
            return false;
        }
    }

    public boolean eliminarEvento(Integer id) {
        try {
            historialDAO.eliminarHistorial(id);
            LoggerUtil.operacionExitosa("Eliminación", "Evento médico");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Eliminación", "Evento médico", e.getMessage());
            return false;
        }
    }

    public List<HistorialMedico> listarTodoElHistorial() {
        return historialDAO.listarTodos();
    }

    public List<HistorialMedico> listarHistorialConInfoCompleta() {
        return historialDAO.listarConInformacionCompleta();
    }

    public HistorialMedico buscarEventoPorId(Integer id) {
        return historialDAO.buscarPorId(id);
    }

    public List<HistorialMedico> buscarHistorialPorMascota(Integer mascotaId) {
        return historialDAO.buscarPorMascota(mascotaId);
    }

    // ================== CONSULTAS ESPECIALIZADAS ==================

    public List<HistorialMedico> obtenerHistorialReciente(Integer mascotaId, int ultimosMeses) {
        List<HistorialMedico> historialCompleto = historialDAO.buscarPorMascota(mascotaId);
        LocalDate fechaLimite = LocalDate.now().minusMonths(ultimosMeses);

        return historialCompleto.stream()
                .filter(h -> h.getFechaEvento().isAfter(fechaLimite))
                .toList();
    }

    public boolean mascotaTieneVacunasAlDia(Integer mascotaId) {
        List<HistorialMedico> historial = historialDAO.buscarPorMascota(mascotaId);
        LocalDate hace12Meses = LocalDate.now().minusMonths(12);

        return historial.stream()
                .anyMatch(h -> h.getEventoTipoId() == 1 && // 1 = Vacunación
                        h.getFechaEvento().isAfter(hace12Meses));
    }

    public int contarConsultasPorMascota(Integer mascotaId) {
        List<HistorialMedico> historial = historialDAO.buscarPorMascota(mascotaId);
        return (int) historial.stream()
                .filter(h -> h.getEventoTipoId() == 2) // 2 = Consulta
                .count();
    }

    public List<HistorialMedico> buscarEventosPorTipo(Integer eventoTipoId) {
        List<HistorialMedico> todoElHistorial = historialDAO.listarTodos();
        return todoElHistorial.stream()
                .filter(h -> h.getEventoTipoId().equals(eventoTipoId))
                .toList();
    }

    public List<HistorialMedico> buscarEventosPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        List<HistorialMedico> todoElHistorial = historialDAO.listarTodos();
        return todoElHistorial.stream()
                .filter(h -> !h.getFechaEvento().isBefore(fechaInicio) &&
                        !h.getFechaEvento().isAfter(fechaFin))
                .toList();
    }

    // ================== REPORTES Y ESTADÍSTICAS ==================

    public String generarResumenMascota(Integer mascotaId) {
        List<HistorialMedico> historial = historialDAO.buscarPorMascota(mascotaId);

        if (historial.isEmpty()) {
            return "Sin historial médico registrado.";
        }

        long vacunas = historial.stream().filter(h -> h.getEventoTipoId() == 1).count();
        long consultas = historial.stream().filter(h -> h.getEventoTipoId() == 2).count();
        long cirugias = historial.stream().filter(h -> h.getEventoTipoId() == 3).count();
        long desparasitaciones = historial.stream().filter(h -> h.getEventoTipoId() == 4).count();

        HistorialMedico ultimoEvento = historial.get(0); // Asumiendo orden DESC por fecha

        return String.format("""
                === RESUMEN MÉDICO ===
                Total de eventos: %d
                - Vacunas: %d
                - Consultas: %d  
                - Cirugías: %d
                - Desparasitaciones: %d
                
                Último evento: %s (%s)
                """,
                historial.size(), vacunas, consultas, cirugias, desparasitaciones,
                ultimoEvento.getFechaEvento(),
                obtenerNombreEventoTipo(ultimoEvento.getEventoTipoId()));
    }

    // ================== UTILIDADES ==================

    public List<EventoTipo> listarTiposDeEventos() {
        return eventoTipoDAO.listarTodos();
    }

    public String obtenerNombreEventoTipo(Integer eventoTipoId) {
        EventoTipo tipo = eventoTipoDAO.buscarPorId(eventoTipoId);
        return tipo != null ? tipo.getNombre() : "Desconocido";
    }

    // ================== VALIDACIONES ==================

    private boolean validarHistorial(HistorialMedico historial) {
        if (historial == null) {
            LoggerUtil.warning("El historial médico no puede ser nulo");
            return false;
        }

        if (historial.getMascotaId() == null || historial.getMascotaId() <= 0) {
            LoggerUtil.warning("La mascota es obligatoria");
            return false;
        }

        if (historial.getFechaEvento() == null) {
            LoggerUtil.warning("La fecha del evento es obligatoria");
            return false;
        }

        if (historial.getFechaEvento().isAfter(LocalDate.now())) {
            LoggerUtil.warning("La fecha del evento no puede ser futura");
            return false;
        }

        if (historial.getEventoTipoId() == null || historial.getEventoTipoId() <= 0) {
            LoggerUtil.warning("El tipo de evento es obligatorio");
            return false;
        }

        if (!ValidadorUtil.esTextoValido(historial.getDescripcion(), 5)) {
            LoggerUtil.warning("La descripción debe tener al menos 5 caracteres");
            return false;
        }

        if (historial.getCentroVeterinarioId() == null || historial.getCentroVeterinarioId() <= 0) {
            LoggerUtil.warning("El centro veterinario es obligatorio");
            return false;
        }

        return true;
    }
}