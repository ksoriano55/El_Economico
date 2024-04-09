package com.example.supermercado_el_economico.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.supermercado_el_economico.R;
import com.example.supermercado_el_economico.SharedPreferencesHelper;
import com.example.supermercado_el_economico.models.Producto;

import java.util.List;

public class ProductosSeleccionadosAdapter extends RecyclerView.Adapter<ProductosSeleccionadosAdapter.ProductoViewHolder> {

    private List<Producto> productosSeleccionados;
    private Context context;

    public ProductosSeleccionadosAdapter(List<Producto> productosSeleccionados) {
        this.productosSeleccionados = productosSeleccionados;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_seleccionado, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Producto producto = productosSeleccionados.get(position);
        holder.textViewNombreProducto.setText(producto.getNombre());
        //holder.textViewPrecioProducto.setText("" + producto.getPrecio());
        Double Precio = producto.getPrecio() * producto.getCantidad();
        holder.textViewPrecioProducto.setText(String.valueOf(Precio));
        //holder.textViewPrecioProducto.setText(String.valueOf(producto.getPrecio()));
        //holder.textViewCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.textViewCantidad.setText(String.valueOf(producto.getCantidad()));
        Glide.with(context)
                .load(producto.getFoto())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageViewProducto);

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = producto.getCantidad() - producto.getCantidad();
               // if (newQuantity >= 0) {
                    producto.setCantidad(newQuantity);
                //    holder.textViewCantidad.setText("Cantidad: " + newQuantity);
                    if (newQuantity == 0) {
                        productosSeleccionados.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productosSeleccionados.size());
                    }
                    SharedPreferencesHelper.saveProductos(context, productosSeleccionados);
           //     }
            }
        });

        holder.btnmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = producto.getCantidad() - 1;
                if (newQuantity >= 0) {
                producto.setCantidad(newQuantity);
                    Double newPrecio = producto.getPrecio() * newQuantity;
                    holder.textViewPrecioProducto.setText(String.valueOf(newPrecio));
                    holder.textViewCantidad.setText(String.valueOf(newQuantity));
                if (newQuantity == 0) {
                    productosSeleccionados.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, productosSeleccionados.size());
                }
                SharedPreferencesHelper.saveProductos(context, productosSeleccionados);
                     }
            }
        });

        holder.btnmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = producto.getCantidad() + 1;
                if (newQuantity >= 0) {
                    producto.setCantidad(newQuantity);
                    Double newPrecio = producto.getPrecio() * newQuantity;
                    holder.textViewPrecioProducto.setText(String.valueOf(newPrecio));
                    holder.textViewCantidad.setText(String.valueOf(newQuantity));

                    if (newQuantity == 0) {
                        productosSeleccionados.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productosSeleccionados.size());
                    }
                    SharedPreferencesHelper.saveProductos(context, productosSeleccionados);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return productosSeleccionados.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProducto;
        TextView textViewNombreProducto;
        TextView textViewPrecioProducto;
        TextView textViewCantidad;
        ImageView btnRemove;
        Button btnmenos,btnmas;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProducto = itemView.findViewById(R.id.imgproducto);
            textViewNombreProducto = itemView.findViewById(R.id.nombreproducto);
            textViewPrecioProducto = itemView.findViewById(R.id.precioproduto);
            textViewCantidad = itemView.findViewById(R.id.textCantidad);
            btnRemove = itemView.findViewById(R.id.btnremove);
            btnmas = itemView.findViewById(R.id.btnMas);
            btnmenos = itemView.findViewById(R.id.btnMenos);
        }
    }
}
