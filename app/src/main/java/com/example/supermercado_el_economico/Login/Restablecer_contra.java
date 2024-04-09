package com.example.supermercado_el_economico.Login;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.supermercado_el_economico.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Restablecer_contra extends AppCompatActivity {
    // Variables para los componentes de Material Design
    private TextInputEditText txtCorreoRe;
    private MaterialButton btnenviartocket;
    ProgressDialog progressDialogEnvioToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_contra);
        txtCorreoRe =(TextInputEditText) findViewById(R.id.txtCorreoRecu);
        btnenviartocket =(MaterialButton) findViewById(R.id.btnenviarT);
        btnenviartocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtCorreoRe.getText().toString().trim();
                if (validar()) {
                    enviarSolicitudRestablecer(username);
                }
            }
        });
    }
    private void enviarSolicitudRestablecer(String username) {
       // showDialog("Enviando...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(Restablecer_contra.this);

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("usuario", username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //showDialog("Enviando clave temporal...");
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                        "https://delivery-service.azurewebsites.net/api/Autenticacion/EnviarClaveTemporal?usuario="+username, requestBody,

                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                   // progressDialogEnvioToken.dismiss();
                                    JSONObject dataObject = response.getJSONObject("data");
                                    Integer userId = dataObject.getInt("userId");
                                    String message = dataObject.getString("message");
                                    String claveTemp = dataObject.getString("claveTemporal");
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    if(claveTemp != ""){
                                        editor.putString("claveTemp", claveTemp);
                                        editor.putInt("userIdReestablecer", userId);
                                        editor.apply();

                                        showAlert("Clave Temporal", message);
                                        Intent intent = new Intent(getApplicationContext(), Pantalla_Nueva_Password.class);
                                        startActivity(intent);
                                    }else{
                                        showAlert("Error", "Intenta reenviar nuevamente la clave temporal.");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el fallo de la solicitud de inicio de sesión
                        AlertDialog.Builder builder = new AlertDialog.Builder(Restablecer_contra.this);
                        builder.setMessage("No se puedo enviar clave temporal.")
                                .setTitle("Clave temporal")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
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
        }).start();
    }
    private boolean validar() {
        String correo = txtCorreoRe.getText().toString().trim();
        if (correo.isEmpty()) {
            txtCorreoRe.setError("Por favor ingrese su correo electrónico");
            txtCorreoRe.requestFocus();
            return false;
        }
        return true;
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .create()
                .show();
    }
    private void showDialog(String message) {
        progressDialogEnvioToken = new ProgressDialog(Restablecer_contra.this);
        progressDialogEnvioToken.setMessage(message);
        progressDialogEnvioToken.setCancelable(false);
        progressDialogEnvioToken.show();
    }
}