package com.example.supermercado_el_economico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supermercado_el_economico.Login.Pantalla_Inicio;
import com.example.supermercado_el_economico.Login.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import com.squareup.picasso.Picasso;

public class Userlogin extends AppCompatActivity {

    private TextView textViewUserId;
    private TextView textViewUsername;
    private TextView textViewNombres;
    private TextView textViewApellidos;
    private TextView textViewTelefono;
    private TextView textViewDireccion;
    private TextView textViewCorreo;
    private ImageView imageViewFoto;

    private SessionManager session;

    private  Button btnRegresar,btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        // Inicializar vistas
        textViewUserId = findViewById(R.id.textViewUserId);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewNombres = findViewById(R.id.textViewNombres);
        textViewApellidos = findViewById(R.id.textViewApellidos);
        textViewTelefono = findViewById(R.id.textViewTelefono);
        textViewDireccion = findViewById(R.id.textViewDireccion);
        textViewCorreo = findViewById(R.id.textViewCorreo);
        imageViewFoto = findViewById(R.id.imageViewFoto);
        Button btnVerDireccion = findViewById(R.id.btnVerDireccion);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnVerDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Userlogin.this, Direcciones.class);
                startActivity(intent);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                Toast.makeText(Userlogin.this, "¡Te esperamos pronto!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Userlogin.this, Pantalla_Inicio.class);
                startActivity(intent);
                finish();
            }
        });



        session = new SessionManager(getApplicationContext());

        // Obtener y mostrar la información del usuario
        String userId = session.getUserId();
        String username = session.getUsername();

        textViewUserId.setText("User ID: " + userId);
        textViewUsername.setText("Username: " + username);

        // Hacer la solicitud HTTP para obtener los datos del usuario
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://delivery-service.azurewebsites.net/api/Usuarios/" + userId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Userlogin.this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        final String nombres = jsonObject.getJSONObject("data").getString("nombres");
                        final String apellidos = jsonObject.getJSONObject("data").getString("apellidos");
                        final String telefono = jsonObject.getJSONObject("data").getString("telefono");
                        final String direccion = jsonObject.getJSONObject("data").getString("direccion");
                        final String correo = jsonObject.getJSONObject("data").getString("correo");
                        final String fotoUrl = jsonObject.getJSONObject("data").getString("foto");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewNombres.setText("Nombres: " + nombres);
                                textViewApellidos.setText("Apellidos: " + apellidos);
                                textViewTelefono.setText("Teléfono: " + telefono);
                                textViewDireccion.setText("Dirección: " + direccion);
                                textViewCorreo.setText("Correo: " + correo);

                                // Carga la imagen utilizando Picasso
                                Picasso.get().load(fotoUrl).into(imageViewFoto);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
