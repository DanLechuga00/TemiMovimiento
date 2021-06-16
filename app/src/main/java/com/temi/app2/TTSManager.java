package com.temi.app2;

import android.content.Context;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TTSManager {
    private TextToSpeech aTts = null;
    private boolean isLoaded = false;
    HashMap<String,String> params = new HashMap<>();
private String TAG = "TTSManager";


    public void init(Context context){
        try {
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,TAG);
            aTts = new TextToSpeech(context, onInitListener,params.get(TAG));
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private TextToSpeech.OnInitListener onInitListener = status -> {
        Locale spanish = new Locale("es", "US");
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
        return aTts.isSpeaking() && aTts != null;
    }
    public void Stop(){aTts.stop();}

    public void addQueue(String text){
        if (isLoaded){
            aTts.speak(text,TextToSpeech.QUEUE_FLUSH, null,"");
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
