package com.example.xpiration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import entidades.ListaLotesAdapter;
import entidades.Lotes;

public class SubmenuActivity extends AppCompatActivity {

//********************************* Elementos en pantalla **************************************
    FloatingActionButton masLote;
    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    int id;
    ArrayList<Lotes> listaLote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializacion de parametros del menu (Nombre superior del menu)
        setContentView(R.layout.activity_submenu);
        getSupportActionBar().setTitle("Lista de Lotes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar Boton para volver atras
    //****************************** Inicializacion de elementos ***************************************
        context = this;
        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this));
        masLote = findViewById(R.id.plusLote);
    //**** Se recibe desde la view anterior el id del producto seleccionado para mostrar sus lotes *****
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            }else{
                id = extras.getInt("ID");
            }
        }else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        //Se Inicializa el RecyclerView con los datos de la lista de lostes en el adapter
        l = new Lotes();
        listaLote = new ArrayList<Lotes>();
        ListaLotesAdapter adapter = new ListaLotesAdapter(l.mostrarLotes(id));
        recyclerViewLote.setAdapter(adapter);

    //****************************** Boton para agregar Lotes ******************************************
        masLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "No existen lotes con id = " + id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, LoteCrearActivity.class);
                intent.putExtra("ID",id);
                startActivity(intent);
            }
        });


    }
    //******************Funcion que maneja las funciones del menu por defecto***********************
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Boton de la esquina superior izquierda que devuelve al menu principal
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}