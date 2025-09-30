package com.happyfeet.view;

import com.happyfeet.util.LoggerUtil;
import java.util.Scanner;

public class MenuGeneralView {
    private final Scanner input;
    private final MenuPrincipalView menuAdministrativo;
    private final MenuPrincipalView2 menuInventario;

    public MenuGeneralView() {
        this.input = new Scanner(System.in);
        this.menuAdministrativo = new MenuPrincipalView();
        this.menuInventario = new MenuPrincipalView2();
    }

    public void mostrarMenu() {
        mostrarBienvenidaGeneral();

        String opcion = "";
        boolean continuar = true;
        while (continuar) {
            mostrarMenuSeleccion();

            // Intentar leer la entrada con un manejo más robusto
            if (input.hasNextLine()) {
                opcion = input.nextLine().trim();
                try {
                    procesarOpcion(opcion);
                } catch (Exception e) {
                    LoggerUtil.error("Error al procesar la opción del menú: " + e.getMessage());
                    System.out.println("❌ Error inesperado al procesar la opción. Intente nuevamente.");
                    pausar();
                }
                if (opcion.equals("0")) {
                    continuar = false; // Fuerza la salida del bucle al seleccionar "0"
                }
            } else {
                LoggerUtil.error("No hay entrada disponible para leer la opción");
                System.out.println("❌ Error: No se pudo leer la entrada. ¿Desea reiniciar o salir? (R/S)");
                if (input.hasNextLine()) {
                    String respuesta = input.nextLine().trim().toUpperCase();
                    if ("S".equals(respuesta)) {
                        continuar = false; // Sale del programa
                    } else if ("R".equals(respuesta)) {
                        opcion = ""; // Reinicia el bucle
                    } else {
                        System.out.println("❌ Opción no válida. Cerrando programa.");
                        continuar = false;
                    }
                } else {
                    System.out.println("❌ No se pudo leer respuesta. Cerrando programa.");
                    continuar = false;
                }
            }
        }

        mostrarDespedidaGeneral();
        if (input != null && input.ioException() == null) {
            input.close();
        }
    }

    private void mostrarBienvenidaGeneral() {
        limpiarPantalla();
        System.out.println("\n\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                               ║");
        System.out.println("║                         🐾 HAPPY FEET VETERINARIA 🐾                          ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                    SISTEMA INTEGRAL DE GESTIÓN VETERINARIA                    ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                           Versión 1.0 - 2025                                  ║");
        System.out.println("║                                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n              🌟 Cuidando la salud de tus mascotas con amor 🌟\n");
        LoggerUtil.info("Sistema Happy Feet iniciado correctamente");
    }

    private void mostrarMenuSeleccion() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        SELECCIÓN DE MÓDULO DEL SISTEMA                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════  ╣");
        System.out.println("║                                                                                 ║");
        System.out.println("║   Por favor, seleccione el módulo al que desea acceder:                         ║");
        System.out.println("║                                                                                 ║");
        System.out.println("║   ┌─────────────────────────────────────────────────────────────────────┐       ║");
        System.out.println("║   │                                                                     │       ║");
        System.out.println("║   │  1. 👨‍⚕️  MÓDULO ADMINISTRATIVO                                       │       ║");
        System.out.println("║   │                                                                     │       ║");
        System.out.println("║   │     • Gestión de Pacientes (Dueños y Mascotas)                      │       ║");
        System.out.println("║   │     • Gestión de Veterinarios                                       │       ║");
        System.out.println("║   │     • Gestión de Citas Médicas                                      │       ║");
        System.out.println("║   │     • Historial Clínico                                             │       ║");
        System.out.println("║   │     • Reportes y Estadísticas                                       │       ║");
        System.out.println("║   │                                                                     │       ║");
        System.out.println("║   └─────────────────────────────────────────────────────────────────────┘       ║");
        System.out.println("║                                                                                 ║");
        System.out.println("║   ┌─────────────────────────────────────────────────────────────────────┐       ║");
        System.out.println("║   │                                                                     │       ║");
        System.out.println("║   │  2. 🏪 MÓDULO DE INVENTARIO Y FACTURACIÓN                           │       ║");
        System.out.println("║   │                                                                     │       ║");
        System.out.println("║   │     • Gestión de Inventario                                         │       ║");
        System.out.println("║   │     • Gestión de Facturas                                           │       ║");
        System.out.println("║   │     • Items de Factura                                              │       ║");
        System.out.println("║   │     • Tipos de Producto                                             │       ║");
        System.out.println("║   │     • Control de Stock                                              │       ║");
        System.out.println("║   │                                                                     │       ║");
        System.out.println("║   └─────────────────────────────────────────────────────────────────────┘       ║");
        System.out.println("║                                                                                 ║");
        System.out.println("║   0. 🚪 Salir del Sistema                                                       ║");
        System.out.println("║                                                                                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════==╝");
        System.out.print("\n>>> Seleccione un módulo [1-2, 0]: ");
    }

    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                LoggerUtil.info("Accediendo al Módulo Administrativo");
                System.out.println("\n✓ Cargando Módulo Administrativo...\n");
                pausar();
                menuAdministrativo.mostrarMenu();
                break;
            case "2":
                LoggerUtil.info("Accediendo al Módulo de Inventario y Facturación");
                System.out.println("\n✓ Cargando Módulo de Inventario y Facturación...\n");
                pausar();
                menuInventario.mostrarMenu();
                break;
            case "0":
                LoggerUtil.info("Usuario solicitó salir del sistema");
                break;
            default:
                System.out.println("\n❌ Opción inválida. Por favor seleccione 1, 2 o 0.");
                pausar();
                break;
        }
    }

    private void mostrarDespedidaGeneral() {
        limpiarPantalla();
        System.out.println("\n\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                               ║");
        System.out.println("║                      ¡Gracias por usar Happy Feet Veterinaria!                ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                    Cuidando la salud de nuestras mascotas                     ║");
        System.out.println("║                         con amor y profesionalismo                            ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                            ¡Hasta pronto! 🐾                                  ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                          Sistema cerrado exitosamente                         ║");
        System.out.println("║                                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
        LoggerUtil.info("Sistema Happy Feet cerrado correctamente");
    }

    private void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla la limpieza, simplemente continuamos
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        try {
            if (input.hasNextLine()) {
                input.nextLine(); // Lee la entrada solo si está disponible
            } else {
                LoggerUtil.info("No hay línea disponible para pausar. Continuando automáticamente.");
                System.out.println(); // Salto de línea para mantener formato
            }
        } catch (Exception e) {
            LoggerUtil.error("Error al pausar: " + e.getMessage());
            System.out.println(); // Salto de línea como fallback
        }
    }
}