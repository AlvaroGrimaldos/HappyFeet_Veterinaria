package com.happyfeet.model;

import com.happyfeet.repository.ConexionDB;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductoTipoDAO implements IProductoTipoDAO{
    private static final Logger logger = (Logger) LogManager.getLogger(ProductoTipoDAO.class);
    private Connection con;

    public ProductoTipoDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarProductoTipo(ProductoTipo productoTipo) {
        String sql = "insert into producto_tipo(nombre) values (?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, productoTipo.getNombre());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("Error al agregar el Producto Tipo" + e.getMessage());
        }
    }

    @Override
    public List<ProductoTipo> listarTodos() {
        List<ProductoTipo> lst = new ArrayList<>();
        String sql = "select * from producto_tipo";
        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                ProductoTipo pt = new ProductoTipo(rs.getInt("id"),
                        rs.getString("nombre"));

                lst.add(pt);
            }
        }catch(SQLException e){
            logger.info("Error al consultar todos los estudiantes" + e.getMessage());
        }

        return lst;
    }

    @Override
    public ProductoTipo buscarPorId(Integer id) {
        ProductoTipo pt = null;
        String sql = "select * from producto_tipo where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {

                if(rs.next()) {
                    pt = new ProductoTipo(rs.getInt("id"),
                            rs.getString("nombre"));
                }
            }
        } catch(SQLException e) {
            logger.info("Error al consultar el producto tipo por ID: " + id);
        }

        return pt;
    }

    @Override
    public void actualizarProductoTipo(ProductoTipo productoTipo) {
        String sql = "update producto_tipo set nombre = ? where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, productoTipo.getNombre());
            pstmt.setInt(2, productoTipo.getId());
            pstmt.executeUpdate();
        }catch(SQLException e) {
            logger.info("Error al actualizar el producto tipo: " + productoTipo);
        }
    }

    @Override
    public void eliminarProductoTipo(Integer id) {
        String sql = "delete from producto_tipo where id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            logger.info("Error al eliminar el producto tipo con ID: " + id);
        }
    }
}
