package com.example.supermercado_el_economico;

import static com.example.supermercado_el_economico.SharedPreferencesHelper.clearSharedPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.supermercado_el_economico.Adapters.DetallePedidoAdapter;
import com.example.supermercado_el_economico.Shop.CustomerAddress;
import com.example.supermercado_el_economico.models.Producto;
import com.example.supermercado_el_economico.Login.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetallePedidoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Producto> productosSeleccionados;
    private Spinner combodireccion;

    private Button btn_cancelar,btn_confirmar;

    private List<String> direccionList;
    private List<JSONObject> direccionesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        combodireccion = findViewById(R.id.combodireccion);
        btn_cancelar = findViewById(R.id.btn_cancelar);
        btn_confirmar = findViewById(R.id.btn_confirmar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Recuperar la lista de productos seleccionados del intent
        productosSeleccionados = getIntent().getParcelableArrayListExtra("productosSeleccionados");

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recycler_pedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DetallePedidoAdapter adapter = new DetallePedidoAdapter(productosSeleccionados);
        recyclerView.setAdapter(adapter);

        double subtotalPagar = calcularSubTotalPagar((ArrayList<Producto>) productosSeleccionados);
        TextView textViewSubTotalPagar = findViewById(R.id.total_subtotal);
        textViewSubTotalPagar.setText(String.valueOf(subtotalPagar));

        // Calcular ISV y mostrarlo
        double isv = calcularISV((ArrayList<Producto>) productosSeleccionados);
        TextView textViewIsv = findViewById(R.id.total_isv);
        textViewIsv.setText(String.valueOf(isv));

        // Calcular total a pagar (subtotal + ISV) y mostrarlo
        double totalPagar = subtotalPagar + isv;
        TextView textViewTotalPagar = findViewById(R.id.total);
        textViewTotalPagar.setText(String.valueOf(totalPagar));


        obtenerYMostrarDirecciones();

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPreferences(DetallePedidoActivity.this);

                Intent intent = new Intent(DetallePedidoActivity.this, Home.class);
                startActivity(intent);

                finish();
            }
        });
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String direccionSeleccionada = combodireccion.getSelectedItem().toString();
                int direccionId = obtenerDireccionId(direccionSeleccionada);

                if (direccionId != -1) {
                    JSONObject jsonPedido = construirJsonPedido(direccionId);
                    enviarPedido(jsonPedido);
                    Toast.makeText(DetallePedidoActivity.this, "Pedido confirmado con direcciónId: " + direccionId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetallePedidoActivity.this, "Error: dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
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
                        finish();
                    }
                    else if(itemId == R.id.page_2){
                        Intent intent = new Intent(getApplicationContext(), HistorialPedidoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(itemId == R.id.page_3){
                        Intent intent = new Intent(getApplicationContext(), CustomerAddress.class);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }
            };
    private void obtenerYMostrarDirecciones() {
        SessionManager session = new SessionManager(getApplicationContext());
        String userId = session.getUserId();

        OkHttpClient client = new OkHttpClient();
        String url = "https://delivery-service.azurewebsites.net/api/Direccion?usuarioId=" + userId;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            direccionesApi = parseDireccionData(responseData);

                            direccionList = new ArrayList<>();
                            for (JSONObject direccion : direccionesApi) {
                                try {
                                    String nombre = direccion.getString("nombre");
                                    String referencia = direccion.getString("referencia");
                                    direccionList.add(nombre + " - " + referencia);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(DetallePedidoActivity.this, android.R.layout.simple_spinner_item, direccionList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            combodireccion.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }


    private List<JSONObject> parseDireccionData(String json) {
        List<JSONObject> direccionList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject addressObject = dataArray.getJSONObject(i);
                direccionList.add(addressObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return direccionList;
    }


    private int obtenerDireccionId(String direccionSeleccionada) {
        for (JSONObject direccion : direccionesApi) {
            try {
                String nombre = direccion.getString("nombre");
                String referencia = direccion.getString("referencia");
                int direccionId = direccion.getInt("direccionId");

                if (direccionSeleccionada.equals(nombre + " - " + referencia)) {
                    return direccionId;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private JSONObject construirJsonPedido(int direccionId) {
        SessionManager session = new SessionManager(getApplicationContext());
        String userId = session.getUserId();
        JSONObject jsonPedido = new JSONObject();
        try {
            jsonPedido.put("direccionId", direccionId);
            jsonPedido.put("clienteId", userId);
            jsonPedido.put("detalle", construirDetallePedido());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPedido;
    }


    private JSONArray construirDetallePedido() {
        JSONArray detallePedido = new JSONArray();
        for (Producto producto : productosSeleccionados) {
            JSONObject detalleProducto = new JSONObject();
            try {
                detalleProducto.put("productoId", producto.getProductoId());
                detalleProducto.put("cantidad", producto.getCantidad());
                detalleProducto.put("precio", producto.getPrecio());
                detalleProducto.put("isv", producto.getIsv());
                detallePedido.put(detalleProducto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detallePedido;
    }

    private void enviarPedido(JSONObject jsonPedido) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://delivery-service.azurewebsites.net/api/Pedidos";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonPedido.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetallePedidoActivity.this, "Error al enviar el pedido", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetallePedidoActivity.this, "Pedido enviado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetallePedidoActivity.this, "Error al enviar el pedido", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    private double calcularSubTotalPagar(ArrayList<Producto> productos) {
        double subtotal = 0;
        for (Producto producto : productos) {
            subtotal += (producto.getPrecio() * producto.getCantidad());
        }
        return subtotal;
    }

    private double calcularISV(ArrayList<Producto> productos) {
        double subtotal = calcularSubTotalPagar(productos);
        double isv = subtotal * 0.15;
        return isv;
    }
}