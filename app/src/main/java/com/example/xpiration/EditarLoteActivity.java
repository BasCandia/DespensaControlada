package com.example.xpiration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import entidades.Lotes;


public class EditarLoteActivity extends AppCompatActivity {
    //********************************* Elementos en pantalla ******************************************
    EditText fecha;
    TextInputLayout fechainput;
    Button guardar;
    TextView nombre;
    TextView notiNaranja,notiRoja;
    ImageView editar;
    ImageView borrar;
    int id;
    Lotes lotes;
    Connection con;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_lote);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        context = this;

//******************* Declaracion y seteo de Elementos ************************
        nombre = findViewById(R.id.NombreEditLote);
        nombre.setFocusable(true);
        fechainput = findViewById(R.id.FechaCaducidadInput);
        fecha = findViewById(R.id.FechaCaducidadEdit);
        notiNaranja = findViewById(R.id.notiNaranjaEdit);
        notiNaranja.setFocusable(true);
        notiRoja = findViewById(R.id.notiRojaEdit);
        notiRoja.setFocusable(true);
        guardar = findViewById(R.id.BtnGuardar);
        editar = findViewById(R.id.IconEditar);
        borrar = findViewById(R.id.iconBorrar);
        editar.setVisibility(View.INVISIBLE);
        borrar.setVisibility(View.INVISIBLE);


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

        lotes = new Lotes().verLote(id);

        if(lotes != null){
            nombre.setText(lotes.getLOTE_NOMBRE());
            String modificado = lotes.getLOTE_FECHA_CADUCIDAD().toString().replace("-","/");
            fecha.setText(modificado);
            notiNaranja.setText(lotes.getLOTE_NOTIFICACION_NARANJA()+"");
            notiRoja.setText(lotes.getLOTE_NOTIFICACION_ROJA()+"");
            getSupportActionBar().setTitle("Lote de "+lotes.getPRODUCTO_NOMBRE());
        }
//******************* Elementos para seleccionar fecha de forma interactiva ************************


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





//***************************** Boton para Editar **************************************************
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();
                DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
                String vDateYMD = dateFormatYMD.format(now);
                //Toast.makeText(context,vDateYMD,Toast.LENGTH_SHORT).show();
                //Toast.makeText(context,fecha.getText().toString(),Toast.LENGTH_SHORT).show();
                boolean resultado = Lotes.Editar(context,id,nombre.getText().toString(),vDateYMD,fecha.getText().toString(),notiNaranja.getText().toString(),notiRoja.getText().toString());
                if(resultado){
                    verRegistro();
                }else{
                    System.out.println("Error tratando de editar un registro");
                }
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


    private void verRegistro(){
        Intent intent = new Intent(this,VerLoteActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }



    //**************** Elementos para seleccionar fecha de forma interactiva ***************************
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
