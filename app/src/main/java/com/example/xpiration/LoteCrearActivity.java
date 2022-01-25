package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private String newStr = "";


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

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.isEmpty()) {
                    nombre.append(newStr);
                    newStr = "";
                } else if (!str.equals(newStr)) {
                    // Replace the regex as per requirement
                    newStr = str.replaceAll("[^A-Za-z0-9\\s]", "");
                    nombre.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        notiNaranja.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.isEmpty()) {
                    notiNaranja.append(newStr);
                    newStr = "";
                } else if (!str.equals(newStr)) {
                    // Replace the regex as per requirement
                    newStr = str.replaceAll("[^0-9]", "");
                    notiNaranja.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        notiRoja.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.isEmpty()) {
                    notiRoja.append(newStr);
                    newStr = "";
                } else if (!str.equals(newStr)) {
                    // Replace the regex as per requirement
                    newStr = str.replaceAll("[^0-9]", "");
                    notiRoja.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

//************* boton para enviar la insercion y pasar a ventana principal *************************
        crear = findViewById(R.id.LoteCrear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Agregar valores por defecto para las notificaciones
                String n,r;
                if(notiNaranja.getHint().equals("Por Defecto 7")){
                    n = "7";
                }else{
                    n = notiNaranja.getText().toString();
                }

                if(notiRoja.getHint().equals("Por Defecto 2")){
                    r = "2";
                }else{
                    r = notiRoja.getText().toString();
                }

                Lotes l = new Lotes();
                l.insertar(context,String.valueOf(id),nombre.getText().toString(),vDateYMD,fecha.getText().toString(),n,r);

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