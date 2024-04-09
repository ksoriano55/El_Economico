package com.example.supermercado_el_economico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supermercado_el_economico.Adapters.ProductosSeleccionadosAdapter;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.example.supermercado_el_economico.Shop.CustomerAddress;
import com.example.supermercado_el_economico.models.Producto;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private SessionManager session;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        Button btnconfirmar = findViewById(R.id.btnconfirmar);
        searchView = findViewById(R.id.searchViewcarrito);

        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la lista de productos seleccionados desde SharedPreferences
                List<Producto> productosSeleccionados = SharedPreferencesHelper.loadProductos(CarritoActivity.this);
                List<Producto> DetallePedidoAdapter = SharedPreferencesHelper.loadProductos(CarritoActivity.this);
                // Verificar si la lista no está vacía
                if (!productosSeleccionados.isEmpty()) {
                    // Iniciar DetallePedidoActivity y pasar la lista de productos seleccionados
                    Intent intent = new Intent(CarritoActivity.this, DetallePedidoActivity.class);
                    intent.putParcelableArrayListExtra("productosSeleccionados", new ArrayList<>(productosSeleccionados));
                    startActivity(intent);
                } else {
                    Toast.makeText(CarritoActivity.this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_profile_photo) {
                    Intent intent = new Intent(CarritoActivity.this, Userlogin.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        // Cargar la lista de productos seleccionados desde SharedPreferences
        List<Producto> productosSeleccionados = SharedPreferencesHelper.loadProductos(this);

        // Verificar si la lista está vacía
        if (productosSeleccionados.isEmpty()) {
            // La lista está vacía, mostrar un mensaje
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        } else {
            // La lista no está vacía, configurar la vista para mostrar los productos seleccionados
            RecyclerView recyclerView = findViewById(R.id.recyclerViewProductosSeleccionados);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ProductosSeleccionadosAdapter adapter = new ProductosSeleccionadosAdapter(productosSeleccionados);
            recyclerView.setAdapter(adapter);
        }
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
