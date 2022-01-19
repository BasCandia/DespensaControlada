package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import entidades.Lotes;
import entidades.Productos;

public class LoteCrearActivity extends AppCompatActivity {

    private Context context; //instancia
//********************************* Elementos en pantalla ******************************************

    Connection con;
    EditText fecha;
    Button crear;
    TextView nombre;
    TextView notiNaranja,notiRoja;
    int id;


    Date now = new Date();
    DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
    String vDateYMD = dateFormatYMD.format(now);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lote_crear);
        context = this;
//****************************** Inicializacion de elementos ***************************************
        nombre = findViewById(R.id.Nombre);
        fecha = findViewById(R.id.FechaCaducidad);
        notiNaranja = findViewById(R.id.notiNaranja);
        notiRoja = findViewById(R.id.notiRoja);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            }else{
                id = extras.getInt("ID");
            }
        }else{
            id = (int) savedInstanceState.getSerializable("ID");
        }

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.FechaCaducidad:
                        showDatePickerDialog();

                        break;
                }

            }
        });

//************* boton para enviar la insercion y pasar a ventana principal *************************
        crear = findViewById(R.id.LoteCrear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lotes l = new Lotes();
                l.insertar(context,String.valueOf(id),nombre.getText().toString(),vDateYMD,fecha.getText().toString(),notiNaranja.getText().toString(),notiRoja.getText().toString());

            }
        });

    }

    //****** obtiene los datos seleccionados interactivamente para la fecha y los formatea *************
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String mesMod,dayMod;

                if(month < 9){
                    mesMod = "/0";
                }else{
                    mesMod = "/";
                }
                if(day < 10){
                    dayMod = "/0";
                }else{
                    dayMod = "/";
                }
                //month+1 es porque enero es un 0
                final String selectedDate = year + mesMod + (month+1) + dayMod + day;

                fecha.setText(selectedDate);
            }
        });

        newFragment.show(getFragmentManager(), "datePicker");
    }
}