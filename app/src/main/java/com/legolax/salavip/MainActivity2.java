package com.legolax.salavip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ArrayList<Game> listaGames;
    private RecyclerView recyclerGames;
    private Button nuevoJuego;
    private SearchView buscador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CargarListaGames();

        recyclerGames = (RecyclerView) findViewById(R.id.Recicler2Id);
        nuevoJuego = (Button) findViewById(R.id.newgamebtn);
        buscador = (SearchView) findViewById(R.id.idbuscador);

        recyclerGames.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorGames adapter = new AdaptadorGames(listaGames);
        recyclerGames.setAdapter(adapter);

        //divide la lista en 2 columnas
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerGames.setLayoutManager(mLayoutManager);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game g = listaGames.get(recyclerGames.getChildAdapterPosition(view));

                //Envío el objeto seleccionado a la scena gameManager
                Intent intento = new Intent(MainActivity2.this , GameManager.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("juego", g);
                intento.putExtras(bundle);
                startActivity(intento);
            }
        });

        nuevoJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //crear un intent para ir a la scena GameManager para crear un juego de cero
                Intent intent = new Intent(MainActivity2.this , GameManager.class);
                startActivity(intent);
            }
        });
    }


    private void CargarListaGames() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ListGameData4", "");
        //Toast.makeText(MainActivity2.this, json, Toast.LENGTH_SHORT ).show();
        if (json.equals("")){
            listaGames = new ArrayList<>();
        }else{
            Type type = new TypeToken<ArrayList<Game>>() {}.getType();
            listaGames = gson.fromJson(json, type);
        }

    }
}