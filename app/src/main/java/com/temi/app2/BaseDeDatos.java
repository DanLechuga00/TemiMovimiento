package com.temi.app2;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class BaseDeDatos   {

    public Connection connection(){
        Connection connection = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Log.d("ConexionBase","El driver funciona correctamente");
            String conectionurl = "jdbc:sqlserver://108.175.10.228; database=Temi; user=temi; password = Ard4byT3cTem1.;";
            //"jdbc:jtds:sqlserver://108.175.10.228;databaseName = Temi; user=temi; password = Ard4byT3cTem1.;"
            connection = DriverManager.getConnection(conectionurl);
            Log.d("ConexionBase","Conectado");
        }catch (Exception e){

            Log.w("Exception","Error: "+e.getMessage());

        }
        return  connection;
    }

    public void CrearBitacoraDeRegistros(int idCatalogo, byte Inicio, byte operando, byte finalizado, byte detenido, byte dejado, String usuarioRegistrado,String Terminal) throws Exception {

        Connection connection  = connection();
        if(connection == null) throw  new Exception("Imposible utilizar la conexion");
        try{
            PreparedStatement prepared =connection.prepareCall("AltaRegistro(?,?,?,?,?,?,?,?)");
            prepared.setInt(1, idCatalogo);
            prepared.setByte(2, Inicio);
            prepared.setByte(3, operando);
            prepared.setByte(4, finalizado);
            prepared.setByte(5, detenido);
            prepared.setByte(6, dejado);
            prepared.setString(7, usuarioRegistrado);
            prepared.setString(8, Terminal);
            prepared.execute();
        }catch (Exception e){
            Log.w("Exception","Exception: "+e.getMessage());
        }
    }
}
