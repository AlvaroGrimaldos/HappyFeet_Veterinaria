package com.happyfeet.repository;

import com.happyfeet.model.entities.Raza;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RazaDAO implements IRazaDAO {
    private Connection con;

    public RazaDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public List<Raza> listarTodas() {
        List<Raza> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.especie_id, r.nombre, e.nombre as nombre_especie " +
                "FROM razas r " +
                "INNER JOIN especies e ON r.especie_id = e.id " +
                "ORDER BY e.nombre, r.nombre";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Raza raza = new Raza(
                        rs.getInt("id"),
                        rs.getInt("especie_id"),
                        rs.getString("nombre")
                );
                raza.setNombreEspecie(rs.getString("nombre_especie"));
                lista.add(raza);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todas las razas\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public Raza buscarPorId(Integer id) {
        Raza raza = null;
        String sql = "SELECT r.id, r.especie_id, r.nombre, e.nombre as nombre_especie " +
                "FROM razas r " +
                "INNER JOIN especies e ON r.especie_id = e.id " +
                "WHERE r.id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    raza = new Raza(
                            rs.getInt("id"),
                            rs.getInt("especie_id"),
                            rs.getString("nombre")
                    );
                    raza.setNombreEspecie(rs.getString("nombre_especie"));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar la raza por ID: " + id + "\n" + e.getMessage());
        }

        return raza;
    }

    @Override
    public List<Raza> buscarPorEspecie(Integer especieId) {
        List<Raza> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.especie_id, r.nombre, e.nombre as nombre_especie " +
                "FROM razas r " +
                "INNER JOIN especies e ON r.especie_id = e.id " +
                "WHERE r.especie_id = ? " +
                "ORDER BY r.nombre";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, especieId);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Raza raza = new Raza(
                            rs.getInt("id"),
                            rs.getInt("especie_id"),
                            rs.getString("nombre")
                    );
                    raza.setNombreEspecie(rs.getString("nombre_especie"));
                    lista.add(raza);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar razas por especie: " + especieId + "\n" + e.getMessage());
        }

        return lista;
    }
}