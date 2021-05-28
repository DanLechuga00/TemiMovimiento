package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Help_Decition extends AppCompatActivity {

    TTSManager ttsManager = null;

    private ImageButton btnSi, btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_decition);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        btnSi = findViewById(R.id.btnSi);
        btnNo = findViewById(R.id.btnNo);



        btnSi.setOnClickListener(v -> {
            ttsManager.initQueue("Indícame en que te puedo ayudar");
            Intent sig = new Intent(Help_Decition.this, Option_Accion.class);
            startActivity(sig);
        });

        btnNo.setOnClickListener(v -> {
            ttsManager.initQueue("Bueno, recuerde que estoy a su servicio en cualquier momento");
            Intent back = new Intent(Help_Decition.this, MainActivity.class);
            startActivity(back);
        });
    }
}