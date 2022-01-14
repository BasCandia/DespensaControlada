package entidades;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.xpiration.ConnectionHelper;
import com.example.xpiration.MainActivity;
import com.example.xpiration.SegundaActivity;

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

public class Productos {
//*************************** Declaracion de variables de Productos ********************************
    private int PRODUCTO_ID;
    private int CATEGORIA_ID;
    private String PRODUCTO_NOMBRE;
    private Date PRODUCTO_FECHA_INGRESO;
    private Date PRODUCTO_FECHA_CADUCIDAD;
    private String PRODUCTO_URL_IMG;
    private int PRODUCTO_NOTIFICACION_NARANJA; //preventiva
    private int PRODUCTO_NOTIFICACION_ROJA; //critica


    Connection con;

//************************** Getter and setter de productos ****************************************
    public int getPRODUCTO_ID() {
        return PRODUCTO_ID;
    }

    public void setPRODUCTO_ID(int PRODUCTO_ID) {
        this.PRODUCTO_ID = PRODUCTO_ID;
    }

    public int getCATEGORIA_ID() {
        return CATEGORIA_ID;
    }

    public void setCATEGORIA_ID(int CATEGORIA_ID) {
        this.CATEGORIA_ID = CATEGORIA_ID;
    }

    public String getPRODUCTO_NOMBRE() {
        return PRODUCTO_NOMBRE;
    }

    public void setPRODUCTO_NOMBRE(String PRODUCTO_NOMBRE) {
        this.PRODUCTO_NOMBRE = PRODUCTO_NOMBRE;
    }

    public Date getPRODUCTO_FECHA_INGRESO() {
        return PRODUCTO_FECHA_INGRESO;
    }

    public void setPRODUCTO_FECHA_INGRESO(Date PRODUCTO_FECHA_INGRESO) {
        this.PRODUCTO_FECHA_INGRESO = PRODUCTO_FECHA_INGRESO;
    }

    public Date getPRODUCTO_FECHA_CADUCIDAD() {
        return PRODUCTO_FECHA_CADUCIDAD;
    }

    public void setPRODUCTO_FECHA_CADUCIDAD(Date PRODUCTO_FECHA_CADUCIDAD) {
        this.PRODUCTO_FECHA_CADUCIDAD = PRODUCTO_FECHA_CADUCIDAD;
    }

    public String getPRODUCTO_URL_IMG() {
        return PRODUCTO_URL_IMG;
    }

    public void setPRODUCTO_URL_IMG(String PRODUCTO_URL_IMG) {
        this.PRODUCTO_URL_IMG = PRODUCTO_URL_IMG;
    }

    public int getPRODUCTO_NOTIFICACION_NARANJA() {
        return PRODUCTO_NOTIFICACION_NARANJA;
    }

    public void setPRODUCTO_NOTIFICACION_NARANJA(int PRODUCTO_NOTIFICACION_NARANJA) {
        this.PRODUCTO_NOTIFICACION_NARANJA = PRODUCTO_NOTIFICACION_NARANJA;
    }

    public int getPRODUCTO_NOTIFICACION_ROJA() {
        return PRODUCTO_NOTIFICACION_ROJA;
    }

