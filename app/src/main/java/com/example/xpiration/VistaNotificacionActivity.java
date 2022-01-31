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

public class VistaNotificacionActivity extends AppCompatActivity {

    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    ArrayList<Lotes> listaLote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_notificacion);
        getSupportActionBar().setTitle("Lotes cerca de caducar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//****************************** Inicializacion de elementos ***************************************
        context = this;
        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this));
//**** Se recibe desde la view anterior el id del producto seleccionado para mostrar sus lotes *****

        //esto se puede cambiar
        l = new Lotes();
        listaLote = new ArrayList<Lotes>();

        ListaLotesAdapter adapter = new ListaLotesAdapter(l.mostrarTodoLotes());

        recyclerViewLote.setAdapter(adapter);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}