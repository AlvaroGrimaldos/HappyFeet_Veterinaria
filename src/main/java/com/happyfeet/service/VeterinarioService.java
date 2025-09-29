package com.happyfeet.service;

import com.happyfeet.model.entities.Veterinario;
import com.happyfeet.repository.*;
import com.happyfeet.util.LoggerUtil;
import com.happyfeet.util.ValidadorUtil;

import java.util.List;

public class VeterinarioService {
    private IVeterinarioDAO veterinarioDAO;
    private ICitaDAO citaDAO;

    public VeterinarioService() {
        this.veterinarioDAO = new VeterinarioDAO();
        this.citaDAO = new CitaDAO();
    }

    // ================== GESTIÓN DE VETERINARIOS ==================

    public boolean registrarVeterinario(Veterinario veterinario) {
        try {
            if (!validarVeterinario(veterinario)) {
                return false;
            }

            // Verificar que no exista ya por documento
            if (veterinarioDAO.buscarPorDocumento(veterinario.getDocumentoIdentidad()) != null) {
                LoggerUtil.warning("Ya existe un veterinario con documento: " + veterinario.getDocumentoIdentidad());
                return false;
            }

            veterinarioDAO.agregarVeterinario(veterinario);
            LoggerUtil.operacionExitosa("Registro", "Veterinario");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Registro", "Veterinario", e.getMessage());
            return false;
        }
    }

    public boolean actualizarVeterinario(Veterinario veterinario) {
        try {
            if (!validarVeterinario(veterinario) || veterinario.getId() == null) {
                return false;
            }

            // Verificar que exista
            if (veterinarioDAO.buscarPorId(veterinario.getId()) == null) {
                LoggerUtil.warning("No existe el veterinario con ID: " + veterinario.getId());
                return false;
            }

            // Verificar que no exista otro veterinario con el mismo documento
            Veterinario existente = veterinarioDAO.buscarPorDocumento(veterinario.getDocumentoIdentidad());
            if (existente != null && !existente.getId().equals(veterinario.getId())) {
                LoggerUtil.warning("Ya existe otro veterinario con documento: " + veterinario.getDocumentoIdentidad());
                return false;
            }

            veterinarioDAO.actualizarVeterinario(veterinario);
            LoggerUtil.operacionExitosa("Actualización", "Veterinario");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Actualización", "Veterinario", e.getMessage());
            return false;
        }
    }

    public boolean eliminarVeterinario(Integer id) {
        try {
            // Verificar que no tenga citas asociadas activas
            var citasActivas = citaDAO.buscarPorVeterinario(id).stream()
                    .filter(cita -> cita.getEstado().toString().equals("PROGRAMADA") ||
                            cita.getEstado().toString().equals("EN_PROCESO"))
                    .toList();

            if (!citasActivas.isEmpty()) {
                LoggerUtil.warning("No se puede eliminar el veterinario. Tiene " + citasActivas.size() + " citas activas");
                return false;
            }

            veterinarioDAO.eliminarVeterinario(id);
            LoggerUtil.operacionExitosa("Eliminación", "Veterinario");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Eliminación", "Veterinario", e.getMessage());
            return false;
        }
    }

    public List<Veterinario> listarVeterinarios() {
        return veterinarioDAO.listarTodos();
    }

    public List<Veterinario> listarVeterinariosDisponibles() {
        return veterinarioDAO.listarDisponibles();
    }

    public Veterinario buscarVeterinarioPorId(Integer id) {
        return veterinarioDAO.buscarPorId(id);
    }

    public Veterinario buscarVeterinarioPorDocumento(String documento) {
        return veterinarioDAO.buscarPorDocumento(documento);
    }

    public List<Veterinario> buscarVeterinariosPorNombre(String nombre) {
        return veterinarioDAO.buscarPorNombre(nombre);
    }

    // ================== CONSULTAS ESPECIALIZADAS ==================

    public String generarPerfilVeterinario(Integer veterinarioId) {
        Veterinario veterinario = veterinarioDAO.buscarPorId(veterinarioId);
        if (veterinario == null) {
            return "Veterinario no encontrado";
        }

        var citasTotal = citaDAO.buscarPorVeterinario(veterinarioId);
        var citasFinalizadas = citasTotal.stream()
                .filter(cita -> cita.getEstado().toString().equals("FINALIZADA"))
                .count();
        var citasPendientes = citasTotal.stream()
                .filter(cita -> cita.getEstado().toString().equals("PROGRAMADA"))
                .count();

        return String.format("""
                === PERFIL VETERINARIO ===
                Nombre: %s
                Documento: %s
                Teléfono: %s
                Email: %s
                
                === ESTADÍSTICAS ===
                Total de citas: %d
                Citas finalizadas: %d
                Citas pendientes: %d
                """,
                veterinario.getNombreCompleto(),
                veterinario.getDocumentoIdentidad(),
                veterinario.getTelefono(),
                veterinario.getEmail(),
                citasTotal.size(),
                citasFinalizadas,
                citasPendientes);
    }

    public int contarCitasPorVeterinario(Integer veterinarioId) {
        return citaDAO.buscarPorVeterinario(veterinarioId).size();
    }

    public boolean veterinarioTieneCitasActivas(Integer veterinarioId) {
        return citaDAO.buscarPorVeterinario(veterinarioId).stream()
                .anyMatch(cita -> cita.getEstado().toString().equals("PROGRAMADA") ||
                        cita.getEstado().toString().equals("EN_PROCESO"));
    }

    // ================== REPORTES ==================

    public String generarReporteVeterinarios() {
        List<Veterinario> veterinarios = veterinarioDAO.listarTodos();

        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE VETERINARIOS ===\n");
        reporte.append(String.format("Total de veterinarios: %d\n\n", veterinarios.size()));

        if (veterinarios.isEmpty()) {
            reporte.append("No hay veterinarios registrados.\n");
        } else {
            veterinarios.forEach(vet -> {
                int totalCitas = contarCitasPorVeterinario(vet.getId());
                reporte.append(String.format("Dr. %s | Doc: %s | Citas: %d | Tel: %s\n",
                        vet.getNombreCompleto(),
                        vet.getDocumentoIdentidad(),
                        totalCitas,
                        vet.getTelefono()));
            });
        }

        return reporte.toString();
    }

    // ================== VALIDACIONES ==================

    private boolean validarVeterinario(Veterinario veterinario) {
        if (veterinario == null) {
            LoggerUtil.warning("El veterinario no puede ser nulo");
            return false;
        }

        if (!ValidadorUtil.esTextoValido(veterinario.getNombreCompleto(), 3)) {
            LoggerUtil.warning("El nombre completo debe tener al menos 3 caracteres");
            return false;
        }

        if (!ValidadorUtil.esDocumentoValido(veterinario.getDocumentoIdentidad())) {
            LoggerUtil.warning("El documento de identidad no es válido");
            return false;
        }

        if (!ValidadorUtil.esEmailValido(veterinario.getEmail())) {
            LoggerUtil.warning("El email no tiene un formato válido");
            return false;
        }

        if (!ValidadorUtil.esTelefonoValido(veterinario.getTelefono())) {
            LoggerUtil.warning("El teléfono no tiene un formato válido");
            return false;
        }

        return true;
    }
}