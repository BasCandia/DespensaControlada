package entidades;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.xpiration.ConnectionHelper;
import com.example.xpiration.MainActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Lotes {
    private int LOTE_ID;
    private int PRODUCTO_ID;
    private String LOTE_NOMBRE;
    private Date LOTE_FECHA_INGRESO;
    private Date LOTE_FECHA_CADUCIDAD;
    private int LOTE_NOTIFICACION_NARANJA; //preventiva
    private int LOTE_NOTIFICACION_ROJA; //critica

    Connection con;

    public int getLOTE_ID() {
        return LOTE_ID;
    }

    public void setLOTE_ID(int LOTE_ID) {
        this.LOTE_ID = LOTE_ID;
    }

    public int getPRODUCTO_ID() {
        return PRODUCTO_ID;
    }

    public void setPRODUCTO_ID(int PRODUCTO_ID) {
        this.PRODUCTO_ID = PRODUCTO_ID;
    }

    public String getLOTE_NOMBRE() {
        return LOTE_NOMBRE;
    }

    public void setLOTE_NOMBRE(String LOTE_NOMBRE) {
        this.LOTE_NOMBRE = LOTE_NOMBRE;
    }

    public Date getLOTE_FECHA_INGRESO() {
        return LOTE_FECHA_INGRESO;
    }

    public void setLOTE_FECHA_INGRESO(Date LOTE_FECHA_INGRESO) {
        this.LOTE_FECHA_INGRESO = LOTE_FECHA_INGRESO;
    }

    public Date getLOTE_FECHA_CADUCIDAD() {
        return LOTE_FECHA_CADUCIDAD;
    }

    public void setLOTE_FECHA_CADUCIDAD(Date LOTE_FECHA_CADUCIDAD) {
        this.LOTE_FECHA_CADUCIDAD = LOTE_FECHA_CADUCIDAD;
    }

    public int getLOTE_NOTIFICACION_NARANJA() {
        return LOTE_NOTIFICACION_NARANJA;
    }

    public void setLOTE_NOTIFICACION_NARANJA(int LOTE_NOTIFICACION_NARANJA) {
        this.LOTE_NOTIFICACION_NARANJA = LOTE_NOTIFICACION_NARANJA;
    }

    public int getLOTE_NOTIFICACION_ROJA() {
        return LOTE_NOTIFICACION_ROJA;
    }

    public void setLOTE_NOTIFICACION_ROJA(int LOTE_NOTIFICACION_ROJA) {
        this.LOTE_NOTIFICACION_ROJA = LOTE_NOTIFICACION_ROJA;
    }

    public ArrayList<Lotes> mostrarLotes(int id){

        ArrayList<Lotes> listaLotes = new ArrayList<>();
        try {
            Lotes l;
            String query1 = "SELECT * FROM LOTE WHERE PRODUCTO_ID = "+ id +";";


            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = null;

            st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);


            while (rs.next()) {
                l = new Lotes();
                l.setPRODUCTO_ID(rs.getInt("PRODUCTO_ID"));
                l.setLOTE_NOMBRE(rs.getString("LOTE_NOMBRE"));
                l.setLOTE_FECHA_INGRESO(rs.getDate("LOTE_FECHA_INGRESO"));
                l.setLOTE_FECHA_CADUCIDAD(rs.getDate("LOTE_FECHA_CADUCIDAD"));
                l.setLOTE_NOTIFICACION_NARANJA(rs.getInt("LOTE_NOTIFICACION_NARANJA"));
                l.setLOTE_NOTIFICACION_ROJA(rs.getInt("LOTE_NOTIFICACION_ROJA"));
                listaLotes.add(l);

            }
        }catch (SQLException e){
            System.out.println("Error in sql statment");
        }
        return listaLotes;
    }

    public void insertar (Context context,String idproducto, String nombre, String fechaIngreso, String fechaCaducidad, String notiNaranja, String notiRoja){
//******************** Validaciones para query de insercion ****************************************
            DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
            Date nuevo = null;
            Date comparado = null;

            if(fechaCaducidad.isEmpty()){
                Toast.makeText(context,"Ingrese una fecha de vencimiento",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    nuevo = dateFormatYMD.parse(fechaIngreso);
                    comparado = dateFormatYMD.parse(fechaCaducidad);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (comparado.compareTo(nuevo) <= 0) {
                    Toast.makeText(context, "El producto caduca hoy o ya ha caducado", Toast.LENGTH_SHORT).show();
                } else {
                    if (nombre.length() == 0) {
                        Toast.makeText(context, "Ingrese Nombre del producto", Toast.LENGTH_SHORT).show();
                    } else {
                        if((notiNaranja.isEmpty()) || (notiNaranja.compareToIgnoreCase("0") == 0)){
                            Toast.makeText(context, "Ingrese dias para notificacion Preventiva", Toast.LENGTH_SHORT).show();
                        }else{
                            if((notiRoja.isEmpty()) || (notiRoja.compareToIgnoreCase("0") == 0)){
                                Toast.makeText(context, "Ingrese dias para notificacion Critica", Toast.LENGTH_SHORT).show();
                            }else{
//************** inicializacion de conexion, construccion y envio de query *************************
                                try {
                                    String query = "INSERT PRODUCTO VALUES (" + idproducto + ",'" + nombre +"','"+ fechaIngreso + "','" + fechaCaducidad + "',"+notiNaranja+','+notiRoja+");";
                                    ConnectionHelper conexion = new ConnectionHelper();
                                    Connection con = conexion.connectionclass();

                                    PreparedStatement pst = con.prepareStatement(query);

                                    pst.executeUpdate();

                                    Toast.makeText(context, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);


                                } catch (SQLException e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                }
            }
    }

    public static boolean Editar(Context context, int ID, String nombre, String fechaIngreso, String fechaCaducidad, String notiNaranja, String notiRoja){
        //******************** Validaciones para query de edicion ****************************************
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        Date nuevo = null;
        Date comparado = null;

        if(fechaCaducidad.isEmpty()){
            Toast.makeText(context,"Ingrese una fecha de vencimiento",Toast.LENGTH_SHORT).show();
        }else {
            try {
                nuevo = dateFormatYMD.parse(fechaIngreso);
                comparado = dateFormatYMD.parse(fechaCaducidad);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (comparado.compareTo(nuevo) <= 0) {
                Toast.makeText(context, "El producto caduca hoy o ya ha caducado", Toast.LENGTH_SHORT).show();
            } else {
                if (nombre.length() == 0) {
                    Toast.makeText(context, "Ingrese Nombre del producto", Toast.LENGTH_SHORT).show();
                } else {
                    if((notiNaranja.isEmpty()) || (notiNaranja.compareToIgnoreCase("0") == 0)){
                        Toast.makeText(context, "Ingrese dias para notificacion Preventiva", Toast.LENGTH_SHORT).show();
                    }else{
                        if((notiRoja.isEmpty()) || (notiRoja.compareToIgnoreCase("0") == 0)){
                            Toast.makeText(context, "Ingrese dias para notificacion Critica", Toast.LENGTH_SHORT).show();
                        }else{
//************** inicializacion de conexion, construccion y envio de query *************************
                            try {
                                String query = (" UPDATE LOTE SET LOTE_NOMBRE = '" + nombre + "', LOTE_FECHA_INGRESO = '" + fechaIngreso + "', LOTE_FECHA_CADUCIDAD = '" + fechaCaducidad + "', LOTE_NOTIFICACION_NARANJA = " + notiNaranja + ", LOTE_NOTIFICACION_ROJA = " + notiRoja + "WHERE LOTE_ID = " + ID);
                                ConnectionHelper conexion = new ConnectionHelper();
                                Connection con = conexion.connectionclass();

                                PreparedStatement pst = con.prepareStatement(query);

                                pst.executeUpdate();

                                Toast.makeText(context, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);

                                return true;


                            } catch (SQLException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    }

                }
            }
        }
        return false;
    }

    public Lotes verLote(int id){
        Lotes l = null;
        try {

            String query1 = "SELECT * FROM LOTE WHERE PRODUCTO_ID = " + id;

            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = null;

            st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);


            while (rs.next()) {
                l = new Lotes();
                //l.setPRODUCTO_ID(rs.getInt("PRODUCTO_ID"));
                l.setLOTE_NOMBRE(rs.getString("LOTE_NOMBRE"));
                //l.setLOTE_FECHA_INGRESO(rs.getDate("LOTE_FECHA_INGRESO"));
                l.setLOTE_FECHA_CADUCIDAD(rs.getDate("LOTE_FECHA_CADUCIDAD"));
                l.setLOTE_NOTIFICACION_NARANJA(rs.getInt("LOTE_NOTIFICACION_NARANJA"));
                l.setLOTE_NOTIFICACION_ROJA(rs.getInt("LOTE_NOTIFICACION_ROJA"));

            }
        }catch (SQLException e){
            System.out.println("Error in sql statement");
        }
        return l;
    }

    public boolean Borrar(int ID){
        boolean bandera;
        try{
            String query = "DELETE FROM LOTE WHERE LOTE_ID="+ID;
            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            bandera = true;

        }catch (SQLException e) {
            bandera = false;
        }


        return bandera;
    }

}
