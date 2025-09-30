package com.happyfeet.controller;

import com.happyfeet.model.entities.Facturas;
import com.happyfeet.model.entities.ItemsFactura;
import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.IFacturasDAO;
import com.happyfeet.repository.FacturaDAO;
import com.happyfeet.repository.IItemsFacturaDAO;
import com.happyfeet.repository.IInventarioDAO;
import com.happyfeet.model.entities.service.PDFGeneratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FacturaController {
    private static final Logger logger = LogManager.getLogger(FacturaController.class);
    private IFacturasDAO facturaDAO;
    private IItemsFacturaDAO itemsFacturaDAO;
    private IInventarioDAO inventarioDAO;
    private PDFGeneratorService pdfService;

    public FacturaController(IFacturasDAO facturaDAO, IItemsFacturaDAO itemsFacturaDAO, IInventarioDAO inventarioDAO) {
        this.facturaDAO = facturaDAO;
        this.itemsFacturaDAO = itemsFacturaDAO;
        this.inventarioDAO = inventarioDAO;
        this.pdfService = new PDFGeneratorService();
    }

    public Facturas crearFacturaCompleta(Integer duenoId, Integer centroVeterinarioId, List<ItemFacturaRequest> itemsRequest) {
        if (!validarDatosFactura(duenoId, centroVeterinarioId, itemsRequest)) {
            logger.error("Error al crear factura. Datos invalidos");
            return null;
        }

        try {
            BigDecimal totalCalculado = calcularTotalItems(itemsRequest);
            Facturas factura = new Facturas(LocalDate.now(), duenoId, totalCalculado, centroVeterinarioId);

            Facturas facturaCreada = agregarFacturaYObtenerID(factura);

            if (facturaCreada == null || facturaCreada.getId() == null) {
                logger.error("Error al crear la factura base");
                return null;
            }

            boolean itemsCreados = crearItemsFactura(facturaCreada.getId(), itemsRequest);

            if (!itemsCreados) {
                facturaDAO.eliminarFacturas(facturaCreada.getId());
                logger.error("Error al crear items de factura. Factura eliminada");
                return null;
            }

            logger.info("Factura completa creada exitosamente con ID: {}", facturaCreada.getId());
            return facturaCreada;

        } catch (Exception e) {
            logger.error("Error inesperado al crear factura completa: {}", e.getMessage());
            return null;
        }
    }

    private Facturas agregarFacturaYObtenerID(Facturas factura) {
        try {
            if (facturaDAO instanceof FacturaDAO) {
                return ((FacturaDAO) facturaDAO).agregarFacturaYRetornar(factura);
            } else {
                facturaDAO.agregarFactura(factura);
                return facturaDAO.buscarUltimaFacturaPorDatos(
                        factura.getDuenoId(),
                        factura.getCentroVeterinarioId(),
                        factura.getFechaEmision().toString()
                );
            }
        } catch (Exception e) {
            logger.error("Error al agregar factura base: {}", e.getMessage());
            return null;
        }
    }

    private boolean crearItemsFactura(Integer facturaId, List<ItemFacturaRequest> itemsRequest) {
        try {
            for (ItemFacturaRequest item : itemsRequest) {
                Inventario inventario = inventarioDAO.buscarPorId(item.getProductoId());

                if (inventario == null) {
                    logger.error("Producto con ID {} no encontrado", item.getProductoId());
                    return false;
                }

                if (inventario.getCantidadStock() < item.getCantidad()) {
                    logger.error("Stock insuficiente para producto ID {}", item.getProductoId());
                    return false;
                }

                BigDecimal precioUnitario = inventario.getPrecioVenta();
                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

                ItemsFactura itemFactura = new ItemsFactura(
                        facturaId,
                        item.getProductoId(),
                        item.getServicioDescripcion(),
                        subtotal,
                        item.getCantidad(),
                        precioUnitario
                );

                itemsFacturaDAO.agregarItemFactura(itemFactura);
                inventarioDAO.actualizarStockVenta(inventario, item.getCantidad());
            }

            return true;

        } catch (Exception e) {
            logger.error("Error al crear items de factura: {}", e.getMessage());
            return false;
        }
    }

    private BigDecimal calcularTotalItems(List<ItemFacturaRequest> itemsRequest) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemFacturaRequest item : itemsRequest) {
            Inventario inventario = inventarioDAO.buscarPorId(item.getProductoId());
            if (inventario != null) {
                BigDecimal subtotal = inventario.getPrecioVenta()
                        .multiply(BigDecimal.valueOf(item.getCantidad()));
                total = total.add(subtotal);
            }
        }

        return total;
    }

    private boolean validarDatosFactura(Integer duenoId, Integer centroVeterinarioId, List<ItemFacturaRequest> items) {
        if (duenoId == null || duenoId <= 0) return false;
        if (centroVeterinarioId == null || centroVeterinarioId <= 0) return false;
        if (items == null || items.isEmpty()) return false;

        for (ItemFacturaRequest item : items) {
            if (item.getProductoId() == null || item.getProductoId() <= 0) return false;
            if (item.getCantidad() == null || item.getCantidad() <= 0) return false;
        }

        return true;
    }

    public void listarTodas() {
        List<Facturas> facturas = facturaDAO.listarTodos();
        if (facturas.isEmpty()) {
            logger.info("No hay facturas registradas");
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                        LISTADO DE FACTURAS                                            ║");
            System.out.println("╠════╦══════════════╦════════════════════════════╦════════════════╦══════════════════╦═════════════════╣");
            System.out.println("║ ID ║  DUEÑO ID    ║       FECHA EMISIÓN        ║ CENTRO VET. ID ║      TOTAL       ║     ESTADO      ║");
            System.out.println("╠════╬══════════════╬════════════════════════════╬════════════════╬══════════════════╬═════════════════╣");

            facturas.forEach(f -> {
                System.out.printf("║ %-2d ║ %12d ║ %-26s ║ %14d ║ $%,14.2f ║ %-15s ║%n",
                        f.getId(),
                        f.getDuenoId(),
                        f.getFechaEmision() != null ? f.getFechaEmision().toString() : "N/A",
                        f.getCentroVeterinarioId(),
                        f.getTotal(),
                        "Pagada");
            });

            System.out.println("╚════╩══════════════╩════════════════════════════╩════════════════╩══════════════════╩═════════════════╝");

            // Estadísticas
            double totalVentas = facturas.stream()
                    .mapToDouble(f -> f.getTotal().doubleValue())
                    .sum();

            System.out.println("Total de facturas: " + facturas.size());
            System.out.printf("Total ventas: $%,.2f%n", totalVentas);
        }
    }

    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud - 3) + "...";
    }
    public void buscarPorId(Integer id) {
        if (id > 0 && id != null) {
            Facturas factura = facturaDAO.buscarPorId(id);
            if (factura != null) {
                System.out.println("Factura encontrada: " + factura);
            } else {
                logger.info("No se encontro una factura con id {}", id);
            }
        } else {
            logger.error("Error. Id no valido, ingrese un id valido");
        }
    }

    public void eliminarFactura(Integer id) {
        if (id > 0 && id != null) {
            facturaDAO.eliminarFacturas(id);
            logger.info("Factura eliminada correctamente");
        } else {
            logger.error("Error al eliminar la factura {}", id);
        }
    }

    public void imprimirFactura(Integer id) {
        if (id > 0 && id != null) {
            facturaDAO.imprimirFacturaPorId(id);
        } else {
            logger.error("Error. Id no valido para imprimir factura");
        }
    }

    public static class ItemFacturaRequest {
        private Integer productoId;
        private String servicioDescripcion;
        private Integer cantidad;

        public ItemFacturaRequest(Integer productoId, String servicioDescripcion, Integer cantidad) {
            this.productoId = productoId;
            this.servicioDescripcion = servicioDescripcion;
            this.cantidad = cantidad;
        }

        public Integer getProductoId() {
            return productoId;
        }

        public void setProductoId(Integer productoId) {
            this.productoId = productoId;
        }

        public String getServicioDescripcion() {
            return servicioDescripcion;
        }

        public void setServicioDescripcion(String servicioDescripcion) {
            this.servicioDescripcion = servicioDescripcion;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}
