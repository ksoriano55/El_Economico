package com.example.supermercado_el_economico.Config;

public class User {

    private int usuarioId;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String correo;
    private String usuario;
    private String pass;
    private String foto;
    private boolean verificado;
    private boolean activo;
    private boolean Repartidor;

    public User(int usuarioId, String nombre, String apellido, String telefono, String direccion, String correo, String usuario, String pass, String foto) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.usuario = usuario;
        this.pass = pass;

        this.foto = foto;
    }
    public User (){

    }
    public int  getUsuarioId() {
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public boolean isRepartidor() {
        return Repartidor;
    }

    public void setRepartidor(boolean Repartidor) {
        this.Repartidor = Repartidor;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    //para pasar a string
    @Override
    public String toString() {
        return "User{" +
                "foto='" + foto + '\'' +
                '}';
    }
}
