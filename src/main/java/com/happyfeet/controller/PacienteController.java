package com.happyfeet.controller;

import com.happyfeet.model.entities.Dueno;
import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.entities.HistorialMedico;
import com.happyfeet.service.PacienteService;
import com.happyfeet.service.HistorialService;
import com.happyfeet.util.LoggerUtil;

import java.util.List;

public class PacienteController {
    private PacienteService pacienteService;
    private HistorialService historialService;

    public PacienteController() {
        this.pacienteService = new PacienteService();
        this.historialService = new HistorialService();
    }

    // ================== MÉTODOS PARA DUEÑOS ==================

    public boolean registrarDueno(Dueno dueno) {
        if (validarDueno(dueno)) {
            boolean resultado = pacienteService.registrarDueno(dueno);
            if (resultado) {
                System.out.println("Dueño registrado exitosamente");
            } else {
                System.out.println("Error al registrar el dueño. Verifique los datos");
            }
            return resultado;
        }
        return false;
    }

    public boolean actualizarDueno(Dueno dueno) {
        if (validarDueno(dueno)) {
            boolean resultado = pacienteService.actualizarDueno(dueno);
            if (resultado) {
                System.out.println("Dueño actualizado exitosamente");
            } else {
                System.out.println("Error al actualizar el dueño. Verifique los datos");
            }
            return resultado;
        }
        return false;
    }

    public boolean eliminarDueno(Integer id) {
        if (id == null || id <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }

        boolean resultado = pacienteService.eliminarDueno(id);
        if (resultado) {
            System.out.println("Dueño eliminado exitosamente");
        } else {
            System.out.println("Error al eliminar el dueño. Puede que tenga mascotas asociadas");
        }
        return resultado;
    }

