package com.happyfeet.repository;

import com.happyfeet.model.entities.Veterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAO implements IVeterinarioDAO {
    private Connection con;

    public VeterinarioDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarVeterinario(Veterinario veterinario) {
        String sql = "INSERT INTO veterinario(nombre_completo, documento_identidad, telefono, email) VALUES(?, ?, ?, ?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, veterinario.getNombreCompleto());
            pstmt.setString(2, veterinario.getDocumentoIdentidad());
            pstmt.setString(3, veterinario.getTelefono());
            pstmt.setString(4, veterinario.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar el veterinario\n" + e.getMessage());
        }
    }

    @Override
    public List<Veterinario> listarTodos() {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "SELECT * FROM veterinario ORDER BY nombre_completo";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Veterinario veterinario = new Veterinario(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("documento_identidad"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                lista.add(veterinario);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todos los veterinarios\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public Veterinario buscarPorId(Integer id) {
        Veterinario veterinario = null;
        String sql = "SELECT * FROM veterinario WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    veterinario = new Veterinario(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getString("documento_identidad"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar el veterinario por ID: " + id + "\n" + e.getMessage());
        }

        return veterinario;
    }

    @Override
    public Veterinario buscarPorDocumento(String documento) {
        Veterinario veterinario = null;
        String sql = "SELECT * FROM veterinario WHERE documento_identidad = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documento);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    veterinario = new Veterinario(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getString("documento_identidad"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar el veterinario por documento: " + documento + "\n" + e.getMessage());
        }

        return veterinario;
    }

    @Override
    public void actualizarVeterinario(Veterinario veterinario) {
        String sql = "UPDATE veterinario SET nombre_completo = ?, documento_identidad = ?, telefono = ?, email = ? WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, veterinario.getNombreCompleto());
            pstmt.setString(2, veterinario.getDocumentoIdentidad());
            pstmt.setString(3, veterinario.getTelefono());
            pstmt.setString(4, veterinario.getEmail());
            pstmt.setInt(5, veterinario.getId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al actualizar el veterinario: " + veterinario + "\n" + e.getMessage());
        }
    }

    @Override
    public void eliminarVeterinario(Integer id) {
        String sql = "DELETE FROM veterinario WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al eliminar el veterinario con ID: " + id + "\n" + e.getMessage());
        }
    }

    @Override
    public List<Veterinario> buscarPorNombre(String nombre) {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "SELECT * FROM veterinario WHERE nombre_completo LIKE ? ORDER BY nombre_completo";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Veterinario veterinario = new Veterinario(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getString("documento_identidad"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                    lista.add(veterinario);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al buscar veterinarios por nombre: " + nombre + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Veterinario> listarDisponibles() {
        // Por simplicidad, consideramos disponibles a todos los veterinarios activos
        // En una implementación más compleja, aquí se verificarían horarios y disponibilidad
        return listarTodos();
    }
}