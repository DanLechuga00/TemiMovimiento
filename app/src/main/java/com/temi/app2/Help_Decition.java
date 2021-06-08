package com.temi.app2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.model.DetectionData;

import org.jetbrains.annotations.NotNull;

public class Help_Decition extends AppCompatActivity implements OnDetectionDataChangedListener, OnDetectionStateChangedListener {

    TTSManager ttsManager = null;
private final String TAG = "Help";
    private ImageButton btnSi, btnNo;
    DeteccionPersonas deteccionPersonas = null;
    SecuenciaDeMovimiento secuenciaDeMovimiento = null;
    Movimiento movimiento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_decition);

        ttsManager = new TTSManager();
        ttsManager.init(this);
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.DetenerMovimiento();
        secuenciaDeMovimiento = new SecuenciaDeMovimiento(ttsManager);
        movimiento = new Movimiento(this,this,ttsManager);

        btnSi = findViewById(R.id.btnSi);
        btnNo = findViewById(R.id.btnNo);



        btnSi.setOnClickListener(v -> {
            ttsManager.initQueue("Indícame en que te puedo ayudar");
            Intent sig = new Intent(Help_Decition.this, Option_Accion.class);
            startActivity(sig);
        });

        btnNo.setOnClickListener(v -> {
            ttsManager.initQueue("Bueno, recuerde que estoy a su servicio en cualquier momento");
            Intent back = new Intent(Help_Decition.this, MainActivity.class);
            startActivity(back);
        });
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
        Log.d(TAG,"onDetectionDataChanged:DetectionData"+detectionData.toString());
    }

    @Override
    public void onDetectionStateChanged(int state) {

        if (OnDetectionStateChangedListener.IDLE == state) {
            deteccionPersonas.DetenerMovimiento();
            ttsManager.initQueue("Bueno, recuerde que estoy a su servicio en cualquier momento");
            Intent back = new Intent(Help_Decition.this, MainActivity.class);
            startActivity(back);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        movimiento.MediaVuelta();
        deteccionPersonas.addListener(Help_Decition.this,Help_Decition.this);
        secuenciaDeMovimiento.addListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        deteccionPersonas.removeListener(Help_Decition.this,Help_Decition.this);
        secuenciaDeMovimiento.removeListener();
    }
}