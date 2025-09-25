package com.happyfeet.view;

import com.happyfeet.controller.ProductoTipoController;
import com.happyfeet.model.entities.ProductoTipo;
import org.apache.logging.log4j.LogManager;

import java.util.Scanner;
import java.util.logging.Logger;

public class ProductoTipoView {
    private static final Logger logger = (Logger) LogManager.getLogger(ProductoTipoView.class);
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
                    3. Actualizar un producto tipo.
                    4. Eliminar un producto tipo.
                    0. Salir.
                    >>> Elige una opcion:
                    """);

            try {
                opcion = input.nextLine();
                switch (opcion) {
                    case "1":
                        listarTodos();
                    case "2":
                        agregarProductoTipo();
                    case "3":
                        buscarPorId();
                    case "4":
                        actualizarProductoTipo();
                    case "5":
                        eliminarProductoTipo();
                    case "0":
                        System.out.println("\nVoviendo al menu principal...");
                        break;
                    default:
                        logger.info("Error. Opcion invalida.");
                        System.out.println("Presione cualquier tecla para continuar...");
                        input.nextLine();
                }
            }catch(Exception e) {
                logger.info("Error al leer la opcion del menu " + e.getMessage());
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
        System.out.println("ID: ");
        int id = input.nextInt();

        controller.buscarPorId(id);
    }

    private void actualizarProductoTipo() {
        System.out.println("\n\n --- 4. ACTUALIZAR UN PRODUCTO TIPO:\n");
        System.out.println("ID: ");
        int id = input.nextInt();

        System.out.println("Nombre: ");
        String nombre = input.nextLine();

        controller.actualizarProductoTipo(new ProductoTipo(id, nombre));
    }

    private void eliminarProductoTipo() {
        System.out.println("\n\n --- 5. ELIMINAR UN PRODUCTO TIPO:\n");
        System.out.println("ID: ");
        int id = input.nextInt();

        controller.eliminarProductoTipo(id);
    }
}
