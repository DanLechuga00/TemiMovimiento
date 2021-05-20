package com.temi.app2;

import android.content.Context;
import android.util.Log;

import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.Robot;

public  final class Bateria {
    public Robot robot;
    public Context context;
    public BatteryData batteryData;
    public  TTSManager ttsManager;
    public  Movimiento movimiento;
    public  Bateria(Context context, TTSManager ttsManager, Movimiento movimiento){
        this.context = context;
        this.robot = Robot.getInstance();
        if(robot == null) throw  new NullPointerException("robot = null");
        batteryData = robot.getBatteryData();
        this.ttsManager = ttsManager;
        this.movimiento = movimiento;
        if(batteryData == null){
            System.out.println("La bateria es nula");
            Log.d("Bateria","Bateria nula");
            throw new NullPointerException("batteryData = null");
        }
    }

    public  boolean EsBateriaBaja(){
       boolean result = false;

        if(!batteryData.isCharging() && batteryData.getBatteryPercentage() <= 20){
            ttsManager.addQueue("Me regreso a casa para recargar bateria");
            movimiento.goTo("home base");
            result = true;
        }
        return result;
    }
}
