package com.happyfeet.model.enums;

public enum EstadoCita {
    PROGRAMADA("Programada"),
    EN_PROCESO("En Proceso"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String valor;

    EstadoCita(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoCita fromString(String texto) {
        if (texto != null) {
            for (EstadoCita estado : EstadoCita.values()) {
                if (estado.valor.equalsIgnoreCase(texto)) {
                    return estado;
                }
            }
        }
        throw new IllegalArgumentException("Estado de cita inválido: " + texto);
    }

    public static EstadoCita fromId(Integer id) {
        if (id == null) return null;

        return switch (id) {
            case 1 -> PROGRAMADA;
            case 2 -> EN_PROCESO;
            case 3 -> FINALIZADA;
            case 4 -> CANCELADA;
            default -> throw new IllegalArgumentException("ID de estado inválido: " + id);
        };
    }

    public Integer getId() {
        return switch (this) {
            case PROGRAMADA -> 1;
            case EN_PROCESO -> 2;
            case FINALIZADA -> 3;
            case CANCELADA -> 4;
        };
    }

    @Override
    public String toString() {
        return valor;
    }
}