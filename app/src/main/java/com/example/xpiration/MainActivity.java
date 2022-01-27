package com.example.xpiration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
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
import java.util.Collections;
import java.util.Comparator;
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
    ListaProductosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Lista de productos");

        setContentView(R.layout.activity_main);
        context = this;

        buildRecyclerView();

//************** Se crea los elementos para activar notificacion a cierto tiempo *******************
        createNotificationChannel();
        Intent intent = new Intent(MainActivity.this,ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Cuando se abre por primera vez la aplicacion, seteo una notificacion diaria a las 9:00 del dia siguiente para que se revisen las cosas por expirar

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DATE, 1);

        alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );


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

    public void buildRecyclerView(){
        //****************************** Inicializacion de elementos ***************************************

        recyclerView = findViewById(R.id.ListaProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        p = new Productos();
        listaMain = new ArrayList<Productos>(p.mostrarProductos());
        adapter = new ListaProductosAdapter(listaMain);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListaProductosAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Borrar producto");
                alert.setMessage("¿Estas seguro de que quieres borrar el producto " + listaMain.get(position).getPRODUCTO_NOMBRE() +"? Esto borrara todos los lotes relacionados con este producto");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        listaMain.get(position).Borrar(listaMain.get(position).getPRODUCTO_ID());
                        removeItem(position);
                        adapter.updateArray(listaMain);
                        //Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        //context.startActivity(intent);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();




            }
        });

    }

    public void removeItem(int position){
        listaMain.remove(position);
        adapter.notifyItemRemoved(position);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.OrdenCreacion:
                ordenCreacion();
                break;

            case R.id.OrdenCategoria:
                ordenCategoria();
                break;

            case R.id.OrdenAlfabetico:
                ordenAlfabetico();
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    public void ordenCreacion(){
        Collections.sort(listaMain, new Comparator<Productos>() {
            @Override
            public int compare(Productos p1, Productos p2) {
                return new Integer(p2.getPRODUCTO_ID()).compareTo(new Integer(p1.getPRODUCTO_ID()));
            }
        });
    }

    public void ordenCategoria(){
        Collections.sort(listaMain, new Comparator<Productos>() {
            @Override
            public int compare(Productos p1, Productos p2) {
                return new Integer(p1.getCATEGORIA_ID()).compareTo(new Integer(p2.getCATEGORIA_ID()));
            }
        });
    }

    public void ordenAlfabetico(){
        Collections.sort(listaMain, new Comparator<Productos>() {
            @Override
            public int compare(Productos p1, Productos p2) {
                return new String(p1.getPRODUCTO_NOMBRE()).compareToIgnoreCase(new String(p2.getPRODUCTO_NOMBRE()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem lotesCaducar= menu.findItem(R.id.lotes_caducar);

        lotesCaducar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent caducar = new Intent(context,VistaNotificacionActivity.class);
                context.startActivity(caducar);
                return false;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}