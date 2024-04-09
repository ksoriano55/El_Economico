package com.example.supermercado_el_economico.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable {
    private int productoId;
    private String nombre;
    private double precio;
    private double isv;
    private int categoriaId;
    private int medidaId;
    private String foto;
    private boolean activo;

    private int cantidad; // Nuevo campo para la cantidad

    // Constructor
    public Producto(int productoId, String nombre, double precio, double isv, int categoriaId, int medidaId, String foto, boolean activo) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.isv = isv;
        this.categoriaId = categoriaId;
        this.medidaId = medidaId;
        this.foto = foto;
        this.activo = activo;
        this.cantidad = 1; // Inicializa la cantidad en 0
    }

    // Getters y Setters

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getIsv() {
        return isv;
    }

    public void setIsv(double isv) {
        this.isv = isv;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getMedidaId() {
        return medidaId;
    }

    public void setMedidaId(int medidaId) {
        this.medidaId = medidaId;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Parcelable
    protected Producto(Parcel in) {
        productoId = in.readInt();
        nombre = in.readString();
        precio = in.readDouble();
        isv = in.readDouble();
        categoriaId = in.readInt();
        medidaId = in.readInt();
        foto = in.readString();
        activo = in.readByte() != 0;
        cantidad = in.readInt(); // Lee la cantidad del Parcel
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productoId);
        dest.writeString(nombre);
        dest.writeDouble(precio);
        dest.writeDouble(isv);
        dest.writeInt(categoriaId);
        dest.writeInt(medidaId);
        dest.writeString(foto);
        dest.writeByte((byte) (activo ? 1 : 0));
        dest.writeInt(cantidad); // Escribe la cantidad en el Parcel
    }
}
