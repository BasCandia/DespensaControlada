package com.example.xpiration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
// - Esta clase se genero en el proceso de Desarrollo pero por diseño se opto por no ser utilizada -
// Clase para editar el contenido de los productos, los elementos mostrados pero bloqueados en la clase
// VerActivity son desbloqueados en esta y permiten ser modificados y updateados en la BD

//********************************* Elementos en pantalla ******************************************
    EditText fecha;
    Spinner spinnerC;
    Button guardar;
    TextView nombre;
    TextView notiNaranja,notiRoja;
    ImageView editar;
    ImageView borrar;
    int id;
    Productos producto;
    Connection con;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        context = this;

//******************* Declaracion y seteo de Elementos *********************************************
        nombre = findViewById(R.id.Nombre);
        fecha = findViewById(R.id.FechaCaducidad);
        notiNaranja = findViewById(R.id.notiNaranja);
        notiRoja = findViewById(R.id.notiRoja);
        guardar = findViewById(R.id.BtnGuardar);
        spinnerC = findViewById(R.id.Categorias);
        editar = findViewById(R.id.IconEditar);
        borrar = findViewById(R.id.iconBorrar);
        editar.setVisibility(View.INVISIBLE);
        borrar.setVisibility(View.INVISIBLE);
        listaCategorias();
//En la vista anterior se envia un parametro al generar la vista, las siguientes lineas recuperan este dato
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
//Recupero los datos del producto y se muestran en el formulario para que el usuario los modifique
        producto = new Productos().verProducto(id);
        if(producto != null){
            nombre.setText(producto.getPRODUCTO_NOMBRE());
           // String modificado = producto.getPRODUCTO_FECHA_CADUCIDAD().toString().replace("-","/");
           // fecha.setText(modificado);
           // notiNaranja.setText(producto.getPRODUCTO_NOTIFICACION_NARANJA()+"");
           // notiRoja.setText(producto.getPRODUCTO_NOTIFICACION_ROJA()+"");
            spinnerC.setSelection(producto.getCATEGORIA_ID());
        }

//******************* Elementos para seleccionar fecha de forma interactiva ************************
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

//***************************** Boton para Editar **************************************************
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date(); //Se obtiene la fecha del dispositivo y se le da el formato decidido
                DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
                String vDateYMD = dateFormatYMD.format(now);
                //Si los datos se ingresaron como se esperan se edita, caso contrario se envia mensaje a usuario
                boolean resultado = producto.Editar(context,id,nombre.getText().toString(),vDateYMD,fecha.getText().toString(),spinnerC.getSelectedItemPosition(),notiNaranja.getText().toString(),notiRoja.getText().toString());
                if(resultado){
                    verRegistro();
                }else{
                    System.out.println("Error tratando de editar un registro");
                }
            }
        });
    }

// Si fue editado correctamente lo devolvemos a la visual del producto
    private void verRegistro(){
        Intent intent = new Intent(this,VerActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
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
