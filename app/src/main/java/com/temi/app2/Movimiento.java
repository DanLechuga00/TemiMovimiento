package com.temi.app2;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;
import com.robotemi.sdk.navigation.model.Position;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public  final class Movimiento  implements
        OnLocationsUpdatedListener,
        OnGoToLocationStatusChangedListener,
        OnDistanceToLocationChangedListener,
        OnCurrentPositionChangedListener,
        OnReposeStatusChangedListener,
        OnLoadMapStatusChangedListener,
        OnRobotReadyListener {
    public Robot robot;
    public TextToSpeech textToSpeech;
    public  Context context;
    public  MainActivity main;
    public  Movimiento(Context context, BusquedaArticulos busquedaArticulos){
        this.context = context;
        this.main = main;
        robot = Robot.getInstance();
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                   int result = textToSpeech.setLanguage(new Locale("es","US"));
                   if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                       System.out.println("Idioma no soportado");
                       Log.d("Lenguaje","Idioma no soportado");
                   }
                }
            }
        });
	robot.addOnLoadMapStatusChangedListener(this);
    }
    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, String status, int descriptionId, @NotNull String description) {
        textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, "");
        switch (status) {
            case OnGoToLocationStatusChangedListener.START:
                textToSpeech.speak("Iniciando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.CALCULATING:
                textToSpeech.speak("Calculando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.GOING:
                textToSpeech.speak("Caminando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.COMPLETE:
                textToSpeech.speak("Completado", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.ABORT:
                textToSpeech.speak("Abortando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;

        }
    }

    @Override
    public void onLocationsUpdated(@NotNull List<String> locations) {

        //    Toast.makeText(this, "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadMapStatusChanged(int status) {
        //  Toast.makeText(this,"load map status: "+status,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentPositionChanged(@NotNull Position position) {
        //Toast.makeText(this,"onCurrentPositionChanged"+position.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDistanceToLocationChanged(@NotNull Map<String, Float> distances) {
      /*  for (String location : distances.keySet()) {
            Toast.makeText(this, "onDistanceToLocation" + location + "location" + distances.get(location), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onReposeStatusChanged(int status, @NotNull String description) {
        // Toast.makeText(this, "repose status: " + status + "Description:" + description, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(main.getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public  void goTo(String location){
        for (String ubicacion : robot.getLocations()){
            if(ubicacion.equals(location.toLowerCase().trim())){
                robot.goTo(location.toLowerCase().trim());
            }
        }
    }
}
