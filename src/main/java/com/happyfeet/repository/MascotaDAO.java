package com.happyfeet.repository;

import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.enums.SexoMascota;
import com.happyfeet.model.enums.EstadoVacunacion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO implements IMascotaDAO {
    private Connection con;

    public MascotaDAO() {
        con = ConexionDB.getInstancia().getConnection();
    }

    @Override
    public void agregarMascota(Mascota mascota) {
        String sql = "INSERT INTO mascotas(dueno_id, nombre, raza_id, fecha_nacimiento, sexo, url_foto, vacunado) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mascota.getDuenoId());
            pstmt.setString(2, mascota.getNombre());
            pstmt.setInt(3, mascota.getRazaId());
            pstmt.setDate(4, Date.valueOf(mascota.getFechaNacimiento()));
            pstmt.setString(5, mascota.getSexo().getValor());
            pstmt.setString(6, mascota.getUrlFoto());
            pstmt.setString(7, mascota.getVacunado().getValor());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar la mascota\n" + e.getMessage());
        }
    }

    @Override
    public List<Mascota> listarTodas() {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas ORDER BY nombre";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Mascota mascota = crearMascotaDesdeResultSet(rs);
                lista.add(mascota);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar todas las mascotas\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Mascota> listarConInformacionCompleta() {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.dueno_id, m.nombre, m.raza_id, m.fecha_nacimiento, " +
                "m.sexo, m.url_foto, m.vacunado, " +
                "d.nombre_completo as nombre_dueno, " +
                "r.nombre as nombre_raza, " +
                "e.nombre as nombre_especie " +
                "FROM mascotas m " +
                "INNER JOIN duenos d ON m.dueno_id = d.id " +
                "INNER JOIN razas r ON m.raza_id = r.id " +
                "INNER JOIN especies e ON r.especie_id = e.id " +
                "ORDER BY m.nombre";

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Mascota mascota = crearMascotaDesdeResultSet(rs);
                mascota.setNombreDueno(rs.getString("nombre_dueno"));
                mascota.setNombreRaza(rs.getString("nombre_raza"));
                mascota.setNombreEspecie(rs.getString("nombre_especie"));
                lista.add(mascota);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar mascotas con información completa\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public Mascota buscarPorId(Integer id) {
        Mascota mascota = null;
        String sql = "SELECT * FROM mascotas WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    mascota = crearMascotaDesdeResultSet(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar la mascota por ID: " + id + "\n" + e.getMessage());
        }

        return mascota;
    }

    @Override
    public List<Mascota> buscarPorDueno(Integer duenoId) {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas WHERE dueno_id = ? ORDER BY nombre";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, duenoId);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Mascota mascota = crearMascotaDesdeResultSet(rs);
                    lista.add(mascota);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al consultar mascotas por dueño: " + duenoId + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Mascota> buscarPorNombre(String nombre) {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas WHERE nombre LIKE ? ORDER BY nombre";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Mascota mascota = crearMascotaDesdeResultSet(rs);
                    lista.add(mascota);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error al buscar mascotas por nombre: " + nombre + "\n" + e.getMessage());
        }

        return lista;
    }

    @Override
    public void actualizarMascota(Mascota mascota) {
        String sql = "UPDATE mascotas SET dueno_id = ?, nombre = ?, raza_id = ?, fecha_nacimiento = ?, sexo = ?, url_foto = ?, vacunado = ? WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mascota.getDuenoId());
            pstmt.setString(2, mascota.getNombre());
            pstmt.setInt(3, mascota.getRazaId());
            pstmt.setDate(4, Date.valueOf(mascota.getFechaNacimiento()));
            pstmt.setString(5, mascota.getSexo().getValor());
            pstmt.setString(6, mascota.getUrlFoto());
            pstmt.setString(7, mascota.getVacunado().getValor());
            pstmt.setInt(8, mascota.getId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error al actualizar la mascota: " + mascota + "\n" + e.getMessage());
        }
    }

    @Override
    public void eliminarMascota(Integer id) {
        String sql = "DELETE FROM mascotas WHERE id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se encontró la mascota con ID: " + id);
            }
        } catch(SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                throw new RuntimeException("No se puede eliminar la mascota con ID: " + id +
                        ". Tiene registros asociados (citas o historial médico). Elimine primero esos registros.");
            }
            throw new RuntimeException("Error al eliminar la mascota con ID: " + id + "\n" + e.getMessage());
        }
    }

    private Mascota crearMascotaDesdeResultSet(ResultSet rs) throws SQLException {
        LocalDate fechaNacimiento = rs.getDate("fecha_nacimiento") != null ?
                rs.getDate("fecha_nacimiento").toLocalDate() : null;

        return new Mascota(
                rs.getInt("id"),
                rs.getInt("dueno_id"),
                rs.getString("nombre"),
                rs.getInt("raza_id"),
                fechaNacimiento,
                SexoMascota.fromString(rs.getString("sexo")),
                rs.getString("url_foto"),
                EstadoVacunacion.fromString(rs.getString("vacunado"))
        );
    }
}