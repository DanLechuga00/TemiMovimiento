
package com.temi.app2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.model.DetectionData;

import org.jetbrains.annotations.NotNull;

public class Help_Inesperada extends AppCompatActivity implements OnDetectionStateChangedListener, OnDetectionDataChangedListener {
private String TAG = "Help_Inesperado";
private final String TAGError = "Exception";
private final String TAGBase = "En frente del robot";
private TTSManager ttsManager;
private Robot robot;
private DeteccionPersonas deteccionPersonas = null;
private final BaseDeDatos baseDeDatos = new BaseDeDatos();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_inesperada);
        ImageButton btnSi = findViewById(R.id.btnSi_h);
        ImageButton btnNo = findViewById(R.id.btnNo_h);
        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()){
            ttsManager.shutDown();
            ttsManager.Stop();
        }
        robot = Robot.getInstance();
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.ConstanteJuntoAMi();
        Log.d(TAG,"OnCreate");
        try {
            Log.d(TAG,"Detener");
            robot.stopMovement();
          //  baseDeDatos.CrearBitacoraDeRegistros(11,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());

        }catch (Exception ex){
            Log.e(TAGError,"Error: "+ex.getMessage());
        }

        btnSi.setOnClickListener(v ->{
            try {
            //    baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                ttsManager.initQueue("Me puedes indicar en que te puedo apoyar");
                Intent option = new Intent(this,Option_Accion.class);
                startActivity(option);
            }catch (Exception ex){
                Log.e(TAGError,"Error: "+ex.getMessage());
            }

        });

        btnNo.setOnClickListener(v ->{
          try {
              //baseDeDatos.CrearBitacoraDeRegistros(12,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
              ttsManager.initQueue("Esta bien; Recuerde estoy para servirle");
              Intent option = new Intent(this, VideosActivity.class);
              startActivity(option);
          }catch (Exception ex){
              Log.e(TAGError,"Error: "+ex.getMessage());
          }

        });

    }

    @Override
    public void onDetectionStateChanged(int state) {
        Log.d(TAG,"onDetectionStateChanged: state"+state);
        if (OnDetectionStateChangedListener.DETECTED == state) {
           try {
               if (ttsManager.isSpeach()){
                   ttsManager.shutDown();
                   ttsManager.Stop();
               }
               deteccionPersonas.ConstanteJuntoAMi();
               ttsManager.addQueue("Te puedo apoyar en algo");
               //baseDeDatos.CrearBitacoraDeRegistros(11,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
           }catch (Exception ex){
            Log.e(TAGError,"Error:"+ ex.getMessage());
           }


        } else if (OnDetectionStateChangedListener.IDLE == state) {
            try {
                //baseDeDatos.CrearBitacoraDeRegistros(12,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                deteccionPersonas.DetenerMovimiento();
                ttsManager.initQueue("Hasta luego que tenga un gran día");
                Intent main = new Intent(this, VideosActivity.class);
                startActivity(main);
            }catch (Exception ex){
                Log.e(TAGError,"Error :"+ex.getMessage());
            }

        }
    }

    public  void addListener(){
        Log.d(TAG,"AddListener");
        robot.addOnDetectionStateChangedListener(this);
        robot.addOnDetectionDataChangedListener(this);
    }
    public  void removeListener(){
        Log.d(TAG,"RemoveListener");
        robot.removeOnDetectionStateChangedListener(this);
        robot.removeOnDetectionDataChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"OnStop");
        addListener();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"OnStop");
        removeListener();
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
        if(!detectionData.isDetected()){
            try {
                deteccionPersonas.DetenerMovimiento();
                ttsManager.initQueue("Hasta luego que tenga un gran día");
                Intent main = new Intent(this, VideosActivity.class);
                startActivity(main);
            }catch (Exception e){
                Log.e(TAGError,"Error:"+e.getMessage());
            }
        }
    }
}
