package com.temi.app2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TTSManager ttsManager = null;

    private ImageButton btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        btnHelp = findViewById(R.id.help);
        btnHelp.setOnClickListener(v -> {
            ttsManager.initQueue("Buen día ¿En qué le puedo ayudar?");
            Intent sig = new Intent(MainActivity.this, Option_Accion.class);
            startActivity(sig);
        });
    }

}