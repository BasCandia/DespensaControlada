package com.example.xpiration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entidades.Productos;

public class VerActivity extends AppCompatActivity {

    //********************************* Elementos en pantalla ******************************************
    EditText fecha;
    Spinner spinnerC;
    Button guardar;
    TextView nombre;
    TextView notiNaranja,notiRoja;
    ImageView editar;
    ImageView borrar;
    Context context;

    int id;
    Productos producto;
    Connection con;

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
        borrar = findViewById(R.id.iconBorrar);
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
            nombre.setInputType(InputType.TYPE_NULL);
            String modificado = producto.getPRODUCTO_FECHA_CADUCIDAD().toString().replace("-","/");
            fecha.setText(modificado);
            fecha.setInputType(InputType.TYPE_NULL);
            notiNaranja.setText(producto.getPRODUCTO_NOTIFICACION_NARANJA()+"");
            notiNaranja.setInputType(InputType.TYPE_NULL);
            notiRoja.setText(producto.getPRODUCTO_NOTIFICACION_ROJA()+"");
            notiRoja.setInputType(InputType.TYPE_NULL);
            spinnerC.setSelection(producto.getCATEGORIA_ID());
            spinnerC.setEnabled(false);

            guardar.setVisibility(View.INVISIBLE);


        }

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);
                builder.setMessage("¿Desea eliminar este producto?");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(producto.Borrar(id)){
                            Intent intent = new Intent(VerActivity.this,MainActivity.class);
                            intent.putExtra("ID",id);
                            startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


        editar=findViewById(R.id.IconEditar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerActivity.this,EditarActivity.class);
                intent.putExtra("ID",id);
                startActivity(intent);
            }
        });



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
}