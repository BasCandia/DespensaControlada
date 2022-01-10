package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Connection connect;
    String ConectioResult="";
    ArrayList<String> CategoriasSpiner = new ArrayList<String>();
    Spinner spinnerC;

    ImageView casa;
    ImageView mas;
    ImageView menu;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        spinnerC = (Spinner) findViewById(R.id.Categorias);
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!=null){


            }else{
                ConectioResult="Revisa la Conexion";
            }
        }catch (Exception ex){
            Log.e("Error", ex.getMessage());
        }






    }

    public void GetTextFromSQL(View v){
        //Categorias


    }
}