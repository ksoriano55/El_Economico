package com.example.supermercado_el_economico.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supermercado_el_economico.R;
import com.example.supermercado_el_economico.models.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {
    private List<Pedido> listaPedidos;
    private List<Pedido> listaPedidosFiltrados;
    private Context context;

    public PedidoAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
        this.listaPedidosFiltrados = new ArrayList<>(listaPedidos);
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.adapterpedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = listaPedidosFiltrados.get(position);
        holder.codigoprduct.setText(pedido.getNumPedido());
        holder.fecha.setText(pedido.getFecha());
        holder.estatus.setText(pedido.getEstado());
        holder.total.setText(String.valueOf(pedido.getTotal()));
        holder.estrellas.setRating(pedido.getCalificacion());

    }

    @Override
    public int getItemCount() {
        return listaPedidosFiltrados.size();
    }

    public void filtrarPorEstado(String estado) {
        listaPedidosFiltrados.clear();

        if (estado.equals("PENDIENTE_Y_EN_PROCESO")) {
            for (Pedido pedido : listaPedidos) {
                if (pedido.getEstado().equals("PENDIENTE") || pedido.getEstado().equals("EN PROCESO")) {
                    listaPedidosFiltrados.add(pedido);
                }
            }
        } else {
            for (Pedido pedido : listaPedidos) {
                if (pedido.getEstado().equals(estado)) {
                    listaPedidosFiltrados.add(pedido);
                }
            }
        }

        notifyDataSetChanged();
    }


    public class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView codigoprduct, fecha, estatus, total;
        RatingBar estrellas;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            codigoprduct = itemView.findViewById(R.id.codigo_pedido);
            fecha = itemView.findViewById(R.id.fecha_pedido);
            estatus = itemView.findViewById(R.id.text_estatus_pedido);
            total = itemView.findViewById(R.id.Total_pedido);
            estrellas = itemView.findViewById(R.id.rating_bar_calificacion);
        }
    }
}
