package com.sv.grupo7.gimnasio_plus_ultra.model;

public enum TipoMovimiento {
    ENTRADA,    // entró al gimnasio
    SALIDA,     // salió del gimnasio
    RECHAZADO,  // no se le permitió entrar
    DUPLICADO   // escaneó dos veces seguidas; no se registra nada nuevo
}
