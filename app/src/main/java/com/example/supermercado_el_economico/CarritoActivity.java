package com.example.supermercado_el_economico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supermercado_el_economico.Adapters.ProductosSeleccionadosAdapter;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.example.supermercado_el_economico.models.Producto;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private TextView textViewUserId;
    private TextView textViewUsername;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        textViewUserId = findViewById(R.id.textViewUserId);
        textViewUsername = findViewById(R.id.textViewUsername);
        session = new SessionManager(getApplicationContext());

        // Obtener y mostrar la información del usuario
        String userId = session.getUserId();
        String username = session.getUsername();

        textViewUserId.setText(userId);
        textViewUsername.setText(username);

        Button btnconfirmar = findViewById(R.id.btnconfirmar);
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


        List<Producto> productosSeleccionados = SharedPreferencesHelper.loadProductos(this);

        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        } else {
            RecyclerView recyclerView = findViewById(R.id.recyclerViewProductosSeleccionados);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ProductosSeleccionadosAdapter adapter = new ProductosSeleccionadosAdapter(productosSeleccionados);
            recyclerView.setAdapter(adapter);
        }
    }
}
