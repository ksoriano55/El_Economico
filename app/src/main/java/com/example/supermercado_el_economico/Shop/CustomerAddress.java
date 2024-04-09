package com.example.supermercado_el_economico.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.supermercado_el_economico.Adapters.DireccionAdapter;
import com.example.supermercado_el_economico.ApiRest.RestApiMethods;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.example.supermercado_el_economico.R;
import com.example.supermercado_el_economico.models.DireccionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerAddress extends AppCompatActivity {
    private SessionManager session;
    String userId;
    RecyclerView recyclerView;
    Button btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direcciones);
        recyclerView = findViewById(R.id.recyclerDirecciones);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        // Inicializa el SessionManager
        session = new SessionManager(getApplicationContext());
        // Obtén y muestra la información del usuario
        userId = session.getUserId();
        String username = session.getUsername();
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerAddress.this,
                        MapView.class);
                startActivity(intent);

            }
        });

        // Realiza la solicitud HTTP para obtener la dirección
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RestApiMethods.EndPointGetDirecciones + userId)
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

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        StringBuilder formattedData = new StringBuilder();
                        List<DireccionModel> direcciones = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject addressObject = dataArray.getJSONObject(i);
                            Integer direccionId = addressObject.getInt("direccionId");
                            String nombre = addressObject.getString("nombre");
                            String referencia = addressObject.getString("referencia");

                            DireccionModel direccion = new DireccionModel(direccionId,nombre, referencia);
                            direcciones.add(direccion);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Configurar el adaptador del RecyclerView aquí
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                DireccionAdapter adapter = new DireccionAdapter(direcciones);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //textViewAddress.setText(formattedResponse);
                        }
                    });
                }
            }
        });
    }
}