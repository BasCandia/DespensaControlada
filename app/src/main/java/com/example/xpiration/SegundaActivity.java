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

    private Context context;
    EditText fecha;
    Spinner spinnerC;
    Connection con;
    Button crear;
    TextView nombre;
    Date now = new Date();
    DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
    String vDateYMD = dateFormatYMD.format(now);
    Date nuevo;
    Date comparado;
    TextView notiNaranja,notiRoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        context = this;

        nombre = findViewById(R.id.Nombre);
        fecha = findViewById(R.id.FechaCaducidad);
        notiNaranja = findViewById(R.id.notiNaranja);
        notiRoja = findViewById(R.id.notiRoja);


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
        listaCategorias();

        crear = findViewById(R.id.BtnCrear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tomar datos de las otras cosas y hacerlos query
                Toast.makeText(getApplicationContext(),String.valueOf(spinnerC.getSelectedItemPosition()),Toast.LENGTH_SHORT).show();
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


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                //final String selectedDate = day + " / " + (month+1) + " / " + year;
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

                final String selectedDate = year + mesMod + (month+1) + dayMod + day;

                fecha.setText(selectedDate);
            }
        });

        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void listaCategorias(){
        try{
            String query1 = "SELECT * FROM CATEGORIA";
            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);

            ArrayList<String> data = new ArrayList<String>();
            data.add("Seleccione una Categoria");
            while(rs.next()){
                String cat = rs.getString("CATEGORIA_NOMBRE");
                data.add(cat);
            }
            spinnerC = (Spinner) findViewById(R.id.Categorias);

            ArrayAdapter array = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
            spinnerC.setAdapter(array);



        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


}








