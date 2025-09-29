package com.happyfeet.service;

import com.happyfeet.model.entities.Dueno;
import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.entities.Raza;
import com.happyfeet.repository.*;
import com.happyfeet.util.LoggerUtil;
import com.happyfeet.util.ValidadorUtil;

import java.util.List;

public class PacienteService {
    private IDuenoDAO duenoDAO;
    private IMascotaDAO mascotaDAO;
    private IRazaDAO razaDAO;

    public PacienteService() {
        this.duenoDAO = new DuenoDAO();
        this.mascotaDAO = new MascotaDAO();
        this.razaDAO = new RazaDAO();
    }

    // ================== GESTIÓN DE DUEÑOS ==================

    public boolean registrarDueno(Dueno dueno) {
        try {
            if (!validarDueno(dueno)) {
                return false;
            }

            // Verificar que no exista ya por documento
            if (duenoDAO.buscarPorDocumento(dueno.getDocumentoIdentidad()) != null) {
                LoggerUtil.warning("Ya existe un dueño con documento: " + dueno.getDocumentoIdentidad());
                return false;
            }

            duenoDAO.agregarDueno(dueno);
            LoggerUtil.operacionExitosa("Registro", "Dueño");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Registro", "Dueño", e.getMessage());
            return false;
        }
    }

    public boolean actualizarDueno(Dueno dueno) {
        try {
            if (!validarDueno(dueno) || dueno.getId() == null) {
                return false;
            }

            // Verificar que exista
            if (duenoDAO.buscarPorId(dueno.getId()) == null) {
                LoggerUtil.warning("No existe el dueño con ID: " + dueno.getId());
                return false;
            }

            duenoDAO.actualizarDueno(dueno);
            LoggerUtil.operacionExitosa("Actualización", "Dueño");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Actualización", "Dueño", e.getMessage());
            return false;
        }
    }

    public boolean eliminarDueno(Integer id) {
        try {
            // Verificar que no tenga mascotas asociadas
            List<Mascota> mascotasAsociadas = mascotaDAO.buscarPorDueno(id);
            if (!mascotasAsociadas.isEmpty()) {
                LoggerUtil.warning("No se puede eliminar el dueño. Tiene " + mascotasAsociadas.size() + " mascotas asociadas");
                return false;
            }

            duenoDAO.eliminarDueno(id);
            LoggerUtil.operacionExitosa("Eliminación", "Dueño");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Eliminación", "Dueño", e.getMessage());
            return false;
        }
    }

    public List<Dueno> listarDuenos() {
        return duenoDAO.listarTodos();
    }

    public Dueno buscarDuenoPorId(Integer id) {
        return duenoDAO.buscarPorId(id);
    }

    public Dueno buscarDuenoPorDocumento(String documento) {
        return duenoDAO.buscarPorDocumento(documento);
    }

    public List<Dueno> buscarDuenosPorNombre(String nombre) {
        return duenoDAO.buscarPorNombre(nombre);
    }

    // ================== GESTIÓN DE MASCOTAS ==================

    public boolean registrarMascota(Mascota mascota) {
        try {
            if (!validarMascota(mascota)) {
                return false;
            }

            // Verificar que el dueño exista
            if (duenoDAO.buscarPorId(mascota.getDuenoId()) == null) {
                LoggerUtil.warning("No existe el dueño con ID: " + mascota.getDuenoId());
                return false;
            }

            // Verificar que la raza exista
            if (razaDAO.buscarPorId(mascota.getRazaId()) == null) {
                LoggerUtil.warning("No existe la raza con ID: " + mascota.getRazaId());
                return false;
            }

            mascotaDAO.agregarMascota(mascota);
            LoggerUtil.operacionExitosa("Registro", "Mascota");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Registro", "Mascota", e.getMessage());
            return false;
        }
    }

