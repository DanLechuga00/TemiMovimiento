package com.temi.app2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;

import org.jetbrains.annotations.NotNull;

public class Option_Accion extends AppCompatActivity implements OnDetectionStateChangedListener, Robot.AsrListener,Robot.WakeupWordListener,Robot.NlpListener {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    DeteccionPersonas deteccionPersonas = null;


    private ImageButton btnDonde, btnRecomendacion, btnHome, btnCosto,micro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_accion);

        ttsManager = new TTSManager();
        ttsManager.init(this);
        movimiento = new Movimiento(this, Option_Accion.this, ttsManager);
        bateria = new Bateria(movimiento, this, Option_Accion.this);
        btnDonde = findViewById(R.id.btnDonde);
        btnRecomendacion = findViewById(R.id.btnRecomendacion);
        micro = findViewById(R.id.micro);
        btnHome = findViewById(R.id.btnHome);
        btnCosto = findViewById(R.id.btnCosto);
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.DetenerMovimiento();


            btnDonde.setOnClickListener(v -> {
                ttsManager.initQueue("¿Qué articulo deseas encontrar?");
                Intent search = new Intent(Option_Accion.this, BusquedaArticulos.class);
                startActivity(search);
            });

            btnCosto.setOnClickListener(v -> {
                ttsManager.initQueue("Necesito tu apoyo por favor; Me puedes permitir el codigo de barras del producto");
                deteccionPersonas.DetenerMovimiento();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.elbyte.development.retailme");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            });

            btnRecomendacion.setOnClickListener(v -> {
                ttsManager.initQueue("Escoja para que tipo de eventos busca");
                Intent recomendation = new Intent(Option_Accion.this, Option_Accion.class);
                startActivity(recomendation);
            });

            btnHome.setOnClickListener(v -> {
                ttsManager.initQueue("Recuerde que estoy a su servicio en cualquier momento");
                Intent home = new Intent(Option_Accion.this, VideosActivity.class);
                startActivity(home);
            });

micro.setOnClickListener(v ->{
    ttsManager.initQueue("Me puedes decir que necesitas; a veces no escucho bien, habla claro por favor");
    Robot.getInstance().askQuestion("");
});
onWakeupWord(Robot.getInstance().getWakeupWord(),0);
    }

    @Override
    public void onDetectionStateChanged(int state) {
        if(state == OnDetectionStateChangedListener.DETECTED){

            if(ttsManager.isSpeach()){
                ttsManager.shutDown();
                ttsManager.Stop();
            }
            ttsManager.addQueue("Si quieres te puedo ayudar; Si seleccionas en ¿Dónde Esta?; Me ire contigo a buscar el articulo deseado; Si seleccionas ¿Cual es mi precio?; necesitare que escanees la botella de tu preferencia; Si seleccionas recomendaciones te puedo decir que botella te conviene mejor para tu evento");

        }else
        if (state == OnDetectionStateChangedListener.IDLE) {
            if(ttsManager.isSpeach()){
                ttsManager.shutDown();
                ttsManager.Stop();
            }
            deteccionPersonas.DetenerMovimiento();
            ttsManager.initQueue("Esta bien que tenga un excelente día; hasta luego");
            Intent main = new Intent(Option_Accion.this, VideosActivity.class);
            startActivity(main);
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OptionAccion","OnStart_Option");
        deteccionPersonas.addListener(null,Option_Accion.this);
        Robot.getInstance().addAsrListener(this);
        Robot.getInstance().addWakeupWordListener(this);
        Robot.getInstance().addNlpListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("OptionAccion","OnStop_Option");
        deteccionPersonas.removeListener(null,Option_Accion.this);
    Robot.getInstance().removeAsrListener(this);
    Robot.getInstance().removeNlpListener(this);
    Robot.getInstance().removeWakeupWordListener(this);
    }

    @Override
    public void onWakeupWord(@NotNull String wakeupWord, int direction) {

    }

    @Override
    public void onAsrResult(@NotNull String asrResult) {
if(asrResult.contains("¿Quién eres tú?")){
    ttsManager.initQueue("Hola yo soy tu asistente robot; me puedes llamar jarvis");
}else if(asrResult.equalsIgnoreCase("Hola")){
    ttsManager.initQueue("Hola como estas");

}
    }

    @Override
    public void onNlpCompleted(@NotNull NlpResult nlpResult) {

    }
}