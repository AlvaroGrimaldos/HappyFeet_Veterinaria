package com.happyfeet.repository;

import com.happyfeet.model.entities.Raza;
import java.util.List;

public interface IRazaDAO {
    List<Raza> listarTodas();
    Raza buscarPorId(Integer id);
    List<Raza> buscarPorEspecie(Integer especieId);
}