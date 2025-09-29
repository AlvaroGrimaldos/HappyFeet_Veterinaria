package com.happyfeet.view;

import com.happyfeet.controller.CitaController;
import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.enums.EstadoCita;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CitaView {
    private final CitaController controller;
    private final Scanner input;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CitaView(CitaController controller) {
        this.controller = controller;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenuPrincipal() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n GESTIÓN DE CITAS - HAPPY FEET");
            System.out.println("1. Programar nueva cita ");
            System.out.println("2. Ver todas las citas");
            System.out.println("3. Ver citas del día");
            System.out.println("4. Ver citas pendientes");
            System.out.println("5. Ver citas en proceso");
            System.out.println("6. Gestionar citas");
            System.out.println("7. Consultas por fecha");
            System.out.println("8. Agenda de veterinario");
            System.out.println("9. Reportes y estadísticas");
            System.out.println("0. Volver al menú principal");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        programarNuevaCita();
                        break;
                    case "2":
                        controller.listarTodasLasCitas();
                        pausar();
                        break;
                    case "3":
                        controller.listarCitasDelDia();
                        pausar();
                        break;
                    case "4":
                        controller.listarCitasPendientes();
                        pausar();
                        break;
                    case "5":
                        controller.listarCitasEnProceso();
                        pausar();
                        break;
                    case "6":
                        mostrarMenuGestionCitas();
                        break;
                    case "7":
                        mostrarMenuConsultasFecha();
                        break;
                    case "8":
                        verAgendaVeterinario();
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



    private void programarNuevaCita() {
        System.out.println("\nProgramar Cita");

        try {
            System.out.print("ID de la mascota: ");
            int mascotaId = Integer.parseInt(input.nextLine().trim());

            // Mostrar veterinarios disponibles
            System.out.println("\nVeterinarios disponibles:");
            controller.listarVeterinariosDisponibles();
            System.out.print("ID del veterinario: ");
            int veterinarioId = Integer.parseInt(input.nextLine().trim());

            System.out.print("Fecha de la cita (yyyy-mm-dd): ");
            LocalDate fecha = LocalDate.parse(input.nextLine().trim(), dateFormatter);

            System.out.print("Hora de la cita (HH:mm): ");
            String horaStr = input.nextLine().trim();
            LocalDateTime fechaHora = LocalDateTime.of(fecha,
                    java.time.LocalTime.parse(horaStr, timeFormatter));


            if (!controller.verificarDisponibilidad(veterinarioId, fechaHora)) {
                System.out.println(" El veterinario no está disponible en ese horario");
                pausar();
                return;
            }

            System.out.print("Motivo de la cita: ");
            String motivo = input.nextLine().trim();

            Cita cita = new Cita(mascotaId, fechaHora, motivo, EstadoCita.PROGRAMADA, veterinarioId);
            controller.programarCita(cita);

        } catch (NumberFormatException e) {
            System.out.println(" Error: Ingrese un número válido");
        } catch (DateTimeParseException e) {
            System.out.println(" Error: Formato de fecha u hora inválido");
        } catch (Exception e) {
            System.out.println(" Error inesperado: " + e.getMessage());
        }
        pausar();
    }



    private void mostrarMenuGestionCitas() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nGESTIÓN DE CITAS ");
            System.out.println("1. Iniciar cita     ");
            System.out.println("2. Finalizar cita   ");
            System.out.println("3. Cancelar cita    ");
            System.out.println("4. Actualizar cita  ");
            System.out.println("5. Eliminar cita    ");
            System.out.println("6. Buscar cita por ID ");
            System.out.println("0. Volver ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        iniciarCita();
                        break;
                    case "2":
                        finalizarCita();
                        break;
                    case "3":
                        cancelarCita();
                        break;
                    case "4":
                        actualizarCita();
                        break;
                    case "5":
                        eliminarCita();
                        break;
                    case "6":
                        buscarCitaPorId();
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

    private void iniciarCita() {
        System.out.println("\n=== INICIAR CITA ===");
        System.out.println("Citas pendientes:");
        controller.listarCitasPendientes();

        try {
            System.out.print("\nIngrese el ID de la cita a iniciar: ");
            int citaId = Integer.parseInt(input.nextLine().trim());
            controller.iniciarCita(citaId);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void finalizarCita() {
        System.out.println("\n=== FINALIZAR CITA ===");
        System.out.println("Citas en proceso:");
        controller.listarCitasEnProceso();

        try {
            System.out.print("\nIngrese el ID de la cita a finalizar: ");
            int citaId = Integer.parseInt(input.nextLine().trim());
            controller.finalizarCita(citaId);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void cancelarCita() {
        System.out.print("\nIngrese el ID de la cita a cancelar: ");
        try {
            int citaId = Integer.parseInt(input.nextLine().trim());

            Cita cita = controller.buscarCitaPorId(citaId);
            if (cita == null) {
                System.out.println(" No se encontró una cita con ese ID");
                pausar();
                return;
            }

            System.out.printf("\n¿Está seguro que desea cancelar la cita del %s? (s/n): ",
                    cita.getFechaHora());
            String confirmacion = input.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                controller.cancelarCita(citaId);
            } else {
                System.out.println("Cancelación no realizada");
            }
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void actualizarCita() {
        System.out.print("\nIngrese el ID de la cita a actualizar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            Cita cita = controller.buscarCitaPorId(id);

            if (cita == null) {
                System.out.println(" No se encontró una cita con ese ID");
                pausar();
                return;
            }

            System.out.println("\n=== ACTUALIZAR CITA ===");
            System.out.println("Deje en blanco los campos que no desea cambiar");

            System.out.printf("Fecha y hora actual: %s%nNueva fecha y hora (yyyy-mm-dd HH:mm): ",
                    cita.getFechaHora());
            String fechaHoraStr = input.nextLine().trim();
            if (!fechaHoraStr.isEmpty()) {
                LocalDateTime nuevaFechaHora = LocalDateTime.parse(fechaHoraStr, dateTimeFormatter);
                cita.setFechaHora(nuevaFechaHora);
            }

            System.out.printf("Motivo actual: %s%nNuevo motivo: ", cita.getMotivo());
            String motivo = input.nextLine().trim();
            if (!motivo.isEmpty()) {
                cita.setMotivo(motivo);
            }

            controller.actualizarCita(cita);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        } catch (DateTimeParseException e) {
            System.out.println(" Formato de fecha inválido");
        }
        pausar();
    }

    private void eliminarCita() {
        System.out.print("\nIngrese el ID de la cita a eliminar: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());

            Cita cita = controller.buscarCitaPorId(id);
            if (cita == null) {
                System.out.println(" No se encontró una cita con ese ID");
                pausar();
                return;
            }

            System.out.printf("\n¿Está seguro que desea eliminar la cita del %s? (s/n): ",
                    cita.getFechaHora());
            String confirmacion = input.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                controller.eliminarCita(id);
            } else {
                System.out.println("Eliminación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void buscarCitaPorId() {
        System.out.print("\nIngrese el ID de la cita: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            Cita cita = controller.buscarCitaPorId(id);

            if (cita != null) {
                mostrarInformacionCita(cita);
            } else {
                System.out.println(" No se encontró una cita con ese ID");
            }
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }



    private void mostrarMenuConsultasFecha() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nCONSULTAS POR FECHA");
            System.out.println("1. Citas por fecha específica  ");
            System.out.println("2. Citas por mascota ");
            System.out.println("3. Citas por veterinario ");
            System.out.println("0. Volver  ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        buscarCitasPorFecha();
                        break;
                    case "2":
                        buscarCitasPorMascota();
                        break;
                    case "3":
                        buscarCitasPorVeterinario();
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

    private void buscarCitasPorFecha() {
        System.out.print("\nIngrese la fecha (yyyy-mm-dd): ");
        try {
            LocalDate fecha = LocalDate.parse(input.nextLine().trim(), dateFormatter);
            controller.buscarCitasPorFecha(fecha);
        } catch (DateTimeParseException e) {
            System.out.println(" Formato de fecha inválido");
        }
        pausar();
    }

    private void buscarCitasPorMascota() {
        System.out.print("\nIngrese el ID de la mascota: ");
        try {
            int mascotaId = Integer.parseInt(input.nextLine().trim());
            controller.buscarCitasPorMascota(mascotaId);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }

    private void buscarCitasPorVeterinario() {
        System.out.print("\nIngrese el ID del veterinario: ");
        try {
            int veterinarioId = Integer.parseInt(input.nextLine().trim());
            controller.buscarCitasPorVeterinario(veterinarioId);
        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        }
        pausar();
    }


    private void verAgendaVeterinario() {
        System.out.println("\n=== AGENDA DE VETERINARIO ===");

        try {
            System.out.print("ID del veterinario: ");
            int veterinarioId = Integer.parseInt(input.nextLine().trim());

            System.out.print("Fecha (yyyy-mm-dd): ");
            LocalDate fecha = LocalDate.parse(input.nextLine().trim(), dateFormatter);

            controller.mostrarAgendaVeterinario(veterinarioId, fecha);

        } catch (NumberFormatException e) {
            System.out.println(" ID inválido");
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido");
        }
        pausar();
    }



    private void mostrarMenuReportes() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\nREPORTES Y ESTADÍSTICAS ");
            System.out.println("1. Estadísticas de citas por período");
            System.out.println("2. Verificar disponibilidad de veterinario ");
            System.out.println("0. Volver ");
            System.out.print(">>> Seleccione una opción: ");

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        mostrarEstadisticasCitas();
                        break;
                    case "2":
                        verificarDisponibilidadVeterinario();
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

    private void mostrarEstadisticasCitas() {
        System.out.println("\n=== ESTADÍSTICAS DE CITAS ===");

        try {
            System.out.print("Fecha de inicio (yyyy-mm-dd): ");
            LocalDate fechaInicio = LocalDate.parse(input.nextLine().trim(), dateFormatter);

            System.out.print("Fecha de fin (yyyy-mm-dd): ");
            LocalDate fechaFin = LocalDate.parse(input.nextLine().trim(), dateFormatter);

            controller.mostrarEstadisticasCitas(fechaInicio, fechaFin);

        } catch (DateTimeParseException e) {
            System.out.println(" Formato de fecha inválido");
        }
        pausar();
    }

    private void verificarDisponibilidadVeterinario() {
        System.out.println("\n=== VERIFICAR DISPONIBILIDAD ===");

        try {
            System.out.print("ID del veterinario: ");
            int veterinarioId = Integer.parseInt(input.nextLine().trim());

            System.out.print("Fecha y hora (yyyy-mm-dd HH:mm): ");
            String fechaHoraStr = input.nextLine().trim();
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, dateTimeFormatter);

            controller.verificarDisponibilidad(veterinarioId, fechaHora);

        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha u hora inválido");
        }
        pausar();
    }



    private void mostrarInformacionCita(Cita cita) {
        System.out.println("\nInformacion Cita");
        System.out.printf("ID: %d%n", cita.getId());
        System.out.printf("Mascota ID: %d%n", cita.getMascotaId());
        System.out.printf("Veterinario ID: %d%n", cita.getVeterinarioId());
        System.out.printf("Fecha y hora: %s%n", cita.getFechaHora());
        System.out.printf("Estado: %s%n", cita.getEstado());
        System.out.printf("Motivo: %s%n", cita.getMotivo());

        if (cita.getNombreMascota() != null) {
            System.out.printf("Nombre mascota: %s%n", cita.getNombreMascota());
        }
        if (cita.getNombreDueno() != null) {
            System.out.printf("Dueño: %s%n", cita.getNombreDueno());
        }
        if (cita.getNombreVeterinario() != null) {
            System.out.printf("Veterinario: %s%n", cita.getNombreVeterinario());
        }
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        input.nextLine();
    }
}