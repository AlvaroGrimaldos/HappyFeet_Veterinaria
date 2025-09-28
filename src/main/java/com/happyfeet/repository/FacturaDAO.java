package com.happyfeet.repository;

import com.happyfeet.model.entities.Facturas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO implements IFacturasDAO{
    private static final Logger logger = LogManager.getLogger(FacturaDAO.class);
    private Connection con;

    public FacturaDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }


    @Override
    public void agregarFactura(Facturas facturas) {
        String sql = "insert into facturas(dueno_id, fecha_emision, total, centro_veterinario_id) values (?, ?, ?, ?)";

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
            logger.error("Error al agregar la factura: {}", e.getMessage());
        }
    }

    @Override
    public Facturas agregarFacturaYRetornar(Facturas facturas) {
        String sql = "insert into facturas(dueno_id, fecha_emision, total, centro_veterinario_id) values (?, ?, ?, ?)";

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
                        return facturas;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error al agregar la factura: {}", e.getMessage());
        }

        return null;
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

    // Método adicional para obtener facturas por dueño
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

    // Método para obtener total de ventas por período
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
}
