package com.example.supermercado_el_economico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.supermercado_el_economico.Shop.CustomerAddress;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PedidosActivity extends AppCompatActivity {
    Button btncarrito, btnhistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        btncarrito = (Button) findViewById(R.id.btncarrito);
        btnhistorial = (Button) findViewById(R.id.btnhistorial);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        btncarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CarritoActivity.class);
                startActivity(intent);
            }
        });
        btnhistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialPedidoActivity.class);
                startActivity(intent);
            }
        });

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if(itemId == R.id.page_1){
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    }
                    else if(itemId == R.id.page_2){
                        Intent intent = new Intent(getApplicationContext(), PedidosActivity.class);
                        startActivity(intent);
                    }
                    else if(itemId == R.id.page_3){
                        Intent intent = new Intent(getApplicationContext(), CustomerAddress.class);
                        startActivity(intent);
                    }
                    return true;
                }
            };
}