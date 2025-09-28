package com.happyfeet.repository;

import com.happyfeet.model.entities.EventoTipo;
import java.util.List;

public interface IEventoTipoDAO {
    List<EventoTipo> listarTodos();
    EventoTipo buscarPorId(Integer id);
}