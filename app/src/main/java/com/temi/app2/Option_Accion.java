package com.temi.app2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;

import java.util.ArrayList;
import java.util.Locale;

public class Option_Accion extends AppCompatActivity implements OnDetectionStateChangedListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    DeteccionPersonas deteccionPersonas = null;
    BaseDeDatos baseDeDatos = null;
    private final  static  String TAGBase = "Usuario";
    private final static String TAGError = "Exception";

    private ImageButton btnDonde, btnRecomendacion, btnHome, btnCosto, btnMicrofono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_accion);

        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()){
            ttsManager.shutDown();
            ttsManager.Stop();
        }
        baseDeDatos = new BaseDeDatos();
        movimiento = new Movimiento(this, Option_Accion.this, ttsManager);
        bateria = new Bateria(movimiento, this, Option_Accion.this);
        btnDonde = findViewById(R.id.btnDonde);
        btnRecomendacion = findViewById(R.id.btnRecomendacion);
        btnHome = findViewById(R.id.btnHome);
        btnCosto = findViewById(R.id.btnCosto);
        btnMicrofono = findViewById(R.id.btnMicrofono);
        deteccionPersonas = new DeteccionPersonas();
        deteccionPersonas.DetenerMovimiento();
        try {
            baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.getInstance().getNickName());
        } catch (Exception e) {
            Log.e(TAGError,"Error:"+e.getMessage());
        }

        btnDonde.setOnClickListener(v -> {
            try {
                baseDeDatos.CrearBitacoraDeRegistros(1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, TAGBase, Robot.getInstance().getNickName());
                ttsManager.initQueue("¿Qué articulo deseas encontrar?");
                Intent search = new Intent(Option_Accion.this, BusquedaArticulos.class);
                startActivity(search);
            } catch (Exception e) {
                Log.e(TAGError, "Error:" + e.getMessage());
            }
        });

            btnCosto.setOnClickListener(v -> {
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(3,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.getInstance().getNickName());
                    ttsManager.initQueue("Necesito tu apoyo por favor; Me puedes permitir el codigo de barras del producto");
                    deteccionPersonas.DetenerMovimiento();
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.elbyte.development.retailme");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    } } catch (Exception e) {
                Log.e(TAGError,"Error:"+e.getMessage());
                }
            });

            btnRecomendacion.setOnClickListener(v -> {
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(2,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                    ttsManager.initQueue("Escoja para que tipo de eventos busca");
                    Intent recomendation = new Intent(Option_Accion.this, Option_Accion.class);
                    startActivity(recomendation);} catch (Exception e) {
                    Log.e(TAGError,"Error:"+e.getMessage());
                }

            });

            btnHome.setOnClickListener(v -> {
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(9,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                    ttsManager.initQueue("Recuerde que estoy a su servicio en cualquier momento");
                    Intent home = new Intent(Option_Accion.this, VideosActivity.class);
                    startActivity(home);} catch (Exception e) {
                    Log.e(TAGError,"Error:"+e.getMessage());
                }

            });

            btnMicrofono.setOnClickListener(v -> {
                try {
                    baseDeDatos.CrearBitacoraDeRegistros(10,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                    ttsManager.initQueue("En que le puedo ayudar");
                    iniciarEntradaVoz(); } catch (Exception e) {
                    Log.e(TAGError,"Error:"+e.getMessage());
                }

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
            Intent main = new Intent(Option_Accion.this, VideosActivity.class);
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
                    if (peticion.equals("Llévame al pasillo del whisky")){
                        movimiento.goTo("whisky");
                    }
                    if (peticion.equals("Llévame al pasillo del mezcal")){
                        movimiento.goTo("mezcal");
                    }
                    if (peticion.equals("Llévame al pasillo del tequila")){
                        movimiento.goTo("tequila");
                    }
                }
                break;
            }
        }
    }
}