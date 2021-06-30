package com.temi.app2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SubCategorias extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private ImageButton btnRegreso, btnByW, btnJW, btnJD;
    TTSManager ttsManager = new TTSManager();
    Movimiento movimiento = null;

    @Override
    protected void onStart() {
        super.onStart();
        movimiento.addListener();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categorias);

        ttsManager.init(this);
        movimiento = new Movimiento(this, SubCategorias.this, ttsManager);

        btnRegreso = findViewById(R.id.btnRegreso);
        btnByW = findViewById(R.id.btnByW);
        btnJW =  findViewById(R.id.btnJW);
        btnJD =  findViewById(R.id.btnJD);

        btnRegreso.setOnClickListener(v -> {
            Intent regreso =  new Intent(SubCategorias.this, BusquedaArticulos.class);
            startActivity(regreso);
        });

        btnByW.setOnClickListener(v -> {
            ttsManager.initQueue("Sígame, y le muestro la ubicación del artículo");
            movimiento.goTo("whisky");
        });

        btnJW.setOnClickListener(v -> {
            ttsManager.initQueue("Sígame, y le muestro la ubicación del artículo");
            movimiento.goTo("whisky");
        });

        btnJD.setOnClickListener(v -> {
            ttsManager.initQueue("Sígame, y le muestro la ubicación del artículo");
            movimiento.goTo("whisky");
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        movimiento.removeListener();
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
                    if (peticion.equals("Llévame al pasillo del Black and White")){
                        movimiento.goTo("whisky");
                    }
                    if (peticion.equals("Llévame al pasillo del Jhonnie Walker")){
                        movimiento.goTo("whisky");
                    }
                    if (peticion.equals("Llévame al pasillo del Jack Daniels")){
                        movimiento.goTo("whisky");
                    }
                }
                break;
            }
        }
    }
}