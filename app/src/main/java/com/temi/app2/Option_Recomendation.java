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

        btnPareja.setOnClickListener(v -> {
            ttsManager.initQueue("Para una ocación con la pareja se le recomienda etiqueta Dorada");
            ttsManager.initQueue("Le puedo ayudar en algo más");
            Intent opcion = new Intent(Option_Recomendation.this, Help_Decition.class);
            startActivity(opcion);
        });

        btnAmigos.setOnClickListener(v -> {
            ttsManager.initQueue("Para una ocación con amigos se le recomienda etiqueta roja");
            ttsManager.initQueue("Le puedo ayudar en algo más");
            Intent opcion = new Intent(Option_Recomendation.this, Help_Decition.class);
            startActivity(opcion);
        });

        btnBack2.setOnClickListener(v -> {
            Intent regresar = new Intent(Option_Recomendation.this, Option_Accion.class);
            startActivity(regresar);
        });
    }
}