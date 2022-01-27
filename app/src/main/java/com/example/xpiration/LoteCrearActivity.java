package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    TextInputLayout notiNaranjainput,notiRojainput;
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
        nombre = findViewById(R.id.NombreEditLote);
        fecha = findViewById(R.id.FechaCaducidadEdit);
        notiNaranja = findViewById(R.id.notiNaranjaEdit);
        notiRoja = findViewById(R.id.notiRojaEdit);
        notiNaranjainput = findViewById(R.id.notiNaranja);
        notiRojainput = findViewById(R.id.notiRoja);


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


        try {
            String query1 = "SELECT PRODUCTO_NOMBRE FROM PRODUCTO WHERE PRODUCTO_ID ="+id;
            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = null;

            st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            rs.next();
            getSupportActionBar().setTitle("Agregar lote de "+rs.getString("PRODUCTO_NOMBRE"));
        }catch (SQLException e){
            System.out.println("Error in sql statment");
        }

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.FechaCaducidadEdit:
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
                //Agregar valores por defecto para las notificaciones
                String n,r;
                if(notiNaranja.getText().toString().isEmpty()){
                    n = "7";
                }else{
                    n = notiNaranja.getText().toString();
                }

                if(notiRoja.getText().toString().isEmpty()){
                    r = "2";
                }else{
                    r = notiRoja.getText().toString();
                }

                Lotes l = new Lotes();
                l.insertar(context,String.valueOf(id),nombre.getText().toString(),vDateYMD,fecha.getText().toString(),n,r);

            }
        });

//*********** Validacion para que no se pueda pegar texto, temas de seguridad **********************
        notiNaranja.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {return true;}
                });
        notiNaranja.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}
            public void onDestroyActionMode(ActionMode mode) {}
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {return false;}
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {return false;}
        });

        notiRoja.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {return true;}
                });
        notiRoja.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}
            public void onDestroyActionMode(ActionMode mode) {}
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {return false;}
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {return false;}
        });

        fecha.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {return true;}
                });
        fecha.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}
            public void onDestroyActionMode(ActionMode mode) {}
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {return false;}
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {return false;}
        });

        nombre.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {return true;}
                });
        nombre.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}
            public void onDestroyActionMode(ActionMode mode) {}
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {return false;}
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {return false;}
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