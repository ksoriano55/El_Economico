package com.example.supermercado_el_economico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.supermercado_el_economico.Login.SessionManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class Direcciones extends AppCompatActivity {

    private TextView textViewUserId;
    private TextView textViewUsername;
    private TextView textViewAddress; // Nuevo TextView para la dirección
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccionusuario);

        // Encuentra los TextView en el diseño
        //textViewUserId = findViewById(R.id.textViewUserId);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewAddress = findViewById(R.id.textViewAddress); // Encuentra el nuevo TextView
        Button btnAgregarDirecccion = findViewById(R.id.btnAgregarDirecccion);

        btnAgregarDirecccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Direcciones.this, Userlogin.class);
                startActivity(intent);
            }
        });

        // Inicializa el SessionManager
        session = new SessionManager(getApplicationContext());

        // Obtén y muestra la información del usuario
        String userId = session.getUserId();
        String username = session.getUsername();

        //textViewUserId.setText("User ID: " + userId);
        textViewUsername.setText("Nombre de usuario: " + username);

        // Realiza la solicitud HTTP para obtener la dirección
        OkHttpClient client = new OkHttpClient();
        String url = "https://delivery-service.azurewebsites.net/api/Direccion?usuarioId=" + userId;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    // Formatea la respuesta JSON antes de mostrarla
                    final String formattedResponse = formatJSON(responseData);
                    // Actualiza la interfaz de usuario en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewAddress.setText(formattedResponse);
                        }
                    });
                }
            }
        });
    }

    // Método para formatear la respuesta JSON
    private String formatJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            StringBuilder formattedData = new StringBuilder();
            //formattedData.append("data: [\n");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject addressObject = dataArray.getJSONObject(i);
                formattedData.append("    \n");
                formattedData.append("        direccionId: ").append(addressObject.getInt("direccionId")).append(",\n");
                formattedData.append("        usuarioId: ").append(addressObject.getInt("usuarioId")).append(",\n");
                formattedData.append("        nombre: ").append(addressObject.getString("nombre")).append(",\n");
                formattedData.append("        referencia: ").append(addressObject.getString("referencia")).append(",\n");
                //formattedData.append("        longitud: ").append(addressObject.getString("longitud")).append(",\n");
                //formattedData.append("        latitud: ").append(addressObject.getString("latitud")).append(",\n");
                //formattedData.append("        usuarios: ").append(addressObject.getString("usuarios")).append("\n");
                formattedData.append("    \n");
            }
            //formattedData.append("]");
            return formattedData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
    }
}
