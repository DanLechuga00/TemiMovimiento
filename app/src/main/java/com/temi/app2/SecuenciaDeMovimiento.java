package com.temi.app2;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener;
import com.robotemi.sdk.model.DetectionData;
import com.robotemi.sdk.navigation.model.SpeedLevel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SecuenciaDeMovimiento implements OnGoToLocationStatusChangedListener , OnDetectionDataChangedListener{
    private final Robot robot;
    private String PositionActual = "";
    private int ContadorPositiones = 0;
    private int ContadorPositionesTotales = 0;
    private final String TAG = "SecuenciaDeMovimiento";
    private TTSManager ttsManager;
    private AppCompatActivity main;
    private Bateria bateria;
    private final BaseDeDatos  baseDeDatos = new BaseDeDatos();
    private final static String TAGBase ="RobotTemiAutonomia";
    private final static  String TAGError = "Exception";


    public SecuenciaDeMovimiento(TTSManager ttsManager, AppCompatActivity main, Bateria bateria) {
        this.robot = Robot.getInstance(); this.ttsManager = ttsManager;
        this.main = main;
        this.bateria = bateria;

    }


    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, @NotNull String status, int descriptionId, @NotNull String description) {
        Log.d(TAG, "onGoToLocationStatusChanged:"+"\n"+ "location:" + location + "\n" + "status: " + status + "\n" + "descriptionId: " + descriptionId + "\n" + "description: " + description);
        //TODO: Agregar Actividad inesperada, mas tiempo de espera
        switch (status) {
            case OnGoToLocationStatusChangedListener.START:
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(7,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
                    this.PositionActual = location;
                    robot.setGoToSpeed(SpeedLevel.MEDIUM);
                    robot.setVolume(4);
                } catch (Exception e) {
                    Log.e(TAGError,"Error:"+e.getMessage());
                }
                break;
            case OnGoToLocationStatusChangedListener.REPOSING:
                ttsManager.initQueue("Analizando el entorno");
                break;
            case OnGoToLocationStatusChangedListener.CALCULATING:
                //ttsManager.initQueue("Creo que por aqui podira irme");
                break;
            case "obstacle detected":
                if(descriptionId == 2002 || descriptionId == 2001 ){
                    try {
                        baseDeDatos.CrearBitacoraDeRegistros(4,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
                        robot.stopMovement();
                    } catch (Exception e) {
                        Log.e(TAGError,"Error:"+e.getMessage());
                    }
                    Log.d("SecuenciaMovimiento","Se encontro un obstaculo");
                }else
                    if(descriptionId == 2003 || descriptionId == 2007){
                        try {
                            baseDeDatos.CrearBitacoraDeRegistros(14,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
                            ttsManager.initQueue("Eh detectado algo espero no chocar con el");
                           // ContadorPositiones--;
                            //robot.stopMovement();
                            Secuencia();
                        } catch (Exception e) {
                            Log.e(TAGError,"Error: "+e.getMessage());
                        }

                }else{
                        ttsManager.initQueue("Ups; Permiso; Le agradezco");
                    }
                break;
            case  OnGoToLocationStatusChangedListener.ABORT:
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(15,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
                    ttsManager.initQueue("Pensando");
                    //ContadorPositiones--;
                    //Secuencia();
                } catch (Exception e) {
                    Log.e(TAGError,"Exception: "+e.getMessage());
                }
                robot.stopMovement();

                break;
            case OnGoToLocationStatusChangedListener.COMPLETE:
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(16,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
                    ttsManager.initQueue("Mira llegue al pasillo: "+location+ "que tendra de nuevo");
                    robot.setVolume(2);
                    Log.d(TAG,"Contador:"+ContadorPositiones);
                    Log.d(TAG,"ContadorTotal: "+ContadorPositionesTotales);
                    if(ContadorPositiones >= ContadorPositionesTotales) {
                        ContadorPositiones = 0;
                    }
                    else{
                        ContadorPositiones++;
                    }
                } catch (Exception e) {
                    Log.e(TAGError,"Error: "+e.getMessage());
                }

                if(!bateria.EsBateriaBaja()&& bateria.EsBateriaCompleta()&&!bateria.EstaCargando())Secuencia();
                break;
        }
    }
    public  List<String> getLocations(){
        ContadorPositionesTotales = robot.getLocations().size()-1;
        return robot.getLocations();
    }

    public void Secuencia() {
        try {
            //baseDeDatos.CrearBitacoraDeRegistros(7,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
            List<String> ubicaciones = getLocations();
            if(!ubicaciones.isEmpty()){
                ubicaciones.remove("home base".toLowerCase());
                System.out.println("Ubicacion:"+PositionActual);
                if(ContadorPositiones >= ContadorPositionesTotales) ContadorPositiones = 0;
                while (ContadorPositiones <= ContadorPositionesTotales) {
                    robot.goTo(ubicaciones.get(ContadorPositiones));
                    break;
                }

            }
        } catch (Exception e) {
            Log.e(TAGError,"Error:" +e.getMessage());
        }

    }
    public void addListener(){
        Log.d("SecuenciaMovimiento","AddListener");
        robot.addOnGoToLocationStatusChangedListener(SecuenciaDeMovimiento.this);

    }
    public  void removeListener(){
        Log.d("SecuenciaMovimiento","removeListener");
        robot.removeOnGoToLocationStatusChangedListener(SecuenciaDeMovimiento.this);


    }


    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
        try {
           // baseDeDatos.CrearBitacoraDeRegistros(4,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
            if(detectionData.isDetected()){
                Intent inseperada = new Intent(main,Help_Inesperada.class);
                main.startActivity(inseperada);
            }else
                ttsManager.initQueue("Disculpe si cheque, estoy aprendiendo a caminar ");

        }catch (Exception e) {
            Log.e(TAGError,"Error:"+e.getMessage());
        }
        }

}
