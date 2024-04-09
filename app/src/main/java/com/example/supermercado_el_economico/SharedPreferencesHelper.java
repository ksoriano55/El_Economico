package com.example.supermercado_el_economico;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.supermercado_el_economico.models.Producto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {
    private static final String SHARED_PREFS_NAME = "PRODUCTOS_SHARED_PREFS";
    private static final String KEY_PRODUCTOS = "PRODUCTOS_LIST";


    public static void saveProductos(Context context, List<Producto> productos) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String productosJson = gson.toJson(productos);
        editor.putString(KEY_PRODUCTOS, productosJson);
        editor.apply();
    }


    public static List<Producto> loadProductos(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String productosJson = sharedPreferences.getString(KEY_PRODUCTOS, null);
        if (productosJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Producto>>() {}.getType();
            return gson.fromJson(productosJson, type);
        }
        return new ArrayList<>();
    }


    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}


