package com.example.xpiration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entidades.ListaLotesAdapter;
import entidades.Lotes;

public class VistaNotificacionActivity extends AppCompatActivity {

    //*************Definicion de parametros utilizados en la vista de la notificacion***************
    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    ArrayList<Lotes> listaLote;  //Lista que contiene todos los lotes que estan por caducar
    ListaLotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializacion de parametros del menu (Nombre superior del menu)
        setContentView(R.layout.activity_vista_notificacion);
        getSupportActionBar().setTitle("Lotes cerca de caducar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar Boton para volver atras

        //Inicializacion de elementos
        context = this;
        l = new Lotes();
        listaLote = new ArrayList<Lotes>(l.mostrarTodoLotes());
        seteoParametros(); //llamado a la funcion para utliziar el RecyclerView con el adapter

    }

    //**************Funcion que inicializa los parametros del Adapter y Recycler View***************
    public void seteoParametros(){
        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this)); //Se utiliza el LinearLayoutManager que viene por defecto
        adapter = new ListaLotesAdapter(listaLote); //se le ingresan todos los datos al adapter
        recyclerViewLote.setAdapter(adapter);  //Se ingresan los datos al Recyclerview utilizando un el adapter
    }

    @Override
    //*****************Funcion que maneja las funciones del menu por defecto************************
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Boton de la esquina superior izquierda que devuelve al menu principal
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(context,MainActivity.class); //Creamos la instancia del menu principal
            context.startActivity(intent); //Comenzamos la instancia
            finish(); //Se finaliza la instancia actual (VistaNotificacion)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //*****Funcion que maneja las funciones del menu agregadas en res/menu/menunotificacion.xml*****
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menunotificacion,menu);

        //items del submenu de la esquina superior derecha
        MenuItem rnr = menu.findItem(R.id.lotes_rnr); //Los lotes RNR son lotes que les quedan 2 dias o menos por caducar
        MenuItem mostrarTodo = menu.findItem(R.id.lotes_todos); //Este Boton se vuelve visible despues de la instancia de lotesRNR y muestra todos los lotes

        rnr.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                rnr();
                rnr.setVisible(false);
                mostrarTodo.setVisible(true);
                return false;
            }
        });

        mostrarTodo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mostrartodo();
                mostrarTodo.setVisible(false);
                rnr.setVisible(true);
                return false;
            }
        });

        return true;
    }

    //*********Funcion que entrega los lotes que estan en RNR (2 dias o menos por caducar)**********
    public void rnr(){
        Date now = new Date(); //Se utiliza la fecha y hora del dispositivo para obtener la fecha actual
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd"); //se crea un formato de fecha
        String vDateYMD = dateFormatYMD.format(now); //se utiliza el formato en la fecha actual del dispositivo

        Date nuevo=null;
        Date comparado=null;
        ArrayList<Lotes> listaRNR = new ArrayList<Lotes>();

        //Se recorre la lista de lotes para agregarlos en la lista de los lotes en RNR
        for (Lotes loteActual: listaLote) {
            try {
                nuevo = dateFormatYMD.parse(vDateYMD);
                comparado = loteActual.getLOTE_FECHA_CADUCIDAD();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000); //Metodo para obtener la diferencia entre la fecha de caducidad y la actual

            if(diff <= 2){ //Se agregan los lotes con diferencia menor o igual que 2
                listaRNR.add(loteActual);
            }
        }

        listaLote.clear(); //Limpiamos la lista anterior
        listaLote = (ArrayList<Lotes>) listaRNR.clone(); //Clonamos la lista RNR en la que teniamos antes (ListaLote);
        seteoParametros();  //Llamamos otra vez a la funcion de seteoParametros() para que cree un nuevo adapter y le agrege el contenido a una recyclerView
        adapter.notifyDataSetChanged(); //Funcion necesaria para que se actualize la vista con los nuevos datos
    }

    //********************Funcion que permite mostrar nuevamente todos los lotes********************
    public void mostrartodo(){
        l = new Lotes();
        listaLote = new ArrayList<Lotes>(l.mostrarTodoLotes()); //Se hace un llamado a la base de datos para comprobar si algun dato a cmabiado
        seteoParametros(); //Llamamos otra vez a la funcion de seteoParametros() para que cree un nuevo adapter y le agrege el contenido a una recyclerView
        adapter.notifyDataSetChanged(); //Funcion necesaria para que se actualize la vista con los nuevos datos
    }
}