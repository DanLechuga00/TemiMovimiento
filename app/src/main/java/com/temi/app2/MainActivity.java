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

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener;
import com.robotemi.sdk.model.DetectionData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnUserInteractionChangedListener, OnDetectionStateChangedListener, OnDetectionDataChangedListener {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    DeteccionPersonas deteccionPersonas = null;
    BaseDeDatos baseDeDatos = null;
    private int contador = 0;
    private int contadorActual = 0;
    private int i = 1;
    EscuchaTemi escuchaTemi;
    SecuenciaDeMovimiento secuenciaDeMovimiento = null;
    private final static String TAG = "Main_Activity";
    private final  static  String TAGBase = "Usuario";
    private final static  String TAGError = "Execption";


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
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        verifyStoragePermissions(this);
        Log.d(TAG,"ActividadCreada()");
        ttsManager = new TTSManager();
        ttsManager.init(this);
        if (ttsManager.isSpeach()) {
            ttsManager.shutDown();
            ttsManager.Stop();
        }
        escuchaTemi = new EscuchaTemi(MainActivity.this.getBaseContext());
        baseDeDatos = new BaseDeDatos();
        movimiento = new Movimiento(this, MainActivity.this, ttsManager);
        bateria = new Bateria(movimiento, this, MainActivity.this);
        secuenciaDeMovimiento = new SecuenciaDeMovimiento(ttsManager, this, bateria);
        deteccionPersonas = new DeteccionPersonas();
        if (!bateria.EsBateriaBaja() || !bateria.EstaCargando() || bateria.EsBateriaCompleta()) {
            if (deteccionPersonas.IsDetectionModeOn())
                deteccionPersonas.startDetectionModeWithDistance();
            btnHelp = findViewById(R.id.btnHelp);
            vV = findViewById(R.id.vV);
            videos = RecolectorDeVideos();


            try {
                startVideo(vV, videos);
                Log.d(TAG, "Aqui inicia la secuencia");
                String bi = "1";

                //baseDeDatos.CrearBitacoraDeRegistros(8,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
                secuenciaDeMovimiento.Secuencia();
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Error", e.getMessage());
            }

        }

    }

    private List<String> RecolectorDeVideos() {
        videos = new ArrayList<>();
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo01);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo02);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo03);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo04);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo05);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo06);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo07);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo08);
        videos.add("android.resource://" + getPackageName() + "/" + R.raw.videodiageo09);
        return videos;
    }

    private void startVideo(VideoView video, List<String> listaVideos) throws Exception {
        if (listaVideos.isEmpty()) throw new Exception("No contiene videos a reproducir");
        contador = listaVideos.size();
        video.setVideoPath(listaVideos.get(0));
        video.start();
        video.setOnCompletionListener(mp -> {
            try {
                startNextVideo(contador, listaVideos);
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Videos_onPrepared", "Error:" + e.getMessage());
            }
        });


    }

    private void startNextVideo(int contador, List<String> listaVideos) throws Exception {
        if (!bateria.EsBateriaBaja() && bateria.EsBateriaCompleta() && !bateria.EstaCargando()) {
            vV.stopPlayback();
            Log.d(TAG, "Aqui sigue la secuencia");
            //baseDeDatos.CrearBitacoraDeRegistros(8,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
            //secuenciaDeMovimiento.Secuencia();
            if (contador == 0) throw new Exception("contador de videos vacio");
            if (contador == i) {
                i = 0;
                contadorActual = 0;
            }

            while (i <= contador) {
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
        } else {
            vV.stopPlayback();
            Log.d("Movimiento_next", "Aqui sigue la secuencia de videos pero temi esta cargando");
            if (contador == 0) throw new Exception("contador de videos vacio");
            if (contador == i) {
                i = 0;
                contadorActual = 0;
            }

            while (i <= contador) {
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
        Log.d(TAG, "OnStart_Main");
        secuenciaDeMovimiento.addListener();
        deteccionPersonas.addListenerUser(this);
        Robot.getInstance().addOnDetectionDataChangedListener(this);
        Robot.getInstance().addOnDetectionStateChangedListener(this);
        Robot.getInstance().addOnUserInteractionChangedListener(this);
        escuchaTemi.addListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop_Main");
        secuenciaDeMovimiento.removeListener();
        deteccionPersonas.addListenerUser(this);
        Robot.getInstance().removeOnDetectionDataChangedListener(this);
        Robot.getInstance().removeOnDetectionStateChangedListener(this);
        Robot.getInstance().removeOnUserInteractionChangedListener(this);
        escuchaTemi.removeListener();
    }

    @Override
    public void onUserInteraction(boolean isInteracting) {
        Log.d(TAG, "Usuario de interacion : " + isInteracting);
        try {
            //baseDeDatos.CrearBitacoraDeRegistros(4, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, TAGBase, Robot.getInstance().getNickName());
            if (isInteracting) {
                Intent intent = new Intent(this, Option_Accion.class);
                startActivity(intent);
            }

        } catch (Exception ex) {
            Log.e(TAGError, "Error: " + ex.getMessage());
        }

    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
       try {
           //baseDeDatos.CrearBitacoraDeRegistros(4,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
           if (!(detectionData.getDistance() < 0.0) && !(detectionData.getAngle() < 0.0)) {
               ttsManager.initQueue("Escaneando objeto y buscando rostro");
           }
       }catch (Exception ex){
           Log.e(TAGError,"Error:"+ ex.getMessage());
       }

    }

    @Override
    public void onDetectionStateChanged(int state) {
        Log.d(TAG, "StateChanged" + state);

        if (state == OnDetectionStateChangedListener.LOST) {
            ttsManager.initQueue("Permiteme reconocerte");
        } else if (state == OnDetectionStateChangedListener.DETECTED) {
            try {
                ttsManager.initQueue("Bienvenido a la tienda, es un placer apoyarte");
              //  baseDeDatos.CrearBitacoraDeRegistros(4,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase,Robot.getInstance().getNickName());
            }catch (Exception ex){
                Log.e(TAGError,"Error: "+ ex.getMessage());
            }

        }
    }
}