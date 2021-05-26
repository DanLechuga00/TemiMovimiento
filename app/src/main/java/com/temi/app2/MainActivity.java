package com.temi.app2;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    TTSManager ttsManager = null;
    Movimiento movimiento = null;
    Bateria bateria = null;
    //private ImageButton btnHelp;
    private Button btnHelp;
    private VideoView vV;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        verifyStoragePermissions(this);
        ttsManager = new TTSManager();
        ttsManager.init(this);
        movimiento = new Movimiento(this,MainActivity.this,ttsManager);
        bateria = new Bateria(movimiento,this,MainActivity.this);
        if(!bateria.EsBateriaBaja()){
            vV = findViewById(R.id.vV);
            vV.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cocacola));
           vV.setOnPreparedListener(mp -> mp.setLooping(true));

            vV.start();
            btnHelp = findViewById(R.id.btnHelp);
            btnHelp.setOnClickListener(v -> {
                ttsManager.initQueue("Buen día ¿En qué le puedo ayudar?");
                Intent sig = new Intent(MainActivity.this, Option_Accion.class);
                startActivity(sig);
            });
            //btnHelp = findViewById(R.id.help);
            /*btnHelp.setOnClickListener(v -> {
                ttsManager.initQueue("Buen día ¿En qué le puedo ayudar?");
                Intent sig = new Intent(MainActivity.this, Option_Accion.class);
                startActivity(sig);
            });*/
        }

    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

}