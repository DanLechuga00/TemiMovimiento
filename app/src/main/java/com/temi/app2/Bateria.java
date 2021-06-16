package com.temi.app2;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;


public final class Bateria implements OnRobotReadyListener {
    public Robot robot;

    public TTSManager ttsManager;
    public Movimiento movimiento;
    public Context context;
    public  AppCompatActivity main;
private final String TAG = "Bateria";
    public Bateria(Movimiento movimiento, Context context, AppCompatActivity main) {
        this.robot = Robot.getInstance();
        if (robot == null) throw new NullPointerException("robot = null");
        this.ttsManager = new TTSManager();
        this.ttsManager.init(main);
        this.movimiento = movimiento;
        this.context = context;
        this.main = main;
    }

    public boolean EsBateriaBaja() {
        boolean result = false;
          BatteryData  batteryData = robot.getBatteryData();
          if(batteryData != null){
              if (!batteryData.isCharging() && batteryData.getBatteryPercentage() <= 20) {
                  ttsManager.initQueue("Se detecto bateria baja; Voy a recargar bateria");
                  movimiento.goTo("home base");
                  Log.d(TAG,"Nivel de bateria:"+batteryData.getBatteryPercentage());
                  result = true;
              }
          }
        return result;
    }
    public  boolean EstaCargando(){
        BatteryData batteryData = robot.getBatteryData();
        if(batteryData != null){
            Log.d(TAG,"Nivel de bateria:"+batteryData.getBatteryPercentage());
            if(batteryData.isCharging() && EsBateriaCompleta()){
                Log.d(TAG,"Carga completa");
                return false;
            }

            return  batteryData.isCharging();
        }
        return false;
    }
    public  boolean EsBateriaCompleta(){
        BatteryData batteryData = robot.getBatteryData();
        if(batteryData != null){
            Log.d(TAG,"Nivel de bateria:"+batteryData.getBatteryPercentage());
            return batteryData.getBatteryPercentage() <= 100 && batteryData.getBatteryPercentage()>= 20;
        }
        return false;
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
}
