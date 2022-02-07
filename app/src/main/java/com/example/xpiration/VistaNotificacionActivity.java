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
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import entidades.ListaLotesAdapter;
import entidades.Lotes;
import entidades.Productos;

public class VistaNotificacionActivity extends AppCompatActivity {

    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    ArrayList<Lotes> listaLote;
    ListaLotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_notificacion);
        getSupportActionBar().setTitle("Lotes cerca de caducar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//****************************** Inicializacion de elementos ***************************************
        context = this;
        l = new Lotes();
        listaLote = new ArrayList<Lotes>(l.mostrarTodoLotes());

        seteoParametros();

    }

    public void seteoParametros(){
        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this));
//**** Se recibe desde la view anterior el id del producto seleccionado para mostrar sus lotes *****


        adapter = new ListaLotesAdapter(listaLote);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menunotificacion,menu);

        MenuItem rnr = menu.findItem(R.id.lotes_rnr);
        MenuItem mostrarTodo = menu.findItem(R.id.lotes_todos);

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

    public void rnr(){
        Date now = new Date();
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        String vDateYMD = dateFormatYMD.format(now);

        Date nuevo=null;
        Date comparado=null;

        ArrayList<Lotes> listaRNR = new ArrayList<Lotes>();
//************************* Se obtiene la cantidad de dias para que expire *************************

        for (Lotes loteActual: listaLote) {
            try {
                nuevo = dateFormatYMD.parse(vDateYMD);
                comparado = loteActual.getLOTE_FECHA_CADUCIDAD();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);

            if(diff <= 2){
                listaRNR.add(loteActual);
            }
        }

        listaLote.clear();

        listaLote = (ArrayList<Lotes>) listaRNR.clone();

        seteoParametros();
        adapter.notifyDataSetChanged();
    }

    public void mostrartodo(){
        l = new Lotes();
        listaLote = new ArrayList<Lotes>(l.mostrarTodoLotes());
        seteoParametros();
        adapter.notifyDataSetChanged();
    }
}