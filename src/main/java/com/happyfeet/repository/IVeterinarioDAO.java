package com.happyfeet.repository;

import com.happyfeet.model.entities.Veterinario;
import java.util.List;

public interface IVeterinarioDAO {
    void agregarVeterinario(Veterinario veterinario);
    List<Veterinario> listarTodos();
    Veterinario buscarPorId(Integer id);
    Veterinario buscarPorDocumento(String documento);
    void actualizarVeterinario(Veterinario veterinario);
    void eliminarVeterinario(Integer id);
    List<Veterinario> buscarPorNombre(String nombre);
    List<Veterinario> listarDisponibles();
}