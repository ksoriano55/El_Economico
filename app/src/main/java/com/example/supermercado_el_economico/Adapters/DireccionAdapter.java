package com.example.supermercado_el_economico.Adapters;
import com.example.supermercado_el_economico.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supermercado_el_economico.R;
import com.example.supermercado_el_economico.models.DireccionModel;

import java.util.List;

public class DireccionAdapter extends RecyclerView.Adapter<DireccionAdapter.ViewHolder> {

    private List<DireccionModel> direcciones;
    private Context context;

    // Constructor
    public DireccionAdapter(List<DireccionModel> direcciones) {
        this.direcciones = direcciones;
    }

    // Método para crear las vistas de cada elemento en el RecyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View direccionView = inflater.inflate(R.layout.direccion_adapter, parent, false);
        return new ViewHolder(direccionView);
    }

    // Método para asociar los datos de cada elemento con las vistas correspondientes
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DireccionModel direccion = direcciones.get(position);
        holder.bind(direccion);
    }

    // Método para obtener el número total de elementos en la lista
    @Override
    public int getItemCount() {
        return direcciones.size();
    }

    // Clase ViewHolder para mantener las vistas de cada elemento en el RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private TextView referenciaTextView;

        // Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.txtNombreDir);
            referenciaTextView = itemView.findViewById(R.id.txtReferencia);
        }

        // Método para asignar los datos de una DireccionModel a las vistas
        public void bind(DireccionModel direccion) {
            nombreTextView.setText(direccion.getNombre());
            referenciaTextView.setText(direccion.getReferencia());
        }
    }
}