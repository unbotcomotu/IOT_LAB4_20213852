package com.example.iot_lab4_20213852.Resultados;

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
import com.example.iot_lab4_20213852.Objetos.Evento;
import com.example.iot_lab4_20213852.Objetos.Eventos;
import com.example.iot_lab4_20213852.R;

import java.util.List;

public class RecyclerViewResultadosAdapter extends RecyclerView.Adapter<RecyclerViewResultadosAdapter.ViewHolder> {
    private final List<Evento> eventos;
    private Context context;

    public RecyclerViewResultadosAdapter(List<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_recyclerview_resultados, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento=eventos.get(position);
        Glide.with(context).load(evento.getStrLeagueBadge()).into(holder.badge); //Se usó ayudita de un LLM para cargar una imagen a partir de un enlace
        holder.nombre.setText(evento.getStrEvent());
        holder.fechaCantidadEspectadores.setText("Fecha: "+evento.getDateEvent()+", #espectadores: "+evento.getIntSpectators());
        holder.equipos.setText("Equipos: Local->"+evento.getStrHomeTeam()+", Visitante->"+evento.getStrAwayTeam());
        holder.resultado.setText("Ganador: "+(evento.getIntHomeScore()>evento.getIntAwayScore()?evento.getStrHomeTeam(): evento.getIntAwayScore()== evento.getIntAwayScore()?"Empate":evento.getStrAwayTeam())+" ("+evento.getIntHomeScore()+"-"+evento.getIntAwayScore()+")");
        holder.ronda.setText("Ronda: "+evento.getIntRound());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resultado;
        TextView ronda;
        TextView fechaCantidadEspectadores;
        TextView equipos;
        TextView nombre;
        ImageView badge;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            badge=itemView.findViewById(R.id.badge);
            fechaCantidadEspectadores=itemView.findViewById(R.id.fechaCantidadEspectadores);
            equipos=itemView.findViewById(R.id.equipos);
            resultado=itemView.findViewById(R.id.resultado);
            ronda=itemView.findViewById(R.id.ronda);
            nombre=itemView.findViewById(R.id.nombre);
        }
    }
}
