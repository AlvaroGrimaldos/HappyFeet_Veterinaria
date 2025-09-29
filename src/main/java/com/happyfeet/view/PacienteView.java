package com.happyfeet.view;

import com.happyfeet.controller.PacienteController;
import com.happyfeet.model.entities.Dueno;
import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.entities.HistorialMedico;
import com.happyfeet.model.enums.SexoMascota;
import com.happyfeet.model.enums.EstadoVacunacion;
import com.happyfeet.util.ValidadorUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PacienteView {
    private final PacienteController controller;
    private final Scanner input;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PacienteView(PacienteController controller) {
        this.controller = controller;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenuPrincipal() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nGESTION DE PACIENTES");
            System.out.println("1. Gestión de Dueños ");
            System.out.println("2. Gestión de Mascotas  ");
            System.out.println("3. Historial Médico  ");
            System.out.println("4. Consultas y Reportes ");
            System.out.println("0. Volver al menú principal ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        mostrarMenuDuenos();
                        break;
                    case "2":
                        mostrarMenuMascotas();
                        break;
                    case "3":
                        mostrarMenuHistorial();
                        break;
                    case "4":
                        mostrarMenuConsultas();
                        break;
                    case "0":
                        System.out.println("\nRegresando al menú principal...");
                        break;
                    default:
                        System.out.println("\n Opción inválida. Intente nuevamente.");
                        pausar();
                }
            } catch (Exception e) {
                System.out.println(" Error al procesar la opción: " + e.getMessage());
                pausar();
                opcion = "";
            }
        }
    }



    private void mostrarMenuDuenos() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nGESTIÓN DE DUEÑOS");
            System.out.println("1. Registrar nuevo dueño ");
            System.out.println("2. Listar todos los dueños ");
            System.out.println("3. Buscar dueño por documento ");
            System.out.println("4. Buscar dueños por nombre ");
            System.out.println("5. Actualizar dueño  ");
            System.out.println("6. Eliminar dueño  ");
            System.out.println("0. Volver ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        registrarDueno();
                        break;
                    case "2":
                        controller.listarDuenos();
                        pausar();
                        break;
                    case "3":
                        buscarDuenoPorDocumento();
                        break;
                    case "4":
                        buscarDuenosPorNombre();
                        break;
                    case "5":
                        actualizarDueno();
                        break;
                    case "6":
                        eliminarDueno();
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("\n Opción inválida. Intente nuevamente.");
                        pausar();
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                pausar();
                opcion = "";
            }
        }
    }

    private void registrarDueno() {
        System.out.println("\n REGISTRAR NUEVO DUEÑO ");

        String nombre = "";
        String documento = "";
        String telefono = "";
        String email = "";
        String direccion = "";

        // Validar nombre
        while (true) {
            System.out.print("Nombre completo: ");
            nombre = input.nextLine().trim();
            String errorNombre = ValidadorUtil.validarNombre(nombre);
            if (errorNombre == null) {
                break;
            } else {
                System.out.println("Error " + errorNombre);
            }
        }

        // Validar documento
        while (true) {
            System.out.print("Documento de identidad: ");
            documento = input.nextLine().trim();
            String errorDocumento = ValidadorUtil.validarDocumento(documento);
            if (errorDocumento == null) {
                break;
            } else {
                System.out.println("Error " + errorDocumento);
            }
        }

        // Validar teléfono
        while (true) {
            System.out.print("Teléfono (solo números): ");
            telefono = input.nextLine().trim();
            String errorTelefono = ValidadorUtil.validarTelefono(telefono);
            if (errorTelefono == null) {
                break;
            } else {
                System.out.println("Error " + errorTelefono);
            }
        }

        // Validar email
        while (true) {
            System.out.print("Email (debe contener @): ");
            email = input.nextLine().trim();
            String errorEmail = ValidadorUtil.validarEmail(email);
            if (errorEmail == null) {
                break;
            } else {
                System.out.println("Error " + errorEmail);
            }
        }

        // Dirección (sin validación especial)
        System.out.print("Dirección: ");
        direccion = input.nextLine().trim();
        if (direccion.isEmpty()) {
            System.out.println("Error: La dirección es obligatoria");
            System.out.print("Dirección: ");
            direccion = input.nextLine().trim();
        }

        Dueno dueno = new Dueno(nombre, documento, direccion, telefono, email);
        controller.registrarDueno(dueno);
        pausar();
    }
    private void buscarDuenoPorDocumento() {
        System.out.print("\nIngrese el documento de identidad: ");
        String documento = input.nextLine().trim();

        Dueno dueno = controller.buscarDuenoPorDocumento(documento);
        if (dueno != null) {
            mostrarInformacionDueno(dueno);
        } else {
            System.out.println(" No se encontró un dueño con ese documento");
        }
        pausar();
    }

    private void buscarDuenosPorNombre() {
        System.out.print("\nIngrese el nombre (parcial o completo): ");
        String nombre = input.nextLine().trim();
        controller.buscarDuenosPorNombre(nombre);
        pausar();
    }

    private void actualizarDueno() {
        System.out.print("\nIngrese el ID del dueño a actualizar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            Dueno dueno = controller.buscarDuenoPorId(id);

            if (dueno == null) {
                System.out.println(" No se encontró un dueño con ese ID");
                pausar();
                return;
            }

            System.out.println("\n=== ACTUALIZAR DUEÑO ===");
            System.out.println("Deje en blanco los campos que no desea cambiar");

            System.out.printf("Nombre actual: %s%nNuevo nombre: ", dueno.getNombreCompleto());
            String nombre = input.nextLine().trim();
            if (!nombre.isEmpty()) dueno.setNombreCompleto(nombre);

            System.out.printf("Documento actual: %s%nNuevo documento: ", dueno.getDocumentoIdentidad());
            String documento = input.nextLine().trim();
            if (!documento.isEmpty()) dueno.setDocumentoIdentidad(documento);

            System.out.printf("Dirección actual: %s%nNueva dirección: ", dueno.getDireccion());
            String direccion = input.nextLine().trim();
            if (!direccion.isEmpty()) dueno.setDireccion(direccion);

            System.out.printf("Teléfono actual: %s%nNuevo teléfono: ", dueno.getTelefono());
            String telefono = input.nextLine().trim();
            if (!telefono.isEmpty()) dueno.setTelefono(telefono);

            System.out.printf("Email actual: %s%nNuevo email: ", dueno.getEmail());
            String email = input.nextLine().trim();
            if (!email.isEmpty()) dueno.setEmail(email);

            controller.actualizarDueno(dueno);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void eliminarDueno() {
        System.out.print("\nIngrese el ID del dueño a eliminar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());

            Dueno dueno = controller.buscarDuenoPorId(id);
            if (dueno == null) {
                System.out.println(" No se encontró un dueño con ese ID");
                pausar();
                return;
            }

            System.out.printf("\n¿Está seguro que desea eliminar al dueño '%s'? (s/n): ", dueno.getNombreCompleto());
            String confirmacion = input.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                controller.eliminarDueno(id);
            } else {
                System.out.println("Eliminación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }



    private void mostrarMenuMascotas() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nGESTIÓN DE MASCOTAS ");
            System.out.println("1. Registrar nueva mascota  ");
            System.out.println("2. Listar todas las mascotas  ");
            System.out.println("3. Buscar mascotas por nombre ");
            System.out.println("4. Ver mascotas de un dueño  ");
            System.out.println("5. Actualizar mascota ");
            System.out.println("6. Eliminar mascota ");
            System.out.println("7. Ver razas disponibles ");
            System.out.println("0. Volver ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        registrarMascota();
                        break;
                    case "2":
                        controller.listarMascotasCompletas();
                        pausar();
                        break;
                    case "3":
                        buscarMascotasPorNombre();
                        break;
                    case "4":
                        verMascotasDeDueno();
                        break;
                    case "5":
                        actualizarMascota();
                        break;
                    case "6":
                        eliminarMascota();
                        break;
                    case "7":
                        controller.listarRazasDisponibles();
                        pausar();
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("\n Opción inválida. Intente nuevamente.");
                        pausar();
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                pausar();
                opcion = "";
            }
        }
    }

    private void registrarMascota() {
        System.out.println("\n=== REGISTRAR NUEVA MASCOTA ===");

        System.out.print("Nombre de la mascota: ");
        String nombre = input.nextLine().trim();

        System.out.print("ID del dueño: ");
        int duenoId = Integer.parseInt(input.nextLine().trim());

        // Mostrar razas disponibles
        System.out.println("\nRazas disponibles:");
        controller.listarRazasDisponibles();
        System.out.print("ID de la raza: ");
        int razaId = Integer.parseInt(input.nextLine().trim());

        System.out.print("Fecha de nacimiento (yyyy-mm-dd): ");
        LocalDate fechaNacimiento = LocalDate.parse(input.nextLine().trim(), dateFormatter);

        System.out.print("Sexo (Macho/Hembra): ");
        String sexoStr = input.nextLine().trim();
        SexoMascota sexo = SexoMascota.fromString(sexoStr);

        System.out.print("URL de la foto (opcional): ");
        String urlFoto = input.nextLine().trim();
        if (urlFoto.isEmpty()) urlFoto = null;

        System.out.print("¿Está vacunada? (Si/No): ");
        String vacunadoStr = input.nextLine().trim();
        EstadoVacunacion vacunado = EstadoVacunacion.fromString(vacunadoStr);

        Mascota mascota = new Mascota(duenoId, nombre, razaId, fechaNacimiento, sexo, urlFoto, vacunado);
        controller.registrarMascota(mascota);
        pausar();
    }

    private void buscarMascotasPorNombre() {
        System.out.print("\nIngrese el nombre (parcial o completo): ");
        String nombre = input.nextLine().trim();
        controller.buscarMascotasPorNombre(nombre);
        pausar();
    }

    private void verMascotasDeDueno() {
        System.out.print("\nIngrese el ID del dueño: ");
        try {
            int duenoId = Integer.parseInt(input.nextLine().trim());
            controller.listarMascotasPorDueno(duenoId);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        }
        pausar();
    }

    private void actualizarMascota() {
        System.out.print("\nIngrese el ID de la mascota a actualizar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            Mascota mascota = controller.buscarMascotaPorId(id);

            if (mascota == null) {
                System.out.println(" No se encontró una mascota con ese ID");
                pausar();
                return;
            }

            System.out.println("\n=== ACTUALIZAR MASCOTA ===");
            System.out.println("Deje en blanco los campos que no desea cambiar");

            System.out.printf("Nombre actual: %s%nNuevo nombre: ", mascota.getNombre());
            String nombre = input.nextLine().trim();
            if (!nombre.isEmpty()) mascota.setNombre(nombre);

            System.out.printf("Estado de vacunación actual: %s%nNuevo estado (Si/No): ", mascota.getVacunado());
            String vacunadoStr = input.nextLine().trim();
            if (!vacunadoStr.isEmpty()) {
                mascota.setVacunado(EstadoVacunacion.fromString(vacunadoStr));
            }

            controller.actualizarMascota(mascota);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        }
        pausar();
    }

    private void eliminarMascota() {
        System.out.print("\nIngrese el ID de la mascota a eliminar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());

            Mascota mascota = controller.buscarMascotaPorId(id);
            if (mascota == null) {
                System.out.println("No se encontró una mascota con ese ID");
                pausar();
                return;
            }

            System.out.printf("\n¿Está seguro que desea eliminar a la mascota '%s'? (s/n): ", mascota.getNombre());
            String confirmacion = input.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                controller.eliminarMascota(id);
            } else {
                System.out.println("Eliminación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        }
        pausar();
    }


    private void mostrarMenuHistorial() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nHISTORIAL MÉDICO");
            System.out.println("HISTORIAL MÉDICO ");
            System.out.println("1. Registrar evento médico ");
            System.out.println("2. Ver historial de una mascota  ");
            System.out.println("3. Ver resumen médico de mascota  ");
            System.out.println("4. Ver tipos de eventos disponibles ");
            System.out.println("0. Volver ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        registrarEventoMedico();
                        break;
                    case "2":
                        verHistorialMascota();
                        break;
                    case "3":
                        verResumenMascota();
                        break;
                    case "4":
                        controller.listarTiposEventosMedicos();
                        pausar();
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("\nOpción inválida. Intente nuevamente.");
                        pausar();
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                pausar();
                opcion = "";
            }
        }
    }

    private void registrarEventoMedico() {
        System.out.println("\n=== REGISTRAR EVENTO MÉDICO ===");

        System.out.print("ID de la mascota: ");
        int mascotaId = Integer.parseInt(input.nextLine().trim());

        System.out.print("Fecha del evento (yyyy-mm-dd): ");
        LocalDate fechaEvento = LocalDate.parse(input.nextLine().trim(), dateFormatter);

        System.out.println("\nTipos de eventos disponibles:");
        controller.listarTiposEventosMedicos();
        System.out.print("ID del tipo de evento: ");
        int eventoTipoId = Integer.parseInt(input.nextLine().trim());

        System.out.print("Descripción del evento: ");
        String descripcion = input.nextLine().trim();

        System.out.print("Diagnóstico: ");
        String diagnostico = input.nextLine().trim();

        System.out.print("Tratamiento recomendado: ");
        String tratamiento = input.nextLine().trim();

        // Por ahora usamos centro veterinario fijo (ID 1)
        int centroVeterinarioId = 1;

        HistorialMedico historial = new HistorialMedico(mascotaId, fechaEvento, eventoTipoId,
                descripcion, diagnostico, tratamiento,
                centroVeterinarioId);
        controller.registrarEventoMedico(historial);
        pausar();
    }

    private void verHistorialMascota() {
        System.out.print("\nIngrese el ID de la mascota: ");
        try {
            int mascotaId = Integer.parseInt(input.nextLine().trim());
            controller.mostrarHistorialMascota(mascotaId);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        }
        pausar();
    }

    private void verResumenMascota() {
        System.out.print("\nIngrese el ID de la mascota: ");
        try {
            int mascotaId = Integer.parseInt(input.nextLine().trim());
            controller.mostrarResumenMascota(mascotaId);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }


    private void mostrarMenuConsultas() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nCONSULTAS Y REPORTES ");
            System.out.println("1. Buscar dueño por documento ");
            System.out.println("2. Ver todas las mascotas ");
            System.out.println("3. Ver todos los dueños  ");
            System.out.println("4. Ver razas disponibles  ");
            System.out.println("0. Volver ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        buscarDuenoPorDocumento();
                        break;
                    case "2":
                        controller.listarMascotasCompletas();
                        pausar();
                        break;
                    case "3":
                        controller.listarDuenos();
                        pausar();
                        break;
                    case "4":
                        controller.listarRazasDisponibles();
                        pausar();
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("\n Opción inválida. Intente nuevamente.");
                        pausar();
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                pausar();
                opcion = "";
            }
        }
    }

    // ================== UTILIDADES ==================

    private void mostrarInformacionDueno(Dueno dueno) {
        System.out.println("\n=== INFORMACIÓN DEL DUEÑO ===");
        System.out.printf("ID: %d%n", dueno.getId());
        System.out.printf("Nombre: %s%n", dueno.getNombreCompleto());
        System.out.printf("Documento: %s%n", dueno.getDocumentoIdentidad());
        System.out.printf("Dirección: %s%n", dueno.getDireccion());
        System.out.printf("Teléfono: %s%n", dueno.getTelefono());
        System.out.printf("Email: %s%n", dueno.getEmail());
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        input.nextLine();
    }
}