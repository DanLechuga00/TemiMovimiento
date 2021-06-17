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

public class Help_Inesperada extends AppCompatActivity implements OnDetectionStateChangedListener{
private String TAG = "Help_Inesperado";
private TTSManager ttsManager;
private Robot robot;
private DeteccionPersonas deteccionPersonas = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_inesperada);
        ImageButton btnSi = findViewById(R.id.btnSi_h);
        ImageButton btnNo = findViewById(R.id.btnNo_h);
        ttsManager = new TTSManager();
        ttsManager.init(this);
        robot = Robot.getInstance();
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.ConstanteJuntoAMi();
        Log.d(TAG,"OnCreate");
        btnSi.setOnClickListener(v ->{
            ttsManager.initQueue("Me puedes indicar en que te puedo apoyar");
            Intent option = new Intent(this,Option_Accion.class);
            startActivity(option);
        });

        btnNo.setOnClickListener(v ->{
            ttsManager.initQueue("Esta bien; Recuerde estoy para servirle");
            Intent option = new Intent(this,MainActivity.class);
            startActivity(option);
        });

    }

    @Override
    public void onDetectionStateChanged(int state) {
        Log.d(TAG,"onDetectionStateChanged: state"+state);
        if (OnDetectionStateChangedListener.DETECTED == state) {
            if (ttsManager.isSpeach()){
                ttsManager.shutDown();
                ttsManager.Stop();
            }
            deteccionPersonas.ConstanteJuntoAMi();
            ttsManager.addQueue("Te puedo apoyar en algo");
        } else if (OnDetectionStateChangedListener.IDLE == state) {
            deteccionPersonas.DetenerMovimiento();
            ttsManager.initQueue("Bueno esta bien; soy su robot asistente personal");
            Intent main = new Intent(this,MainActivity.class);
            startActivity(main);
        }
    }

    public  void addListener(){
        Log.d(TAG,"AddListener");
        robot.addOnDetectionStateChangedListener(this);
    }
    public  void removeListener(){
        Log.d(TAG,"RemoveListener");
     robot.removeOnDetectionStateChangedListener(this);
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
}
