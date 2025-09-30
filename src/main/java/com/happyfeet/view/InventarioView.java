package com.happyfeet.view;

import com.happyfeet.controller.InventarioController;
import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.creator.*;
import com.happyfeet.repository.IInventarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InventarioView {
    private static final Logger logger = LogManager.getLogger(InventarioView.class);
    private final InventarioController controller;
    private final Scanner input;
    private IInventarioDAO inventarioDAO;

    public InventarioView(InventarioController controller, IInventarioDAO inventarioDAO) {
        this.controller = controller;
        this.inventarioDAO = inventarioDAO;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenu() {
        String opcion = "";
        while(!opcion.equals("0")) {
            System.out.println("\n--- GESTION DE INVENTARIO (CONSOLA) ---");
            System.out.println("""
                    1. Listar todo el inventario.
                    2. Agregar inventario.
                    3. Buscar por id.
                    4. Eliminar un inventario.
                    5. Agregar stock de un inventario.
                    0. Salir.
                    >>> Elige una opcion:
                    """);

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        listarTodos();
                        break;
                    case "2":
                        agregarInventario();
                        break;
                    case "3":
                        buscarPorId();
                        break;
                    case "4":
                        eliminarInventario();
                        break;
                    case "5":
                        agregarStockInventario();
                        break;
                    case "0":
                        System.out.println("Volviendo al menu principal...");
                        break;
                    default:
                        logger.info("Error. Opcion no valida.");
                        System.out.println("Presione Enter para continuar...");
                        input.nextLine();
                }
            } catch (Exception e) {
                logger.error("Error al leer la opcion del menu: {}", e.getMessage());
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                opcion = "";
            }
        }
    }

    public void listarTodos() {
        System.out.println("\n\n --- 1. LISTAR TODO EL INVENTARIO:\n");
        controller.listarTodos();
        System.out.println("\nPresione Enter para continuar...");
        input.nextLine();
    }

    private void agregarInventario() {
        System.out.println("\n\n --- 2. AGREGAR INVENTARIO:\n");
        try {
            System.out.print("Nombre: ");
            String nombre = input.nextLine().trim();

            // Validación del tipo de producto
            System.out.println("\nTipos de Producto:");
            System.out.println("1. Medicamento");
            System.out.println("2. Vacuna");
            System.out.println("3. Insumo Médico");
            System.out.println("4. Alimento");
            System.out.print("Seleccione el tipo (1-4): ");

            Integer productoTipoId = null;
            try {
                productoTipoId = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                logger.error("Error. Debe ingresar un número");
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                return;
            }

            if (productoTipoId < 1 || productoTipoId > 4) {
                logger.error("Error. Tipo de producto no válido (debe ser 1-4)");
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                return;
            }

            System.out.print("Descripción: ");
            String descripcion = input.nextLine().trim();

            System.out.print("Fabricante: ");
            String fabricante = input.nextLine().trim();

            System.out.print("Lote: ");
            String lote = input.nextLine().trim();

            System.out.print("Cantidad Stock: ");
            Integer cantidadStock = null;
            try {
                cantidadStock = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                logger.error("Error. Cantidad de stock inválida");
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                return;
            }

            System.out.print("Stock Mínimo: ");
            Integer stockMinimo = null;
            try {
                stockMinimo = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                logger.error("Error. Stock mínimo inválido");
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                return;
            }

            System.out.print("Fecha de Vencimiento (YYYY-MM-DD): ");
            LocalDate fecha = null;
            try {
                String fechaStr = input.nextLine().trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                fecha = LocalDate.parse(fechaStr, formatter);
            } catch (DateTimeParseException e) {
                logger.error("Formato de fecha inválido. Use el formato YYYY-MM-DD");
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                return;
            }

            System.out.print("Precio de Venta: ");
            BigDecimal precioVenta = null;
            try {
                precioVenta = new BigDecimal(input.nextLine().trim());
            } catch (NumberFormatException e) {
                logger.error("Error. Precio de venta inválido");
                System.out.println("Presione Enter para continuar...");
                input.nextLine();
                return;
            }

            Inventario nuevoInventario = crearInventarioPorTipo(
                    productoTipoId, nombre, fabricante, descripcion, lote,
                    stockMinimo, cantidadStock, fecha, precioVenta
            );

            if (nuevoInventario != null) {
                controller.agregarInventario(nuevoInventario);
                System.out.println("\n✓ Inventario agregado con éxito");
            } else {
                logger.error("Error. No se pudo crear el inventario");
            }
        } catch (Exception e) {
            logger.error("Error al agregar inventario: {}", e.getMessage(), e);
        }

        System.out.println("\nPresione Enter para continuar...");
        input.nextLine();
    }

    private void buscarPorId() {
        System.out.println("\n\n --- 3. BUSCAR POR ID:\n");
        try {
            System.out.print("ID: ");
            Integer id = Integer.parseInt(input.nextLine().trim());

            if (id > 0) {
                controller.buscarPorId(id);
            } else {
                logger.error("Error. ID debe ser positivo");
            }
        } catch (NumberFormatException e) {
            logger.error("Error. ID inválido");
        }

        System.out.println("\nPresione Enter para continuar...");
        input.nextLine();
    }

    private void eliminarInventario() {
        System.out.println("\n\n --- 4. ELIMINAR UN INVENTARIO:\n");
        try {
            System.out.print("ID: ");
            Integer id = Integer.parseInt(input.nextLine().trim());

            if (id > 0) {
                System.out.print("¿Está seguro de eliminar el inventario con ID " + id + "? (S/N): ");
                String confirmacion = input.nextLine().trim().toUpperCase();

                if (confirmacion.equals("S")) {
                    controller.eliminarInventario(id);
                    System.out.println("\n✓ Inventario eliminado correctamente");
                } else {
                    System.out.println("Operación cancelada");
                }
            } else {
                logger.error("Error. ID debe ser positivo");
            }
        } catch (NumberFormatException e) {
            logger.error("Error. ID inválido");
        }

        System.out.println("\nPresione Enter para continuar...");
        input.nextLine();
    }

    private void agregarStockInventario() {
        System.out.println("\n\n --- 5. AGREGAR STOCK AL INVENTARIO:\n");
        try {
            System.out.print("ID: ");
            Integer id = Integer.parseInt(input.nextLine().trim());

            Inventario inventario = inventarioDAO.buscarPorId(id);
            if (inventario == null) {
                System.out.println("Error: No se encontró inventario con ID: " + id);
                System.out.println("\nPresione Enter para continuar...");
                input.nextLine();
                return;
            }

            System.out.print("Cantidad a agregar: ");
            Integer cantidad = Integer.parseInt(input.nextLine().trim());

            System.out.print("Nueva Fecha de Vencimiento (YYYY-MM-DD): ");
            String fechaStr = input.nextLine().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(fechaStr, formatter);

            if (cantidad > 0) {
                controller.agregarStock(inventario, cantidad, fecha);
                System.out.println("\n✓ Stock agregado con éxito");
            } else {
                logger.error("Error. La cantidad debe ser positiva");
            }
        } catch (NumberFormatException e) {
            logger.error("Error. Número inválido");
        } catch (DateTimeParseException e) {
            logger.error("Error. Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            logger.error("Error al agregar stock: {}", e.getMessage(), e);
        }

        System.out.println("\nPresione Enter para continuar...");
        input.nextLine();
    }

    private Inventario crearInventarioPorTipo(Integer tipoId, String nombre, String fabricante,
                                              String descripcion, String lote, Integer stockMinimo,
                                              Integer cantidadStock, LocalDate fecha, BigDecimal precioVenta) {
        InventarioCreator creator = null;

        switch (tipoId) {
            case 1:
                creator = new MedicamentoCreator();
                break;
            case 2:
                creator = new VacunaCreator();
                break;
            case 3:
                creator = new InsumoMedicoCreator();
                break;
            case 4:
                creator = new AlimentoCreator();
                break;
            default:
                logger.error("Tipo de producto no reconocido: {}", tipoId);
                return null;
        }

        return creator.createInventario(nombre, tipoId, fabricante, descripcion, lote,
                stockMinimo, cantidadStock, fecha, precioVenta);
    }
}