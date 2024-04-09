package com.example.supermercado_el_economico.Login;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.supermercado_el_economico.ApiRest.AuthenticationApiMethods;
import com.example.supermercado_el_economico.ApiRest.RestApiMethods;
import com.example.supermercado_el_economico.Delivery.HomeRepartidor;
import com.example.supermercado_el_economico.Home;
import com.example.supermercado_el_economico.R;
import com.example.supermercado_el_economico.registro_user;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // Variables para los componentes de Material Design
    private TextInputEditText txtcorreoEn;
    private TextInputEditText txtpasswordEntrada;
    private MaterialButton btnentrar, btnCrear, btnestablecer;
    private RequestQueue requestQueue;
    private SessionManager session;
    private boolean doubleBackToExitPressedOnce = false; //para salir dela app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtcorreoEn = (TextInputEditText) findViewById(R.id.txtcorreoEn);
        txtpasswordEntrada = (TextInputEditText) findViewById(R.id.txtpasswordEntrada);
        btnentrar = (MaterialButton) findViewById(R.id.btnlogin);
        btnCrear = (MaterialButton) findViewById(R.id.btnCrear);
        btnestablecer = (MaterialButton) findViewById(R.id.btnrestablecer);
        session = new SessionManager(getApplicationContext());
        btnentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtcorreoEn.getText().toString().trim();
                String password = txtpasswordEntrada.getText().toString().trim();
                // Validar que los campos no estén vacíos
                if (TextUtils.isEmpty(username)) {
                    txtcorreoEn.setError("Campo obligatorio");
                    return;
                } else {
                    txtcorreoEn.setError(null);
                }
                if (TextUtils.isEmpty(password)) {
                    txtpasswordEntrada.setError("Campo obligatorio");
                    return;
                } else {
                    txtpasswordEntrada.setError(null);
                }
                // Aquí puedes llamar a tu método de inicio de sesión
                loginUsuario(username, password);
            }
        });
        btnestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Restablecer_contra.class);
                startActivity(intent);
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registro_user.class);
                startActivity(intent);
            }
        });

    }

    private void loginUsuario(final String username, final String password) {
        // Crear un diálogo de progreso mientras se realiza la solicitud de inicio de sesión
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
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
                try{
                    progressDialog.dismiss(); // Ocultar el diálogo de progreso

                    JSONObject dataObject = response.getJSONObject("data");
                    int userId = dataObject.getInt("userId");
                    int status = dataObject.getInt("status");
                    String codVerificacion = dataObject.getString("codVerificacion");
                    String username = dataObject.getString("username");
                    String rol = dataObject.getString("rol");

                    if (status == 200) { // Inicio de sesión exitoso
                        String welcomeMessage = "¡Bienvenido, " + username + "!";
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(welcomeMessage)
                                .setTitle("Inicio de sesión exitoso")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Limpiar los campos de usuario y contraseña
                                        limpiarCampos();
                                        // Cerrar el diálogo o realizar alguna acción adicional si es necesario
                                        dialog.dismiss();
                                        // Iniciar la actividad correspondiente después de mostrar el mensaje de bienvenida
                                        if (codVerificacion.equals("")) {
                                            //guardar datos de login
                                            session.login(String.valueOf(userId), username);
                                            //Redireccionar al home de repartidor o Cliente
                                            redirectNext(rol);
                                        } else {
                                            // Guardar el código de verificación y el ID del usuario en SharedPreferences
                                            saveSharePreferents(codVerificacion,userId,username,password);
                                            // Ir a la pantalla de verificación
                                            Intent intent = new Intent(getApplicationContext(), Pantalla_verificacion.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        // Otro estado no manejado, muestra un mensaje genérico
                        alertErrorInicioSesion();
                    }
                }catch (Exception ex){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss(); // Ocultar el diálogo de progreso
                // Manejar el fallo de la solicitud de inicio de sesión
                alertErrorInicioSesion();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void limpiarCampos(){
        txtcorreoEn.setText("");
        txtpasswordEntrada.setText("");
    }

    private void redirectNext(String rol){
        if (rol.equals("Cliente")) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        } else if (rol.equals("Repartidor")) {
            Intent intent = new Intent(getApplicationContext(), HomeRepartidor.class);
            startActivity(intent);
        }
    }

    private void saveSharePreferents(String codVerificacion, Integer userId, String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("codVerificacion", codVerificacion);
        editor.putInt("userId", userId);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private void alertErrorInicioSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona nuevamente para salir", Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
