package com.example.xpiration;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectionHelper {
    Connection connect;
    @SuppressLint("NewApi")
    public Connection connectionclass(){
        connect=null;
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connect = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.119:1433;databaseName=xpiration;user=sa;password=Darktruenox0123;");
        }catch (Exception ex){


        }

        return connect;

    }
}
