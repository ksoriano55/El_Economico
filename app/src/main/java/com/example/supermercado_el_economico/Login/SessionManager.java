package com.example.supermercado_el_economico.Login;
import android.content.Context;
import android.content.SharedPreferences;
public class SessionManager {
    private static final String PREF_NAME = "MyAppPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    // Método para iniciar sesión
    public void login(String userId, String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }
    // Método para cerrar sesión
    public void logout() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.commit();
    }
    // Método para verificar si el usuario está conectado
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    // Método para obtener el ID del usuario
    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }
    // Método para obtener el nombre de usuario
    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }
}


