package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

// REQ = DTO que entra (Request), RES = DTO que sale (Response).
public interface CrudService<REQ, RES> {
    RES crear(REQ request);

    List<RES> obtenerTodos();

    RES obtenerPorId(Integer id);       // null si no existe

    RES actualizar(Integer id, REQ request); // null si no existe

    boolean eliminar(Integer id);       // false si no existe
}
