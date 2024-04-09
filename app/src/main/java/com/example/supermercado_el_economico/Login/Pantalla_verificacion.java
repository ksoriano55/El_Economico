package com.example.supermercado_el_economico.Login;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.supermercado_el_economico.Home;
import com.example.supermercado_el_economico.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.supermercado_el_economico.ApiRest.AuthenticationApiMethods;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class Pantalla_verificacion extends AppCompatActivity {
    // Variables para los componentes de Material Design
    private TextInputEditText txtcodigo;
    private RequestQueue requestQueue;
    private MaterialButton btnreenviar, btnverificarcodigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_verificacion);
        txtcodigo = (TextInputEditText) findViewById(R.id.txtcodigo);
        btnreenviar = (MaterialButton) findViewById(R.id.btnlogin);
        btnverificarcodigo = (MaterialButton) findViewById(R.id.btnverificarcodigo);
        btnverificarcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // verificarCodigo(codigo);
                verificarCodigo(); // Llama al método para verificar el código
            }
        });
        btnreenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                final String username = sharedPreferences.getString("username", "");
                final String pass = sharedPreferences.getString("password", "");
                reenviarCodigo(username, pass);
            }
        });
    }
    private void reenviarCodigo(String username, String password) {
        ProgressDialog progressDialog = new ProgressDialog(Pantalla_verificacion.this);
        progressDialog.setMessage("Reenviando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userName", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AuthenticationApiMethods.EndPointLogin,  requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss(); // Ocultar el diálogo de progreso
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    int userId = dataObject.getInt("userId");
                    String message = dataObject.getString("message");
                    int status = dataObject.getInt("status");
                    String codVerificacion = dataObject.getString("codVerificacion");
                    String username = dataObject.getString("username");
                    String rol = dataObject.getString("rol");
                    if (status == 200) { // Inicio de sesión exitoso
                        if (!codVerificacion.equals("")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("codVerificacion", codVerificacion);
                            editor.putInt("userId", userId);
                            editor.apply();

                            showAlert("Código Verifiación", "Se ha reeviado código verificación.");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(getApplicationContext(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss(); // Ocultar el diálogo de progreso
                // Manejar el fallo de la solicitud de inicio de sesión
                AlertDialog.Builder builder = new AlertDialog.Builder(Pantalla_verificacion.this);
                builder.setMessage("Usuario o contraseña incorrectos.")
                        .setTitle("Error de inicio de sesión")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cerrar el diálogo o realizar alguna acción adicional si es necesario
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
    private void verificarCodigo() {
        ProgressDialog progressDialog = new ProgressDialog(Pantalla_verificacion.this);
        progressDialog.setMessage("Verificando código...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Recuperar el código de verificación y el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String codigoguardado = sharedPreferences.getString("codVerificacion", "");
        final int userId = sharedPreferences.getInt("userId", -1);

        // Obtener el código ingresado por el usuario desde el EditText
        TextInputEditText txtcodigo = findViewById(R.id.txtcodigo);
        String codigoIngresado = txtcodigo.getText().toString();

        // Verificar si el código ingresado coincide con el código guardado en SharedPreferences
        if (!codigoIngresado.equals(codigoguardado)) {
            progressDialog.dismiss(); // Ocultar el diálogo de progreso
            showAlert("Error", "El código ingresado no es correcto.");
            txtcodigo.setText(""); // Limpiar el campo de texto
            //return; // Salir del método sin continuar con la verificación en el servidor
        }else{
            // El código ingresado es correcto, proceder con la verificación en el servidor
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestQueue requestQueue = Volley.newRequestQueue(Pantalla_verificacion.this);

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("codVerificacion", codigoguardado);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                            "https://delivery-service.azurewebsites.net/api/Autenticacion/VerificarUsuario?usuarioId=" + userId, requestBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.dismiss(); // Ocultar el diálogo de progreso
                                    try {
                                        boolean data = response.getBoolean("data");
                                        if (data == true){
                                            showAlert("Éxito", "Código verificado correctamente: ");
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            startActivity(intent);
                                        }else{
                                            showAlert("Error", "El código de verificación no es válido.");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        showAlert("Error", "La respuesta del servidor no contiene información de verificación: " + e.toString());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                showAlert("Error", "Error en la solicitud: " + statusCode); // Muestra el código de estado del error
                            } else {
                                showAlert("Error", "Error en la solicitud");
                            }
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
    }
    // Método para mostrar un diálogo de alerta con un título y un mensaje
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .create()
                .show();
    }
}

