package com.happyfeet.view;

import com.happyfeet.controller.InventarioController;
import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.Medicamento;
import com.happyfeet.model.entities.creator.*;
import com.happyfeet.repository.IInventarioDAO;
import com.happyfeet.repository.InventarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
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
                        System.out.println("Presione cualquier tecla para continuar ...");
                        input.nextLine();
                }
            }catch (Exception e){
                logger.info("Error al leer la opcion del menu {}", e.getMessage());
                System.out.println("Presione cualquier tecla para continuar...");
                input.nextLine();
                opcion = "";
            }
        }
        input.close();
    }

    public void listarTodos() {
        System.out.println("\n\n --- 1. LISTAR TODO EL INVENTARIO:\n");
        controller.listarTodos();
    }

    private void agregarInventario() {
        System.out.println("\n\n --- 2. AGREGAR INVENTARIO:\n");
        try {
            System.out.println("Nombre: ");
            String nombre = input.nextLine();

            Integer productoTipoId = leerEntero(input, "Inventario ID: ");
            if (productoTipoId < 1 || productoTipoId > 4) {
                logger.info("Error. Tipo de producto no valido");
            }

            System.out.println("Descripcion: ");
            String descripcion = input.nextLine();

            System.out.println("Fabricante: ");
            String fabricante = input.nextLine();

            System.out.println("Lote: ");
            String lote = input.nextLine();

            System.out.println("Cantidad Stock: ");
            Integer cantidadStock = input.nextInt();

            System.out.println("Stock Minimo: ");
            Integer stockMinimo = input.nextInt();


            System.out.println("Fecha de Vencimiento (YYYY-MM-DD): ");
            LocalDate fecha = null;
            try {
                fecha = LocalDate.parse(input.nextLine().trim());
            }catch (DateTimeParseException e){
                logger.error("Formato de fecha invalido. Use el formato YYYY-MM-DD");
            }

            System.out.println("Precio de Venta: ");
            BigDecimal precioVenta = input.nextBigDecimal();
            input.nextLine();

            Inventario nuevoInventario = crearInventarioPorTipo(productoTipoId, nombre, fabricante, descripcion, lote, stockMinimo, cantidadStock, fecha, precioVenta);

            if (nuevoInventario != null) {
                controller.agregarInventario(nuevoInventario);
                logger.info("Inventario agregado con exito");
            } else {
                logger.info("Error. No se pudo crear el inventario");
            }
        }catch (Exception e){
            logger.error("Error al agregar inventario: {}", e.getMessage());
        }
    }

    private void buscarPorId(){
        System.out.println("\n\n --- 3. BUSCAR POR ID:\n");
        try {
            Integer id = leerEntero(input, "ID: ");
            input.nextLine();
            if(id > 0) {
                controller.buscarPorId(id);
            }else {
                logger.info("Error. ID negativo");
            }
        }catch (Exception e){
            logger.info("Error. ID invalido");
        }
    }


    private void eliminarInventario() {
        System.out.println("\n\n --- 4. ELIMINAR UN INVENTARIO:\n");
        Integer id = leerEntero(input, "ID: ");
        input.nextLine();

        if(id > 0){
            controller.eliminarInventario(id);
            logger.info("Inventario eliminado correctamente: {}", id);
        }else {
            logger.info("Error. ID negativo");
        }
    }

    private void agregarStockInventario() {
        System.out.println("\n\n --- 5. AGREGAR STOCK AL INVENTARIO:\n");
        try {
            Integer id = leerEntero(input, "ID: ");
            input.nextLine();

            Inventario inventario = inventarioDAO.buscarPorId(id);
            if (inventario == null) {
                System.out.println("Error: No se encontró inventario con ID: " + id);
                return;
            }

            Integer cantidad = leerEntero(input, "Cantidad a agregar: ");
            input.nextLine();

            System.out.println("Nueva Fecha de Vencimiento (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(input.nextLine().trim());

            if (id > 0) {
                controller.agregarStock(inventario, cantidad, fecha);
            }
        }catch (Exception e){
            logger.error("Error al agregar stock. {}", e.getMessage());
        }
    }

    private static int leerEntero(Scanner scanner, String mensaje){
        System.out.println(mensaje);

        while(!scanner.hasNextInt()) {
            System.out.println("Ingresa un numero entero valido: ");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }

    private Inventario crearInventarioPorTipo(Integer tipoId, String nombre, String fabricante, String descripcion, String lote, Integer stockMinimo, Integer cantidadStock, LocalDate fecha, BigDecimal precioVenta) {
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
                logger.info("Tipo de producto no reconocido: {}", tipoId);
                return null;
        }

        return creator.createInventario(nombre, tipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fecha, precioVenta);
    }

}
