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

        spinnerC = (Spinner) findViewById(R.id.Categorias);
        FillSpiner();
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

    public void FillSpiner(){

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            String query1 = "SELECT CATEGORIA_NOMBRE FROM CATEGORIA";
            PreparedStatement stmt = connect.prepareStatement(query1);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> data = new ArrayList<String>();
            while(rs.next()){
                String cat = rs.getString("CATEGORIA_NOMBRE");
                data.add(cat);
            }

            ArrayAdapter array = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
            spinnerC.setAdapter(array);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


}








