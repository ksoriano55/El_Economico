package com.example.supermercado_el_economico.models;

public class DireccionModel {
    private int direccionId;
    private String nombre;
    private String referencia;

    public DireccionModel(Integer direccionId, String nombre, String referencia) {
        this.direccionId = direccionId;
        this.nombre = nombre;
        this.referencia = referencia;
    }

    public int getDireccionId() {
        return direccionId;
    }
    public String getNombre() {
        return nombre;
    }

    public String getReferencia() {
        return referencia;
    }
}
