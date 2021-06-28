package com.temi.app2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;
import com.robotemi.sdk.navigation.model.Position;
import com.robotemi.sdk.navigation.model.SpeedLevel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public  final class Movimiento  implements
        OnLocationsUpdatedListener,
        OnGoToLocationStatusChangedListener,
        OnDistanceToLocationChangedListener,
        OnCurrentPositionChangedListener,
        OnReposeStatusChangedListener,
        OnLoadMapStatusChangedListener,
        OnRobotReadyListener {

    public Robot robot;
    public  TTSManager ttsManager;
    public  Context context;
    public  AppCompatActivity main;

    public  Movimiento(Context context, AppCompatActivity main , TTSManager ttsManager){
        this.context = context;
        this.main = main;
        robot = Robot.getInstance();
        this.ttsManager = ttsManager;
       	robot.addOnLoadMapStatusChangedListener(this);
    }



    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, String status, int descriptionId, @NotNull String description) {
        System.out.println(description);
        switch (status) {
            case OnGoToLocationStatusChangedListener.START:
                robot.setGoToSpeed(SpeedLevel.MEDIUM);
                robot.setVolume(4);

                //ttsManager.addQueue("Iniciando");
                break;
            case OnGoToLocationStatusChangedListener.CALCULATING:
                //ttsManager.addQueue("Calculando");
                break;
            case OnGoToLocationStatusChangedListener.GOING:
                robot.setGoToSpeed(SpeedLevel.MEDIUM);
                //ttsManager.addQueue("Caminando");
                break;
            case OnGoToLocationStatusChangedListener.COMPLETE:
                robot.setVolume(3);

                ttsManager.initQueue("En este pasillo se encuentra su artículo");
                ttsManager.initQueue("¿Le puedo ayudar en algo más?");
                if(ttsManager.isSpeach()){
                    ttsManager.Stop();
                }
                Intent help = new Intent(main, Help_Decition.class);
                main.startActivity(help);
                break;
            case OnGoToLocationStatusChangedListener.ABORT:
                //ttsManager.addQueue("Abortando");
                break;
        }
    }

    @Override
    public void onLocationsUpdated(@NotNull List<String> locations) {
    Log.d("onLocationsUpdated","Locatios: "+locations.toString());
        //    Toast.makeText(this, "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadMapStatusChanged(int status) {
        Log.d("onLoadMapStatusChanged","Status Map:"+status);
        //  Toast.makeText(this,"load map status: "+status,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentPositionChanged(@NotNull Position position) {
        Log.d("onCurrentPosition","Position: "+position.toString());
        //Toast.makeText(this,"onCurrentPositionChanged"+position.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDistanceToLocationChanged(@NotNull Map<String, Float> distances) {
       for (String location : distances.keySet()) {
            Log.d("onDistanceTo","ToLocations: "+location);
        }
    }

    @Override
    public void onReposeStatusChanged(int status, @NotNull String description) {
       Log.d("onReposeStatus","Status: "+status+"\n"+"Description: "+description);
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

    public void bailar() {
        ttsManager.initQueue("Solo fijate en esto");
        robot.turnBy(360,1F);
    }
    public  void MoonWalk(){
        long t = System.currentTimeMillis();
        long end = t + 700;
        while (System.currentTimeMillis() < end){
            robot.skidJoy(-1F,1F);
        }
    }
    public void addListener(){
        robot.addOnGoToLocationStatusChangedListener(this);

    }
    public  void removeListener(){
        robot.removeOnGoToLocationStatusChangedListener(this);
    }
public  void MediaVuelta(){
        Log.d("Movimiento","Girar el robot a 180°");
        robot.turnBy(180,1F);
        NombreRobot();
}
public  void LevantaCabeza(){
        Log.d("Movimiemto","Levantar la cabeza del robot");
        robot.tiltBy(30);
    }
    public void NombreRobot (){
        Log.d("Info_Robot","Nombre del robot:"+robot.getNickName()+"\n"+"Numero de serie del robot:"+robot.getSerialNumber()+"\n"+"Version de robox:"+robot.getRoboxVersion()+"\n"+"Version del Launcher:"+robot.getLauncherVersion());

    }

}
