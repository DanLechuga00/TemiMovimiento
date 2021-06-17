package com.temi.app2;

import android.util.Log;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;

import kotlin.jvm.Throws;

public class DeteccionPersonas {
    private final Robot robot;
    public  DeteccionPersonas(){
        this.robot = Robot.getInstance();

        if(robot == null) throw new NullPointerException("robot = null");

    }

    public void startDetectionModeWithDistance() {
        try {
            String distanceString = "0.8";
            float distance = Float.parseFloat(distanceString);
            robot.setDetectionModeOn(true, distance);
            Log.d("Detection", "Deteccion habilitada");
            System.out.println("Detection = true");
        } catch (Exception e) {
            Log.w("Detection", "Error: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void ConstanteJuntoAMi() {
        robot.constraintBeWith();
    }

    public void DetenerMovimiento() {
        robot.stopMovement();
    }

    public void addListener(OnDetectionDataChangedListener dataChangedListener, OnDetectionStateChangedListener stateChangedListener) {
        Log.d("DeteccionPersonas","AddListener");
        if(dataChangedListener != null)robot.addOnDetectionDataChangedListener(dataChangedListener);
        robot.addOnDetectionStateChangedListener(stateChangedListener);
    }

    public void removeListener(OnDetectionDataChangedListener dataChangedListener, OnDetectionStateChangedListener stateChangedListener) {
        Log.d("DeteccionDePersonas","RemoveListener");
        if(dataChangedListener != null)robot.removeOnDetectionDataChangedListener(dataChangedListener);
        robot.removeOnDetectionStateChangedListener(stateChangedListener);
    }


}
