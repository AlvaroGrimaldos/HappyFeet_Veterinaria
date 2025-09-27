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
                opcion = input.nextLine();
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

            System.out.println("Servicio Descripcion: ");
            String servicioDescripcion = input.nextLine();

            Integer cantidad = leerEntero(input, "Cantidad: ");

            Inventario inventario = inventarioDAO.buscarPorId(productoId);
            BigDecimal precio_unitario = inventario.getPrecioVenta();

            BigDecimal subtotal = precio_unitario.multiply(BigDecimal.valueOf(cantidad));

            controller.agregarItemFactura(new ItemsFactura(facturaId, productoId, servicioDescripcion, cantidad, precio_unitario, subtotal));
        }catch (Exception e){
            logger.error("Error al agregar item factura: {}", e.getMessage());
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


}
