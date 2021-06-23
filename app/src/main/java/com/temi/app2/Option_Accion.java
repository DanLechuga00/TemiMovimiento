package com.temi.app2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.model.DetectionData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class Option_Accion<onAc> extends AppCompatActivity implements OnDetectionStateChangedListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    DeteccionPersonas deteccionPersonas = null;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private ImageButton btnDonde, btnRecomendacion, btnHome, btnCosto, btnmicrofono;

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
        btnHome = findViewById(R.id.btnHome);
        btnCosto = findViewById(R.id.btnCosto);
        btnmicrofono = findViewById(R.id.microfono);
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
                Intent recomendation = new Intent(Option_Accion.this, Option_Recomendation.class);
                startActivity(recomendation);
            });

            btnHome.setOnClickListener(v -> {
                ttsManager.initQueue("Recuerde que estoy a su servicio en cualquier momento");
                Intent home = new Intent(Option_Accion.this, MainActivity.class);
                startActivity(home);
            });

            btnmicrofono.setOnClickListener(v -> {
                ttsManager.initQueue("En que te puedo ayudar");
                iniciarEntradaVoz();
            });

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
            Intent main = new Intent(Option_Accion.this,MainActivity.class);
            startActivity(main);
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OptionAccion","OnStart_Option");
        deteccionPersonas.addListener(null,Option_Accion.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("OptionAccion","OnStop_Option");
        deteccionPersonas.removeListener(null,Option_Accion.this);
    }

    private void iniciarEntradaVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hola dime lo que sea");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, ""+ e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Toast.makeText(this, "Tu dijiste: " + result.get(0), Toast.LENGTH_LONG).show();
                    String peticion = result.get(0);
                    Toast.makeText(this, "Tu dijiste: " + peticion, Toast.LENGTH_LONG).show();
                    if (peticion.equals("Llévame al pasillo de cereales")){
                        movimiento.goTo("cereales");
                    }
                    if (peticion.equals("Llévame al pasillo de café")){
                        movimiento.goTo("nescafe");
                    }
                    if (peticion.equals("Llévame al pasillo de nutrición")){
                        movimiento.goTo("nutricion");
                    }
                    if (peticion.equals("Llévame al pasillo de chocolates")){
                        movimiento.goTo("chocolates");
                    }
                }
                break;
            }
        }
    }
}