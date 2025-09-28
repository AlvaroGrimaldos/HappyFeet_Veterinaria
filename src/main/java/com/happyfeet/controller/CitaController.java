package com.happyfeet.controller;

import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.entities.Veterinario;
import com.happyfeet.model.enums.EstadoCita;
import com.happyfeet.service.CitaService;
import com.happyfeet.service.VeterinarioService;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public class CitaController {
    private CitaService citaService;
    private VeterinarioService veterinarioService;

    public CitaController() {
        this.citaService = new CitaService();
        this.veterinarioService = new VeterinarioService();
    }

    // ================== MÉTODOS PARA CITAS ==================

    public boolean programarCita(Cita cita) {
        if (validarCita(cita)) {
            boolean resultado = citaService.programarCita(cita);
            if (resultado) {
                System.out.println("Cita programada exitosamente");
            } else {
                System.out.println("Error al programar la cita. Verifique los datos y disponibilidad");
            }
            return resultado;
        }
        return false;
    }

    public boolean actualizarCita(Cita cita) {
        if (validarCita(cita)) {
            boolean resultado = citaService.actualizarCita(cita);
            if (resultado) {
                System.out.println("Cita actualizada exitosamente");
            } else {
                System.out.println("Error al actualizar la cita");
            }
            return resultado;
        }
        return false;
    }

    public boolean cancelarCita(Integer citaId) {
        if (citaId == null || citaId <= 0) {
            System.out.println("Error: ID de cita inválido");
            return false;
        }

        boolean resultado = citaService.cancelarCita(citaId);
        if (resultado) {
            System.out.println("Cita cancelada exitosamente");
        } else {
            System.out.println("Error al cancelar la cita");
        }
        return resultado;
    }

    public boolean iniciarCita(Integer citaId) {
        if (citaId == null || citaId <= 0) {
            System.out.println("Error: ID de cita inválido");
            return false;
        }

        boolean resultado = citaService.iniciarCita(citaId);
        if (resultado) {
            System.out.println("Cita iniciada exitosamente");
        } else {
            System.out.println("Error al iniciar la cita");
        }
        return resultado;
    }

    public boolean finalizarCita(Integer citaId) {
        if (citaId == null || citaId <= 0) {
            System.out.println("Error: ID de cita inválido");
            return false;
        }

        boolean resultado = citaService.finalizarCita(citaId);
        if (resultado) {
            System.out.println("Cita finalizada exitosamente");
        } else {
            System.out.println("Error al finalizar la cita");
        }
        return resultado;
    }

    public boolean eliminarCita(Integer id) {
        if (id == null || id <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }

        boolean resultado = citaService.eliminarCita(id);
        if (resultado) {
            System.out.println("Cita eliminada exitosamente");
        } else {
            System.out.println("Error al eliminar la cita");
        }
        return resultado;
    }

    // ================== CONSULTAS DE CITAS ==================

    public void listarTodasLasCitas() {
        List<Cita> citas = citaService.listarCitasConInfoCompleta();
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas");
        } else {
            System.out.println("\n=== TODAS LAS CITAS ===");
            citas.forEach(cita -> {
                System.out.printf("ID: %d | %s | %s | Mascota: %s | Dr. %s | Estado: %s%n",
                        cita.getId(),
                        cita.getFechaHora().toLocalDate(),
                        cita.getFechaHora().toLocalTime(),
                        cita.getNombreMascota() != null ? cita.getNombreMascota() : "ID: " + cita.getMascotaId(),
                        cita.getNombreVeterinario() != null ? cita.getNombreVeterinario() : "ID: " + cita.getVeterinarioId(),
                        cita.getEstado());
            });
        }
    }

    public void listarCitasDelDia() {
        List<Cita> citas = citaService.obtenerCitasDelDia();
        if (citas.isEmpty()) {
            System.out.println("No hay citas programadas para hoy");
        } else {
            System.out.println("\n=== CITAS DE HOY ===");
            citas.forEach(cita -> {
                System.out.printf("%s | %s | Mascota ID: %d | Dr. ID: %d | %s%n",
                        cita.getFechaHora().toLocalTime(),
                        cita.getEstado(),
                        cita.getMascotaId(),
                        cita.getVeterinarioId(),
                        cita.getMotivo());
            });
        }
    }

    public void listarCitasPendientes() {
        List<Cita> citas = citaService.obtenerCitasPendientes();
        if (citas.isEmpty()) {
            System.out.println("No hay citas pendientes");
        } else {
            System.out.println("\n=== CITAS PENDIENTES ===");
            citas.forEach(cita -> {
                System.out.printf("ID: %d | %s %s | Mascota ID: %d | Dr. ID: %d%n",
                        cita.getId(),
                        cita.getFechaHora().toLocalDate(),
                        cita.getFechaHora().toLocalTime(),
                        cita.getMascotaId(),
                        cita.getVeterinarioId());
            });
        }
    }

    public void listarCitasEnProceso() {
        List<Cita> citas = citaService.obtenerCitasEnProceso();
        if (citas.isEmpty()) {
            System.out.println("No hay citas en proceso");
        } else {
            System.out.println("\n=== CITAS EN PROCESO ===");
            citas.forEach(cita -> {
                System.out.printf("ID: %d | %s %s | Mascota ID: %d | Dr. ID: %d | %s%n",
                        cita.getId(),
                        cita.getFechaHora().toLocalDate(),
                        cita.getFechaHora().toLocalTime(),
                        cita.getMascotaId(),
                        cita.getVeterinarioId(),
                        cita.getMotivo());
            });
        }
    }

    public void mostrarAgendaVeterinario(Integer veterinarioId, LocalDate fecha) {
        String reporte = citaService.generarReporteAgenda(veterinarioId, fecha);
        System.out.println(reporte);
    }

    public void mostrarEstadisticasCitas(LocalDate fechaInicio, LocalDate fechaFin) {
        String estadisticas = citaService.generarEstadisticasCitas(fechaInicio, fechaFin);
        System.out.println(estadisticas);
    }

    public void buscarCitasPorMascota(Integer mascotaId) {
        List<Cita> citas = citaService.buscarCitasPorMascota(mascotaId);
        if (citas.isEmpty()) {
            System.out.println("La mascota no tiene citas registradas");
        } else {
            System.out.println("\n=== CITAS DE LA MASCOTA ===");
            citas.forEach(cita -> {
                System.out.printf("%s | %s | Dr. ID: %d | %s%n",
                        cita.getFechaHora(),
                        cita.getEstado(),
                        cita.getVeterinarioId(),
                        cita.getMotivo());
            });
        }
    }

    public void buscarCitasPorVeterinario(Integer veterinarioId) {
        List<Cita> citas = citaService.buscarCitasPorVeterinario(veterinarioId);
        if (citas.isEmpty()) {
            System.out.println("El veterinario no tiene citas registradas");
        } else {
            System.out.println("\n=== CITAS DEL VETERINARIO ===");
            citas.forEach(cita -> {
                System.out.printf("%s | %s | Mascota ID: %d | %s%n",
                        cita.getFechaHora(),
                        cita.getEstado(),
                        cita.getMascotaId(),
                        cita.getMotivo());
            });
        }
    }

    public void buscarCitasPorFecha(LocalDate fecha) {
        List<Cita> citas = citaService.buscarCitasPorFecha(fecha);
        if (citas.isEmpty()) {
            System.out.println("No hay citas para esa fecha");
        } else {
            System.out.println("\n=== CITAS DE LA FECHA " + fecha + " ===");
            citas.forEach(cita -> {
                System.out.printf("%s | %s | Mascota ID: %d | Dr. ID: %d%n",
                        cita.getFechaHora().toLocalTime(),
                        cita.getEstado(),
                        cita.getMascotaId(),
                        cita.getVeterinarioId());
            });
        }
    }

    public Cita buscarCitaPorId(Integer id) {
        return citaService.buscarCitaPorId(id);
    }

    // ================== UTILIDADES ==================

    public void listarVeterinariosDisponibles() {
        List<Veterinario> veterinarios = veterinarioService.listarVeterinariosDisponibles();
        if (veterinarios.isEmpty()) {
            System.out.println("No hay veterinarios disponibles");
        } else {
            System.out.println("\n=== VETERINARIOS DISPONIBLES ===");
            veterinarios.forEach(vet -> {
                System.out.printf("ID: %d | Dr. %s | Tel: %s%n",
                        vet.getId(), vet.getNombreCompleto(), vet.getTelefono());
            });
        }
    }

    public boolean verificarDisponibilidad(Integer veterinarioId, LocalDateTime fechaHora) {
        boolean disponible = !citaService.existeConflictoHorario(veterinarioId, fechaHora);
        if (disponible) {
            System.out.println("El veterinario está disponible en ese horario");
        } else {
            System.out.println("El veterinario NO está disponible en ese horario");
        }
        return disponible;
    }

    // ================== VALIDACIONES ==================

    private boolean validarCita(Cita cita) {
        if (cita == null) {
            System.out.println("Error: Los datos de la cita son obligatorios");
            return false;
        }

        if (cita.getMascotaId() == null || cita.getMascotaId() <= 0) {
            System.out.println("Error: La mascota es obligatoria");
            return false;
        }

        if (cita.getVeterinarioId() == null || cita.getVeterinarioId() <= 0) {
            System.out.println("Error: El veterinario es obligatorio");
            return false;
        }

        if (cita.getFechaHora() == null) {
            System.out.println("Error: La fecha y hora son obligatorias");
            return false;
        }

        if (cita.getMotivo() == null || cita.getMotivo().trim().isEmpty()) {
            System.out.println("Error: El motivo de la cita es obligatorio");
            return false;
        }

        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            System.out.println("Error: No se puede programar una cita en el pasado");
            return false;
        }

        return true;
    }
}