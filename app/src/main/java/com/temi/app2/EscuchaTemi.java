package com.temi.app2;

import android.content.Context;
import android.util.Log;

import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;

import org.jetbrains.annotations.NotNull;

public class EscuchaTemi implements Robot.NlpListener,Robot.AsrListener,Robot.WakeupWordListener {
private static final String TAG = "EscuchaTeemi";
private  final Robot robot;
private final TTSManager ttsManager = null;
private final Context context;
public EscuchaTemi(Context context){
    this.context = context;
    robot = Robot.getInstance();
    ttsManager.init(this.context);
    if(ttsManager.isSpeach()){
        ttsManager.Stop();
        ttsManager.shutDown();
    }

}

    @Override
    public void onWakeupWord(@NotNull String wakeupWord, int direction) {
    //Palabra registrada word:
     Log.d(TAG,"word:"+wakeupWord+"direction:"+direction);

    }

    @Override
    public void onAsrResult(@NotNull String asrResult) {
Log.d(TAG,"Lo que escuche fue.."+asrResult);
if(asrResult.contains("Hola")) {
    ttsManager.initQueue("hola me permites reconocerte");
}else if(asrResult.equalsIgnoreCase("")){
    ttsManager.initQueue("Aveces hablo solo");
}else if(asrResult.contains("quien eres tu")){
    ttsManager.initQueue("Yo soy Temi,botsito para los amigos");
    }

    }

    @Override
    public void onNlpCompleted(@NotNull NlpResult nlpResult) {
    nlpResult.resolvedQuery = "Hola";
    }

    public void addListener(){
        robot.addWakeupWordListener(this);
        robot.addNlpListener(this);
        robot.addAsrListener(this);

    }
    public  void  removeListener(){
        robot.removeWakeupWordListener(this);
        robot.removeAsrListener(this);
        robot.removeNlpListener(this);
    }

}
