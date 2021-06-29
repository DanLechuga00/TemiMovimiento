package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.robotemi.sdk.Robot;

public class Option_Recomendation extends AppCompatActivity {

    TTSManager ttsManager = null;

    private ImageButton btnPareja, btnAmigos, btnBack2;
    private final BaseDeDatos baseDeDatos = new BaseDeDatos();
    private final String TAGBase = "Usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_recomendation);

        ttsManager = new TTSManager();
        ttsManager.init(this);
        if(ttsManager.isSpeach()){
            ttsManager.shutDown();
            ttsManager.Stop();
        }
        baseDeDatos.CrearBitacoraDeRegistros(2,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());

        btnPareja = findViewById(R.id.button1);
        btnAmigos = findViewById(R.id.button2);
        btnBack2 = findViewById(R.id.btnBack2);

        btnPareja.setOnClickListener(v -> {
            baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            ttsManager.initQueue("Buscamos leche para nido");
            ttsManager.initQueue("Puedo apoyarlo en algo más");
            Intent opcion = new Intent(Option_Recomendation.this, Help_Decition.class);
            startActivity(opcion);
        });

        btnAmigos.setOnClickListener(v -> {
            baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            ttsManager.initQueue("Un buen cereal siempre es algo que tu y los tuyos disfrutan");
            ttsManager.initQueue("Le puedo ayudar en algo más");
            Intent opcion = new Intent(Option_Recomendation.this, Help_Decition.class);
            startActivity(opcion);
        });

        btnBack2.setOnClickListener(v -> {
            baseDeDatos.CrearBitacoraDeRegistros(9,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
            Intent regresar = new Intent(Option_Recomendation.this, Option_Accion.class);
            startActivity(regresar);
        });
    }
}