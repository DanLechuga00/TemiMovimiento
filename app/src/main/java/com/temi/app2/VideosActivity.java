package com.temi.app2;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.robotemi.sdk.listeners.OnUserInteractionChangedListener;

import java.util.ArrayList;
import java.util.List;

public class VideosActivity extends AppCompatActivity implements OnUserInteractionChangedListener {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    DeteccionPersonas deteccionPersonas = null;
    private int contador = 0;
    private int contadorActual = 0;
    private int i = 1;
    SecuenciaDeMovimiento secuenciaDeMovimiento = null;


    //private ImageButton btnHelp;
    private Button btnHelp;
    private VideoView vV;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    List<String> videos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        verifyStoragePermissions(this);
        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()) ttsManager.shutDown();
        movimiento = new Movimiento(this, VideosActivity.this,ttsManager);
        bateria = new Bateria(movimiento,this, VideosActivity.this);
        secuenciaDeMovimiento = new SecuenciaDeMovimiento(ttsManager,this,bateria);
        deteccionPersonas = new DeteccionPersonas();
        if(!bateria.EsBateriaBaja()||!bateria.EstaCargando()||bateria.EsBateriaCompleta()){
       if(deteccionPersonas.IsDetectionModeOn()) deteccionPersonas.startDetectionModeWithDistance();
        btnHelp = findViewById(R.id.btnHelp);
        vV = findViewById(R.id.vV);
        videos = RecolectorDeVideos();


            try {
                startVideo(vV,videos);
                Log.d("Movimiento","Aqui inicia la secuencia");
                secuenciaDeMovimiento.Secuencia();
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Error",e.getMessage());
            }

        }

    }

    private List<String> RecolectorDeVideos() {
        videos = new ArrayList<>();
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.familia_nestle);
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.ahorra);
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.cereales);
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.coffee_mate);
        videos.add("android.resource://" + getPackageName() + "/" +R.raw.nido_etapas);
        return  videos;
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
        if(!bateria.EsBateriaBaja()&&!bateria.EstaCargando()&&bateria.EsBateriaCompleta()){
            vV.stopPlayback();
            //Log.d("Movimiento_next","Aqui sigue la secuencia");
            //secuenciaDeMovimiento.Secuencia();
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
        }else{
            vV.stopPlayback();
            Log.d("Movimiento_next","Aqui sigue la secuencia de videos pero temi esta cargando");
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
    protected void onStart() {
        super.onStart();
        Log.d("Main_Activity","OnStart_Main");
        secuenciaDeMovimiento.addListener();
        deteccionPersonas.addListenerUser(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main_Activity","OnStop_Main");
        secuenciaDeMovimiento.removeListener();
        deteccionPersonas.addListenerUser(this);
    }

    @Override
    public void onUserInteraction(boolean isInteracting) {
        Log.d("Main_Activity","Detecion de usuario cerca de temi");
        if(isInteracting){
            ttsManager.initQueue("Bienvenido soy su robot asistente");
            ttsManager.initQueue("Puedes llamarme Jarvis");
            Intent option = new Intent(this,Option_Accion.class);
            startActivity(option);
        }

    }
}