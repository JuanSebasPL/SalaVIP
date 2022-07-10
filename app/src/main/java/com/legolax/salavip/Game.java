package com.legolax.salavip;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private String Name;
    private String Descripcion;
    private String Genero;
    private String Detalles;
    private String jugadoresLocal;
    private String jugadoresOnline;
    private float Rating;
    private int year;
    private ArrayList<String> listaImagenes;
    private String Video;
    private ArrayList<String> listaPlataformas;

    public Game() {
    }

    public Game(String name, String descripcion, String detalles, String genero, int _year, float rating , String jugadoresO, String jugadoresL, String _video ) {
        Name = name;
        Descripcion = descripcion;
        Genero = genero;
        Detalles = detalles;
        jugadoresLocal = jugadoresL;
        jugadoresOnline = jugadoresO;
        year = _year;
        Rating = rating;
        listaImagenes = new ArrayList<>();
        Video = _video;
        listaPlataformas = new ArrayList<>();
    }

    //agregar una imagen a la lista
    public void agregarImagen(String img) {
        this.listaImagenes.add(img);
    }

    //agregar una plataforma a la lista
    public void agregarPlataforma(String plataforma) {
        this.listaPlataformas.add(plataforma);
    }


    //getter and setters
    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDetalles() {
        return Detalles;
    }

    public void setDetalles(String detalles) {
        Detalles = detalles;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getJugadoresLocal() {
        return jugadoresLocal;
    }

    public void setJugadoresLocal(String jugadoresLocal) {
        this.jugadoresLocal = jugadoresLocal;
    }

    public String getJugadoresOnline() {
        return jugadoresOnline;
    }

    public void setJugadoresOnline(String jugadoresOnline) {
        this.jugadoresOnline = jugadoresOnline;
    }

    public ArrayList<String> getListaPlataformas() {
        return listaPlataformas;
    }

    public void setListaPlataformas(ArrayList<String> listaPlataformas) {
        this.listaPlataformas = listaPlataformas;
    }

    public ArrayList<String> getListaImagenes() {
        return listaImagenes;
    }

    public void setListaImagenes(ArrayList<String> listaImagenes) {
        this.listaImagenes = listaImagenes;
    }
}
