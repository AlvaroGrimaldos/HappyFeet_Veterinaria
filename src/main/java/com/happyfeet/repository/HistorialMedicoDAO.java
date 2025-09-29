package com.happyfeet.repository;

import com.happyfeet.model.entities.HistorialMedico;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistorialMedicoDAO implements IHistorialMedicoDAO {
    private Connection con;

    public HistorialMedicoDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarHistorial(HistorialMedico historial) {
        String sql = "INSERT INTO historial_medico(mascota_id, fecha_evento, evento_tipo_id, descripcion, diagnostico, tratamiento_recomendado, centro_veterinario_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, historial.getMascotaId());
            pstmt.setDate(2, Date.valueOf(historial.getFechaEvento()));
            pstmt.setInt(3, historial.getEventoTipoId());
            pstmt.setString(4, historial.getDescripcion());
            pstmt.setString(5, historial.getDiagnostico());
            pstmt.setString(6, historial.getTratamientoRecomendado());
            pstmt.setInt(7, historial.getCentroVeterinarioId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar el historial médico\n" + e.getMessage());
        }
    }

    @Override
    public List<HistorialMedico> listarTodos() {
        List<HistorialMedico> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_medico ORDER BY fecha_evento DESC";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                HistorialMedico historial = crearHistorialDesdeResultSet(rs);
                lista.add(historial);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todo el historial médico\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<HistorialMedico> listarConInformacionCompleta() {
        List<HistorialMedico> lista = new ArrayList<>();
        String sql = "SELECT h.id, h.mascota_id, h.fecha_evento, h.evento_tipo_id, " +
                "h.descripcion, h.diagnostico, h.tratamiento_recomendado, h.centro_veterinario_id, " +
                "m.nombre as nombre_mascota, " +
                "d.nombre_completo as nombre_dueno, " +
                "et.nombre as evento_tipo_nombre, " +
                "cv.nombre as centro_veterinario_nombre " +
                "FROM historial_medico h " +
                "INNER JOIN mascotas m ON h.mascota_id = m.id " +
                "INNER JOIN duenos d ON m.dueno_id = d.id " +
                "INNER JOIN evento_tipos et ON h.evento_tipo_id = et.id " +
                "INNER JOIN centro_veterinario cv ON h.centro_veterinario_id = cv.id " +
                "ORDER BY h.fecha_evento DESC";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                HistorialMedico historial = crearHistorialDesdeResultSet(rs);
                historial.setNombreMascota(rs.getString("nombre_mascota"));
                historial.setNombreDueno(rs.getString("nombre_dueno"));
                historial.setEventoTipoNombre(rs.getString("evento_tipo_nombre"));
                historial.setCentroVeterinarioNombre(rs.getString("centro_veterinario_nombre"));
                lista.add(historial);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar historial médico con información completa\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public HistorialMedico buscarPorId(Integer id) {
        HistorialMedico historial = null;
        String sql = "SELECT * FROM historial_medico WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    historial = crearHistorialDesdeResultSet(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar el historial médico por ID: " + id + "\n" + e.getMessage());
        }

        return historial;
    }

    @Override
    public List<HistorialMedico> buscarPorMascota(Integer mascotaId) {
        List<HistorialMedico> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_medico WHERE mascota_id = ? ORDER BY fecha_evento DESC";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mascotaId);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    HistorialMedico historial = crearHistorialDesdeResultSet(rs);
                    lista.add(historial);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar historial médico por mascota: " + mascotaId + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public void actualizarHistorial(HistorialMedico historial) {
        String sql = "UPDATE historial_medico SET mascota_id = ?, fecha_evento = ?, evento_tipo_id = ?, descripcion = ?, diagnostico = ?, tratamiento_recomendado = ?, centro_veterinario_id = ? WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, historial.getMascotaId());
            pstmt.setDate(2, Date.valueOf(historial.getFechaEvento()));
            pstmt.setInt(3, historial.getEventoTipoId());
            pstmt.setString(4, historial.getDescripcion());
            pstmt.setString(5, historial.getDiagnostico());
            pstmt.setString(6, historial.getTratamientoRecomendado());
            pstmt.setInt(7, historial.getCentroVeterinarioId());
            pstmt.setInt(8, historial.getId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al actualizar el historial médico: " + historial + "\n" + e.getMessage());
        }
    }

    @Override
    public void eliminarHistorial(Integer id) {
        String sql = "DELETE FROM historial_medico WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al eliminar el historial médico con ID: " + id + "\n" + e.getMessage());
        }
    }

    private HistorialMedico crearHistorialDesdeResultSet(ResultSet rs) throws SQLException {
        LocalDate fechaEvento = rs.getDate("fecha_evento") != null ?
                rs.getDate("fecha_evento").toLocalDate() : null;

        return new HistorialMedico(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                fechaEvento,
                rs.getInt("evento_tipo_id"),
                rs.getString("descripcion"),
                rs.getString("diagnostico"),
                rs.getString("tratamiento_recomendado"),
                rs.getInt("centro_veterinario_id")
        );
    }
}