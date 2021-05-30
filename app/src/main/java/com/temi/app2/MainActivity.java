package com.temi.app2;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.model.DetectionData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnDetectionStateChangedListener, OnDetectionDataChangedListener {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    DeteccionPersonas deteccionPersonas = null;
    private int contador = 0;
    private int contadorActual = 0;
    private int i = 1;


    //private ImageButton btnHelp;
    private Button btnHelp;
    private VideoView vV;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        verifyStoragePermissions(this);
        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()) ttsManager.shutDown();
        movimiento = new Movimiento(this,MainActivity.this,ttsManager);
        bateria = new Bateria(movimiento,this,MainActivity.this);
        if(!bateria.EsBateriaBaja()){
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.startDetectionModeWithDistance();
        btnHelp = findViewById(R.id.btnHelp);
        vV = findViewById(R.id.vV);
        List<String> videos = new ArrayList<>();
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.cocacola);
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.herederos);
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.heineken);
            try {
                startVideo(vV,videos);
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Error_Videos","No se puede reproducir el video: "+e.getMessage());
            }

        }

    }
    private void startVideo(VideoView video, List<String> listaVideos) throws Exception {
        if(listaVideos.isEmpty()) throw new Exception("No contiene videos a reproducir");
        contador = listaVideos.size();
        video.setVideoPath(listaVideos.get(0));
        video.start();
        video.setOnCompletionListener(mp -> {
            try {
                startNextVideo(contador,listaVideos);
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Videos_onPrepared","Error:"+e.getMessage());
            }
        });


    }

    private void startNextVideo(int contador, List<String> listaVideos) throws Exception {
        vV.stopPlayback();
        if(contador == 0) throw  new Exception("contador de videos vacio");
        if(contador == i){
            i = 0;
            contadorActual = 0;
        }

        while (i<= contador) {
            if (contadorActual == 0) {
                vV.setVideoPath(listaVideos.get(i));
                vV.start();
                contadorActual = i;
                i++;
                break;
            }
            vV.setVideoPath(listaVideos.get(i));
            vV.start();
            i++;
            break;
        }

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
    System.out.println("onDetectionDataChanged : Detection"+detectionData.toString());
    Log.d("onDetectionDataChanged","onDetectionDataChanged: state : "+detectionData.toString());
    }

    @Override
    public void onDetectionStateChanged(int state) {
        System.out.println("onDetectionStateChanged : state " + state);
        if (state == OnDetectionStateChangedListener.DETECTED) {
            deteccionPersonas.ConstanteJuntoAMi();
            ttsManager.initQueue("Buen día ¿En qué le puedo ayudar?");
            Intent sig = new Intent(MainActivity.this, Option_Accion.class);
            startActivity(sig);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("OnStart");
        deteccionPersonas.addListener(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("OnStop");
        deteccionPersonas.removeListener(this,this);
    }
}