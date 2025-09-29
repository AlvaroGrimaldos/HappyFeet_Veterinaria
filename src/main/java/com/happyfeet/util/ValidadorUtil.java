package com.happyfeet.util;

import java.util.regex.Pattern;

public class ValidadorUtil {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^[0-9]{7,15}$");

    private static final Pattern DOCUMENTO_PATTERN =
            Pattern.compile("^[0-9A-Za-z]{5,20}$");

    // ================== MÉTODOS ORIGINALES (MANTENER COMPATIBILIDAD) ==================

    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Verificar que contenga @
        if (!email.contains("@")) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }

        // Remover espacios y verificar que solo contenga números
        String telefonoLimpio = telefono.trim().replaceAll("\\s+", "");

        // Verificar que solo contenga números
        if (!telefonoLimpio.matches("^[0-9]+$")) {
            return false;
        }

        return TELEFONO_PATTERN.matcher(telefonoLimpio).matches();
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

    // ================== MÉTODOS CON MENSAJES DE ERROR ESPECÍFICOS ==================

    public static String validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "El email es obligatorio";
        }

        if (!email.contains("@")) {
            return "El email debe contener el símbolo @";
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El formato del email es inválido (ejemplo: usuario@dominio.com)";
        }

        return null; // null significa que es válido
    }

    public static String validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return "El teléfono es obligatorio";
        }

        String telefonoLimpio = telefono.trim().replaceAll("\\s+", "");

        if (!telefonoLimpio.matches("^[0-9]+$")) {
            return "El teléfono solo debe contener números (sin espacios, guiones o símbolos)";
        }

        if (telefonoLimpio.length() < 7) {
            return "El teléfono debe tener al menos 7 dígitos";
        }

        if (telefonoLimpio.length() > 15) {
            return "El teléfono no puede tener más de 15 dígitos";
        }

        return null; // null significa que es válido
    }

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio";
        }

        if (nombre.trim().length() < 2) {
            return "El nombre debe tener al menos 2 caracteres";
        }

        return null; // null significa que es válido
    }

    public static String validarDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return "El documento de identidad es obligatorio";
        }

        if (!DOCUMENTO_PATTERN.matcher(documento.trim()).matches()) {
            return "El documento debe tener entre 5 y 20 caracteres alfanuméricos";
        }

        return null; // null significa que es válido
    }

    public static String validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            return "La dirección es obligatoria";
        }

        if (direccion.trim().length() < 5) {
            return "La dirección debe tener al menos 5 caracteres";
        }

        return null; // null significa que es válido
    }
}