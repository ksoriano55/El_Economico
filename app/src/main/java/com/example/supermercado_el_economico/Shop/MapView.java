package com.example.supermercado_el_economico.Shop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.supermercado_el_economico.ApiRest.RestApiMethods;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.example.supermercado_el_economico.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapView extends AppCompatActivity {

    EditText NombreInput, AddressInput;
    Button saveButton;
    private SessionManager session;
    String userId;
    String latitud, longitud;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        NombreInput = findViewById(R.id.nombreInput);
        AddressInput = findViewById(R.id.addressInput);
        saveButton = findViewById(R.id.saveButton);

        session = new SessionManager(this);
        userId = session.getUserId();

        latitud = "15.4765652";
        longitud = "-87.9993775";

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
                //sendData();

            }
        });
    }

    private void ValidarDatos(){
        if(!NombreInput.getText().toString().equals("")){
            showAlert("Advertencia", "El nombre esta vacio");
        } else if(AddressInput.getText().toString().equals("")) {
            showAlert("Advertencia", "Debe de escribir su nombre");
        }
        sendData();
        limpiarCampos();
    }

    private void sendData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Address address = new Address();
        address.setNombre(NombreInput.getText().toString());
        address.setReferencia(AddressInput.getText().toString());
        address.setLatitud(longitud);
        address.setLongitud(latitud);

        requestQueue = Volley.newRequestQueue(this);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("usuarioId", Integer.parseInt(userId));
            requestBody.put("nombre", address.getNombre());
            requestBody.put("referencia", address.getReferencia());
            requestBody.put("longitud", address.getLongitud());
            requestBody.put("latitud", address.getLatitud());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                RestApiMethods.EndPointCraeteDirecciones, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject dataObject = response.getJSONObject("data");
                            int userId = dataObject.getInt("usuarioId");
                            if (userId > 0) {
                                showAlert("Exito", "Se ha creado con exito la direccion");
                            } else {
                                showAlert("Advertencia", "No se creo la direccion");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlert("Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        showAlert("Error", error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .create()
                .show();
    }
    private void limpiarCampos() {
        NombreInput.setText("");
        AddressInput.setText("");

    }
}
