package com.happyfeet.repository;

import com.happyfeet.model.entities.*;
import com.happyfeet.model.entities.observer.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO implements IInventarioDAO{
    private static final Logger logger =  LogManager.getLogger(InventarioDAO.class);
    private Connection con;

    private List<Observer> observers = new ArrayList<>();

    public InventarioDAO(){ con = ConexionDB.getInstancia().getConnection();}

    @Override
    public void agregarInventario(Inventario inventario) {
        String sql = "insert into inventario(nombre_producto, producto_tipo_id, descripcion, fabricante, lote, cantidad_stock, stock_minimo, fecha_vencimiento, precio_venta) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, inventario.getNombreProducto());
            pstmt.setInt(2, inventario.getProductoTipoId());
            pstmt.setString(3, inventario.getDescripcion());
            pstmt.setString(4, inventario.getFabricante());
            pstmt.setString(5, inventario.getLote());
            pstmt.setInt(6, inventario.getCantidadStock());
            pstmt.setInt(7, inventario.getStockMinimo());
            pstmt.setDate(8, Date.valueOf(inventario.getFechaVencimiento()));
            pstmt.setBigDecimal(9, inventario.getPrecioVenta());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Inventario agregado correctamente: {}", inventario.getNombreProducto());
            }
        }catch (SQLException e){
            logger.error("Error al agregar el inventario: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Inventario> listarTodos() {
        List<Inventario> lst = new ArrayList<>();
        String sql = "select * from inventario";
        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Inventario i = crearInventarioPorTipo(
                        rs.getString("nombre_producto"),
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getInt("producto_tipo_id"),
                        rs.getString("fabricante"),
                        rs.getString("lote"),
                        rs.getInt("cantidad_stock"),
                        rs.getInt("stock_minimo"),
                        rs.getDate("fecha_vencimiento").toLocalDate(),
                        rs.getBigDecimal("precio_venta"));

                if (i != null) {
                    lst.add(i);
                }
            }
        }catch(SQLException e) {
            logger.error("Error al consultar todo el inventario: {}", e.getMessage(), e);
        }
        return lst;
    }

    @Override
    public Inventario buscarPorId(Integer id) {
        String sql = "select * from inventario where id= ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return crearInventarioPorTipo(
                            rs.getString("nombre_producto"),
                            rs.getInt("id"),
                            rs.getString("descripcion"),
                            rs.getInt("producto_tipo_id"),
                            rs.getString("fabricante"),
                            rs.getString("lote"),
                            rs.getInt("cantidad_stock"),
                            rs.getInt("stock_minimo"),
                            rs.getDate("fecha_vencimiento").toLocalDate(),
                            rs.getBigDecimal("precio_venta")
                    );
                }
            }
        }catch (SQLException e) {
            logger.error("Error al buscar inventario por ID {}: {}", id, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void actualizarInventario(Inventario inventario) {
        String sql = "update inventario set nombre_producto = ?, descripcion = ?, producto_tipo_id = ?, lote = ?, fabricante = ?, cantidad_stock = ?, stock_minimo = ?, fecha_vencimiento = ?, precio_venta = ? where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, inventario.getNombreProducto());
            pstmt.setString(2, inventario.getDescripcion());
            pstmt.setInt(3, inventario.getProductoTipoId());
            pstmt.setString(4, inventario.getLote());
            pstmt.setString(5, inventario.getFabricante());
            pstmt.setInt(6, inventario.getCantidadStock());
            pstmt.setInt(7, inventario.getStockMinimo());
            pstmt.setDate(8, Date.valueOf(inventario.getFechaVencimiento()));
            pstmt.setBigDecimal(9, inventario.getPrecioVenta());
            pstmt.setInt(10, inventario.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Inventario actualizado correctamente: ID {}", inventario.getId());
            } else {
                logger.warn("No se encontró inventario con ID {} para actualizar", inventario.getId());
            }
        }catch (SQLException e) {
            logger.error("Error al actualizar el inventario con id {}: {}", inventario.getId(), e.getMessage(), e);
        }
    }

    @Override
    public void eliminarInventario(Integer id) {
        String sql = "delete from inventario where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Inventario eliminado correctamente: ID {}", id);
            } else {
                logger.warn("No se encontró inventario con ID {} para eliminar", id);
            }
        }catch (SQLException e) {
            logger.error("Error al eliminar el inventario con id {}: {}", id, e.getMessage(), e);
        }
    }

    private Inventario crearInventarioPorTipo(String nombreProducto, Integer id, String descripcion, Integer productoTipoId, String fabricante, String lote, Integer cantidadStock, Integer stockMinimo, LocalDate fechaVencimiento, BigDecimal precioVenta) {
        Inventario inventario = null;
        switch(productoTipoId) {
            case 1:
                inventario = new Medicamento(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
                break;
            case 2:
                inventario = new Vacuna(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
                break;
            case 3:
                inventario = new InsumoMedico(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
                break;
            case 4:
                inventario = new Alimento(nombreProducto, productoTipoId, fabricante, descripcion, lote, stockMinimo, cantidadStock, fechaVencimiento, precioVenta);
                break;
            default:
                logger.warn("Tipo de producto no reconocido: {}", productoTipoId);
                return null;
        }

        if(inventario != null){
            inventario.setId(id);
        }

        return inventario;
    }

    public void addObserver(Observer o) { observers.add(o);}

    public void notifyObservers(Inventario i) {
        observers.forEach(o -> o.update(i));
    }

    @Override
    public void actualizarStockVenta(Inventario i, int cantidadVendida) {
        String sql = "update inventario set cantidad_stock = ? where id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)){
            int nuevoStock = i.getCantidadStock() - cantidadVendida;

            if(nuevoStock < 0) {
                logger.error("Error: Stock Insuficiente para {}. Stock actual: {}, cantidad solicitada: {}",
                        i.getNombreProducto(), i.getCantidadStock(), cantidadVendida);
                return; // CRÍTICO: Detener la ejecución si no hay stock suficiente
            }

            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, i.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                int stockAnterior = i.getCantidadStock();
                i.setCantidadStock(nuevoStock);
                logger.info("Stock actualizado para {}: {} -> {} (-{})",
                        i.getNombreProducto(), stockAnterior, nuevoStock, cantidadVendida);

                notifyObservers(i);
            }

        } catch (SQLException e) {
            logger.error("Error al actualizar el stock del producto {}: {}", i.getNombreProducto(), e.getMessage(), e);
        }
    }

    @Override
    public void agregarStock(Inventario i, int cantidad, LocalDate fecha){
        String sql = "update inventario set cantidad_stock = ?, fecha_vencimiento = ? where id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)){
            int stockAnterior = i.getCantidadStock();
            int nuevoStock = i.getCantidadStock() + cantidad;

            pstmt.setInt(1, nuevoStock);
            pstmt.setDate(2, Date.valueOf(fecha));
            pstmt.setInt(3, i.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                i.setCantidadStock(nuevoStock);
                i.setFechaVencimiento(fecha);
                logger.info("Stock agregado para {}: {} -> {} (+{})",
                        i.getNombreProducto(), stockAnterior, nuevoStock, cantidad);

                notifyObservers(i);
            } else {
                logger.warn("No se encontró inventario con ID {} para agregar stock", i.getId());
            }

        } catch (SQLException e) {
            logger.error("Error al agregar stock del producto {}: {}", i.getNombreProducto(), e.getMessage(), e);
        }
    }
}