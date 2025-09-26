package com.happyfeet.view;

import com.happyfeet.controller.InventarioController;
import com.happyfeet.model.entities.creator.InventarioCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class InventarioView {
    private static final Logger logger = (Logger) LogManager.getLogger(InventarioView.class);
    private final InventarioController controller;
    private final Scanner input;

    public InventarioView(InventarioController controller) {
        this.controller = controller;
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
                    4. Actualizar un inventario.
                    5. Eliminar un inventario.
                    6. Agregar stock de un inventario.
                    0. Salir.
                    >>> Elige una opcion:
                    """);

            try {
                opcion = input.nextLine();
                switch (opcion) {
                    case "1":
                        listarTodos();
                    case "2":
                        agregarInventario();
                    case "3":
                        buscarPorId();
                    case "4":
                        actualizarInventario();
                    case "5":
                        eliminarInventario();
                    case "6":
                        agregarStockInventario();
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
        System.out.println("Nombre: ");
        String nombre = input.nextLine();

        System.out.println("Producto Tipo ID: ");
        Integer productoTipoId = input.nextInt();

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

        System.out.println("Fecha de Vencimiento: ");
        LocalDate fecha = LocalDate.parse(input.nextLine());

        System.out.println("Precio de Venta: ");
        BigDecimal precioVenta = input.nextBigDecimal();

        controller.agregarInventario(new Inventario(nombre, productoTipoId, descripcion, fabricante, lote, cantidadStock, stockMinimo, fecha, precioVenta));

    }

    private void buscarPorId(){

    }

    private void actualizarInventario(){

    }

    private void eliminarInventario() {

    }

    private void agregarStockInventario(){

    }

    private void leerEntero(Scanner scanner, String mensaje){

    }

}
