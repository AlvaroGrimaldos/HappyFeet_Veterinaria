package com.happyfeet.repository;

import com.happyfeet.model.entities.HistorialMedico;
import java.util.List;

public interface IHistorialMedicoDAO {
    void agregarHistorial(HistorialMedico historial);
    List<HistorialMedico> listarTodos();
    HistorialMedico buscarPorId(Integer id);
    List<HistorialMedico> buscarPorMascota(Integer mascotaId);
    void actualizarHistorial(HistorialMedico historial);
    void eliminarHistorial(Integer id);
    List<HistorialMedico> listarConInformacionCompleta();
}