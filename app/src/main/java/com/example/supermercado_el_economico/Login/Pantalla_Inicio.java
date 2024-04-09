package com.example.supermercado_el_economico.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.supermercado_el_economico.R;

public class Pantalla_Inicio extends AppCompatActivity {

    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);

        btnlogin = (Button) findViewById(R.id.btnIniciar);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pantalla_Inicio.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}