package com.example.iot_lab4_20213852.Ligas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.iot_lab4_20213852.Objetos.Liga;
import com.example.iot_lab4_20213852.R;

import java.util.List;

public class RecyclerViewLigasAdapter extends RecyclerView.Adapter<RecyclerViewLigasAdapter.ViewHolder> {
    private final List<Liga> ligas;

    public RecyclerViewLigasAdapter(List<Liga> ligas) {
        this.ligas = ligas;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_recyclerview_ligas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Liga liga=ligas.get(position);
        holder.nombre.setText(liga.getStrLeague());
        holder.idDeporteNombreAlternado.setText("ID: "+liga.getIdLeague()+", Deporte: "+liga.getStrSport()+", Nombre alternativo: "+(liga.getStrLeagueAlternate()!=null?liga.getStrLeagueAlternate():" No tiene"));
    }

    @Override
    public int getItemCount() {
        return ligas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView idDeporteNombreAlternado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre);
            idDeporteNombreAlternado=itemView.findViewById(R.id.idDeporteNombreAlternado);
        }
    }
}
