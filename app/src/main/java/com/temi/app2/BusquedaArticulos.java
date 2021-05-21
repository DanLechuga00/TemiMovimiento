package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;

import org.jetbrains.annotations.NotNull;

public class BusquedaArticulos extends AppCompatActivity {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;

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
//        movimiento.robot.addOnLocationsUpdatedListener(new onLocationsUpdatedListener);
        movimiento.robot.addOnGoToLocationStatusChangedListener(new OnGoToLocationStatusChangedListener() {
            @Override
            public void onGoToLocationStatusChanged(@NotNull String location, @NotNull String status, int descriptionId, @NotNull String description) {
                //ttsManager.addQueue(description);
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
                        ttsManager.initQueue("En este pasillo se encuentra su artículo");
                        break;
                    case OnGoToLocationStatusChangedListener.ABORT:
                        //ttsManager.addQueue("Abortando");
                        break;
                }
            }
        });
        /*movimiento.robot.addOnDistanceToLocationChangedListener(onDistanceToLocationChangedListener);
        movimiento.robot.addOnCurrentPositionChangedListener(onCurrentPositionChangedListener);
        movimiento.robot.addOnReposeStatusChangedListener(onReposeStatusChangedListener);
        movimiento.robot.addOnLoadMapStatusChangedListener(onLoadMapStatusChangedListener);*/
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

        btnRefresco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("Los refrescos se encuentran en el pasillo 1; sígame y le muestro su ubicación");
                movimiento.goTo("uno");
            }
        });

        btnVinosLicores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("Los vinos y licores se encuentran en el pasillo 2; sígame y le muestro su ubicación");
                movimiento.goTo("dos");
                //movimiento.bailar();
            }
        });

        btnDulceria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("La dulceria se encuentra en el pasillo 3; sígame y le muestro su ubicación");
                movimiento.goTo("tres");
            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresar = new Intent(BusquedaArticulos.this, Option_Accion.class);
                startActivity(regresar);
            }
        });
    }
}