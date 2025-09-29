package com.happyfeet.controller;

import com.happyfeet.model.entities.Veterinario;
import com.happyfeet.service.VeterinarioService;

import java.util.List;

public class VeterinarioController {
    private VeterinarioService veterinarioService;

    public VeterinarioController() {
        this.veterinarioService = new VeterinarioService();
    }

    // ================== MÉTODOS PARA VETERINARIOS ==================

    public boolean registrarVeterinario(Veterinario veterinario) {
        if (validarVeterinario(veterinario)) {
            boolean resultado = veterinarioService.registrarVeterinario(veterinario);
            if (resultado) {
                System.out.println("Veterinario registrado exitosamente");
            } else {
                System.out.println("Error al registrar el veterinario. Verifique los datos");
            }
            return resultado;
        }
        return false;
    }

    public boolean actualizarVeterinario(Veterinario veterinario) {
        if (validarVeterinario(veterinario)) {
            boolean resultado = veterinarioService.actualizarVeterinario(veterinario);
            if (resultado) {
                System.out.println("Veterinario actualizado exitosamente");
            } else {
                System.out.println("Error al actualizar el veterinario. Verifique los datos");
            }
            return resultado;
        }
        return false;
    }

    public boolean eliminarVeterinario(Integer id) {
        if (id == null || id <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }

        boolean resultado = veterinarioService.eliminarVeterinario(id);
        if (resultado) {
            System.out.println("Veterinario eliminado exitosamente");
        } else {
            System.out.println("Error al eliminar el veterinario. Puede que tenga citas asociadas");
        }
        return resultado;
    }

    public void listarVeterinarios() {
        List<Veterinario> veterinarios = veterinarioService.listarVeterinarios();
        if (veterinarios.isEmpty()) {
            System.out.println("No hay veterinarios registrados");
        } else {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                           LISTA DE VETERINARIOS                                                ║");
            System.out.println("╠════╦════════════════════════════════╦═══════════════╦═════════════════════════════════════╦═════════════════╣");
            System.out.println("║ ID ║            DOCTOR              ║   DOCUMENTO   ║                EMAIL                ║    TELÉFONO     ║");
            System.out.println("╠════╬════════════════════════════════╬═══════════════╬═════════════════════════════════════╬═════════════════╣");

            veterinarios.forEach(vet -> {
                System.out.printf("║ %-2d ║ Dr. %-29s ║ %-13s ║ %-35s ║ %-15s ║%n",
                        vet.getId(),
                        truncarTexto(vet.getNombreCompleto(), 29),
                        vet.getDocumentoIdentidad(),
                        truncarTexto(vet.getEmail(), 35),
                        vet.getTelefono());
            });

            System.out.println("╚════╩════════════════════════════════╩═══════════════╩═════════════════════════════════════╩═════════════════╝");
            System.out.println("Total de veterinarios: " + veterinarios.size());
        }
    }
    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud - 3) + "...";
    }

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

    public Veterinario buscarVeterinarioPorId(Integer id) {
        return veterinarioService.buscarVeterinarioPorId(id);
    }

    public Veterinario buscarVeterinarioPorDocumento(String documento) {
        return veterinarioService.buscarVeterinarioPorDocumento(documento);
    }

    public void buscarVeterinariosPorNombre(String nombre) {
        List<Veterinario> veterinarios = veterinarioService.buscarVeterinariosPorNombre(nombre);
        if (veterinarios.isEmpty()) {
            System.out.println("No se encontraron veterinarios con ese nombre");
        } else {
            System.out.println("\n=== VETERINARIOS ENCONTRADOS ===");
            veterinarios.forEach(vet -> {
                System.out.printf("ID: %d | Dr. %s | Doc: %s%n",
                        vet.getId(), vet.getNombreCompleto(), vet.getDocumentoIdentidad());
            });
        }
    }

    public void mostrarPerfilVeterinario(Integer veterinarioId) {
        String perfil = veterinarioService.generarPerfilVeterinario(veterinarioId);
        System.out.println(perfil);
    }

    public void mostrarReporteVeterinarios() {
        String reporte = veterinarioService.generarReporteVeterinarios();
        System.out.println(reporte);
    }

    // ================== CONSULTAS ESPECIALIZADAS ==================

    public void mostrarEstadisticasVeterinario(Integer veterinarioId) {
        Veterinario veterinario = veterinarioService.buscarVeterinarioPorId(veterinarioId);
        if (veterinario == null) {
            System.out.println("Veterinario no encontrado");
            return;
        }

        int totalCitas = veterinarioService.contarCitasPorVeterinario(veterinarioId);
        boolean tieneCitasActivas = veterinarioService.veterinarioTieneCitasActivas(veterinarioId);

        System.out.println("\n=== ESTADÍSTICAS DEL VETERINARIO ===");
        System.out.printf("Dr. %s%n", veterinario.getNombreCompleto());
        System.out.printf("Total de citas históricas: %d%n", totalCitas);
        System.out.printf("Tiene citas activas: %s%n", tieneCitasActivas ? "Sí" : "No");
    }

    public void verificarVeterinarioActivo(Integer veterinarioId) {
        boolean tieneActivas = veterinarioService.veterinarioTieneCitasActivas(veterinarioId);
        if (tieneActivas) {
            System.out.println("El veterinario tiene citas activas (programadas o en proceso)");
        } else {
            System.out.println("El veterinario no tiene citas activas");
        }
    }

    // ================== VALIDACIONES ==================

    private boolean validarVeterinario(Veterinario veterinario) {
        if (veterinario == null) {
            System.out.println("Error: Los datos del veterinario son obligatorios");
            return false;
        }

        if (veterinario.getNombreCompleto() == null || veterinario.getNombreCompleto().trim().isEmpty()) {
            System.out.println("Error: El nombre completo es obligatorio");
            return false;
        }

        if (veterinario.getDocumentoIdentidad() == null || veterinario.getDocumentoIdentidad().trim().isEmpty()) {
            System.out.println("Error: El documento de identidad es obligatorio");
            return false;
        }

        if (veterinario.getEmail() == null || veterinario.getEmail().trim().isEmpty()) {
            System.out.println("Error: El email es obligatorio");
            return false;
        }

        if (veterinario.getTelefono() == null || veterinario.getTelefono().trim().isEmpty()) {
            System.out.println("Error: El teléfono es obligatorio");
            return false;
        }

        return true;
    }
}