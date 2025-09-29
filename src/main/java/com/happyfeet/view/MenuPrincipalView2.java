package com.happyfeet.view;

import com.happyfeet.controller.FacturaController;
import com.happyfeet.controller.InventarioController;
import com.happyfeet.controller.ItemsFacturaController;
import com.happyfeet.controller.ProductoTipoController;
import com.happyfeet.repository.*;
import com.happyfeet.util.LoggerUtil;

import java.util.Scanner;

public class MenuPrincipalView2 {
    private final Scanner input;
    private final FacturaView facturaView;
    private final InventarioView inventarioView;
    private final ItemsFacturaView itemsFacturaView;
    private final ProductoTipoView productoTipoView;

    // Declarar todos los DAOs como atributos de clase
    private final IInventarioDAO inventarioDAO;
    private final IItemsFacturaDAO itemsFacturaDAO;
    private final IFacturasDAO facturaDAO;
    private final IProductoTipoDAO productoTipoDAO;

    public MenuPrincipalView2() {
        this.input = new Scanner(System.in);

        // Inicializar DAO compartido
        this.inventarioDAO = new InventarioDAO();
        this.itemsFacturaDAO = new ItemsFacturaDAO();
        this.facturaDAO = new FacturaDAO();
        this.productoTipoDAO = new ProductoTipoDAO(); // Añadir este DAO

        // Inicializar controladores
        FacturaController facturaController = new FacturaController(facturaDAO, itemsFacturaDAO, inventarioDAO);
        InventarioController inventarioController = new InventarioController(inventarioDAO);
        ItemsFacturaController itemsFacturaController = new ItemsFacturaController(itemsFacturaDAO, inventarioDAO);
        ProductoTipoController productoTipoController = new ProductoTipoController(productoTipoDAO);

        // Inicializar vistas
        this.facturaView = new FacturaView(facturaController, inventarioDAO);
        this.inventarioView = new InventarioView(inventarioController, inventarioDAO);
        this.itemsFacturaView = new ItemsFacturaView(itemsFacturaController, inventarioDAO);
        this.productoTipoView = new ProductoTipoView(productoTipoController);
    }

    public void mostrarMenu() {
        mostrarBienvenida();

        String opcion = "";
        while (!opcion.equals("0")) {
            mostrarMenuPrincipal();

            try {
                opcion = input.nextLine().trim();
                procesarOpcion(opcion);
            } catch (Exception e) {
                LoggerUtil.error("Error al procesar la opción del menú: " + e.getMessage());
                System.out.println("❌ Error inesperado. Intente nuevamente.");
                pausar();
                opcion = "";
            }
        }

        mostrarDespedida();
    }

    private void mostrarBienvenida() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║           🏪 MÓDULO DE INVENTARIO Y FACTURACIÓN 🏪             ║");
        System.out.println("║                                                                ║");
        System.out.println("║             Sistema de Gestión Comercial - Happy Feet          ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("                  ¡Bienvenido al módulo comercial!\n");
        LoggerUtil.info("Módulo de Inventario y Facturación iniciado");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              MENÚ INVENTARIO Y FACTURACIÓN                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                ║");
        System.out.println("║  1. 📦 Gestión de Inventario                                  ║");
        System.out.println("║     • Control de productos y stock                            ║");
        System.out.println("║     • Alertas de stock mínimo                                 ║");
        System.out.println("║     • Registro de vencimientos                                ║");
        System.out.println("║                                                                ║");
        System.out.println("║  2. 🧾 Gestión de Facturas                                    ║");
        System.out.println("║     • Crear y consultar facturas                              ║");
        System.out.println("║     • Impresión de comprobantes                               ║");
        System.out.println("║     • Historial de ventas                                     ║");
        System.out.println("║                                                                ║");
        System.out.println("║  3. 📋 Gestión de Items de Factura                            ║");
        System.out.println("║     • Detalle de productos vendidos                           ║");
        System.out.println("║     • Control de cantidades                                   ║");
        System.out.println("║                                                                ║");
        System.out.println("║  4. 🏷️  Gestión de Tipos de Producto                          ║");
        System.out.println("║     • Categorías de inventario                                ║");
        System.out.println("║     • Clasificación de productos                              ║");
        System.out.println("║                                                                ║");
        System.out.println("║  0. 🚪 Volver al menú principal                               ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.print(">>> Seleccione una opción [1-4, 0]: ");
    }

    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                LoggerUtil.info("Accediendo al módulo de Gestión de Inventario");
                inventarioView.mostrarMenu();
                break;

            case "2":
                LoggerUtil.info("Accediendo al módulo de Gestión de Facturas");
                facturaView.mostrarMenu();
                break;

            case "3":
                LoggerUtil.info("Accediendo al módulo de Gestión de Items de Factura");
                itemsFacturaView.mostrarMenu();
                break;

            case "4":
                LoggerUtil.info("Accediendo al módulo de Gestión de Tipos de Producto");
                productoTipoView.mostrarMenu();
                break;

            case "0":
                LoggerUtil.info("Volviendo al menú principal general");
                break;

            default:
                System.out.println("\n❌ Opción inválida. Por favor seleccione una opción válida (1-4, 0).");
                pausar();
                break;
        }
    }

    private void mostrarDespedida() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║              Saliendo del módulo de Inventario...              ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        LoggerUtil.info("Módulo de Inventario y Facturación cerrado");
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        input.nextLine();
    }
}