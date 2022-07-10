package com.legolax.salavip;

import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorGames extends
        RecyclerView.Adapter<AdaptadorGames.GamesViewHolder> implements View.OnClickListener {

    ArrayList<Game> listaGames;
    private View.OnClickListener listener;

    public AdaptadorGames(ArrayList<Game> listaGames) {
        this.listaGames = listaGames;
    }

    @NonNull
    @Override
    public GamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);

        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        view.setOnClickListener(this);

        return new GamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesViewHolder holder, int position) {
        holder.titulo.setText(listaGames.get(position).getName());
        holder.genero.setText("Genero: "+ listaGames.get(position).getGenero());
        holder.descripcion.setText(listaGames.get(position).getDescripcion());
        Uri img = null;
        try {
             img = Uri.parse(listaGames.get(position).getListaImagenes().get(0));
        }catch (NullPointerException e){
            System.out.println("error: "+ e);
        }
        holder.imagen.setImageURI(img);
        holder.rating.setRating(listaGames.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return listaGames.size();
    }

    // metodo de click listener
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }

    public class GamesViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView descripcion;
        TextView genero;
        ImageView imagen;
        RatingBar rating;

        public GamesViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            genero = (TextView) itemView.findViewById(R.id.genero);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
}
