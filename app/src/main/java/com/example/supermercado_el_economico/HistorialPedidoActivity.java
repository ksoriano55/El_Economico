package com.example.supermercado_el_economico;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.supermercado_el_economico.Adapters.PedidoAdapter;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.example.supermercado_el_economico.Shop.CustomerAddress;
import com.example.supermercado_el_economico.models.Pedido;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistorialPedidoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewPedidos;
    private PedidoAdapter pedidoAdapter;
    private List<Pedido> listaPedidos;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedido);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        mRecyclerViewPedidos = findViewById(R.id.recycler_pedidos);
        mRecyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        listaPedidos = new ArrayList<>();
        pedidoAdapter = new PedidoAdapter(listaPedidos);
        mRecyclerViewPedidos.setAdapter(pedidoAdapter);

        tabLayout = findViewById(R.id.tabLayout);

        cargarPedidos();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    pedidoAdapter.filtrarPorEstado("PENDIENTE_Y_EN_PROCESO");
                } else if (tab.getPosition() == 1) {
                    pedidoAdapter.filtrarPorEstado("CERRADO");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.page_1) {
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    } else if (itemId == R.id.page_2) {
                        Intent intent = new Intent(getApplicationContext(), PedidosActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.page_3) {
                        Intent intent = new Intent(getApplicationContext(), CustomerAddress.class);
                        startActivity(intent);
                    }
                    return true;
                }
            };

    private void cargarPedidos() {
        SessionManager session = new SessionManager(getApplicationContext());
        String userId = session.getUserId();
        String url = "https://delivery-service.azurewebsites.net/api/Pedidos/customer?cliente=" + userId;

        // Crear una nueva solicitud JSON
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONArray dataObject = response.getJSONArray("data");


                                for (int i = 0; i < dataObject.length(); i++) {
                                    JSONObject pedidoObject = dataObject.getJSONObject(i);

                                    // Obtener los datos específicos de cada pedido
                                    int pedidoId = pedidoObject.getInt("pedidoId");
                                    String numPedido = pedidoObject.getString("numPedido");
                                    String fecha = pedidoObject.getString("fecha");
                                    String estado = pedidoObject.getString("estado");
                                    double total = pedidoObject.getDouble("total");
                                    float calificacion = (float) pedidoObject.getDouble("calificacion");


                                    Pedido pedido = new Pedido(pedidoId, numPedido, fecha, estado, total, calificacion);

                                    listaPedidos.add(pedido);
                                }
                                pedidoAdapter.filtrarPorEstado("PENDIENTE_Y_EN_PROCESO");
                                pedidoAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al cargar los pedidos", Toast.LENGTH_SHORT).show();
                        Log.e("Error de solicitud", error.toString());

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
