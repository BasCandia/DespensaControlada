package com.example.xpiration;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import entidades.ListaProductosAdapter;
import entidades.Lotes;
import entidades.Productos;

public class Submenu extends AppCompatActivity {

    FloatingActionButton masLote;
    ImageView estadoLote;
    RecyclerView recyclerViewLote;
    private Context contextLote;
    Lotes l;
    int id;
    ArrayList<Lotes> listaLote;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        listaLote = l.mostrarLotes(id);

        recyclerViewLote = findViewById(R.id.ListaLote);
        recyclerViewLote.setLayoutManager(new LinearLayoutManager(this));


        //p = new Productos();
        //listaMain = new ArrayList<Productos>();
        //ListaProductosAdapter adapter = new ListaProductosAdapter(p.mostrarProductos());
        // recyclerView.setAdapter(adapter);
    }
}
