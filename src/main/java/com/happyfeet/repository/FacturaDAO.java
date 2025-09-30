package com.happyfeet.repository;

import com.happyfeet.model.entities.CentroVeterinario;
import com.happyfeet.model.entities.Facturas;
import com.happyfeet.model.entities.ItemsFactura;
import com.happyfeet.model.entities.service.PDFGeneratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO implements IFacturasDAO{
    private static final Logger logger = LogManager.getLogger(FacturaDAO.class);
    private Connection con;
    private PDFGeneratorService pdfService;

    public FacturaDAO() {
        con = ConexionDB.getInstancia().getConnection();
        pdfService = new PDFGeneratorService();
    }

    @Override
    public void agregarFactura(Facturas facturas) {
        // SOLUCIÓN 1: Verifica primero que existan los IDs referenciados
        if (!validarReferenciasForeignKey(facturas)) {
            logger.error("Error: IDs inválidos. Verifica que el dueño y centro veterinario existan");
            return;
        }

        String sql = "INSERT INTO facturas(dueno_id, fecha_emision, total, centro_veterinario_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, facturas.getDuenoId());
            pstmt.setDate(2, Date.valueOf(facturas.getFechaEmision()));
            pstmt.setBigDecimal(3, facturas.getTotal());
            pstmt.setInt(4, facturas.getCentroVeterinarioId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Factura agregada exitosamente");
            }
        } catch (SQLException e) {
            logger.error("Error al agregar la factura: {}", e.getMessage(), e);
            // Mostrar mensaje más específico según el error
            if (e.getMessage().contains("foreign key constraint")) {
                System.out.println("\nERROR: Uno de los IDs no existe en la base de datos:");
                System.out.println("- Verifica que el ID del dueño exista en la tabla de clientes/dueños");
                System.out.println("- Verifica que el ID del centro veterinario exista");
            }
        }
    }

    @Override
    public Facturas agregarFacturaYRetornar(Facturas facturas) {
        // SOLUCIÓN 1: Verifica primero que existan los IDs referenciados
        if (!validarReferenciasForeignKey(facturas)) {
            logger.error("Error: IDs inválidos. Verifica que el dueño y centro veterinario existan");
            System.out.println("\nERROR: No se puede crear la factura.");
            System.out.println("- Dueño ID " + facturas.getDuenoId() + " no existe en la base de datos");
            System.out.println("- O Centro Veterinario ID " + facturas.getCentroVeterinarioId() + " no existe");
            return null;
        }

        String sql = "INSERT INTO facturas(dueno_id, fecha_emision, total, centro_veterinario_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, facturas.getDuenoId());
            pstmt.setDate(2, Date.valueOf(facturas.getFechaEmision()));
            pstmt.setBigDecimal(3, facturas.getTotal());
            pstmt.setInt(4, facturas.getCentroVeterinarioId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        Integer generatedId = rs.getInt(1);
                        facturas.setId(generatedId);
                        logger.info("Factura agregada exitosamente con ID: {}", generatedId);

                        generarPDFAutomatico(facturas);

                        return facturas;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error al agregar la factura: {}", e.getMessage(), e);
            if (e.getMessage().contains("foreign key constraint")) {
                System.out.println("\nERROR DE BASE DE DATOS:");
                System.out.println("Uno de los IDs referenciados no existe:");
                System.out.println("- Dueño ID: " + facturas.getDuenoId());
                System.out.println("- Centro Veterinario ID: " + facturas.getCentroVeterinarioId());
            }
        }

        return null;
    }

    // MÉTODO NUEVO: Validar que existan las referencias antes de insertar
    private boolean validarReferenciasForeignKey(Facturas facturas) {
        // Validar que existe el dueño (tabla: duenos - PLURAL)
        String sqlDueno = "SELECT COUNT(*) FROM duenos WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sqlDueno)) {
            pstmt.setInt(1, facturas.getDuenoId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    logger.error("No existe dueño con ID: {}", facturas.getDuenoId());
                    System.out.println("\nERROR: No existe un dueño con ID: " + facturas.getDuenoId());
                    System.out.println("Use la opción para listar dueños disponibles");
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al validar dueño: {}", e.getMessage());
            return false;
        }

        // Validar que existe el centro veterinario
        String sqlCentro = "SELECT COUNT(*) FROM centro_veterinario WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sqlCentro)) {
            pstmt.setInt(1, facturas.getCentroVeterinarioId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    logger.error("No existe centro veterinario con ID: {}", facturas.getCentroVeterinarioId());
                    System.out.println("\nERROR: No existe un centro veterinario con ID: " + facturas.getCentroVeterinarioId());
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al validar centro veterinario: {}", e.getMessage());
            return false;
        }

        return true;
    }

    // MÉTODO NUEVO: Listar dueños disponibles
    public void listarDuenosDisponibles() {
        String sql = "SELECT id, nombre_completo FROM duenos ORDER BY id";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== DUEÑOS REGISTRADOS ===");
            boolean hayDuenos = false;
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " - " + rs.getString("nombre_completo"));
                hayDuenos = true;
            }
            if (!hayDuenos) {
                System.out.println("No hay dueños registrados");
            }
            System.out.println("========================\n");
        } catch (SQLException e) {
            logger.error("Error al listar dueños: {}", e.getMessage());
        }
    }

    // MÉTODO NUEVO: Listar centros veterinarios disponibles
    public void listarCentrosDisponibles() {
        String sql = "SELECT id, nombre FROM centro_veterinario ORDER BY id";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== CENTROS VETERINARIOS REGISTRADOS ===");
            boolean hayCentros = false;
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " - " + rs.getString("nombre"));
                hayCentros = true;
            }
            if (!hayCentros) {
                System.out.println("No hay centros veterinarios registrados");
            }
            System.out.println("======================================\n");
        } catch (SQLException e) {
            logger.error("Error al listar centros veterinarios: {}", e.getMessage());
        }
    }

    private void generarPDFAutomatico(Facturas factura) {
        try {
            List<ItemsFactura> items = obtenerItemsDeFactura(factura.getId());
            if (items != null && !items.isEmpty()) {
                String rutaCompleta = pdfService.generarRutaCompleta(factura.getId(), null);
                boolean exito = pdfService.generarFacturaPDF(factura, items, rutaCompleta);

                if (exito) {
                    System.out.println("\n¡FACTURA CREADA Y PDF GENERADO AUTOMÁTICAMENTE!");
                    System.out.println("Archivo guardado en: " + rutaCompleta);
                    mostrarResumenEnConsola(factura, items);
                } else {
                    logger.warn("Factura creada pero falló la generación automática del PDF");
                }
            } else {
                logger.warn("Factura creada sin items, PDF no generado automáticamente");
            }
        } catch (Exception e) {
            logger.warn("Error al generar PDF automático para factura {}: {}", factura.getId(), e.getMessage());
        }
    }

    @Override
    public List<Facturas> listarTodos() {
        List<Facturas> lst = new ArrayList<>();
        String sql = "SELECT * FROM facturas ORDER BY id DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Facturas factura = new Facturas(
                        rs.getInt("id"),
                        rs.getDate("fecha_emision").toLocalDate(),
                        rs.getInt("dueno_id"),
                        rs.getBigDecimal("total"),
                        rs.getInt("centro_veterinario_id")
                );
                lst.add(factura);
            }
        } catch (SQLException e) {
            logger.error("Error al consultar todas las facturas: {}", e.getMessage());
        }

        return lst;
    }

    @Override
    public Facturas buscarPorId(Integer id) {
        Facturas factura = null;
        String sql = "SELECT * FROM facturas WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    factura = new Facturas(
                            rs.getInt("id"),
                            rs.getDate("fecha_emision").toLocalDate(),
                            rs.getInt("dueno_id"),
                            rs.getBigDecimal("total"),
                            rs.getInt("centro_veterinario_id")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error al consultar la factura por ID: {} - {}", id, e.getMessage());
        }

        return factura;
    }

    @Override
    public void eliminarFacturas(Integer id) {
        eliminarItemsDeFactura(id);

        String sql = "DELETE FROM facturas WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Factura eliminada correctamente");
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar la factura con ID: {}", id);
        }
    }

    private void eliminarItemsDeFactura(Integer facturaId) {
        String sql = "DELETE FROM items_factura WHERE factura_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, facturaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al eliminar items de factura con ID: {}", facturaId);
        }
    }

    @Override
    public void imprimirFacturaPorId(Integer id) {
        imprimirFacturaEnPDF(id, null);
    }

    public boolean imprimirFacturaEnPDF(Integer id, String rutaDestino) {
        Facturas factura = buscarPorId(id);
        if (factura == null) {
            logger.error("No se encontró factura con ID: {}", id);
            return false;
        }

        List<ItemsFactura> items = obtenerItemsDeFactura(id);

        try {
            String rutaCompleta;
            if (rutaDestino == null) {
                rutaCompleta = pdfService.generarRutaCompleta(id, null);
            } else {
                rutaCompleta = rutaDestino;
            }

            boolean exito = pdfService.generarFacturaPDF(factura, items, rutaCompleta);

            if (exito) {
                System.out.println("\n¡FACTURA GENERADA EXITOSAMENTE EN PDF!");
                System.out.println("Archivo guardado en: " + rutaCompleta);
                mostrarResumenEnConsola(factura, items);
                return true;
            } else {
                logger.error("Error al generar PDF para factura ID: {}", id);
                return false;
            }

        } catch (Exception e) {
            logger.error("Error inesperado al generar PDF de factura: {}", e.getMessage());
            return false;
        }
    }

    private void mostrarResumenEnConsola(Facturas factura, List<ItemsFactura> items) {
        CentroVeterinario centro = CentroVeterinario.getInstancia();

        System.out.println("\n========== RESUMEN FACTURA #" + String.format("%06d", factura.getId()) + " ==========");
        System.out.println(" " + centro.getNombre() + " - " + centro.getTelefono());
        System.out.println(" Fecha: " + factura.getFechaEmision());
        System.out.println(" Cliente ID: " + factura.getDuenoId());
        System.out.println(" Centro ID: " + factura.getCentroVeterinarioId());
        System.out.println("\n--- PRODUCTOS/SERVICIOS (" + items.size() + " items) ---");

        for (ItemsFactura item : items) {
            String descripcion = item.getServicioDescripcion();
            if (descripcion == null || descripcion.trim().isEmpty()) {
                descripcion = "Producto ID: " + item.getProductoId();
            }
            System.out.println("• " + descripcion + " (x" + item.getCantidad() + ") - $" + item.getSubtotal());
        }

        System.out.println("\n TOTAL: $" + factura.getTotal());
        System.out.println("==========================================\n");
    }

    private List<ItemsFactura> obtenerItemsDeFactura(Integer facturaId) {
        List<ItemsFactura> items = new ArrayList<>();
        String sql = "SELECT * FROM items_factura WHERE factura_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, facturaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ItemsFactura item = new ItemsFactura(
                            rs.getInt("id"),
                            rs.getInt("factura_id"),
                            rs.getInt("producto_id"),
                            rs.getString("servicio_descripcion"),
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio_unitario"),
                            rs.getBigDecimal("subtotal")
                    );
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener items de factura ID: {}", facturaId);
        }

        return items;
    }

    @Override
    public Facturas buscarUltimaFacturaPorDatos(Integer duenoId, Integer centroVeterinarioId, String fecha) {
        Facturas factura = null;
        String sql = "SELECT * FROM facturas WHERE dueno_id = ? AND centro_veterinario_id = ? AND fecha_emision = ? ORDER BY id DESC LIMIT 1";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, duenoId);
            pstmt.setInt(2, centroVeterinarioId);
            pstmt.setDate(3, Date.valueOf(fecha));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    factura = new Facturas(
                            rs.getInt("id"),
                            rs.getDate("fecha_emision").toLocalDate(),
                            rs.getInt("dueno_id"),
                            rs.getBigDecimal("total"),
                            rs.getInt("centro_veterinario_id")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar última factura: {}", e.getMessage());
        }

        return factura;
    }

    public List<Facturas> buscarPorDueno(Integer duenoId) {
        List<Facturas> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas WHERE dueno_id = ? ORDER BY fecha_emision DESC";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, duenoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Facturas factura = new Facturas(
                            rs.getInt("id"),
                            rs.getDate("fecha_emision").toLocalDate(),
                            rs.getInt("dueno_id"),
                            rs.getBigDecimal("total"),
                            rs.getInt("centro_veterinario_id")
                    );
                    facturas.add(factura);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar facturas por dueño ID: {}", duenoId);
        }

        return facturas;
    }

    public java.math.BigDecimal obtenerTotalVentasPorPeriodo(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        String sql = "SELECT SUM(total) as total_ventas FROM facturas WHERE fecha_emision BETWEEN ? AND ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fechaInicio));
            pstmt.setDate(2, Date.valueOf(fechaFin));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getBigDecimal("total_ventas");
                    if (total == null) {
                        total = java.math.BigDecimal.ZERO;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error al calcular total de ventas por período: {}", e.getMessage());
        }

        return total;
    }

    public boolean regenerarPDF(Integer facturaId, String rutaDestino) {
        return imprimirFacturaEnPDF(facturaId, rutaDestino);
    }

    public boolean existePDF(Integer facturaId) {
        try {
            String rutaCompleta = pdfService.generarRutaCompleta(facturaId, null);
            java.io.File archivo = new java.io.File(rutaCompleta);
            return archivo.exists();
        } catch (Exception e) {
            logger.error("Error al verificar existencia de PDF para factura {}: {}", facturaId, e.getMessage());
            return false;
        }
    }

    public String obtenerRutaPDF(Integer facturaId) {
        try {
            return pdfService.generarRutaCompleta(facturaId, null);
        } catch (Exception e) {
            logger.error("Error al obtener ruta de PDF para factura {}: {}", facturaId, e.getMessage());
            return null;
        }
    }
}