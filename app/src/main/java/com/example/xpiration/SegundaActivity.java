package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;


public class SegundaActivity extends AppCompatActivity {

    EditText fecha;
    Spinner spinnerC;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);



        fecha = findViewById(R.id.FechaCaducidad);

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

    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                fecha.setText(selectedDate);
            }
        });

        newFragment.show(getFragmentManager(), "datePicker");
    }

    public Connection conexionDB(){

        Connection connect=null;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connect = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.13:1433;databaseName=xpiration;user=sa;password=asddsa123;");
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();

        }

        return connect;

    }

    public void listaCategorias(){
        try{
            String query1 = "SELECT * FROM CATEGORIA";
            Statement st = conexionDB().createStatement();
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








