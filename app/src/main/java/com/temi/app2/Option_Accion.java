package com.temi.app2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Option_Accion extends AppCompatActivity {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;

    private ImageButton btnDonde, btnRecomendacion, btnHome, btnCosto;

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

            btnDonde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ttsManager.initQueue("¿Qué articulo deseas encontrar?");
                    Intent search = new Intent(Option_Accion.this, BusquedaArticulos.class);
                    startActivity(search);
                }
            });

            btnCosto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.codigo2");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }
                }
            });

            btnRecomendacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ttsManager.initQueue("Escoja para que tipo de eventos busca");
                    Intent recomendation = new Intent(Option_Accion.this, Option_Recomendation.class);
                    startActivity(recomendation);
                }
            });

            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ttsManager.initQueue("Recuerde que estoy a su servicio en cualquier momento");
                    Intent home = new Intent(Option_Accion.this, MainActivity.class);
                    startActivity(home);
                }
            });


    }
}