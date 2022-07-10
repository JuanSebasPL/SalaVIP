package com.legolax.salavip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.legolax.salavip.ui.main.SectionsPagerAdapter;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class Fragment1 extends Fragment {

    private ArrayList<Game> listaGames;
    private ArrayList<String> listaImagenes;
    private RecyclerView recyclerGames;
    private int consola;
    private static int NUM_PAGES = 0;
    Timer swipeTimer;
    private ViewPager2 mPager;
    private static int currentPage = 0;
    public View vistaGeneral;


    public Fragment1(int consola) {
        this.consola = consola;
    }

     public Game g;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista1,container,false);
        //volver el recycler de 3 lineas
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        //recyclerGames.setLayoutManager(mLayoutManager);
        listaGames = new ArrayList<>();
        listaImagenes= new ArrayList<>();
        CargarListaGames();
        recyclerGames = (RecyclerView) view.findViewById(R.id.ReciclerId);
        recyclerGames.setLayoutManager(new LinearLayoutManager(getContext()));
        AdaptadorGames adapter = new AdaptadorGames(listaGames);
        recyclerGames.setAdapter(adapter);

        //llenando detalles
        final TextView tituloDetalles = (TextView) view.findViewById(R.id.tituloDetalles);
        final TextView descripcionDetalles = (TextView) view.findViewById(R.id.descripcionDetalles);
        final TextView generoDetalles = (TextView) view.findViewById(R.id.generoDetalles);
        final TextView jugadoresDetalles = (TextView) view.findViewById(R.id.jugadoresDetalles);
        final RatingBar ratingDetalles = (RatingBar) view.findViewById(R.id.ratingDetalles);
        final TextView numeroRating = (TextView) view.findViewById(R.id.ratingNumero);
        final View lineaDivisora = (View) view.findViewById(R.id.divisor);
        final View lineaDivisora2 = (View) view.findViewById(R.id.divisor2);
        final ImageButton BtnVideo = (ImageButton) view.findViewById(R.id.BtnVideo);

        mPager = (ViewPager2) view.findViewById(R.id.sliderImagenes);

        //inicializando toda la info en el objeto 1
        if (listaGames.size()>0)
        {
            g = listaGames.get(0);
            vistaGeneral = view;
            ratingDetalles.setVisibility(View.VISIBLE);
            mPager.setVisibility(View.VISIBLE);
            if(g.getVideo().equals("")){
                BtnVideo.setVisibility(View.INVISIBLE);
            }else {
                BtnVideo.setVisibility(View.VISIBLE);
            }
            lineaDivisora.setVisibility(View.VISIBLE);
            lineaDivisora2.setVisibility(View.VISIBLE);
            init(view, g);
            tituloDetalles.setText(g.getName());
            descripcionDetalles.setText(g.getDetalles());
            generoDetalles.setText("Genero: "+ g.getGenero() +"\n" + "Año: " + g.getYear());
            jugadoresDetalles.setText("Local: "+ g.getJugadoresLocal() + "Jugador(es)" +"\n" + "Online: " + g.getJugadoresOnline());
            String num1 = String.valueOf(g.getRating());
            numeroRating.setText(num1);
            ratingDetalles.setRating(g.getRating());
        }

        // accion cuando seleccionan un juego
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                g = listaGames.get(recyclerGames.getChildAdapterPosition(view));
                if(g.getVideo().equals("")){
                    BtnVideo.setVisibility(View.INVISIBLE);
                }else {
                    BtnVideo.setVisibility(View.VISIBLE);
                }
                init(vistaGeneral, g);
                tituloDetalles.setText(g.getName());
                descripcionDetalles.setText(g.getDetalles());
                ratingDetalles.setRating(g.getRating());
                String num2 = String.valueOf(g.getRating());
                numeroRating.setText(num2);
                generoDetalles.setText("Genero: "+ g.getGenero() +"\n" + "Año: " + g.getYear());
                jugadoresDetalles.setText("Local: "+ g.getJugadoresLocal() + "Jugador(es)" +"\n" + "Online: " + g.getJugadoresOnline() );
            }
        });

        BtnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent siguientePantalla = new Intent(getContext(), videoVista.class);
                //envia el video de "g" el juego que se seleccionó de ultimo y que se muestra en el fragment
                siguientePantalla.putExtra("video", g.getVideo());
                startActivity(siguientePantalla);
            }
        });

        return view;
    }

    private void CargarListaGames()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ListGameData4", "");
        ArrayList<Game> listaGamesData = new ArrayList<>();
        if (json.equals("")){
        }else{
            Type type = new TypeToken<ArrayList<Game>>() {}.getType();
            listaGamesData = gson.fromJson(json, type);
          //  Toast.makeText(getContext(), json, Toast.LENGTH_SHORT ).show();
        }
        //agregandolos a la lista de pc, xbox y ps4
        switch (this.consola){
            case 0:
                for (Game juego: listaGamesData) {
                    if(juego.getListaPlataformas().contains("PLAY STATION 4")){
                        listaGames.add(juego);
                    }
                }
                break;
            case 1:
                for (Game juego: listaGamesData) {
                    if(juego.getListaPlataformas().contains("XBOX ONE")){
                        listaGames.add(juego);
                    }
                }
                break;
            case 2:
                for (Game juego: listaGamesData) {
                    if(juego.getListaPlataformas().contains("REALIDAD VIRTUAL")){
                        listaGames.add(juego);
                    }
                }
                break;
            case 3:
                for (Game juego: listaGamesData) {
                    if(juego.getListaPlataformas().contains("PC")){
                        listaGames.add(juego);
                    }
                }
                break;
        }
    }

    private void init(View vista, Game juego)
    {
        swipeTimer = new Timer();
        listaImagenes = new ArrayList<>();
        for(int i=1; i<juego.getListaImagenes().size(); i++){
            listaImagenes.add(juego.getListaImagenes().get(i));
        }
        mPager.setAdapter( new ViewPagerAdapter2(listaImagenes, mPager));
        CircleIndicator3 indicator = (CircleIndicator3) vista.findViewById(R.id.sliderImagenesindicator);
        indicator.setViewPager(mPager);
        //final float density = getResources().getDisplayMetrics().density;

        NUM_PAGES =listaImagenes.size();

        // Auto start of viewpager
        /*final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage, true);
            }
        };
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        /*
         */
    }

}
