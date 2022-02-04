package com.example.xpiration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import entidades.ListaLotesAdapter;
import entidades.Lotes;

public class VistaMermaActivity extends AppCompatActivity {

    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    ArrayList<Lotes> listaLote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_merma);

        getSupportActionBar().setTitle("Merma de productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //****************************** Inicializacion de elementos ***************************************
        context = this;
        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this));


        l = new Lotes();
        listaLote = new ArrayList<Lotes>();


        Toast.makeText(context, " Cantidad de lotes " + l.MermaLotes().size()  , Toast.LENGTH_SHORT).show();



        ListaLotesAdapter adapter = new ListaLotesAdapter(l.MermaLotes());

        recyclerViewLote.setAdapter(adapter);


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}