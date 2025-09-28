package com.happyfeet.util;

import java.util.regex.Pattern;

public class ValidadorUtil {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^[0-9]{7,15}$");

    private static final Pattern DOCUMENTO_PATTERN =
            Pattern.compile("^[0-9A-Za-z]{5,20}$");

    public static boolean esEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && TELEFONO_PATTERN.matcher(telefono).matches();
    }

    public static boolean esDocumentoValido(String documento) {
        return documento != null && DOCUMENTO_PATTERN.matcher(documento).matches();
    }

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean esTextoValido(String texto, int minLength) {
        return esTextoValido(texto) && texto.trim().length() >= minLength;
    }

    public static boolean esTextoValido(String texto, int minLength, int maxLength) {
        return esTextoValido(texto, minLength) && texto.trim().length() <= maxLength;
    }
}