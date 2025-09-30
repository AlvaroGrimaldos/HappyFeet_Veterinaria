package com.happyfeet.view;

import com.happyfeet.controller.PacienteController;
import com.happyfeet.controller.CitaController;
import com.happyfeet.controller.VeterinarioController;
import com.happyfeet.util.LoggerUtil;

import java.util.Scanner;

public class MenuPrincipalView {
    private final Scanner input;
    private final PacienteView pacienteView;
    private final CitaView citaView;
    private final VeterinarioView veterinarioView;

    public MenuPrincipalView() {
        this.input = new Scanner(System.in);

        PacienteController pacienteController = new PacienteController();
        CitaController citaController = new CitaController();
        VeterinarioController veterinarioController = new VeterinarioController();

        this.pacienteView = new PacienteView(pacienteController);
        this.citaView = new CitaView(citaController);
        this.veterinarioView = new VeterinarioView(veterinarioController);
    }

    public void mostrarMenu() {
        mostrarBienvenida();

        String opcion = "";
        while (!opcion.equals("0")) {
            mostrarMenuPrincipal();

            try {
                opcion = input.nextLine().trim();
                procesarOpcion(opcion);
            } catch (Exception e) {
                LoggerUtil.error("Error al procesar la opción del menú: " + e.getMessage());
                System.out.println("Error inesperado. Intente nuevamente.");
                pausar();
                opcion = "";
            }
        }

        mostrarDespedida();
        // NO CERRAR EL SCANNER AQUÍ - Lo cierra MenuGeneralView
        // input.close(); ← LÍNEA ELIMINADA
    }

    private void mostrarBienvenida() {
        System.out.println("\n");
        System.out.println("HAPPY FEET VETERINARIA");
        System.out.println("¡Bienvenido al sistema!\n");
        LoggerUtil.info("Sistema iniciado correctamente");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n Menu Principal");
        System.out.println("1.Gestión de Pacientes (Dueños y Mascotas)");
        System.out.println("  • Registro y gestión de dueños");
        System.out.println("  • Registro y gestión de mascotas");
        System.out.println("  • Historial médico completo");
        System.out.println("2.Gestión de Veterinarios");
        System.out.println("   • Registro de veterinarios");
        System.out.println("   • Perfiles y estadísticas");
        System.out.println("3.Gestión de Citas Médicas");
        System.out.println("   • Programación de citas");
        System.out.println("   • Control de agenda");
        System.out.println("   • Seguimiento de estados");
        System.out.println("0.Salir del sistema");
        System.out.print(">>> Seleccione una opción [1-3, 0]: ");
    }

    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                LoggerUtil.info("Accediendo al módulo de Gestión de Pacientes");
                pacienteView.mostrarMenuPrincipal();
                break;

            case "2":
                LoggerUtil.info("Accediendo al módulo de Gestión de Veterinarios");
                veterinarioView.mostrarMenuPrincipal();
                break;

            case "3":
                LoggerUtil.info("Accediendo al módulo de Gestión de Citas");
                citaView.mostrarMenuPrincipal();
                break;

            case "0":
                LoggerUtil.info("Regresando al menú principal general");
                break;

            default:
                System.out.println("\nOpción inválida. Por favor seleccione una opción válida (1-3, 0).");
                pausar();
                break;
        }
    }

    private void mostrarDespedida() {
        System.out.println("\nRegresando al menú principal...");
        LoggerUtil.info("Módulo administrativo cerrado");
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        input.nextLine();
    }
}