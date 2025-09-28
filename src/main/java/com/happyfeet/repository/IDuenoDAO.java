// IDuenoDAO.java
package com.happyfeet.repository;

import com.happyfeet.model.entities.Dueno;
import java.util.List;

public interface IDuenoDAO {
    void agregarDueno(Dueno dueno);
    List<Dueno> listarTodos();
    Dueno buscarPorId(Integer id);
    Dueno buscarPorDocumento(String documento);
    void actualizarDueno(Dueno dueno);
    void eliminarDueno(Integer id);
    List<Dueno> buscarPorNombre(String nombre);
}