    public void listarDuenos() {
        List<Dueno> duenos = pacienteService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("No hay dueños registrados");
        } else {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                            LISTA DE DUEÑOS                                                 ║");
            System.out.println("╠════╦══════════════════════════════╦═══════════════╦═══════════════════════════════════╦═══════════════════╣");
            System.out.println("║ ID ║            NOMBRE            ║   DOCUMENTO   ║               EMAIL               ║     TELÉFONO      ║");
            System.out.println("╠════╬══════════════════════════════╬═══════════════╬═══════════════════════════════════╬═══════════════════╣");

            duenos.forEach(dueno -> {
                System.out.printf("║ %-2d ║ %-28s ║ %-13s ║ %-33s ║ %-17s ║%n",
                        dueno.getId(),
                        truncarTexto(dueno.getNombreCompleto(), 28),
                        dueno.getDocumentoIdentidad(),
                        truncarTexto(dueno.getEmail(), 33),
                        dueno.getTelefono());
            });

            System.out.println("╚════╩══════════════════════════════╩═══════════════╩═══════════════════════════════════╩═══════════════════╝");
            System.out.println("Total de dueños: " + duenos.size());
        }
    }


    public Dueno buscarDuenoPorId(Integer id) {
        return pacienteService.buscarDuenoPorId(id);
    }

    public Dueno buscarDuenoPorDocumento(String documento) {
        return pacienteService.buscarDuenoPorDocumento(documento);
    }

    public void buscarDuenosPorNombre(String nombre) {
        List<Dueno> duenos = pacienteService.buscarDuenosPorNombre(nombre);
        if (duenos.isEmpty()) {
            System.out.println("No se encontraron dueños con ese nombre");
        } else {
            System.out.println("\n=== DUEÑOS ENCONTRADOS ===");
            duenos.forEach(dueno -> {
                System.out.printf("ID: %d | %s | Doc: %s%n",
                        dueno.getId(), dueno.getNombreCompleto(), dueno.getDocumentoIdentidad());
            });
        }
    }

    // ================== MÉTODOS PARA MASCOTAS ==================

    public boolean registrarMascota(Mascota mascota) {
        if (validarMascota(mascota)) {
            boolean resultado = pacienteService.registrarMascota(mascota);
            if (resultado) {
                System.out.println("Mascota registrada exitosamente");
            } else {
                System.out.println("Error al registrar la mascota. Verifique los datos");
            }
            return resultado;
        }
        return false;
    }

    public boolean actualizarMascota(Mascota mascota) {
        if (validarMascota(mascota)) {
            boolean resultado = pacienteService.actualizarMascota(mascota);
            if (resultado) {
                System.out.println("Mascota actualizada exitosamente");
            } else {
                System.out.println("Error al actualizar la mascota. Verifique los datos");
            }
            return resultado;
        }
        return false;
    }

    public boolean eliminarMascota(Integer id) {
        if (id == null || id <= 0) {
            System.out.println("Error: ID inválido");
            return false;
        }

        boolean resultado = pacienteService.eliminarMascota(id);
        if (resultado) {
            System.out.println("Mascota eliminada exitosamente");
        } else {
            System.out.println("Error al eliminar la mascota");
        }
        return resultado;
    }

    public void listarMascotasCompletas() {
        List<Mascota> mascotas = pacienteService.listarMascotasConInfoCompleta();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas registradas");
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                              LISTA DE MASCOTAS                                                    ║");
            System.out.println("╠════╦════════════════╦═══════════════════════════╦══════════════╦════════╦═════════════╦══════════════════════════╣");
            System.out.println("║ ID ║     NOMBRE     ║           DUEÑO           ║     RAZA     ║  SEXO  ║  VACUNADO   ║       F.NACIMIENTO       ║");
            System.out.println("╠════╬════════════════╬═══════════════════════════╬══════════════╬════════╬═════════════╬══════════════════════════╣");

            mascotas.forEach(mascota -> {
                System.out.printf("║ %-2d ║ %-12s ║ %-25s ║ %-12s ║ %-6s ║ %-11s ║ %-24s ║%n",
                        mascota.getId(),
                        truncarTexto(mascota.getNombre(), 12),
                        truncarTexto(mascota.getNombreDueno() != null ? mascota.getNombreDueno() : "ID: " + mascota.getDuenoId(), 25),
                        truncarTexto(mascota.getNombreRaza() != null ? mascota.getNombreRaza() : "ID: " + mascota.getRazaId(), 12),
                        mascota.getSexo().toString(),
                        mascota.getVacunado().toString(),
                        mascota.getFechaNacimiento() != null ? mascota.getFechaNacimiento().toString() : "No especificada");
            });

            System.out.println("╚════╩════════════════╩═══════════════════════════╩══════════════╩════════╩═════════════╩══════════════════════════╝");
            System.out.println("Total de mascotas: " + mascotas.size());
        }
    }
    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud - 3) + "...";
    }
    public void listarMascotasPorDueno(Integer duenoId) {
        List<Mascota> mascotas = pacienteService.buscarMascotasPorDueno(duenoId);
        if (mascotas.isEmpty()) {
            System.out.println("El dueño no tiene mascotas registradas");
        } else {
            System.out.println("\n=== MASCOTAS DEL DUEÑO ===");
            mascotas.forEach(mascota -> {
                System.out.printf("ID: %d | %s | Nacimiento: %s | Vacunado: %s%n",
                        mascota.getId(), mascota.getNombre(),
                        mascota.getFechaNacimiento(), mascota.getVacunado());
            });
        }
    }

    public Mascota buscarMascotaPorId(Integer id) {
        return pacienteService.buscarMascotaPorId(id);
    }

    public void buscarMascotasPorNombre(String nombre) {
        List<Mascota> mascotas = pacienteService.buscarMascotasPorNombre(nombre);
        if (mascotas.isEmpty()) {
            System.out.println("No se encontraron mascotas con ese nombre");
        } else {
            System.out.println("\n=== MASCOTAS ENCONTRADAS ===");
            mascotas.forEach(mascota -> {
                System.out.printf("ID: %d | %s | Sexo: %s | Nacimiento: %s%n",
                        mascota.getId(), mascota.getNombre(),
                        mascota.getSexo(), mascota.getFechaNacimiento());
            });
        }
    }

    // ================== MÉTODOS PARA HISTORIAL ==================

    public boolean registrarEventoMedico(HistorialMedico historial) {
        boolean resultado = historialService.registrarEvento(historial);
        if (resultado) {
            System.out.println("Evento médico registrado exitosamente");
        } else {
            System.out.println("Error al registrar el evento médico. Verifique los datos");
        }
        return resultado;
    }

    public void mostrarHistorialMascota(Integer mascotaId) {
        List<HistorialMedico> historial = historialService.buscarHistorialPorMascota(mascotaId);
        if (historial.isEmpty()) {
            System.out.println("La mascota no tiene historial médico registrado");
        } else {
            System.out.println("\n=== HISTORIAL MÉDICO ===");
            historial.forEach(evento -> {
                System.out.printf("%s | %s | %s%n",
                        evento.getFechaEvento(),
                        historialService.obtenerNombreEventoTipo(evento.getEventoTipoId()),
                        evento.getDescripcion());
            });
        }
    }

    public void mostrarResumenMascota(Integer mascotaId) {
        String resumen = historialService.generarResumenMascota(mascotaId);
        System.out.println(resumen);
    }

    // ================== UTILIDADES ==================

    public void listarRazasDisponibles() {
        var razas = pacienteService.listarRazas();
        if (razas.isEmpty()) {
            System.out.println("No hay razas disponibles");
        } else {
            System.out.println("\n=== RAZAS DISPONIBLES ===");
            razas.forEach(raza -> {
                System.out.printf("ID: %d | %s (%s)%n",
                        raza.getId(), raza.getNombre(), raza.getNombreEspecie());
            });
        }
    }

    public void listarTiposEventosMedicos() {
        var tipos = historialService.listarTiposDeEventos();
        if (tipos.isEmpty()) {
            System.out.println("No hay tipos de eventos disponibles");
        } else {
            System.out.println("\n=== TIPOS DE EVENTOS MÉDICOS ===");
            tipos.forEach(tipo -> {
                System.out.printf("ID: %d | %s%n", tipo.getId(), tipo.getNombre());
            });
        }
    }

    // ================== VALIDACIONES ==================

    private boolean validarDueno(Dueno dueno) {
        if (dueno == null) {
            System.out.println("Error: Los datos del dueño son obligatorios");
            return false;
        }

        if (dueno.getNombreCompleto() == null || dueno.getNombreCompleto().trim().isEmpty()) {
            System.out.println("Error: El nombre completo es obligatorio");
            return false;
        }

        if (dueno.getDocumentoIdentidad() == null || dueno.getDocumentoIdentidad().trim().isEmpty()) {
            System.out.println("Error: El documento de identidad es obligatorio");
            return false;
        }

        if (dueno.getEmail() == null || dueno.getEmail().trim().isEmpty()) {
            System.out.println("Error: El email es obligatorio");
            return false;
        }

        if (dueno.getTelefono() == null || dueno.getTelefono().trim().isEmpty()) {
            System.out.println("Error: El teléfono es obligatorio");
            return false;
        }

        if (dueno.getDireccion() == null || dueno.getDireccion().trim().isEmpty()) {
            System.out.println("Error: La dirección es obligatoria");
            return false;
        }

        return true;
    }

    private boolean validarMascota(Mascota mascota) {
        if (mascota == null) {
            System.out.println("Error: Los datos de la mascota son obligatorios");
            return false;
        }

        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            System.out.println("Error: El nombre de la mascota es obligatorio");
            return false;
        }

        if (mascota.getDuenoId() == null || mascota.getDuenoId() <= 0) {
            System.out.println("Error: La mascota debe tener un dueño válido");
            return false;
        }

        if (mascota.getRazaId() == null || mascota.getRazaId() <= 0) {
            System.out.println("Error: La mascota debe tener una raza válida");
            return false;
        }

        if (mascota.getFechaNacimiento() == null) {
            System.out.println("Error: La fecha de nacimiento es obligatoria");
            return false;
        }

        if (mascota.getSexo() == null) {
            System.out.println("Error: El sexo de la mascota es obligatorio");
            return false;
        }

        if (mascota.getVacunado() == null) {
            System.out.println("Error: El estado de vacunación es obligatorio");
            return false;
        }

        return true;
    }

}



