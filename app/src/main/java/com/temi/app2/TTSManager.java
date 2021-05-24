package com.temi.app2;

import android.content.Context;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;
import java.util.Map;

public class TTSManager {
    private TextToSpeech aTts = null;
    private boolean isLoaded = false;

    public void init(Context context){
        try {
            aTts = new TextToSpeech(context, onInitListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private TextToSpeech.OnInitListener onInitListener = status -> {
        Locale spanish = new Locale("es", "MX");
        if (status == TextToSpeech.SUCCESS){
            int result = aTts.setLanguage(spanish);
            isLoaded = true;

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("error", "Este leguaje no esta permitido");
            }
        }
    };

    public void shutDown(){
        aTts.shutdown();
    }


    public  boolean isSpeach(){
        return aTts.isSpeaking();
    }

    public void addQueue(String text){
        if (isLoaded){
            aTts.speak(text,TextToSpeech.QUEUE_ADD, null,"");
        }else {
            Log.e("error", "El TTS no se esta utilizando");
        }
    }

    public void initQueue(String text){
        if (isLoaded){
            aTts.speak(text, TextToSpeech.QUEUE_ADD, null,"");
        }else {
            Log.e("error", "El TTS no se esta utilizando");
        }
    }
}
