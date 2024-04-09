package com.example.supermercado_el_economico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.util.Base64;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.supermercado_el_economico.ApiRest.RestApiMethods;
import com.example.supermercado_el_economico.Config.User;
import com.example.supermercado_el_economico.Login.MainActivity;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class registro_user extends AppCompatActivity {
    static final int  peticion_foto = 101;
    static final int peticion_camara = 100;
    private RequestQueue requestQueue;
    String imagenBase64;
    Integer _usuarioId = 0;
    private TextInputEditText nombre, apellido, telefono, direccion, correo, usuarios, pass, confiPass;
    private TextInputLayout passp, confiPassp;
    boolean verificado = false;
    ImageView imageView;
    TextView lbTitulo;
    private MaterialButton btnSiguiente, btnFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

        btnSiguiente = findViewById(R.id.btnSiguiente);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnFoto= findViewById(R.id.btnFoto);

        nombre = findViewById(R.id.txtnombre);
        apellido = findViewById(R.id.txtApellido);
        telefono = findViewById(R.id.txtTelefono);
        direccion = findViewById(R.id.txtDireccion);
        correo = findViewById(R.id.txtCorreo);
        usuarios = findViewById(R.id.txtUsuario);
        pass = findViewById(R.id.txtPass);
        passp = findViewById(R.id.txtPassp);
        confiPass = findViewById(R.id.txtConfirmarPass);
        confiPassp = findViewById(R.id.txtConfirmarPassw);
        lbTitulo = findViewById(R.id.textView);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            pass.setVisibility(View.GONE);
            confiPass.setVisibility(View.GONE);
            passp.setVisibility(View.GONE);
            confiPassp.setVisibility(View.GONE);
            btnSiguiente.setText("Actualizar");
            lbTitulo.setText("Actualizar Perfil");
            usuarios.setEnabled(false);

            _usuarioId = intent.getIntExtra("usuarioId", 0);
            String _usuario = intent.getStringExtra("usuario");
            String _nombres = intent.getStringExtra("nombres");
            String _apellidos = intent.getStringExtra("apellidos");
            String _telefono = intent.getStringExtra("telefono");
            String _direccion = intent.getStringExtra("direccion");
            String _correo = intent.getStringExtra("correo");
            String _foto = intent.getStringExtra("foto");
            boolean _verificado = intent.getBooleanExtra("verificado", false);

            nombre.setText(_nombres);
            apellido.setText(_apellidos);
            telefono.setText(_telefono);
            direccion.setText(_direccion);
            correo.setText(_correo);
            usuarios .setText(_usuario);
            verificado = _verificado;

            Picasso.get().load(_foto).into(imageView);
        }
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(registro_user.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    PermisosCamera();
                } else {
                    PermisosCamera();
                }
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarDatos();
            }
        });
    }


    //VALIDA QUE QUEDEN CAMPOS VACIOS
    private void validarDatos() {
        btnSiguiente.setEnabled(false);
        if(!confiPass.getText().toString().equals(pass.getText().toString()) && _usuarioId == 0){
            showAlert("Advertencia", "La contraseña no coincide");
        } else if (nombre.getText().toString().equals("")) {
            showAlert("Datos Inválido", "Debe de escribir su nombre");
        } else if (apellido.getText().toString().equals("")) {
            showAlert("Datos Inválido", "Debe de escribir sus apellidos");
        } else if (telefono.getText().toString().equals("")) {
            showAlert("Datos Inválido", "Debe de escribir su telefono");
        } else if (direccion.getText().toString().equals("")) {
            showAlert("Datos Inválido", "Debe de escribir su direccion");
        } else if (correo.getText().toString().equals("")) {
            showAlert("Datos Inválido", "Debe de escribir su correo");
        } else if (usuarios.getText().toString().equals("") && _usuarioId == 0) {
            showAlert("Datos Inválido", "Debe de escribir un usuario");
        } else if (pass.getText().toString().equals("") && _usuarioId == 0) {
            showAlert("Datos Inválido", "Debe de escribir su contraseña");
        } else {
            SendData();
        }
        btnSiguiente.setEnabled(true);
    }


    private void SendData() {
        ProgressDialog progressDialog = new ProgressDialog(registro_user.this);
        progressDialog.setMessage(_usuarioId == 0 ? "Creando..." : "Actualizando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        User usuario = new User();
        usuario.setUsuarioId(_usuarioId);
        usuario.setNombre(nombre.getText().toString());
        usuario.setApellido(apellido.getText().toString());
        usuario.setTelefono(telefono.getText().toString());
        usuario.setDireccion(direccion.getText().toString());
        usuario.setCorreo(correo.getText().toString());
        usuario.setUsuario(usuarios.getText().toString());
        usuario.setPass(pass.getText().toString());
        usuario.setFoto(imagenBase64);
        usuario.setVerificado(verificado);
        usuario.setActivo(true);

        requestQueue = Volley.newRequestQueue(this);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("usuarioId", _usuarioId);
            requestBody.put("usuario", usuario.getUsuario());
            requestBody.put("password", usuario.getPass());
            requestBody.put("nombres", usuario.getNombre());
            requestBody.put("apellidos", usuario.getApellido());
            requestBody.put("telefono", usuario.getTelefono());
            requestBody.put("direccion", usuario.getDireccion());
            requestBody.put("correo", usuario.getCorreo());
            requestBody.put("foto", "");
            requestBody.put("imgBase64", usuario.getFoto());
            requestBody.put("esRepartidor", false);
            requestBody.put("activo", usuario.isActivo());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(_usuarioId == 0 ? Request.Method.POST : Request.Method.PUT,
                RestApiMethods.EndPointCreatePerson, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();

                    if(_usuarioId == 0){
                        JSONObject dataObject = response.getJSONObject("data");
                        int userId =  dataObject.getInt("usuarioId");
                        if(userId > 0){
                            AlertDialog.Builder alert = new AlertDialog.Builder(registro_user.this);
                            alert.setMessage("Su cuenta ha sido creada exitosamente.")
                                    .setTitle("¡Exito!")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        };
                                    })
                                    .create()
                                    .show();
                        }else{
                            showAlert("Advertencia", "No se creo el usuario");
                        }
                        limpiarCampos();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(registro_user.this);
                        alert.setMessage("Su cuenta ha sido actualizada exitosamente.")
                                .setTitle("¡Exito!")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        startActivity(intent);
                                    };
                                })
                                .create()
                                .show();
                    }


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showAlert("Advertencia", error.getMessage());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .create()
                .show();
    }

    private void limpiarCampos()
    {
        nombre.setText("");
        apellido.setText("");
        telefono.setText("");
        direccion.setText("");
        correo.setText("");
        usuarios.setText("");
        pass.setText("");
        confiPass.setText("");
        imageView.setImageDrawable(null);
    }


    private void PermisosCamera() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    peticion_camara);
        }
        else
        {
            CapturarFoto();
        }
    }


    private void CapturarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, peticion_foto);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_camara)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                CapturarFoto();
            }
            else
            {
                //Toast.makeText(getApplicationContext(), "!Alto ahi!, Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_foto && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imagen);

            /*---------Convertir imagen a base64-------*/
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imagenBase64 = "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }
}