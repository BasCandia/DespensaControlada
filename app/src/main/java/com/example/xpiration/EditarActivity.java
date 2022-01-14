package com.example.xpiration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entidades.Productos;

public class EditarActivity extends AppCompatActivity {

    //********************************* Elementos en pantalla ******************************************
    EditText fecha;
    Spinner spinnerC;
    Button guardar;
    TextView nombre;
    TextView notiNaranja,notiRoja;
    int id;
    Productos producto;
    Connection con;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        context = this;


        nombre = findViewById(R.id.Nombre);
        fecha = findViewById(R.id.FechaCaducidad);
        notiNaranja = findViewById(R.id.notiNaranja);
        notiRoja = findViewById(R.id.notiRoja);
        guardar = findViewById(R.id.BtnGuardar);
        spinnerC = findViewById(R.id.Categorias);
        listaCategorias();

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

        producto = new Productos().verProducto(id);

        if(producto != null){
            nombre.setText(producto.getPRODUCTO_NOMBRE());
            String modificado = producto.getPRODUCTO_FECHA_CADUCIDAD().toString().replace("-","/");
            fecha.setText(modificado);
            notiNaranja.setText(producto.getPRODUCTO_NOTIFICACION_NARANJA()+"");
            notiRoja.setText(producto.getPRODUCTO_NOTIFICACION_ROJA()+"");
            spinnerC.setSelection(producto.getCATEGORIA_ID());

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


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();
                DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
                String vDateYMD = dateFormatYMD.format(now);
                //Toast.makeText(context,vDateYMD,Toast.LENGTH_SHORT).show();
                //Toast.makeText(context,fecha.getText().toString(),Toast.LENGTH_SHORT).show();
                boolean resultado = producto.Editar(context,id,nombre.getText().toString(),vDateYMD,fecha.getText().toString(),spinnerC.getSelectedItemPosition(),notiNaranja.getText().toString(),notiRoja.getText().toString());
                if(resultado){
                    verRegistro();
                }else{
                    System.out.println("Error tratando de editar un registro");
                }
            }
        });



    }


    private void verRegistro(){
        Intent intent = new Intent(this,VerActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }

    public void listaCategorias(){
        try{
            //prepara query
            String query1 = "SELECT * FROM CATEGORIA";
            ConnectionHelper conexion = new ConnectionHelper();
            //realiza conexion
            con = conexion.connectionclass();
            Statement st = con.createStatement();
            //obtiene respuesta
            ResultSet rs = st.executeQuery(query1);
            ArrayList<String> data = new ArrayList<String>();
            data.add("Seleccione una Categoria");
            while(rs.next()){
                String cat = rs.getString("CATEGORIA_NOMBRE");
                //entrega datos a array data
                data.add(cat);
            }
            spinnerC = (Spinner) findViewById(R.id.Categorias);

            ArrayAdapter array = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
            spinnerC.setAdapter(array);
            //entrega datos de array a spinner
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

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
