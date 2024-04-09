package com.example.supermercado_el_economico.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.supermercado_el_economico.R;
import com.example.supermercado_el_economico.models.Producto;

import java.util.List;

public class DetallePedidoAdapter extends RecyclerView.Adapter<DetallePedidoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private Context context;

    public DetallePedidoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.adapterpedidodetalle, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.textViewNombreProducto.setText(producto.getNombre());
        //holder.textViewPrecioProducto.setText(String.valueOf(producto.getPrecio()));
        Double Precio = producto.getPrecio() * producto.getCantidad();
        holder.textViewPrecioProducto.setText(String.valueOf(Precio));
        holder.textViewCantidadProducto.setText(String.valueOf(producto.getCantidad()));
        // Si tienes la URL de la imagen en tu modelo de Producto, puedes cargarla usando Glide
        Glide.with(context)
                .load(producto.getFoto())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageViewProducto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProducto;
        TextView textViewNombreProducto;
        TextView textViewPrecioProducto;
        TextView textViewCantidadProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProducto = itemView.findViewById(R.id.imgproducto);
            textViewNombreProducto = itemView.findViewById(R.id.nombreproducto);
            textViewPrecioProducto = itemView.findViewById(R.id.Total);
            textViewCantidadProducto = itemView.findViewById(R.id.cantproducto);
        }
    }
}
