package com.example.xpiration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entidades.ListaProductosAdapter;
import entidades.Productos;

public class MainActivity extends AppCompatActivity {

//********************************* Elementos en pantalla ******************************************
    FloatingActionButton mas;
    ImageView estado;
    RecyclerView recyclerView;
    private Context context;
    Productos p;
    ArrayList<Productos> listaMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Lista de productos");



        setContentView(R.layout.activity_main);
//****************************** Inicializacion de elementos ***************************************
        context = this;
        recyclerView = findViewById(R.id.ListaProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        p = new Productos();
        listaMain = new ArrayList<Productos>();
        ListaProductosAdapter adapter = new ListaProductosAdapter(p.mostrarProductos());
        recyclerView.setAdapter(adapter);

//************** Se crea los elementos para activar notificacion a cierto tiempo *******************
        createNotificationChannel();
        Intent intent = new Intent(MainActivity.this,ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Tiempo en cuando se mandara la notificacion en milisegundos
        long Activar = System.currentTimeMillis();
        long dies = 1000 * 10;


        alarmManager.set(AlarmManager.RTC_WAKEUP,Activar + dies,pendingIntent);


//****************************** Boton para agregar productos **************************************
        mas = findViewById(R.id.plus);
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SegundaActivity.class);
                context.startActivity(intent);
            }
        });

    }

//*** Desde una cierta version de android es requerido crear un canal para enviar notificaciones****
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Xpiration";
            String description = "Notificacion de Xpiration";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ChannelXpiration",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu,menu);
        return true;
    }
}