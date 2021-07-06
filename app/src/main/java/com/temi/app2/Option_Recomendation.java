package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.robotemi.sdk.Robot;

public class Option_Recomendation extends AppCompatActivity {

    TTSManager ttsManager = null;
    private final static  String TAGError = "Exception";

    private ImageButton btnPareja, btnAmigos, btnBack2;
    private final BaseDeDatos baseDeDatos = new BaseDeDatos();
    private final String TAGBase = "Usuario";
    EscuchaTemi escuchaTemi = null;

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
        escuchaTemi = new EscuchaTemi(ttsManager);
        try {
           // baseDeDatos.CrearBitacoraDeRegistros(2,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
        } catch (Exception e) {

            Log.e(TAGError,"Error:"+e.getMessage());
        }

        btnPareja = findViewById(R.id.button1);
        btnAmigos = findViewById(R.id.button2);
        btnBack2 = findViewById(R.id.btnBack2);

        btnPareja.setOnClickListener(v -> {
            try {
               // baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                ttsManager.initQueue("Buscamos leche para nido");
                ttsManager.initQueue("Puedo apoyarlo en algo más");
                Intent opcion = new Intent(Option_Recomendation.this, Help_Decition.class);
                startActivity(opcion);} catch (Exception e) {
                Log.e(TAGError,"Error:"+e.getMessage());
            }

        });

        btnAmigos.setOnClickListener(v -> {
            try {
              //  baseDeDatos.CrearBitacoraDeRegistros(6,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                ttsManager.initQueue("Un buen cereal siempre es algo que tu y los tuyos disfrutan");
                ttsManager.initQueue("Le puedo ayudar en algo más");
                Intent opcion = new Intent(Option_Recomendation.this, Help_Decition.class);
                startActivity(opcion);} catch (Exception e) {
                Log.e(TAGError,"Error:"+e.getMessage());
            }

        });

        btnBack2.setOnClickListener(v -> {
            try {
               // baseDeDatos.CrearBitacoraDeRegistros(9,(byte)1,(byte)1,(byte)0,(byte)0,(byte)0,TAGBase, Robot.  getInstance().getNickName());
                Intent regresar = new Intent(Option_Recomendation.this, Option_Accion.class);
                startActivity(regresar);} catch (Exception e) {
                Log.e(TAGError,"Error:"+e.getMessage());
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        escuchaTemi.addListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        escuchaTemi.removeListener();
    }
}