package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entidades.Productos;


public class SegundaActivity extends AppCompatActivity {

    private Context context; //instancia
//********************************* Elementos en pantalla ******************************************
    Spinner spinnerC;
    Connection con;
    Button crear;
    TextView nombre;


    Date now = new Date(); //Se utiliza la fecha y hora del dispositivo para obtener la fecha actual
    DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");  //se crea un formato de fecha
    String vDateYMD = dateFormatYMD.format(now); //se utiliza el formato en la fecha actual del dispositivo
    private String newStr = "";

//**************** Elementos para seleccionar fecha de forma interactiva ***************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        context = this;
        getSupportActionBar().setTitle("Crear producto");
//****************************** Inicializacion de elementos ***************************************
        nombre = findViewById(R.id.Nombre);

//****************** funcion para cargar en spinner datos de la tabla  *****************************
        listaCategorias();

//************* boton para enviar la insercion y pasar a ventana principal *************************
        crear = findViewById(R.id.BtnGuardar);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Productos p = new Productos();
                p.insertar(context,nombre.getText().toString(),spinnerC.getSelectedItemPosition(),vDateYMD);
            }
        });
//*********** Validacion para que no se pueda pegar texto, temas de seguridad **********************
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








