package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.robotemi.sdk.Robot;
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
    private final BaseDeDatos baseDeDatos = new BaseDeDatos();

    private ImageButton btnBack1, btnWhisky;
    private VideoView videoBusqueda;
    private final String TAGBase = "Ir a un pasillo";
    private final String TAGError = "Exception";

//TODO: REvisar el oyente en cambio canstante de actividad
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("BusquedaArticulos","Onstart_Busqueda");
        deteccionPersonas.addListener(this,this);
        movimiento.addListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_articulos);

        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()){
            ttsManager.shutDown();
            ttsManager.Stop();
        }

        movimiento = new Movimiento(this, BusquedaArticulos.this,ttsManager);

        /*btn1A = findViewById(R.id.button1A);
        btn2A = findViewById(R.id.button2A);
        btn3A = findViewById(R.id.button3A);
        btn4A = findViewById(R.id.button4A);*/
        btnWhisky = findViewById(R.id.btnWhisky);
        videoBusqueda = findViewById(R.id.VideoBusqueda);
        btnBack1 = findViewById(R.id.btnBack1);
        deteccionPersonas = new DeteccionPersonas();
        videoBusqueda.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.ahorra);
        videoBusqueda.setOnCompletionListener(mp -> {
            videoBusqueda.stopPlayback();
            videoBusqueda.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.familia_nestle);
            videoBusqueda.start();
        });
        videoBusqueda.stopPlayback();
        videoBusqueda.start();
        System.out.println("OnCreate_Busqueda");

        /*btn1A.setOnClickListener(v -> {
            baseDeDatos.CrearBitacoraDeRegistros(8,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            *//*Intent video = new Intent(this,VideosActivity.class);
            startActivity(video);*//*

            videoBusqueda.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.carlos_v);
            videoBusqueda.start();
            videoBusqueda.setOnCompletionListener(mp -> {
            videoBusqueda.stopPlayback();
                videoBusqueda.setVideoPath("android.resource://" + getPackageName() + "/" +R.raw.kitkat);
                videoBusqueda.start();
            });
            videoBusqueda.stopPlayback();

            baseDeDatos.CrearBitacoraDeRegistros(1,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            ttsManager.initQueue("S??game y le muestro su ubicaci??n; En el ??rea de Chocolates se encuentra en diferentes sabores como chocolate oscuro;blanco;??Cual es tu favorito?;");
            movimiento.goTo("chocolates");

        });

        btn2A.setOnClickListener(v -> {
            baseDeDatos.CrearBitacoraDeRegistros(1,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            ttsManager.initQueue("S??game y le muestro su ubicaci??n; sab??as que el cereales es rica hierro y en vitamina;??Cual es tu sabor favorito?; ");
            movimiento.goTo("cereales");

        });

        btn3A.setOnClickListener(v -> {
            baseDeDatos.CrearBitacoraDeRegistros(1,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            ttsManager.initQueue("S??game y le muestro su ubicaci??n;caf?? rico para iniciar el d??a ??no crees?; ??Cafeinado o Descafeinado?");
            movimiento.goTo("nescafe");
        });
        btn4A.setOnClickListener(v ->{
            baseDeDatos.CrearBitacoraDeRegistros(1,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            ttsManager.initQueue("S??game y le muestro su ubicaci??n;Algo rico y nutritivo;??Buscabas alg?? en especial?;");
            movimiento.goTo("nutricion");

        });*/

        btnWhisky.setOnClickListener(v -> {
            try {
 //               baseDeDatos.CrearBitacoraDeRegistros(1,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                ttsManager.initQueue("Me podr??a indicar que Whisky desea encontrar");
                Intent sig = new Intent(this, SubCategorias.class);
                startActivity(sig);
            } catch (Exception e) {
                Log.e(TAGError,"Error:"+e.getMessage());
            }

        });

        btnBack1.setOnClickListener(v -> {
           try {
               //baseDeDatos.CrearBitacoraDeRegistros(9,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
               Intent regresar = new Intent(BusquedaArticulos.this, Option_Accion.class);
               startActivity(regresar);
           }catch (Exception ex){
              Log.e("TAGError","Error: "+ex.getMessage());
           }
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
        ttsManager.initQueue("Esta bien que tenga un gran d??a");
        Intent regresar = new Intent(BusquedaArticulos.this, VideosActivity.class);
        startActivity(regresar);
    }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("BusquedaArticulos","OnStop_Busqueda");
        deteccionPersonas.removeListener(this,this);
        movimiento.removeListener();
    }
}