    public boolean actualizarMascota(Mascota mascota) {
        try {
            if (!validarMascota(mascota) || mascota.getId() == null) {
                return false;
            }

            // Verificar que exista
            if (mascotaDAO.buscarPorId(mascota.getId()) == null) {
                LoggerUtil.warning("No existe la mascota con ID: " + mascota.getId());
                return false;
            }

            mascotaDAO.actualizarMascota(mascota);
            LoggerUtil.operacionExitosa("Actualización", "Mascota");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Actualización", "Mascota", e.getMessage());
            return false;
        }
    }

    public boolean eliminarMascota(Integer id) {
        try {
            // Verificar si tiene citas activas
            // Esto depende si tienes acceso al CitaDAO aquí

            mascotaDAO.eliminarMascota(id);
            LoggerUtil.operacionExitosa("Eliminación", "Mascota");
            return true;
        } catch (Exception e) {
            LoggerUtil.operacionFallida("Eliminación", "Mascota", e.getMessage());
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    public List<Mascota> listarMascotas() {
        return mascotaDAO.listarTodas();
    }

    public List<Mascota> listarMascotasConInfoCompleta() {
        return mascotaDAO.listarConInformacionCompleta();
    }

    public Mascota buscarMascotaPorId(Integer id) {
        return mascotaDAO.buscarPorId(id);
    }

    public List<Mascota> buscarMascotasPorDueno(Integer duenoId) {
        return mascotaDAO.buscarPorDueno(duenoId);
    }

    public List<Mascota> buscarMascotasPorNombre(String nombre) {
        return mascotaDAO.buscarPorNombre(nombre);
    }

    // ================== UTILIDADES ==================

    public List<Raza> listarRazas() {
        return razaDAO.listarTodas();
    }

    public List<Raza> buscarRazasPorEspecie(Integer especieId) {
        return razaDAO.buscarPorEspecie(especieId);
    }

    // ================== VALIDACIONES ==================

    private boolean validarDueno(Dueno dueno) {
        if (dueno == null) {
            LoggerUtil.warning("El dueño no puede ser nulo");
            return false;
        }

        if (!ValidadorUtil.esTextoValido(dueno.getNombreCompleto(), 3)) {
            LoggerUtil.warning("El nombre completo debe tener al menos 3 caracteres");
            return false;
        }

        if (!ValidadorUtil.esDocumentoValido(dueno.getDocumentoIdentidad())) {
            LoggerUtil.warning("El documento de identidad no es válido");
            return false;
        }

        if (!ValidadorUtil.esEmailValido(dueno.getEmail())) {
            LoggerUtil.warning("El email no tiene un formato válido");
            return false;
        }

        if (!ValidadorUtil.esTelefonoValido(dueno.getTelefono())) {
            LoggerUtil.warning("El teléfono no tiene un formato válido");
            return false;
        }

        if (!ValidadorUtil.esTextoValido(dueno.getDireccion(), 5)) {
            LoggerUtil.warning("La dirección debe tener al menos 5 caracteres");
            return false;
        }

        return true;
    }

    private boolean validarMascota(Mascota mascota) {
        if (mascota == null) {
            LoggerUtil.warning("La mascota no puede ser nula");
            return false;
        }

        if (!ValidadorUtil.esTextoValido(mascota.getNombre(), 2)) {
            LoggerUtil.warning("El nombre de la mascota debe tener al menos 2 caracteres");
            return false;
        }

        if (mascota.getDuenoId() == null || mascota.getDuenoId() <= 0) {
            LoggerUtil.warning("La mascota debe tener un dueño válido");
            return false;
        }

        if (mascota.getRazaId() == null || mascota.getRazaId() <= 0) {
            LoggerUtil.warning("La mascota debe tener una raza válida");
            return false;
        }

        if (mascota.getFechaNacimiento() == null) {
            LoggerUtil.warning("La fecha de nacimiento es obligatoria");
            return false;
        }

        if (mascota.getSexo() == null) {
            LoggerUtil.warning("El sexo de la mascota es obligatorio");
            return false;
        }

        if (mascota.getVacunado() == null) {
            LoggerUtil.warning("El estado de vacunación es obligatorio");
            return false;
        }

        return true;
    }
}