package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
        setContentView(R.layout.activity_submenu);
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
        //esto se puede cambiar
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
                intent.putExtra("Nombre",adapter.nombreproducto);
                startActivity(intent);
            }
        });


    }


}