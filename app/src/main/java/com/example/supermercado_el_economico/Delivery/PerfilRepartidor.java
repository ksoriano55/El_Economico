package com.example.supermercado_el_economico.Delivery;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.supermercado_el_economico.R;

public class PerfilRepartidor extends AppCompatActivity {

    private ImageView imageView;
    private TextView nombreTextView;
    private TextView telefonoTextView;
    private TextView direccionTextView;
    private TextView correoTextView;
    private TextView contraseñaTextView;
    private Button editarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_repartidor);

        imageView = findViewById(R.id.imageView);
        nombreTextView = findViewById(R.id.nombreTextView);
        telefonoTextView = findViewById(R.id.telefonoTextView);
        direccionTextView = findViewById(R.id.direccionTextView);
        correoTextView = findViewById(R.id.correoTextView);
        contraseñaTextView = findViewById(R.id.contraseñaTextView);
        editarButton = findViewById(R.id.editarButton);

        imageView.setImageResource(R.drawable.logocli);
        nombreTextView.setText("Nombre: Juan Pérez");
        telefonoTextView.setText("Teléfono: 123456789");
        direccionTextView.setText("Dirección: Calle Principal #123");
        correoTextView.setText("Correo Electrónico: ejemplo@correo.com");
        contraseñaTextView.setText("Contraseña: ********");

        editarButton.setOnClickListener(view -> {
            // Aquí puedes agregar la lógica para abrir la pantalla de edición de perfil
        });
    }
}
