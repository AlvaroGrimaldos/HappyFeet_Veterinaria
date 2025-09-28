package com.happyfeet.model.enums;

public enum TipoValidacion {
    NOMBRE_VACIO("El nombre no puede estar vacío"),
    DOCUMENTO_VACIO("El documento de identidad es obligatorio"),
    EMAIL_INVALIDO("El formato del email es inválido"),
    TELEFONO_INVALIDO("El formato del teléfono es inválido"),
    FECHA_INVALIDA("La fecha de nacimiento es inválida"),
    DUENO_NO_EXISTE("El dueño especificado no existe"),
    RAZA_NO_EXISTE("La raza especificada no existe");

    private final String mensaje;

    TipoValidacion(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}