package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.util.List;

public class VistaCancelacionResponse {

    private List<CambioFechaSuscripcion> cambios;
    private boolean quedaSinPlanVigente;

    public VistaCancelacionResponse() {
    }

    public List<CambioFechaSuscripcion> getCambios() {
        return cambios;
    }

    public void setCambios(List<CambioFechaSuscripcion> cambios) {
        this.cambios = cambios;
    }

    public boolean isQuedaSinPlanVigente() {
        return quedaSinPlanVigente;
    }

    public void setQuedaSinPlanVigente(boolean quedaSinPlanVigente) {
        this.quedaSinPlanVigente = quedaSinPlanVigente;
    }
}
