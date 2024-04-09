package com.example.supermercado_el_economico.ApiRest;

public class AuthenticationApiMethods {
    private static final String URLAPI= "https://delivery-service.azurewebsites.net/api/";
    public static final String EndPointLogin = URLAPI + "Autenticacion/Login";
    public static final String EndPointVerificarCodigo = "https://delivery-service.azurewebsites.net/api/Autenticacion/VerificarUsuario?usuarioId=1";

    public static final String  EndPointReenviarCodigo ="";


}
