package com.example.supermercado_el_economico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MasVendidos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_vendidos);

        Button btnatras = findViewById(R.id.btnatras);


        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasVendidos.this, Home.class);
                startActivity(intent);
            }
        });


    }
}