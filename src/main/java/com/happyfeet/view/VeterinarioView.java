package com.happyfeet.view;

import com.happyfeet.controller.VeterinarioController;
import com.happyfeet.model.entities.Veterinario;
import com.happyfeet.util.ValidadorUtil;

import java.util.Scanner;

public class VeterinarioView {
    private final VeterinarioController controller;
    private final Scanner input;

    public VeterinarioView(VeterinarioController controller) {
        this.controller = controller;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenuPrincipal() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nGESTIÓN DE VETERINARIOS");
            System.out.println(" 1. Registrar nuevo veterinario    ");
            System.out.println(" 2. Listar todos los veterinarios    ");
            System.out.println(" 3. Listar veterinarios disponibles    ");
            System.out.println(" 4. Buscar veterinario por documento    ");
            System.out.println(" 5. Buscar veterinarios por nombre    ");
            System.out.println(" 6. Ver perfil de veterinario         ");
            System.out.println(" 7. Actualizar veterinario             ");
            System.out.println(" 8. Eliminar veterinario         ");
            System.out.println(" 9. Reportes y estadísticas     ");
            System.out.println(" 0. Volver al menú principal     ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        registrarVeterinario();
                        break;
                    case "2":
                        controller.listarVeterinarios();
                        pausar();
                        break;
                    case "3":
                        controller.listarVeterinariosDisponibles();
                        pausar();
                        break;
                    case "4":
                        buscarVeterinarioPorDocumento();
                        break;
                    case "5":
                        buscarVeterinariosPorNombre();
                        break;
                    case "6":
                        verPerfilVeterinario();
                        break;
                    case "7":
                        actualizarVeterinario();
                        break;
                    case "8":
                        eliminarVeterinario();
                        break;
                    case "9":
                        mostrarMenuReportes();
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


    private void registrarVeterinario() {
        System.out.println("\n=== REGISTRAR NUEVO VETERINARIO ===");

        String nombre = "";
        String documento = "";
        String telefono = "";
        String email = "";

        // Validar nombre
        while (true) {
            System.out.print("Nombre completo del veterinario: ");
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
            System.out.print("Email profesional (debe contener @): ");
            email = input.nextLine().trim();
            String errorEmail = ValidadorUtil.validarEmail(email);
            if (errorEmail == null) {
                break;
            } else {
                System.out.println("Error " + errorEmail);
            }
        }

        Veterinario veterinario = new Veterinario(nombre, documento, telefono, email);
        controller.registrarVeterinario(veterinario);
        pausar();
    }


    private void buscarVeterinarioPorDocumento() {
        System.out.print("\nIngrese el documento de identidad: ");
        String documento = input.nextLine().trim();

        Veterinario veterinario = controller.buscarVeterinarioPorDocumento(documento);
        if (veterinario != null) {
            mostrarInformacionVeterinario(veterinario);
        } else {
            System.out.println("No se encontró un veterinario con ese documento");
        }
        pausar();
    }

    private void buscarVeterinariosPorNombre() {
        System.out.print("\nIngrese el nombre (parcial o completo): ");
        String nombre = input.nextLine().trim();
        controller.buscarVeterinariosPorNombre(nombre);
        pausar();
    }

    private void verPerfilVeterinario() {
        System.out.print("\nIngrese el ID del veterinario: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            controller.mostrarPerfilVeterinario(id);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    // ================== ACTUALIZACIÓN ==================

    private void actualizarVeterinario() {
        System.out.print("\nIngrese el ID del veterinario a actualizar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            Veterinario veterinario = controller.buscarVeterinarioPorId(id);

            if (veterinario == null) {
                System.out.println(" No se encontró un veterinario con ese ID");
                pausar();
                return;
            }

            System.out.println("\nACTUALIZAR VETERINARIO ");
            System.out.println("Deje en blanco los campos que no desea cambiar");

            System.out.printf("Nombre actual: %s%nNuevo nombre: ", veterinario.getNombreCompleto());
            String nombre = input.nextLine().trim();
            if (!nombre.isEmpty()) veterinario.setNombreCompleto(nombre);

            System.out.printf("Documento actual: %s%nNuevo documento: ", veterinario.getDocumentoIdentidad());
            String documento = input.nextLine().trim();
            if (!documento.isEmpty()) veterinario.setDocumentoIdentidad(documento);

            System.out.printf("Teléfono actual: %s%nNuevo teléfono: ", veterinario.getTelefono());
            String telefono = input.nextLine().trim();
            if (!telefono.isEmpty()) veterinario.setTelefono(telefono);

            System.out.printf("Email actual: %s%nNuevo email: ", veterinario.getEmail());
            String email = input.nextLine().trim();
            if (!email.isEmpty()) veterinario.setEmail(email);

            controller.actualizarVeterinario(veterinario);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }



    private void eliminarVeterinario() {
        System.out.print("\nIngrese el ID del veterinario a eliminar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());

            Veterinario veterinario = controller.buscarVeterinarioPorId(id);
            if (veterinario == null) {
                System.out.println(" No se encontró un veterinario con ese ID");
                pausar();
                return;
            }

            // Mostrar información del veterinario
            mostrarInformacionVeterinario(veterinario);

            // Verificar si tiene citas activas
            controller.verificarVeterinarioActivo(id);

            System.out.printf("\n¿Está seguro que desea eliminar al Dr. %s? (s/n): ",
                    veterinario.getNombreCompleto());
            String confirmacion = input.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                controller.eliminarVeterinario(id);
            } else {
                System.out.println("Eliminación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }



    private void mostrarMenuReportes() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nREPORTES Y ESTADÍSTICAS");
            System.out.println("1. Reporte general de veterinarios ");
            System.out.println("2. Estadísticas de veterinario específico ");
            System.out.println("3. Verificar estado de veterinario");
            System.out.println("0. Volver");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        controller.mostrarReporteVeterinarios();
                        pausar();
                        break;
                    case "2":
                        mostrarEstadisticasVeterinario();
                        break;
                    case "3":
                        verificarEstadoVeterinario();
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

    private void mostrarEstadisticasVeterinario() {
        System.out.print("\nIngrese el ID del veterinario: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            controller.mostrarEstadisticasVeterinario(id);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void verificarEstadoVeterinario() {
        System.out.print("\nIngrese el ID del veterinario: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());

            Veterinario veterinario = controller.buscarVeterinarioPorId(id);
            if (veterinario == null) {
                System.out.println(" No se encontró un veterinario con ese ID");
                pausar();
                return;
            }

            System.out.printf("\n=== ESTADO DEL DR. %s ===\n", veterinario.getNombreCompleto());
            controller.verificarVeterinarioActivo(id);
            controller.mostrarEstadisticasVeterinario(id);

        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }


    private void mostrarInformacionVeterinario(Veterinario veterinario) {
        System.out.println("\n=== INFORMACIÓN DEL VETERINARIO ===");
        System.out.printf("ID: %d%n", veterinario.getId());
        System.out.printf("Nombre: Dr. %s%n", veterinario.getNombreCompleto());
        System.out.printf("Documento: %s%n", veterinario.getDocumentoIdentidad());
        System.out.printf("Teléfono: %s%n", veterinario.getTelefono());
        System.out.printf("Email: %s%n", veterinario.getEmail());
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        input.nextLine();
    }
}