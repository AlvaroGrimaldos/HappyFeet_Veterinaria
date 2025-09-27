package com.happyfeet.repository;

import com.happyfeet.model.entities.ProductoTipo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductoTipoDAO implements IProductoTipoDAO {
    private static final Logger logger = (Logger) LogManager.getLogger(ProductoTipoDAO.class);
    private Connection con;

    public ProductoTipoDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarProductoTipo(ProductoTipo productoTipo) {
        String sql = "insert into producto_tipos(nombre) values (?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, productoTipo.getNombre());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al agregar el Producto Tipo{}", e.getMessage());
        }
    }

    @Override
    public List<ProductoTipo> listarTodos() {
        List<ProductoTipo> lst = new ArrayList<>();
        String sql = "select * from producto_tipos";
        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                ProductoTipo pt = new ProductoTipo(rs.getInt("id"),
                        rs.getString("nombre"));

                lst.add(pt);
            }
        }catch(SQLException e){
            logger.error("Error al consultar todos los producto tipos{}", e.getMessage());
        }

        return lst;
    }

    @Override
    public ProductoTipo buscarPorId(Integer id) {
        ProductoTipo pt = null;
        String sql = "select * from producto_tipos where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {

                if(rs.next()) {
                    pt = new ProductoTipo(rs.getInt("id"),
                            rs.getString("nombre"));
                }
            }
        } catch(SQLException e) {
            logger.error("Error al consultar el producto tipo por ID: {}", id);
        }

        return pt;
    }

    @Override
    public void actualizarProductoTipo(ProductoTipo productoTipo) {
        String sql = "update producto_tipos set nombre = ? where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, productoTipo.getNombre());
            pstmt.setInt(2, productoTipo.getId());
            pstmt.executeUpdate();
        }catch(SQLException e) {
            logger.error("Error al actualizar el producto tipo: {}", productoTipo);
        }
    }

    @Override
    public void eliminarProductoTipo(Integer id) {
        String sql = "delete from producto_tipos where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            logger.error("Error al eliminar el producto tipo con ID: {}", id);
        }
    }
}
