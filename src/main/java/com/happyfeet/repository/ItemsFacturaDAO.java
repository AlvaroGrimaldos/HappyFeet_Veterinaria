package com.happyfeet.repository;

import com.happyfeet.model.entities.ItemsFactura;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsFacturaDAO implements IItemsFacturaDAO{
    private static final Logger logger = LogManager.getLogger(ItemsFacturaDAO.class);
    private Connection con;

    public ItemsFacturaDAO(){ con = ConexionDB.getInstancia().getConnection();}

    @Override
    public void agregarItemFactura(ItemsFactura itemsFactura) {
        String sql = "insert into items_factura(factura_id, producto_id, servicio_descripcion, cantidad, precio_unitario, subtotal) values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, itemsFactura.getFacturaId());
            pstmt.setInt(2, itemsFactura.getProductoId());
            pstmt.setString(3, itemsFactura.getServicioDescripcion());
            pstmt.setInt(4, itemsFactura.getCantidad());
            pstmt.setBigDecimal(5, itemsFactura.getPrecioUnitario());
            pstmt.setBigDecimal(6, itemsFactura.getSubtotal());
            pstmt.executeUpdate();
        }catch(SQLException e){
            logger.error("Error al agregar el Item Factura {}", e.getMessage());
        }
    }

    @Override
    public List<ItemsFactura> listarTodos() {
        List<ItemsFactura> lst = new ArrayList<>();
        String sql = "select * from items_factura";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()) {
                ItemsFactura itf =  new ItemsFactura(rs.getInt("id"),
                        rs.getInt("factura_id"),
                        rs.getInt("producto_id"),
                        rs.getString("servicio_descripcion"),
                        rs.getInt("cantidad"),
                        rs.getBigDecimal("precio_unitario"),
                        rs.getBigDecimal("subtotal"));

                lst.add(itf);
            }
        }catch (SQLException e){
            logger.error("Error al consultar todos los items factura {}", e.getMessage());
        }

        return lst;
    }

    @Override
    public ItemsFactura buscarPorId(Integer id) {
        ItemsFactura itf = null;
        String sql = "select * from items_factura where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    itf = new ItemsFactura(rs.getInt("id"),
                            rs.getInt("factura_id"),
                            rs.getInt("producto_id"),
                            rs.getString("servicio_descripcion"),
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio_unitario"),
                            rs.getBigDecimal("subtotal"));
                }
            }
        }catch (SQLException e) {
            logger.error("Error al consultar el item factura por ID: {} - {}", id, e.getMessage());
        }

        return itf;
    }

    @Override
    public void eliminarItemFactura(Integer id) {
        String sql = "delete from items_factura where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            logger.error("Error al eliminar el item factura con ID: {}", id);
        }

    }
}
