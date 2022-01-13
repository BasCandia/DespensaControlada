package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entidades.ListaProductosAdapter;
import entidades.Productos;

public class MainActivity extends AppCompatActivity {


    ImageView casa;
    ImageView mas;
    ImageView menu;
    ImageView estado;

    RecyclerView recyclerView;
    private Context context;
    Productos p;
    ArrayList<Productos> listaMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.ListaProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        p = new Productos();
        listaMain = new ArrayList<Productos>();

        ListaProductosAdapter adapter = new ListaProductosAdapter(p.mostrarProductos());
        recyclerView.setAdapter(adapter);


        context = this;

        casa = findViewById(R.id.Casa);
        casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SegundaActivity.class);
                context.startActivity(intent);
            }
        });

        mas = findViewById(R.id.plus);
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SegundaActivity.class);
                context.startActivity(intent);
            }
        });

        menu = findViewById(R.id.Menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SegundaActivity.class);
                context.startActivity(intent);
            }
        });







    }


}