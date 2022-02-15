package com.example.xpiration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

import entidades.ListaLotesAdapter;
import entidades.Lotes;

public class VistaMermaActivity extends AppCompatActivity {

    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    ArrayList<Lotes> listaLote;
    ListaLotesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializacion de parametros del menu (Nombre superior del menu)
        setContentView(R.layout.activity_vista_merma);
        getSupportActionBar().setTitle("Merma de productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar Boton para volver atras

        //Inicializacion de elementos
        context = this;
        l = new Lotes();
        listaLote = new ArrayList<Lotes>(l.MermaLotes());
        seteoParametros(); //llamado a la funcion para utliziar el RecyclerView con el adapter

    }
    //**************Funcion que inicializa los parametros del Adapter y Recycler View***************
    public void seteoParametros(){
        recyclerViewLote = findViewById(R.id.ListaMerma);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this)); //Se utiliza el LinearLayoutManager que viene por defecto
        adapter = new ListaLotesAdapter(listaLote); //se le ingresan todos los datos al adapter
        recyclerViewLote.setAdapter(adapter);  //Se ingresan los datos al Recyclerview utilizando un el adapter
    }

    //******************Funcion que maneja las funciones del menu por defecto***********************
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Boton de la esquina superior izquierda que devuelve al menu principal
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(context,MainActivity.class); //Creamos la instancia del menu principal
            context.startActivity(intent); //Comenzamos la instancia
            finish(); //Se finaliza la instancia actual (VistaMerma)
        }
        return super.onOptionsItemSelected(item);
    }
}