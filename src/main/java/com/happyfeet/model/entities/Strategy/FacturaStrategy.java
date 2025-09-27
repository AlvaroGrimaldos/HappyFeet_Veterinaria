package com.happyfeet.model.entities.Strategy;

import java.math.BigDecimal;

public interface FacturaStrategy {
    double calcularTotal(BigDecimal subtotal);
}
