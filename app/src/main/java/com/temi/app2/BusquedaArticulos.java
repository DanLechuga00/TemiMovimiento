package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class BusquedaArticulos extends AppCompatActivity {

    TTSManager ttsManager = null;

    private ImageButton btnRefresco, btnVinosLicores, btnDulceria, btnBack1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_articulos);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        btnRefresco = findViewById(R.id.btnRefresco);
        btnVinosLicores = findViewById(R.id.btnVinosLicores);
        btnDulceria = findViewById(R.id.btnDulceria);
        btnBack1 = findViewById(R.id.btnBack1);

        btnRefresco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("Los refrescos se encuentran en el pasillo 1; sígame y le muestro su ubicación");
            }
        });

        btnVinosLicores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("Los vinos y licores se encuentran en el pasillo 2; sígame y le muestro su ubicación");
            }
        });

        btnDulceria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue("La dulceria se encuentra en el pasillo 3; sígame y le muestro su ubicación");
            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresar = new Intent(BusquedaArticulos.this, Option_Accion.class);
                startActivity(regresar);
            }
        });
    }
}