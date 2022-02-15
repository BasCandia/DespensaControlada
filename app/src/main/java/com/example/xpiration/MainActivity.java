package com.example.xpiration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;



import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
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
// Clase para la vista principal del Software, en esta se mostraran una lista de productos, distintas opciones de ordenamiento y se podran seleccionar opciones para pasar
// a otras vistas.

//********************************* Elementos en pantalla ******************************************
    FloatingActionButton mas;
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

        buildRecyclerView();// se realiza la logica para mostrar los productos

//************** Se crean los elementos para activar notificacion  *********************************
        createNotificationChannel(); //Desde una cierta version de android se requiere un canal de notificaciones para mostrarlas
        Intent intent = new Intent(MainActivity.this,ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Cuando ingresa a pantalla principal,se setea una notificacion 9:00 AM del dia siguiente para que se revisen las cosas por expirar

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );
        //InexactRepeating enviara una notificacion alrededor de la hora especificada, intentara juntar varias notificaciones de distintas aplicaciones
        // para mostrarlas todas juntas y ser mas eficiente en terminos de energia

//****************************** Boton para agregar productos **************************************
        mas = findViewById(R.id.plus);
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SegundaActivity.class);
                context.startActivity(intent);
            }
        });
        //Cuando se abre app por primera vez se piden permisos para guardar, necesario para generar el reporte en excel
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

//RecyclerView es la lista donde se generan las visuales de los productos y se les da funcionalidad
    public void buildRecyclerView(){
        //****************************** Inicializacion de elementos ***************************************
        recyclerView = findViewById(R.id.ListaProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        p = new Productos();
        listaMain = new ArrayList<Productos>(p.mostrarProductos());
        adapter = new ListaProductosAdapter(listaMain);
        recyclerView.setAdapter(adapter);

        //funcionalidad para mostrar ventana de confirmacion al intentar eliminar un producto
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
//Al eliminar un producto se debe eliminar de BD y visualmente quitar de la vista, la siguiente funcion se encarga de lo visual
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
// Formas de ordernar la lista de productos
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
// Ordena en base a la fecha de creacion
    public void ordenCreacion(){
        Collections.sort(listaMain, new Comparator<Productos>() {
            @Override
            public int compare(Productos p1, Productos p2) {
                return new Integer(p2.getPRODUCTO_ID()).compareTo(new Integer(p1.getPRODUCTO_ID()));
            }
        });
    }

// Ordena en base al ID de la categoria del producto
    public void ordenCategoria(){
        Collections.sort(listaMain, new Comparator<Productos>() {
            @Override
            public int compare(Productos p1, Productos p2) {
                return new Integer(p1.getCATEGORIA_ID()).compareTo(new Integer(p2.getCATEGORIA_ID()));
            }
        });
    }

// Ordena en base al Nombre de los productos
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
        MenuItem reporte = menu.findItem(R.id.generar_reporte);
        MenuItem lotesMerma = menu.findItem(R.id.lotes_merma);

        lotesCaducar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent caducar = new Intent(context,VistaNotificacionActivity.class);
                context.startActivity(caducar);
                return false;
            }
        });

        lotesMerma.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent merma = new Intent(context,VistaMermaActivity.class);
                context.startActivity(merma);
                return false;
            }
        });

        reporte.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ExcelExporter exporter = new ExcelExporter();
                exporter.buttonCreateExcel(context);
                sendReport(exporter.getFileName());

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
// Funcion para prerrellenar el correo con el que se enviara el reporte
    public void sendReport(String fileName){
        Date now = new Date();
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        String vDateYMD = dateFormatYMD.format(now);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String mimeTypeForXLSFile = mime.getMimeTypeFromExtension(".xls");

        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName ));

        try {
            Intent intentEmail = new Intent(Intent.ACTION_SEND);
            intentEmail.setType("message/rfc822");
            intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"sup.rnr.merma@gmail.com"} );
            intentEmail.putExtra(Intent.EXTRA_SUBJECT,"Reporte Merma " + vDateYMD.replaceAll("/","_"));
            intentEmail.putExtra(Intent.EXTRA_TEXT,"Adjunte Reporte de nombre '" +  fileName + "'");
            intentEmail.putExtra(Intent.EXTRA_STREAM, uri.getPath());

            Toast.makeText(context, "Se a generado el reporte. Ruta: " +  uri.getPath(), Toast.LENGTH_SHORT).show();
            if(  intentEmail.resolveActivity(getPackageManager()) != null){
                startActivity(intentEmail);
            }else{
                Toast.makeText(context, "No existe una aplicacion para enviar correos", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            System.out.println("is exception raises during sending mail"+e);
        }
    }

}