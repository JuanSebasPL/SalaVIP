package com.legolax.salavip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GameManager extends AppCompatActivity {

    private boolean editar;
    private int seleccionImagen;
    public EditText Nombre, Ano, Descripcion, Rating, Detalles;
    private Game juego;
    public CheckBox ps4, xbox, vr, pc;
    private Spinner jugadoresLocales, onnline, genero;
    public ImageButton icono, img1, img2, img3, img4;
    public VideoView video;
    public String UriIcono, Uriimg1, UriImg2, UriImg3, UriImg4, UriVideo;
    private Button BtnLimpiar, BtnBorrar, BtnGuardar;
    private FileOutputStream outputStream;
    public ArrayList<Game> listaGames;

    private static final int PICK_IMAGE_REQUEST = 0;
    private int PICK_VIDEO_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);


        cargarListaGames();

        UriVideo = "";
        Nombre = (EditText) findViewById(R.id.editNombreGame);
        Ano = (EditText) findViewById(R.id.editAnoGame);
        Descripcion = (EditText) findViewById(R.id.editDescripcionGame);
        genero = (Spinner) findViewById(R.id.editGeneroGame);
        Rating = (EditText) findViewById(R.id.editRatingGame);
        onnline = (Spinner) findViewById(R.id.editOnlineGames);
        Detalles = (EditText) findViewById(R.id.editDetallesGame);
        jugadoresLocales = (Spinner) findViewById(R.id.editJugadoresGame);
        BtnLimpiar = (Button) findViewById(R.id.buttonLimpiar);
        BtnBorrar = (Button) findViewById(R.id.buttonBorrar);
        BtnGuardar = (Button) findViewById(R.id.buttonGuardar);
        ps4 = (CheckBox) findViewById(R.id.ps4Button);
        xbox = (CheckBox) findViewById(R.id.xboxButton);
        vr = (CheckBox) findViewById(R.id.vrButton);
        pc = (CheckBox) findViewById(R.id.pcButton);
        icono = (ImageButton) findViewById(R.id.imageIconButton);
        img1 = (ImageButton) findViewById(R.id.image1Button);
        img2 = (ImageButton) findViewById(R.id.image2Button);
        img3 = (ImageButton) findViewById(R.id.image3Button);
        img4 = (ImageButton) findViewById(R.id.image4Button);
        video = (VideoView) findViewById(R.id.videoViewbtn);

        //adapter de los jugadores y onnline
        ArrayAdapter<CharSequence> jugadoresAdapter = ArrayAdapter.createFromResource(this, R.array.jugadores, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> OnlineAdapter = ArrayAdapter.createFromResource(this, R.array.modoOnline,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.generos, android.R.layout.simple_spinner_item);

        jugadoresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OnlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        jugadoresLocales.setAdapter(jugadoresAdapter);
        onnline.setAdapter(OnlineAdapter);
        genero.setAdapter(adapter);

        //revisa si se va a crear un nuevo juego o se esta editando uno anterior
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            juego = (Game) bundle.getSerializable("juego");
            editar = true;
            llenardatos();
        }else{
            juego = new Game();
            editar = false;
        }

        //Botones imagenes
        icono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen(0);
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen(1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen(2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen(3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen(4);
            }
        });

        //boton video
        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                seleccionarVideo();
                return false;
            }
        });


        //borra las imagenes que se haban seleccionado
        BtnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1.setImageURI(null);
                img2.setImageURI(null);
                img3.setImageURI(null);
                img4.setImageURI(null);
            }
        });

        //Borra los datos del juego y se devulve a la pantalla mainactivity2
        BtnBorrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                borrarjuego();
            }
        });

        //Guarda los datos del juego y se devulve a la pantalla mainativity2
        BtnGuardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Nombre.getText().toString().equals("") || Ano.getText().toString().equals("") || Descripcion.getText().toString().equals("")
                        || Rating.getText().toString().equals("") || Detalles.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Recuerde llenar todos los campos de texto", Toast.LENGTH_SHORT ).show();
                } else
                {
                    //verifica si hay imaggenes
                    if ((icono.getDrawable()!=null)&&(img1.getDrawable()!=null)&&(img2.getDrawable()!=null)&&(img3.getDrawable()!=null)&&(img4.getDrawable()!=null))
                    {
                        //en esta zona marca un error
                         GuardarObjeto();
                         GuardarLista();
                         finish();
                        //Toast.makeText(getApplicationContext(), "se guardó el objeto" , Toast.LENGTH_SHORT ).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Recuerde agregar todas las imagenes", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });


    }

    public void borrarjuego(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("¿ Desea borrar este juego ?");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                for(int i=0; i<listaGames.size(); i++){
                    if(listaGames.get(i).getName().equals(juego.getName())) {
                        listaGames.remove(i);
                    }
                }
                GuardarLista();
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void llenardatos()
    {
        Nombre.setText(juego.getName());
        Ano.setText(Integer.toString(juego.getYear()));
        Descripcion.setText(juego.getDescripcion());
        Rating.setText(Float.toString(juego.getRating()));
        Detalles.setText(juego.getDetalles());

        //establece el spinner en la seccion del objeto de genero
        for(int i=0; i<12; i++){
            if(juego.getGenero().equals(genero.getItemAtPosition(i))){
                genero.setSelection(i);
            }
        }
        //establece los checkbox de las plataformas
        for (int i=0; i<juego.getListaPlataformas().size(); i++)
        {
            if(juego.getListaPlataformas().get(i).equals(ps4.getText())){
                ps4.setChecked(true);
            }else if(juego.getListaPlataformas().get(i).equals(xbox.getText())){
                xbox.setChecked(true);
            }else if(juego.getListaPlataformas().get(i).equals(vr.getText())){
                vr.setChecked(true);
            }else if(juego.getListaPlataformas().get(i).equals(pc.getText())){
                pc.setChecked(true);
            }
        }
        //establece el desplegable en la seccion del objeto de los jugadores locales
        for(int i=0; i<4; i++){
            if(juego.getJugadoresLocal().equals(jugadoresLocales.getItemAtPosition(i))){
                jugadoresLocales.setSelection(i);
            }
        }
        //establece el desplegable en la seccion del objeto de los jugadores online
        for(int i=0; i<2; i++){
            if(juego.getJugadoresOnline().equals(onnline.getItemAtPosition(i))){
                onnline.setSelection(i);
            }
        }
        //establece las imagenes del objeto
        UriIcono = juego.getListaImagenes().get(0);
        Uriimg1 = juego.getListaImagenes().get(1);
        UriImg2 = juego.getListaImagenes().get(2);
        UriImg3 = juego.getListaImagenes().get(3);
        UriImg4 = juego.getListaImagenes().get(4);

        if(!(juego.getVideo().equals(""))){
            UriVideo = juego.getVideo();
            video.setVideoURI(Uri.parse(juego.getVideo()));
        }

        icono.setImageURI(Uri.parse(juego.getListaImagenes().get(0)));
        img1.setImageURI(Uri.parse(juego.getListaImagenes().get(1)));
        img2.setImageURI(Uri.parse(juego.getListaImagenes().get(2)));
        img3.setImageURI(Uri.parse(juego.getListaImagenes().get(3)));
        img4.setImageURI(Uri.parse(juego.getListaImagenes().get(4)));



    }

    public void cargarListaGames()
    {
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

    public void seleccionarImagen(int x)
    {
        seleccionImagen = x;
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void seleccionarVideo(){
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select video"), PICK_VIDEO_REQUEST);
    }

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    public void GuardarObjeto()
    {

        juego.setYear(Integer.parseInt(Ano.getText().toString()));
        juego.setDescripcion(Descripcion.getText().toString());
        juego.setDetalles(Detalles.getText().toString());
        juego.setGenero(genero.getSelectedItem().toString());
        juego.setRating(Float.parseFloat(Rating.getText().toString()));
        juego.setJugadoresLocal(jugadoresLocales.getSelectedItem().toString());
        juego.setJugadoresOnline(onnline.getSelectedItem().toString());

        //agregando las plataformas
        juego.setListaPlataformas(new ArrayList<String>());
        if (ps4.isChecked()){
            juego.agregarPlataforma(ps4.getText().toString());
        }
        if (xbox.isChecked()){
            juego.agregarPlataforma(xbox.getText().toString());
        }
        if (vr.isChecked()){
            juego.agregarPlataforma(vr.getText().toString());
        }
        if (pc.isChecked()){
            juego.agregarPlataforma(pc.getText().toString());
        }

        //guardando la nueva lista de imagenes en archivos y en el objeto
        //eliminarImagenes();
        juego.setListaImagenes(new ArrayList<String>());
        //guardarImagen(icono, juego, i);
        //Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        juego.agregarImagen(UriIcono);
        juego.agregarImagen(Uriimg1);
        juego.agregarImagen(UriImg2);
        juego.agregarImagen(UriImg3);
        juego.agregarImagen(UriImg4);

        //agregando video
        juego.setVideo(UriVideo);

        //reemplazando el objeto en la lista
        Boolean verifica = true;
            for (int i = 0; i < listaGames.size(); i++) {
                if (listaGames.get(i).getName().equals(juego.getName())) {
                    juego.setName(Nombre.getText().toString());
                    listaGames.set(i, juego);
                    verifica = false;
                }
            }
            if(verifica){
                juego.setName(Nombre.getText().toString());
                listaGames.add(juego);
            }
    }

    //guarda en shared preferences la lista
    public void GuardarLista()
    {
        Gson gson = new Gson();
        String json = gson.toJson(listaGames);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ListGameData4", json);
        editor.apply();
        //Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT ).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {
                    //inserta la imagen segun el contenedor de donde fue seleccionado
                    switch (seleccionImagen) {
                        case 0:
                            icono.setImageURI(data.getData());
                            icono.invalidate();
                            UriIcono = String.valueOf(data.getData());
                            break;
                        case 1:
                            img1.setImageURI(data.getData());
                            img1.invalidate();
                            Uriimg1 = String.valueOf(data.getData());
                            break;
                        case 2:
                            img2.setImageURI(data.getData());
                            img2.invalidate();
                            UriImg2 = String.valueOf(data.getData());
                            break;
                        case 3:
                            img3.setImageURI(data.getData());
                            img3.invalidate();
                            UriImg3 = String.valueOf(data.getData());
                            break;
                        case 4:
                            img4.setImageURI(data.getData());
                            img4.invalidate();
                            UriImg4 = String.valueOf(data.getData());
                            break;
                    }
                }
            }
        }else if (requestCode == PICK_VIDEO_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {
                    //video.setVideoURI(data.getData());
                    //video.invalidate();
                    UriVideo = String.valueOf(data.getData());
                } else {
                    Toast.makeText(getApplicationContext(), "seleccione un video valido", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }//and activity results

}