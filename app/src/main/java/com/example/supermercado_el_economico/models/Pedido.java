package com.example.supermercado_el_economico.models;

public class Pedido {
    private int pedidoId;
    private String numPedido;
    private String fecha;
    private String estado;
    private double total;
    private float calificacion;




    public Pedido(int pedidoId, String numPedido, String fecha, String estado, double total, float calificacion) {
        this.pedidoId = pedidoId;
        this.numPedido = numPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.calificacion = calificacion;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }
}
