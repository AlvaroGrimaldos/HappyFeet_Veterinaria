package com.happyfeet.view;

import com.happyfeet.controller.ItemsFacturaController;
import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.ItemsFactura;
import com.happyfeet.repository.IInventarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ItemsFacturaView {
    private static final Logger logger = LogManager.getLogger(ItemsFacturaView.class);
    private final ItemsFacturaController controller;
    private final Scanner input;
    private IInventarioDAO inventarioDAO;


    public ItemsFacturaView(ItemsFacturaController controller, IInventarioDAO inventarioDAO) {
        this.controller = controller;
        this.inventarioDAO = inventarioDAO;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenu() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n--- GESTION DE ITEMS FACTURA (CONSOLA) ---");
            System.out.println("""
                    1. Listar todos los items factura.
                    2. Agregar un item factura.
                    3. Buscar por id.
                    4. Eliminar un item factura.
                    0. Salir.
                    >>> Elige una opcion:
                    """);

            try{
                opcion = input.nextLine().trim();
                switch (opcion){
                    case "1":
                        listarTodos();
                        break;
                    case "2":
                        agregarItemFactura();
                        break;
                    case "3":
                        buscarPorId();
                        break;
                    case "4":
                        eliminarItemFactura();
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menu principal...");
                        break;
                    default:
                        logger.error("Error. Opcion invalida");
                        System.out.println("Presione cualquier tecla para continuar...");
                        input.nextLine();
                }
            }catch (Exception e){
                logger.error("Error al leer la opcion del menu {}", e.getMessage());
                System.out.println("Presione cualquier tecla para continuar...");
                input.nextLine();
                opcion = "";
            }
        }
        input.close();
    }

    private void listarTodos(){
        System.out.println("\n\n --- 1. LISTAR TODOS LOS ITEMS FACTURA:\n");
        controller.listarTodos();
    }

    private void agregarItemFactura() {
        System.out.println("\n\n --- 2. AGREGAR ITEM FACTURA:\n");
        try{
            Integer facturaId = leerEntero(input, "Factura ID: ");

            Integer productoId = leerEntero(input, "Producto ID: ");

            Inventario inventario = inventarioDAO.buscarPorId(productoId);
            if (inventario == null) {
                logger.error("Error: No se encontró el producto con ID: {}",  productoId);
                return;
            }

            System.out.println("Servicio Descripcion: ");
            String servicioDescripcion = input.nextLine();

            Integer cantidad;
            do {
                cantidad = leerEntero(input, "Cantidad (disponible: " + inventario.getCantidadStock() + "): ");
                if (cantidad > inventario.getCantidadStock()) {
                    System.out.println("Error: Cantidad solicitada excede el stock disponible.");
                }
            } while (cantidad > inventario.getCantidadStock());

            BigDecimal precio_unitario = inventario.getPrecioVenta();

            BigDecimal subtotal = precio_unitario.multiply(BigDecimal.valueOf(cantidad));

            controller.agregarItemFactura(new ItemsFactura(facturaId, productoId, servicioDescripcion, precio_unitario, cantidad, subtotal));
        }catch (Exception e){
            logger.error("Error al agregar item factura: {}", e.getMessage());
        }
    }

    private void buscarPorId(){
        System.out.println("\n\n --- 3. BUSCAR POR ID:\n");
        Integer id = leerEntero(input, "ID: ");
        input.nextLine();

        controller.buscarPorId(id);
    }

    private void eliminarItemFactura() {
        System.out.println("\n\n --- 5. ELIMINAR UN ITEM FACTURA:\n");
        Integer id = leerEntero(input, "ID: ");
        input.nextLine();

        controller.eliminarItemFactura(id);
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


}
