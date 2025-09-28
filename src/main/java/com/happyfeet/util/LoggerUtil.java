package com.happyfeet.util;

import com.happyfeet.model.enums.NivelLog;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(NivelLog nivel, String mensaje) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logLine = String.format("[%s] [%s] %s", timestamp, nivel.getNivel(), mensaje);

        switch (nivel) {
            case ERROR:
                System.err.println(logLine);
                break;
            case WARNING:
                System.out.println(logLine);
                break;
            case INFO:
            case DEBUG:
            default:
                System.out.println(logLine);
                break;
        }
    }

    public static void info(String mensaje) {
        log(NivelLog.INFO, mensaje);
    }

    public static void warning(String mensaje) {
        log(NivelLog.WARNING, mensaje);
    }

    public static void error(String mensaje) {
        log(NivelLog.ERROR, mensaje);
    }

    public static void debug(String mensaje) {
        log(NivelLog.DEBUG, mensaje);
    }

    public static void operacionExitosa(String operacion, String entidad) {
        info(String.format("%s de %s realizada exitosamente", operacion, entidad));
    }

    public static void operacionFallida(String operacion, String entidad, String error) {
        error(String.format("Error en %s de %s: %s", operacion, entidad, error));
    }
}