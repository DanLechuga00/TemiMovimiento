package com.temi.app2;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SecuenciaDeMovimiento extends AppCompatActivity implements OnGoToLocationStatusChangedListener {
    private final Robot robot;
    private String PositionActual = "";
    private int ContadorPositiones = 0;
    private int ContadorPositionesTotales = 0;
    private final String TAG = "SecuenciaDeMovimiento";
    private TTSManager ttsManager;

    public SecuenciaDeMovimiento(TTSManager ttsManager) {
        this.robot = Robot.getInstance(); this.ttsManager = ttsManager;
    }


    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, @NotNull String status, int descriptionId, @NotNull String description) {
        Log.d(TAG, "onGoToLocationStatusChanged= location:" + location + "\n" + "status: " + status + "\n" + "descriptionId: " + descriptionId + "\n" + "description: " + description);
        switch (status) {
            case OnGoToLocationStatusChangedListener.START:
                this.PositionActual = location;
                break;

            case "obstacle detected":
                if(ttsManager.isSpeach()) ttsManager.shutDown();
                ttsManager.initQueue("Se detecto un obstaculo; Por favor retirate ");
                break;

                case OnGoToLocationStatusChangedListener.COMPLETE:
                Log.d(TAG,"Contador:"+ContadorPositiones);
                Log.d(TAG,"ContadorTotal: "+ContadorPositionesTotales);
                if(ContadorPositiones >= ContadorPositionesTotales) {
                    ContadorPositiones = 0;
                }
                else{
                    ContadorPositiones++;
                }
                break;

        }
    }
    public  List<String> getLocations(){
        ContadorPositionesTotales = robot.getLocations().size()-1;
        return robot.getLocations();
    }

    public void Secuencia() {
        List<String> ubicaciones = getLocations();
        if(!ubicaciones.isEmpty()){
            ubicaciones.remove("home base".toLowerCase());
                System.out.println("Ubicacion:"+PositionActual);
                if(ContadorPositiones >= ContadorPositionesTotales) ContadorPositiones = 0;
                while (ContadorPositiones <= ContadorPositionesTotales) {
                    robot.goTo(ubicaciones.get(ContadorPositiones));
                    break;
                }

        }
    }
    public void addListener(){
        robot.addOnGoToLocationStatusChangedListener(SecuenciaDeMovimiento.this);
    }
    public  void removeListener(){
        robot.addOnGoToLocationStatusChangedListener(SecuenciaDeMovimiento.this);
    }
}
