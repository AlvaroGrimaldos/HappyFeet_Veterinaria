package com.happyfeet.view;

import com.happyfeet.controller.FacturaController;
import com.happyfeet.model.entities.Facturas;
import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.IInventarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FacturaView {
    private static final Logger logger = LogManager.getLogger(FacturaView.class);
    private final FacturaController controller;
    private final Scanner input;
    private IInventarioDAO inventarioDAO;

    public FacturaView(FacturaController controller, IInventarioDAO inventarioDAO) {
        this.controller = controller;
        this.inventarioDAO = inventarioDAO;
        this.input = new Scanner(System.in);
    }

    public void mostrarMenu() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n--- GESTION DE FACTURAS (CONSOLA) ---");
            System.out.println("""
                    1. Listar todas las facturas.
                    2. Crear factura completa.
                    3. Buscar factura por ID.
                    4. Imprimir factura por ID.
                    5. Eliminar factura.
                    0. Salir.
                    >>> Elige una opcion:
                    """);

            try {
                opcion = input.nextLine().trim();
                switch (opcion) {
                    case "1":
                        listarTodas();
                        break;
                    case "2":
                        crearFacturaCompleta();
                        break;
                    case "3":
                        buscarPorId();
                        break;
                    case "4":
                        imprimirFactura();
                        break;
                    case "5":
                        eliminarFactura();
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menu principal...");
                        break;
                    default:
                        logger.error("Error. Opcion invalida");
                        System.out.println("Presione cualquier tecla para continuar...");
                        input.nextLine();
                }
            } catch (Exception e) {
                logger.error("Error al leer la opcion del menu {}", e.getMessage());
                System.out.println("Presione cualquier tecla para continuar...");
                input.nextLine();
                opcion = "";
            }
        }
    }

    private void listarTodas() {
        System.out.println("\n\n --- 1. LISTAR TODAS LAS FACTURAS:\n");
        controller.listarTodas();
        System.out.println("\nPresione cualquier tecla para continuar...");
        input.nextLine();
    }

    private void crearFacturaCompleta() {
        System.out.println("\n\n --- 2. CREAR FACTURA COMPLETA:\n");

        try {
            Integer duenoId = leerEntero(input, "ID del dueno: ");
            Integer centroVeterinarioId = leerEntero(input, "ID del centro veterinario: ");

            List<FacturaController.ItemFacturaRequest> itemsRequest = new ArrayList<>();

            System.out.println("\n--- AGREGAR PRODUCTOS A LA FACTURA ---");
            String continuar = "s";

            while (continuar.equalsIgnoreCase("s")) {
                System.out.println("\n" + (itemsRequest.size() + 1) + ". Agregando producto:");

                Integer productoId = leerEntero(input, "ID del producto: ");

                Inventario inventario = inventarioDAO.buscarPorId(productoId);
                if (inventario == null) {
                    System.out.println("Error: Producto no encontrado. Intente de nuevo.");
                    continue;
                }

                System.out.println("Producto encontrado: " + inventario.getNombreProducto());
                System.out.println("Stock disponible: " + inventario.getCantidadStock());
                System.out.println("Precio: $" + inventario.getPrecioVenta());

                Integer cantidad;
                do {
                    cantidad = leerEntero(input, "Cantidad deseada: ");
                    if (cantidad > inventario.getCantidadStock()) {
                        System.out.println("Error: Cantidad solicitada (" + cantidad +
                                ") excede el stock disponible (" + inventario.getCantidadStock() + ")");
                    } else if (cantidad <= 0) {
                        System.out.println("Error: La cantidad debe ser mayor a 0");
                    }
                } while (cantidad > inventario.getCantidadStock() || cantidad <= 0);

                System.out.println("Descripcion del servicio (opcional): ");
                String descripcionServicio = input.nextLine().trim();
                if (descripcionServicio.isEmpty()) {
                    descripcionServicio = inventario.getNombreProducto();
                }

                FacturaController.ItemFacturaRequest itemRequest =
                        new FacturaController.ItemFacturaRequest(productoId, descripcionServicio, cantidad);

                itemsRequest.add(itemRequest);

                System.out.println("\nItem agregado:");
                System.out.println("- Producto: " + inventario.getNombreProducto());
                System.out.println("- Cantidad: " + cantidad);
                System.out.println("- Precio unitario: $" + inventario.getPrecioVenta());
                System.out.println("- Subtotal: $" + inventario.getPrecioVenta().multiply(java.math.BigDecimal.valueOf(cantidad)));

                System.out.println("\n¿Desea agregar otro producto? (s/n): ");
                continuar = input.nextLine().trim();
            }

            if (itemsRequest.isEmpty()) {
                System.out.println("Error: No se agregaron productos a la factura.");
                return;
            }

            mostrarResumenFactura(duenoId, centroVeterinarioId, itemsRequest);

            System.out.println("\n¿Confirma la creacion de la factura? (s/n): ");
            String confirmacion = input.nextLine().trim();

            if (confirmacion.equalsIgnoreCase("s")) {
                Facturas facturaCreada = controller.crearFacturaCompleta(duenoId, centroVeterinarioId, itemsRequest);

                if (facturaCreada != null) {
                    System.out.println("\n¡FACTURA CREADA EXITOSAMENTE!");
                    System.out.println("ID de la factura: " + facturaCreada.getId());
                    System.out.println("Total: $" + facturaCreada.getTotal());
                    System.out.println("PDF generado automaticamente");
                } else {
                    System.out.println("Error al crear la factura. Revise los logs para mas detalles.");
                }
            } else {
                System.out.println("Creacion de factura cancelada.");
            }

        } catch (Exception e) {
            logger.error("Error al crear factura completa: {}", e.getMessage());
        }

        System.out.println("\nPresione cualquier tecla para continuar...");
        input.nextLine();
    }

    private void mostrarResumenFactura(Integer duenoId, Integer centroVeterinarioId,
                                       List<FacturaController.ItemFacturaRequest> items) {
        System.out.println("\n========== RESUMEN DE LA FACTURA ==========");
        System.out.println("Dueno ID: " + duenoId);
        System.out.println("Centro Veterinario ID: " + centroVeterinarioId);
        System.out.println("Fecha: " + java.time.LocalDate.now());
        System.out.println("\n--- PRODUCTOS ---");

        java.math.BigDecimal totalCalculado = java.math.BigDecimal.ZERO;

        for (int i = 0; i < items.size(); i++) {
            FacturaController.ItemFacturaRequest item = items.get(i);
            Inventario inventario = inventarioDAO.buscarPorId(item.getProductoId());

            if (inventario != null) {
                java.math.BigDecimal subtotal = inventario.getPrecioVenta()
                        .multiply(java.math.BigDecimal.valueOf(item.getCantidad()));
                totalCalculado = totalCalculado.add(subtotal);

                System.out.println((i + 1) + ". " + inventario.getNombreProducto());
                System.out.println("   Cantidad: " + item.getCantidad() +
                        " | Precio: $" + inventario.getPrecioVenta() +
                        " | Subtotal: $" + subtotal);
                if (!item.getServicioDescripcion().equals(inventario.getNombreProducto())) {
                    System.out.println("   Descripcion: " + item.getServicioDescripcion());
                }
            }
        }

        System.out.println("\nTOTAL ESTIMADO: $" + totalCalculado);
        System.out.println("==========================================");
    }

    private void buscarPorId() {
        System.out.println("\n\n --- 3. BUSCAR FACTURA POR ID:\n");
        Integer id = leerEntero(input, "ID de la factura: ");

        controller.buscarPorId(id);
        System.out.println("\nPresione cualquier tecla para continuar...");
        input.nextLine();
    }

    private void imprimirFactura() {
        System.out.println("\n\n --- 4. IMPRIMIR FACTURA POR ID:\n");
        Integer id = leerEntero(input, "ID de la factura: ");

        controller.imprimirFactura(id);
        System.out.println("\nPresione cualquier tecla para continuar...");
        input.nextLine();
    }

    private void eliminarFactura() {
        System.out.println("\n\n --- 5. ELIMINAR FACTURA:\n");
        Integer id = leerEntero(input, "ID de la factura: ");

        System.out.println("¿Esta seguro que desea eliminar la factura ID " + id + "? (s/n): ");
        String confirmacion = input.nextLine().trim();

        if (confirmacion.equalsIgnoreCase("s")) {
            controller.eliminarFactura(id);
        } else {
            System.out.println("Eliminacion cancelada.");
        }

        System.out.println("\nPresione cualquier tecla para continuar...");
        input.nextLine();
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        System.out.print(mensaje);

        while (!scanner.hasNextInt()) {
            System.out.print("Ingresa un numero entero valido: ");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }
}
