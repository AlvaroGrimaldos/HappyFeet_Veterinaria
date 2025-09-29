package com.happyfeet.repository;

import com.happyfeet.model.entities.Dueno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DuenoDAO implements IDuenoDAO {
    private Connection con;

    public DuenoDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarDueno(Dueno dueno) {
        String sql = "INSERT INTO duenos(nombre_completo, documento_identidad, direccion, telefono, email) VALUES(?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, dueno.getNombreCompleto());
            pstmt.setString(2, dueno.getDocumentoIdentidad());
            pstmt.setString(3, dueno.getDireccion());
            pstmt.setString(4, dueno.getTelefono());
            pstmt.setString(5, dueno.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar el dueño\n" + e.getMessage());
        }
    }

    @Override
    public List<Dueno> listarTodos() {
        List<Dueno> lista = new ArrayList<>();
        String sql = "SELECT * FROM duenos ORDER BY nombre_completo";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Dueno dueno = new Dueno(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("documento_identidad"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                lista.add(dueno);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todos los dueños\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public Dueno buscarPorId(Integer id) {
        Dueno dueno = null;
        String sql = "SELECT * FROM duenos WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    dueno = new Dueno(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getString("documento_identidad"),
                            rs.getString("direccion"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar el dueño por ID: " + id + "\n" + e.getMessage());
        }

        return dueno;
    }

    @Override
    public Dueno buscarPorDocumento(String documento) {
        Dueno dueno = null;
        String sql = "SELECT * FROM duenos WHERE documento_identidad = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documento);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    dueno = new Dueno(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getString("documento_identidad"),
                            rs.getString("direccion"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar el dueño por documento: " + documento + "\n" + e.getMessage());
        }

        return dueno;
    }

    @Override
    public void actualizarDueno(Dueno dueno) {
        String sql = "UPDATE duenos SET nombre_completo = ?, documento_identidad = ?, direccion = ?, telefono = ?, email = ? WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, dueno.getNombreCompleto());
            pstmt.setString(2, dueno.getDocumentoIdentidad());
            pstmt.setString(3, dueno.getDireccion());
            pstmt.setString(4, dueno.getTelefono());
            pstmt.setString(5, dueno.getEmail());
            pstmt.setInt(6, dueno.getId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al actualizar el dueño: " + dueno + "\n" + e.getMessage());
        }
    }

    @Override
    public void eliminarDueno(Integer id) {
        String sql = "DELETE FROM duenos WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al eliminar el dueño con ID: " + id + "\n" + e.getMessage());
        }
    }

    @Override
    public List<Dueno> buscarPorNombre(String nombre) {
        List<Dueno> lista = new ArrayList<>();
        String sql = "SELECT * FROM duenos WHERE nombre_completo LIKE ? ORDER BY nombre_completo";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Dueno dueno = new Dueno(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getString("documento_identidad"),
                            rs.getString("direccion"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                    lista.add(dueno);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al buscar dueños por nombre: " + nombre + "\n" + e.getMessage());
        }

        return lista;
    }
}