    public void setPRODUCTO_NOTIFICACION_ROJA(int PRODUCTO_NOTIFICACION_ROJA) {
        this.PRODUCTO_NOTIFICACION_ROJA = PRODUCTO_NOTIFICACION_ROJA;
    }

//************************ Funcion para insertar query en base de datos ****************************
    public void insertar (Context context, String nombre, String fechaIngreso, String fechaCaducidad,int Categoria, String notiNaranja, String notiRoja){
//******************** Validaciones para query de insercion ****************************************

        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        Date nuevo = null;
        Date comparado = null;


        if( Categoria == 0) {
            Toast.makeText(context,"Seleccione una Categoria",Toast.LENGTH_SHORT).show();
        }else {
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
                                    String query = "INSERT PRODUCTO VALUES (" + Categoria + ",'" + nombre +"','"+ fechaIngreso + "','" + fechaCaducidad + "',"+notiNaranja+','+notiRoja+",null);";
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
    }

//************************ Funcion para editar query en base de datos ****************************
    public boolean Editar (Context context,int ID, String nombre, String fechaIngreso, String fechaCaducidad,int Categoria, String notiNaranja, String notiRoja){
        //******************** Validaciones para query de edicion ****************************************

        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        Date nuevo = null;
        Date comparado = null;


        if( Categoria == 0) {
            Toast.makeText(context,"Seleccione una Categoria",Toast.LENGTH_SHORT).show();
        }else {
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
                        if ((notiNaranja.isEmpty()) || (notiNaranja.compareToIgnoreCase("0") == 0)) {
                            Toast.makeText(context, "Ingrese dias para notificacion Preventiva", Toast.LENGTH_SHORT).show();
                        } else {
                            if ((notiRoja.isEmpty()) || (notiRoja.compareToIgnoreCase("0") == 0)) {
                                Toast.makeText(context, "Ingrese dias para notificacion Critica", Toast.LENGTH_SHORT).show();
                            } else {
//************** inicializacion de conexion, construccion y envio de query *************************
                                try {
                                    String query = (" UPDATE PRODUCTO SET CATEGORIA_ID = " + Categoria + ", PRODUCTO_NOMBRE = '" + nombre + "', PRODUCTO_FECHA_INGRESO = '" + fechaIngreso + "', PRODUCTO_FECHA_CADUCIDAD = '" + fechaCaducidad + "', PRODUCTO_NOTIFICACION_NARANJA = " + notiNaranja + ", PRODUCTO_NOTIFICACION_ROJA = " + notiRoja + "WHERE PRODUCTO_ID = " + ID);

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
        }
        return false;
    }
//************************ Funcion para borrar query en base de datos ******************************
    public boolean Borrar(int ID){
        boolean bandera;
        try{
            String query = "DELETE FROM PRODUCTO WHERE PRODUCTO_ID="+ID;
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

    public ArrayList<Productos> mostrarProductos(){

        ArrayList<Productos> listaProductos = new ArrayList<Productos>();
        try {
            Productos p;
            String query1 = "SELECT * FROM PRODUCTO";


            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = null;

            st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);


            while (rs.next()) {
               p = new Productos();
               p.setPRODUCTO_ID(rs.getInt("PRODUCTO_ID"));
               p.setCATEGORIA_ID(rs.getInt("CATEGORIA_ID"));
               p.setPRODUCTO_NOMBRE(rs.getString("PRODUCTO_NOMBRE"));
               p.setPRODUCTO_FECHA_CADUCIDAD(rs.getDate("PRODUCTO_FECHA_CADUCIDAD"));
               p.setPRODUCTO_NOTIFICACION_NARANJA(rs.getInt("PRODUCTO_NOTIFICACION_NARANJA"));
                p.setPRODUCTO_NOTIFICACION_ROJA(rs.getInt("PRODUCTO_NOTIFICACION_ROJA"));
               //url de la imagen
               listaProductos.add(p);
               //Log.w("Producto: ",p.getPRODUCTO_ID() + " - " + p.getCATEGORIA_ID() + "-" + p.getPRODUCTO_NOMBRE());
            }
        }catch (SQLException e){
            System.out.println("Error in sql statment");
        }
        return listaProductos;
    }

    public Productos verProducto(int id){
        Productos p = null;
        try {

            String query1 = "SELECT * FROM PRODUCTO WHERE PRODUCTO_ID = " + id;

            ConnectionHelper conexion = new ConnectionHelper();
            con = conexion.connectionclass();
            Statement st = null;

            st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);


            while (rs.next()) {
                p = new Productos();
                p.setPRODUCTO_ID(rs.getInt("PRODUCTO_ID"));
                p.setCATEGORIA_ID(rs.getInt("CATEGORIA_ID"));
                p.setPRODUCTO_NOMBRE(rs.getString("PRODUCTO_NOMBRE"));
                p.setPRODUCTO_FECHA_CADUCIDAD(rs.getDate("PRODUCTO_FECHA_CADUCIDAD"));
                p.setPRODUCTO_NOTIFICACION_NARANJA(rs.getInt("PRODUCTO_NOTIFICACION_NARANJA"));
                p.setPRODUCTO_NOTIFICACION_ROJA(rs.getInt("PRODUCTO_NOTIFICACION_ROJA"));
                //url de la imagen

                //Log.w("Producto: ",p.getPRODUCTO_ID() + " - " + p.getCATEGORIA_ID() + "-" + p.getPRODUCTO_NOMBRE());
            }
        }catch (SQLException e){
            System.out.println("Error in sql statement");
        }
        return p;
    }


}
