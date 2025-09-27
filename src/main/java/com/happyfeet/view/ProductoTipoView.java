package com.happyfeet.view;

import com.happyfeet.controller.ProductoTipoController;
import com.happyfeet.model.entities.ProductoTipo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLOutput;
import java.util.Scanner;


public class ProductoTipoView {
    private static final Logger logger = LogManager.getLogger(ProductoTipoView.class);
    private final ProductoTipoController controller;
    private final Scanner input;

    public ProductoTipoView(ProductoTipoController controller) {
        this.controller = controller;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenu() {
        String opcion = "";
        while(!opcion.equals("0")) {
            System.out.println("\n--- GESTION DE PRODUCTOS TIPOS (CONSOLA) ---");
            System.out.println("""
                    1. Listar todos los productos tipos.
                    2. Agregar un producto tipo.
                    3. Buscar por id.
                    4. Actualizar un producto tipo.
                    5. Eliminar un producto tipo.
                    0. Salir.
                    >>> Elige una opcion:
                    """);

            try {
                opcion = input.nextLine();
                switch (opcion) {
                    case "1":
                        listarTodos();
                        break;
                    case "2":
                        agregarProductoTipo();
                        break;
                    case "3":
                        buscarPorId();
                        break;
                    case "4":
                        actualizarProductoTipo();
                        break;
                    case "5":
                        eliminarProductoTipo();
                        break;
                    case "0":
                        System.out.println("\nVoviendo al menu principal...");
                        break;
                    default:
                        logger.error("Error. Opcion invalida.");
                        System.out.println("Presione cualquier tecla para continuar...");
                        input.nextLine();
                }
            }catch(Exception e) {
                logger.error("Error al leer la opcion del menu {}", e.getMessage());
                System.out.println("Presione cualquier tecla para continuar...");
                input.nextLine();
                opcion = "";
            }
        }
        input.close();
    }

    private void listarTodos(){
        System.out.println("\n\n --- 1. LISTAR TODOS LOS PRODUCTOS TIPOS:\n");
        controller.listarTodos();
    }

    private void agregarProductoTipo() {
        System.out.println("\n\n --- 2. AGREGAR UN PRODUCTO TIPO:\n");
        System.out.println("Nombre: ");
        String nombre = input.nextLine();

        controller.agregarProductoTipo(new ProductoTipo(nombre));
    }

    private void buscarPorId() {
        System.out.println("\n\n --- 3. BUSCAR POR ID:\n");
        int id = leerEntero(input, "ID: ");
        input.nextLine();

        controller.buscarPorId(id);
    }

    private void actualizarProductoTipo() {
        System.out.println("\n\n --- 4. ACTUALIZAR UN PRODUCTO TIPO:\n");
        int id = leerEntero(input, "ID: ");
        input.nextLine();

        System.out.println("Nombre: ");
        String nombre = input.nextLine();

        controller.actualizarProductoTipo(new ProductoTipo(id, nombre));
    }

    private void eliminarProductoTipo() {
        System.out.println("\n\n --- 5. ELIMINAR UN PRODUCTO TIPO:\n");
        int id = leerEntero(input, "ID: ");
        input.nextLine();

        controller.eliminarProductoTipo(id);
    }

    private static int leerEntero(Scanner scanner, String mensaje){
        System.out.println(mensaje);

        while(!scanner.hasNextInt()) {
            System.out.println("Ingresa un numero entero valido: ");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
