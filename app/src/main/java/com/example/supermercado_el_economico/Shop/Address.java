package com.example.supermercado_el_economico.Shop;

public class Address {
    private int usuarioId;
    private String nombre;
    private String referencia;
    private String latitud;
    private String longitud;

    // Constructor
    public Address (int usuarioId, String nombre, String referencia, String latitud, String longitud) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public Address(){

    }

    // Getters y Setters
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
