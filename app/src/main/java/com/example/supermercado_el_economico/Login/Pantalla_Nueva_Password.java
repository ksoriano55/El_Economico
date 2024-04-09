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
public class Pantalla_Nueva_Password extends AppCompatActivity {
    // Variables para los componentes de Material Design
    private TextInputEditText txtpassNew, txtpassVeriNew, txtPassTemporal;
    private MaterialButton btncanbiarcont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nueva_password);
        txtpassNew =(TextInputEditText) findViewById(R.id.txtpassNew);
        txtpassVeriNew =(TextInputEditText) findViewById(R.id.txtpassVeriNew);
        txtPassTemporal =(TextInputEditText) findViewById(R.id.txtPassTemporal);
        btncanbiarcont =(MaterialButton) findViewById(R.id.btnCambiarContra);

        btncanbiarcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                final String claveTemp = sharedPreferences.getString("claveTemp", "");
                final Integer userId = sharedPreferences.getInt("userIdReestablecer", -1);

                String passNew = txtpassNew.getText().toString();
                String passNewConfi = txtpassVeriNew.getText().toString();
                String claveTempoDig = txtPassTemporal.getText().toString();

                if(passNew.isEmpty() || passNewConfi.isEmpty()
                        || claveTempoDig.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Pantalla_Nueva_Password.this);
                    builder.setMessage("Campos vacios.")
                            .setTitle("Campos Requeridos")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (!passNew.equals(passNewConfi)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Pantalla_Nueva_Password.this);
                    builder.setMessage("Contraseña no coincide.")
                            .setTitle("Datos inválidas")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(!claveTempoDig.equals(claveTemp)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Pantalla_Nueva_Password.this);
                    builder.setMessage("Clave temporal no coincide, favor revisar su correo")
                            .setTitle("Clave temporal incorrecta")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    reestablecerPassword(userId, passNewConfi);
                }
            }
        });
    }
    private void reestablecerPassword(Integer userId, String password){
        ProgressDialog progressDialog = new ProgressDialog(Pantalla_Nueva_Password.this);
        progressDialog.setMessage("Reestableciendo...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(Pantalla_Nueva_Password.this);
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("userId", userId);
                    requestBody.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.PUT,
                        "https://delivery-service.azurewebsites.net/api/Autenticacion/ReestablecerPassword?password="+password+"&userId="+userId, requestBody,
                        //"https://delivery-service.azurewebsites.net/api/Autenticacion/EnviarClaveTemporal?usuario="+username, requestBody,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressDialog.dismiss(); // Ocultar el diálogo de progreso
                                try {
                                    boolean data = response.getBoolean("data");
                                    if (data == true){
                                        showAlert("Éxito", "Se ha reestablecido la contraseña correctamente: ");
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        showAlert("Advertencia", "No hubo actualizacio de contraseña.");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss(); // Ocultar el diálogo de progreso
                        // Manejar el fallo de la solicitud de inicio de sesión
                        AlertDialog.Builder builder = new AlertDialog.Builder(Pantalla_Nueva_Password.this);
                        builder.setMessage("No se puedo enviar clave temporal.")
                                .setTitle("Clave temporal")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Cerrar el diálogo o realizar alguna acción adicional si es necesario
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
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .create()
                .show();
    }
}