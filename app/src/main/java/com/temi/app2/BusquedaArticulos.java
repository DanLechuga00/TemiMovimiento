package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener;
import com.robotemi.sdk.model.DetectionData;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;

import org.jetbrains.annotations.NotNull;

public class BusquedaArticulos extends AppCompatActivity implements OnDetectionStateChangedListener, OnDetectionDataChangedListener {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    static String orden = "";
    DeteccionPersonas deteccionPersonas = null;

    private ImageButton btnRefresco, btnVinosLicores, btnDulceria, btnBack1;

    /*OnLocationsUpdatedListener onLocationsUpdatedListener = null;
    OnGoToLocationStatusChangedListener onGoToLocationStatusChangedListener = null;
    OnDistanceToLocationChangedListener onDistanceToLocationChangedListener = null;
    OnCurrentPositionChangedListener onCurrentPositionChangedListener = null;
    OnReposeStatusChangedListener onReposeStatusChangedListener = null;
    OnLoadMapStatusChangedListener onLoadMapStatusChangedListener = null;
    OnRobotReadyListener onRobotReadyListener = null;*/

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Onstart_Busqueda");
        deteccionPersonas.addListener(this,this);
//        movimiento.robot.addOnLocationsUpdatedListener(new onLocationsUpdatedListener);
        movimiento.robot.addOnGoToLocationStatusChangedListener((location, status, descriptionId, description) -> {
            //ttsManager.addQueue(description);
            System.out.println(description);
            switch (status) {
                case OnGoToLocationStatusChangedListener.START:
                    //ttsManager.addQueue("Iniciando");
                    break;
                case OnGoToLocationStatusChangedListener.CALCULATING:
                    //ttsManager.addQueue("Calculando");
                    break;
                case OnGoToLocationStatusChangedListener.GOING:
                    //ttsManager.addQueue("Caminando");
                    break;
                case OnGoToLocationStatusChangedListener.COMPLETE:
                    if(ttsManager.isSpeach()){
                        ttsManager.shutDown();
                    }
                    ttsManager.initQueue("En este pasillo se encuentra su artículo");
                    ttsManager.initQueue("¿Le puedo ayudar en algo más?");
                    Intent help = new Intent(BusquedaArticulos.this, Help_Decition.class);
                    startActivity(help);
                    break;
                case OnGoToLocationStatusChangedListener.ABORT:
                    //ttsManager.addQueue("Abortando");
                    break;
            }
        });
        //movimiento.robot.addOnDistanceToLocationChangedListener(onDistanceToLocationChangedListener);
       // movimiento.robot.addOnCurrentPositionChangedListener(onCurrentPositionChangedListener);
        movimiento.robot.addOnReposeStatusChangedListener((status, description) -> {
            switch (status){
                case OnReposeStatusChangedListener.REPOSING_OBSTACLE_DETECTED:
                    ttsManager.initQueue("Humano puedes moverte por favor");
                    break;

            }
        });
        deteccionPersonas.addListener(this,this);
        //movimiento.robot.addOnLoadMapStatusChangedListener(onLoadMapStatusChangedListener);
        //movimiento.robot.addOnRobotReadyListener(movimiento.robot.addOnRobotReadyListener((OnRobotReadyListener) this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_articulos);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        movimiento = new Movimiento(this, BusquedaArticulos.this,ttsManager);

        btnRefresco = findViewById(R.id.btnRefresco);
        btnVinosLicores = findViewById(R.id.btnVinosLicores);
        btnDulceria = findViewById(R.id.btnDulceria);
        btnBack1 = findViewById(R.id.btnBack1);
        deteccionPersonas = new DeteccionPersonas();

        System.out.println("OnCreate_Busqueda");
        btnRefresco.setOnClickListener(v -> {
            ttsManager.initQueue("Los refrescos se encuentran en el pasillo 1; sígame y le muestro su ubicación");

            //movimiento.bailar();
            movimiento.goTo("uno");
        });

        btnVinosLicores.setOnClickListener(v -> {
            ttsManager.initQueue("Los vinos y licores se encuentran en el pasillo 2; sígame y le muestro su ubicación");
            movimiento.goTo("dos");
            //movimiento.bailar();
        });

        btnDulceria.setOnClickListener(v -> {
            ttsManager.initQueue("La dulceria se encuentra en el pasillo 3; sígame y le muestro su ubicación");
            movimiento.goTo("tres");
        });

        btnBack1.setOnClickListener(v -> {
            Intent regresar = new Intent(BusquedaArticulos.this, Option_Accion.class);
            startActivity(regresar);
        });
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
        System.out.println("onDetectionDataChanged : Detection"+detectionData.toString());
        Log.d("Busqueda_OnChange","onDetectionDataChanged: state : "+detectionData.toString());
    }

    @Override
    public void onDetectionStateChanged(int state) {

    }


}