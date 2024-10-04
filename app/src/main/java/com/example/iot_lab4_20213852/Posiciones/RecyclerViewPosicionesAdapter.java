package com.example.iot_lab4_20213852.Posiciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iot_lab4_20213852.Objetos.Equipo;
import com.example.iot_lab4_20213852.Objetos.Liga;
import com.example.iot_lab4_20213852.Objetos.Tabla;
import com.example.iot_lab4_20213852.R;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewPosicionesAdapter extends RecyclerView.Adapter<RecyclerViewPosicionesAdapter.ViewHolder> {
    private final List<Equipo> equipos;
    private Context context;

    public RecyclerViewPosicionesAdapter(List<Equipo> equipos, Context context) {
        this.equipos = equipos;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_recyclerview_posiciones, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Equipo equipo=equipos.get(position);
        Glide.with(context).load(equipo.getStrBadge()).into(holder.badge); //Se usó ayudita de un LLM para cargar una imagen a partir de un enlace
        holder.rankingNombre.setText("#"+equipo.getIntRank()+" - "+equipo.getStrTeam());
        holder.partidos.setText("Victorias: "+equipo.getIntWin()+", Empates: "+equipo.getIntDraw()+", Derrotas: "+equipo.getIntLoss());
        holder.goles.setText("Goles: Anotados-> "+equipo.getIntGoalsFor()+", Concedidos-> "+equipo.getIntGoalsAgainst()+", Diferencia-> "+equipo.getIntGoalDifference());
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankingNombre;
        TextView partidos;
        TextView goles;
        ImageView badge;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            badge=itemView.findViewById(R.id.badge);
            rankingNombre=itemView.findViewById(R.id.rankingNombre);
            partidos=itemView.findViewById(R.id.partidos);
            goles=itemView.findViewById(R.id.goles);
        }
    }
}
