package com.happyfeet.repository;

import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.enums.EstadoCita;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO implements ICitaDAO {
    private Connection con;

    public CitaDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarCita(Cita cita) {
        String sql = "INSERT INTO citas(mascota_id, fecha_hora, motivo, estado_id, veterinario_id) VALUES(?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, cita.getMascotaId());
            pstmt.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            pstmt.setString(3, cita.getMotivo());
            pstmt.setInt(4, cita.getEstado().getId());
            pstmt.setInt(5, cita.getVeterinarioId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar la cita\n" + e.getMessage());
        }
    }

    @Override
    public List<Cita> listarTodas() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY fecha_hora DESC";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Cita cita = crearCitaDesdeResultSet(rs);
                lista.add(cita);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todas las citas\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Cita> listarConInformacionCompleta() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.mascota_id, c.fecha_hora, c.motivo, c.estado_id, c.veterinario_id, " +
                "m.nombre as nombre_mascota, " +
                "d.nombre_completo as nombre_dueno, " +
                "v.nombre_completo as nombre_veterinario " +
                "FROM citas c " +
                "INNER JOIN mascotas m ON c.mascota_id = m.id " +
                "INNER JOIN duenos d ON m.dueno_id = d.id " +
                "INNER JOIN veterinario v ON c.veterinario_id = v.id " +
                "ORDER BY c.fecha_hora DESC";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Cita cita = crearCitaDesdeResultSet(rs);
                cita.setNombreMascota(rs.getString("nombre_mascota"));
                cita.setNombreDueno(rs.getString("nombre_dueno"));
                cita.setNombreVeterinario(rs.getString("nombre_veterinario"));
                lista.add(cita);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar citas con información completa\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public Cita buscarPorId(Integer id) {
        Cita cita = null;
        String sql = "SELECT * FROM citas WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    cita = crearCitaDesdeResultSet(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar la cita por ID: " + id + "\n" + e.getMessage());
        }

        return cita;
    }

    @Override
    public List<Cita> buscarPorMascota(Integer mascotaId) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE mascota_id = ? ORDER BY fecha_hora DESC";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mascotaId);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Cita cita = crearCitaDesdeResultSet(rs);
                    lista.add(cita);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar citas por mascota: " + mascotaId + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Cita> buscarPorVeterinario(Integer veterinarioId) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE veterinario_id = ? ORDER BY fecha_hora";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, veterinarioId);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Cita cita = crearCitaDesdeResultSet(rs);
                    lista.add(cita);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar citas por veterinario: " + veterinarioId + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Cita> buscarPorEstado(EstadoCita estado) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE estado_id = ? ORDER BY fecha_hora";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, estado.getId());

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Cita cita = crearCitaDesdeResultSet(rs);
                    lista.add(cita);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar citas por estado: " + estado + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Cita> buscarPorFecha(LocalDate fecha) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE DATE(fecha_hora) = ? ORDER BY fecha_hora";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fecha));

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Cita cita = crearCitaDesdeResultSet(rs);
                    lista.add(cita);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar citas por fecha: " + fecha + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Cita> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE fecha_hora BETWEEN ? AND ? ORDER BY fecha_hora";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            pstmt.setTimestamp(2, Timestamp.valueOf(fechaFin));

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Cita cita = crearCitaDesdeResultSet(rs);
                    lista.add(cita);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar citas por rango de fechas\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public void actualizarCita(Cita cita) {
        String sql = "UPDATE citas SET mascota_id = ?, fecha_hora = ?, motivo = ?, estado_id = ?, veterinario_id = ? WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, cita.getMascotaId());
            pstmt.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            pstmt.setString(3, cita.getMotivo());
            pstmt.setInt(4, cita.getEstado().getId());
            pstmt.setInt(5, cita.getVeterinarioId());
            pstmt.setInt(6, cita.getId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al actualizar la cita: " + cita + "\n" + e.getMessage());
        }
    }

    @Override
    public void eliminarCita(Integer id) {
        String sql = "DELETE FROM citas WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al eliminar la cita con ID: " + id + "\n" + e.getMessage());
        }
    }

    @Override
    public boolean existeConflictoHorario(Integer veterinarioId, LocalDateTime fechaHora, Integer citaId) {
        String sql = "SELECT COUNT(*) FROM citas WHERE veterinario_id = ? AND fecha_hora = ? AND estado_id IN (1, 2)";

        if (citaId != null) {
            sql += " AND id != ?";
        }

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, veterinarioId);
            pstmt.setTimestamp(2, Timestamp.valueOf(fechaHora));

            if (citaId != null) {
                pstmt.setInt(3, citaId);
            }

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al verificar conflicto de horario\n" + e.getMessage());
        }

        return false;
    }

    private Cita crearCitaDesdeResultSet(ResultSet rs) throws SQLException {
        LocalDateTime fechaHora = rs.getTimestamp("fecha_hora") != null ?
                rs.getTimestamp("fecha_hora").toLocalDateTime() : null;

        EstadoCita estado = EstadoCita.fromId(rs.getInt("estado_id"));

        return new Cita(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                fechaHora,
                rs.getString("motivo"),
                estado,
                rs.getInt("veterinario_id")
        );
    }
}