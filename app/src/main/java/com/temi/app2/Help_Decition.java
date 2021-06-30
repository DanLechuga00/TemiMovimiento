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

public class Help_Decition extends AppCompatActivity implements OnDetectionDataChangedListener, OnDetectionStateChangedListener {

    TTSManager ttsManager = null;
private final String TAG = "Help";
    private final String TAGError = "Exception";
    private ImageButton btnSi, btnNo;
    DeteccionPersonas deteccionPersonas = null;
    SecuenciaDeMovimiento secuenciaDeMovimiento = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    private final BaseDeDatos baseDeDatos = new BaseDeDatos();
    private final String TAGBase = "ContnuarInteractuando";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_decition);

        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()){
            ttsManager.shutDown();
            ttsManager.Stop();
        }
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.DetenerMovimiento();
        bateria = new Bateria(movimiento,this,Help_Decition.this);
        secuenciaDeMovimiento = new SecuenciaDeMovimiento(ttsManager,this,bateria);
        movimiento = new Movimiento(this,this,ttsManager);

        btnSi = findViewById(R.id.btnSi);
        btnNo = findViewById(R.id.btnNo);
       try {
        //   baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
       }catch (Exception ex){
           Log.e(TAGError,"Error: "+ex.getMessage());
       }



        btnSi.setOnClickListener(v -> {
          try {
           //   baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
              ttsManager.initQueue("Indícame en que te puedo ayudar");
              Intent sig = new Intent(Help_Decition.this, Option_Accion.class);
              startActivity(sig);
          }catch (Exception ex){
              Log.e(TAGError,"Error: "+ ex.getMessage());
          }

        });

        btnNo.setOnClickListener(v -> {
           try {
             //  baseDeDatos.CrearBitacoraDeRegistros(17,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
               ttsManager.initQueue("Bueno, recuerde que estoy a su servicio en cualquier momento");
               Intent back = new Intent(Help_Decition.this, VideosActivity.class);
               startActivity(back);
           }catch (Exception ex){
               Log.e(TAGError,"Error: "+ex.getMessage());
           }

        });
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
      if(!detectionData.isDetected()){
         try {
             //baseDeDatos.CrearBitacoraDeRegistros(12,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
             deteccionPersonas.DetenerMovimiento();
             ttsManager.initQueue("Bueno, recuerde que estoy a su servicio en cualquier momento");
             Intent back = new Intent(Help_Decition.this, VideosActivity.class);
             startActivity(back);
         }catch (Exception ex){
             Log.e(TAGError,"Error:"+ex.getMessage());
         }

      }
        Log.d(TAG,"onDetectionDataChanged:DetectionData"+detectionData.toString());
    }

    @Override
    public void onDetectionStateChanged(int state) {

        if (OnDetectionStateChangedListener.IDLE == state) {
           try {
               //baseDeDatos.CrearBitacoraDeRegistros(12,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
               deteccionPersonas.DetenerMovimiento();
               ttsManager.initQueue("Bueno, recuerde que estoy a su servicio en cualquier momento");
               Intent back = new Intent(Help_Decition.this, VideosActivity.class);
               startActivity(back);
           }catch (Exception ex){
               Log.e(TAGError,"Error: "+ex.getMessage());
           }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Help_Inseperada","OnStart_help");
        movimiento.MediaVuelta();
        movimiento.LevantaCabeza();
        deteccionPersonas.addListener(Help_Decition.this,Help_Decition.this);
        secuenciaDeMovimiento.addListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("HelpDeteccion","OnStop_help");
        deteccionPersonas.removeListener(Help_Decition.this,Help_Decition.this);
        secuenciaDeMovimiento.removeListener();
    }
}