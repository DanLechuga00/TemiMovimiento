package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Option_Recomendation extends AppCompatActivity {

    TTSManager ttsManager = null;

    private ImageButton btnPareja, btnAmigos, btnBack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_recomendation);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        btnPareja = findViewById(R.id.btnPareja);
        btnAmigos = findViewById(R.id.btnAmigos);
        btnBack2 = findViewById(R.id.btnBack2);

        btnPareja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("Para una ocación con la pareja se le recomienda etiqueta Dorada");
            }
        });

        btnAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("Para una ocación con amigos se le recomienda etiqueta roja");
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresar = new Intent(Option_Recomendation.this, Option_Accion.class);
                startActivity(regresar);
            }
        });
    }
}