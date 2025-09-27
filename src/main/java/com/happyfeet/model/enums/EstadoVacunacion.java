package com.happyfeet.model.enums;

public enum EstadoVacunacion {
    SI("Si"),
    NO("No");

    private final String valor;

    EstadoVacunacion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoVacunacion fromString(String texto) {
        if (texto != null) {
            for (EstadoVacunacion estado : EstadoVacunacion.values()) {
                if (estado.valor.equalsIgnoreCase(texto)) {
                    return estado;
                }
            }
        }
        throw new IllegalArgumentException("Estado de vacunación inválido: " + texto);
    }

    @Override
    public String toString() {
        return valor;
    }
}