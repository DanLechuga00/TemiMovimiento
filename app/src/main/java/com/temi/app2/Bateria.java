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

    public Bateria(TTSManager ttsManager, Movimiento movimiento, Context context, AppCompatActivity main) {
        this.robot = Robot.getInstance();
        if (robot == null) throw new NullPointerException("robot = null");
        this.ttsManager = ttsManager;
        this.movimiento = movimiento;
        this.context = context;
        this.main = main;
    }

    public boolean EsBateriaBaja() {
        boolean result = false;
          BatteryData  batteryData = robot.getBatteryData();
          if(batteryData != null){
              if (!batteryData.isCharging() && batteryData.getBatteryPercentage() <= 50) {
                  ttsManager.initQueue("Me regreso a casa para recargar bateria");
                  movimiento.goTo("home base");
                  result = true;
              }
          }
        return result;
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
