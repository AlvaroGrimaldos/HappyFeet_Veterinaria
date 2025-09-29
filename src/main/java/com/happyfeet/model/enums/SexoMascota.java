package com.happyfeet.model.enums;

public enum SexoMascota {
    MACHO("Macho"),
    HEMBRA("Hembra");

    private final String valor;

    SexoMascota(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static SexoMascota fromString(String texto) {
        if (texto != null) {
            for (SexoMascota sexo : SexoMascota.values()) {
                if (sexo.valor.equalsIgnoreCase(texto)) {
                    return sexo;
                }
            }
        }
        throw new IllegalArgumentException("Sexo inválido: " + texto);
    }

    @Override
    public String toString() {
        return valor;
    }
}