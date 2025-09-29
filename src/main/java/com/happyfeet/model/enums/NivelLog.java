package com.happyfeet.model.enums;

public enum NivelLog {
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    DEBUG("DEBUG");

    private final String nivel;

    NivelLog(String nivel) {
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return nivel;
    }
}