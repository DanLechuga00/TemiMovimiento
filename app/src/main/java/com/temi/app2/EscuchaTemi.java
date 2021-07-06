package com.temi.app2;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;

import org.jetbrains.annotations.NotNull;

public class EscuchaTemi implements Robot.NlpListener,Robot.AsrListener,Robot.WakeupWordListener {
private static final String TAG = "EscuchaTeemi";
private  final Robot robot;
private  TTSManager ttsManager = null;
public EscuchaTemi(TTSManager ttsManager){
   this.ttsManager = ttsManager;
    robot = Robot.getInstance();

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
    Log.d(TAG,"nlpResult: "+nlpResult.toString());
    }

    public void addListener(){
        robot.addWakeupWordListener(this);
        robot.addNlpListener(this);
        robot.addAsrListener(this);

    }
    public  void  removeListener(){
        robot.removeWakeupWordListener(this);
        robot.removeNlpListener(this);
        robot.removeAsrListener(this);
    }

}
