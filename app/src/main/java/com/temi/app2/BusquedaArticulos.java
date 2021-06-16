package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.model.DetectionData;

import org.jetbrains.annotations.NotNull;

public class
BusquedaArticulos extends AppCompatActivity implements OnDetectionStateChangedListener, OnDetectionDataChangedListener {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    static String orden = "";
    DeteccionPersonas deteccionPersonas = null;

    private ImageButton btn1A, btn2A, btn3A, btnBack1, btn4A;
    private VideoView videoBusqueda;

//TODO: REvisar el oyente en cambio canstante de actividad
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Onstart_Busqueda");
        deteccionPersonas.addListener(this,this);
        movimiento.addListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_articulos);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        movimiento = new Movimiento(this, BusquedaArticulos.this,ttsManager);

        btn1A = findViewById(R.id.button1A);
        btn2A = findViewById(R.id.button2A);
        btn3A = findViewById(R.id.button3A);
        btn4A = findViewById(R.id.button4A);
        videoBusqueda = findViewById(R.id.VideoBusqueda);
        btnBack1 = findViewById(R.id.btnBack1);
        deteccionPersonas = new DeteccionPersonas();

        System.out.println("OnCreate_Busqueda");

        btn1A.setOnClickListener(v -> {
            videoBusqueda.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.carlos_v);
            videoBusqueda.start();
            videoBusqueda.setOnCompletionListener(mp -> {
            videoBusqueda.stopPlayback();
                videoBusqueda.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.kitkat);
                videoBusqueda.start();
            });
            videoBusqueda.stopPlayback();
            ttsManager.initQueue("Sígame y le muestro su ubicación; En el área de Chocolates se encuentra en diferentes sabores como chocolate oscuro;blanco;¿Cual es tu favorito?;");
            movimiento.goTo("chocolates");

        });

        btn2A.setOnClickListener(v -> {
            ttsManager.initQueue("Sígame y le muestro su ubicación; sabías que el cereales es rica hierro y en vitamina;¿Cual es tu sabor favorito?; ");
            movimiento.goTo("cereales");

        });

        btn3A.setOnClickListener(v -> {
            ttsManager.initQueue("Sígame y le muestro su ubicación;café rico para iniciar el día ¿no crees?; ¿Cafeinado o Descafeinado?");
            movimiento.goTo("nescafe");
        });
        btn4A.setOnClickListener(v ->{
            ttsManager.initQueue("Sígame y le muestro su ubicación;Algo rico y nutritivo;¿Buscabas algó en especial?;");
            movimiento.goTo("nutricion");

        });

        btnBack1.setOnClickListener(v -> {
            Intent regresar = new Intent(BusquedaArticulos.this, Option_Accion.class);
            startActivity(regresar);
        });
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
     Log.d("onDetectionDataChanged","onDetectionDataChanged: state : "+detectionData.toString());

    }

    @Override
    public void onDetectionStateChanged(int state) {
    Log.d("onDetectionStateChanged","onDetectionStateChanged : state::"+state);
    if(OnDetectionStateChangedListener.IDLE == state){
        ttsManager.initQueue("Esta bien que tenga un gran día");
        Intent regresar = new Intent(BusquedaArticulos.this, MainActivity.class);
        startActivity(regresar);
    }
    }

    @Override
    protected void onStop() {
        super.onStop();
        deteccionPersonas.removeListener(this,this);
        movimiento.removeListener();
    }
}