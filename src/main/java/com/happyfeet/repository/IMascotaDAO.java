package com.happyfeet.repository;

import com.happyfeet.model.entities.Mascota;
import java.util.List;

public interface IMascotaDAO {
    void agregarMascota(Mascota mascota);
    List<Mascota> listarTodas();
    Mascota buscarPorId(Integer id);
    List<Mascota> buscarPorDueno(Integer duenoId);
    void actualizarMascota(Mascota mascota);
    void eliminarMascota(Integer id);
    List<Mascota> buscarPorNombre(String nombre);
    List<Mascota> listarConInformacionCompleta();
}