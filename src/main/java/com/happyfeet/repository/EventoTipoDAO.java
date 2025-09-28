package com.happyfeet.repository;

import com.happyfeet.model.entities.EventoTipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoTipoDAO implements IEventoTipoDAO {
    private Connection con;

    public EventoTipoDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public List<EventoTipo> listarTodos() {
        List<EventoTipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento_tipos ORDER BY nombre";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                EventoTipo eventoTipo = new EventoTipo(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
                lista.add(eventoTipo);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todos los tipos de eventos\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public EventoTipo buscarPorId(Integer id) {
        EventoTipo eventoTipo = null;
        String sql = "SELECT * FROM evento_tipos WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    eventoTipo = new EventoTipo(
                            rs.getInt("id"),
                            rs.getString("nombre")
                    );
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar el tipo de evento por ID: " + id + "\n" + e.getMessage());
        }

        return eventoTipo;
    }
}