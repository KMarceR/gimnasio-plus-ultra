package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDate;

public enum TipoPlan {

    MENSUAL(1),
    ANUAL(12);

    private final int meses;

    TipoPlan(int meses) {
        this.meses = meses;
    }

    public int getMeses() {
        return meses;
    }

    public LocalDate calcularVencimiento(LocalDate inicio) {
        return inicio.plusMonths(meses).minusDays(1);
    }
}
