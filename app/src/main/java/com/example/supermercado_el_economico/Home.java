package com.example.supermercado_el_economico;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.supermercado_el_economico.ApiRest.RestApiMethods;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.example.supermercado_el_economico.Shop.CustomerAddress;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Home extends AppCompatActivity {

    private LinearLayout layout;
    private SessionManager session;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layout = findViewById(R.id.layout);
        //ImageButton btnuser = findViewById(R.id.btnuser);
        new FetchCategoriesTask().execute("https://delivery-service.azurewebsites.net/api/Categorias");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        /*btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,  Userlogin.class);
                startActivity(intent);
            }
        });*/
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_profile_photo) {
                    Intent intent = new Intent(Home.this, Userlogin.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if(itemId == R.id.page_1){
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(itemId == R.id.page_2){
                        Intent intent = new Intent(getApplicationContext(), PedidosActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(itemId == R.id.page_3){
                        Intent intent = new Intent(getApplicationContext(), CustomerAddress.class);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }
            };


    private class FetchCategoriesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject category = data.getJSONObject(i);
                        boolean isActive = category.getBoolean("activo");
                        if (isActive) {
                            int categoryId = category.getInt("categoriaId");
                            if (categoryId >= 2) { // Comienza desde el ID 2
                                String imageUrl = category.getString("foto");
                                String description = category.getString("descripcion");
                                createImageButton(imageUrl, description, categoryId);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void ActualizarPerfil(){
        session = new SessionManager(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                RestApiMethods.EndPointCreatePerson + "/" + session.getUserId().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObject = response.getJSONObject("data");
                            int _usuarioId = dataObject.getInt("usuarioId");
                            String _usuario = dataObject.getString("usuario");
                            String _nombres = dataObject.getString("nombres");
                            String _apellidos = dataObject.getString("apellidos");
                            String _telefono = dataObject.getString("telefono");
                            String _direccion = dataObject.getString("direccion");
                            String _correo = dataObject.getString("correo");
                            String _foto = dataObject.getString("foto");
                            boolean _verificado = dataObject.getBoolean("verificado");

                            Intent intent = new Intent(Home.this, registro_user.class);
                            intent.putExtra("usuarioId", _usuarioId);
                            intent.putExtra("usuario", _usuario);
                            intent.putExtra("nombres", _nombres);
                            intent.putExtra("apellidos", _apellidos);
                            intent.putExtra("telefono", _telefono);
                            intent.putExtra("direccion", _direccion);
                            intent.putExtra("correo", _correo);
                            intent.putExtra("foto", _foto);
                            intent.putExtra("verificado", _verificado);
                            startActivity(intent);
                        }catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Error")
                                .setMessage("Ocurrio un error: " + error.getMessage().toString())
                                .setPositiveButton("Aceptar", null)
                                .create()
                                .show();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
    private void createImageButton(String imageUrl, String description, int categoryId) {


        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(containerParams);
        container.setPadding(16, 16, 16, 16); // Ajustar el padding para separar los elementos

        ImageButton imageButton = new ImageButton(this);
        LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(300, 280);
        imageButton.setLayoutParams(imageButtonParams);
        container.addView(imageButton);

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textViewParams.gravity = Gravity.CENTER_VERTICAL; // Centrar el texto verticalmente
        textView.setLayoutParams(textViewParams);
        textView.setText(description);
        textView.setPadding(16, 0, 0, 0); // Ajustar el padding para separar el texto del botón
        container.addView(textView);

        layout.addView(container);

        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Error al cargar la imagen: " + e.getMessage());
                        return false; // Devuelve false para que Glide maneje el error
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // productos por id
                int selectedCategoryId = categoryId;

                Intent intent = new Intent(Home.this, PantallaProductos.class);
                intent.putExtra("categoryId", selectedCategoryId);
                startActivity(intent);
            }
        });
    }
}