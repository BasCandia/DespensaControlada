package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import entidades.ListaLotesAdapter;
import entidades.Lotes;

public class SubmenuActivity extends AppCompatActivity {

    FloatingActionButton masLote;
    ImageView estadoLote;
    RecyclerView recyclerViewLote;
    private Context context;
    Lotes l;
    int id;
    ArrayList<Lotes> listaLote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu);

        context = this;
        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this));

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

        l = new Lotes();
        listaLote = new ArrayList<Lotes>();

        ListaLotesAdapter adapter = new ListaLotesAdapter(l.mostrarLotes(id));

        recyclerViewLote.setAdapter(adapter);

    }


}