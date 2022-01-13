package entidades;

import android.util.Log;
import android.widget.Toast;

import com.example.xpiration.ConnectionHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Productos {
    private int PRODUCTO_ID;
    private int CATEGORIA_ID;
    private String PRODUCTO_NOMBRE;
    private Date PRODUCTO_FECHA_INGRESO;
    private Date PRODUCTO_FECHA_CADUCIDAD;
    private String PRODUCTO_URL_IMG;
    private int PRODUCTO_NOTIFICACION_NARANJA; //preventiva
    private int PRODUCTO_NOTIFICACION_ROJA; //critica


    Connection con;

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


}
