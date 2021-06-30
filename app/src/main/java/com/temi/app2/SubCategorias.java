package com.temi.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.robotemi.sdk.Robot;

public class SubCategorias extends AppCompatActivity {

    private ImageButton btnRegreso, btnByW, btnJW, btnJD;
    TTSManager ttsManager = new TTSManager();
    Movimiento movimiento = null;
    private final static  String TAGError = "Exception";
    private final static String TAGBase = "Usuario";
    private final static  BaseDeDatos baseDeDatos = new BaseDeDatos();

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
            try {
                baseDeDatos.CrearBitacoraDeRegistros(9,(byte)1,(byte)1,(byte)1,(byte)0,(byte)1,TAGBase, Robot.getInstance().getNickName());
                Intent regreso =  new Intent(SubCategorias.this, BusquedaArticulos.class);
                startActivity(regreso);
            }catch (Exception e){
                Log.e(TAGError,"Error:"+e.getMessage());
            }

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
}