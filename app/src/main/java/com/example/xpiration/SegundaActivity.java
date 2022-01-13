package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SegundaActivity extends AppCompatActivity {

    private Context context; //instancia
//********************************* Elementos en pantalla ******************************************
    EditText fecha;
    Spinner spinnerC;
    Connection con;
    Button crear;
    TextView nombre;
    TextView notiNaranja,notiRoja;
//**************** Elementos para seleccionar fecha de forma interactiva ***************************
    Date now = new Date();
    DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
    String vDateYMD = dateFormatYMD.format(now);
    Date nuevo;
    Date comparado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        context = this;
//****************************** Inicializacion de elementos ***************************************
        nombre = findViewById(R.id.Nombre);
        fecha = findViewById(R.id.FechaCaducidad);
        notiNaranja = findViewById(R.id.notiNaranja);
        notiRoja = findViewById(R.id.notiRoja);

//**************** Al hacer click en fecha desplega elemento interactivo ***************************
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

//****************** funcion para cargar en spinner datos de la tabla  *****************************
        listaCategorias();

//************* boton para enviar la insercion y pasar a ventana principal *************************
        crear = findViewById(R.id.BtnCrear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//******************** Validaciones para query de insercion ****************************************
                //Toast.makeText(getApplicationContext(),String.valueOf(spinnerC.getSelectedItemPosition()),Toast.LENGTH_SHORT).show();
               if(spinnerC.getSelectedItemPosition() == 0) {
                   Toast.makeText(getApplicationContext(),"Seleccione una Categoria",Toast.LENGTH_SHORT).show();
               }else {
                   //Toast.makeText(getApplicationContext(),fecha.getText().toString(),Toast.LENGTH_SHORT).show();
                       //Podria mejorarse la seguridad
                       if(fecha.getText().toString().isEmpty()){
                           Toast.makeText(getApplicationContext(),"Ingrese una fecha de vencimiento",Toast.LENGTH_SHORT).show();
                       }else {
                           try {
                               nuevo = dateFormatYMD.parse(vDateYMD);
                               comparado = dateFormatYMD.parse(fecha.getText().toString());
                           } catch (ParseException e) {
                               e.printStackTrace();
                           }
                           if (comparado.compareTo(nuevo) <= 0) {
                               Toast.makeText(getApplicationContext(), "El producto caduca hoy o ya ha caducado", Toast.LENGTH_SHORT).show();
                           } else {
                               if (nombre.getText().length() == 0) {
                                   Toast.makeText(getApplicationContext(), "Ingrese Nombre del producto", Toast.LENGTH_SHORT).show();
                               } else {
                                   if((notiNaranja.getText().toString().isEmpty() == true) || (notiNaranja.getText().toString().compareToIgnoreCase("0") == 0)){
                                       Toast.makeText(getApplicationContext(), "Ingrese dias para notificacion Preventiva", Toast.LENGTH_SHORT).show();
                                   }else{
                                       if((notiRoja.getText().toString().isEmpty() == true) || (notiRoja.getText().toString().compareToIgnoreCase("0") == 0)){
                                           Toast.makeText(getApplicationContext(), "Ingrese dias para notificacion Critica", Toast.LENGTH_SHORT).show();
                                       }else{
//************** inicializacion de conexion, construccion y envio de query *************************
                                           try {
                                               String query = "INSERT PRODUCTO VALUES (" + spinnerC.getSelectedItemPosition() + ",'" + nombre.getText() + "',CONVERT(varchar,GETDATE(),111),'" + fecha.getText().toString() + "',"+notiNaranja.getText()+','+notiRoja.getText()+",null)";
                                               ConnectionHelper conexion = new ConnectionHelper();
                                               con = conexion.connectionclass();

                                               PreparedStatement pst = con.prepareStatement(query);
                                               // pst.setInt(1,spinnerC.getSelectedItemPosition());
                                               // pst.setString(2,nombre.getText().toString());
                                               // pst.setString(3,fecha.getText().toString());

                                               pst.executeUpdate();

                                               Toast.makeText(getApplicationContext(), "Producto agregado correctamente", Toast.LENGTH_SHORT).show();

                                               Intent intent = new Intent(SegundaActivity.this, MainActivity.class);
                                               context.startActivity(intent);
                                           } catch (SQLException e) {
                                               Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }

                               }
                           }
                       }
               }
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

//****************** funcion para cargar en spinner datos de la tabla  *****************************
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


}








