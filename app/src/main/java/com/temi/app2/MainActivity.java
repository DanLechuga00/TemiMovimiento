package com.temi.app2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements
        OnLocationsUpdatedListener,
        OnGoToLocationStatusChangedListener,
        OnDistanceToLocationChangedListener,
        OnCurrentPositionChangedListener,
        OnReposeStatusChangedListener,
        OnLoadMapStatusChangedListener,
        OnRobotReadyListener {
 private Robot robot;
 private TextToSpeech textToSpeech;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        robot = Robot.getInstance();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(new Locale("es","US"));
                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                        System.out.println("Lenguaje no soportado");
                        Log.w("TextToSpeach","Lenguaje no soportado");
                    }
                }
            }
        });
        robot.addOnLoadMapStatusChangedListener(this);

    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);
        robot.addOnLocationsUpdatedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addOnDistanceToLocationChangedListener(this);
        robot.addOnCurrentPositionChangedListener(this);
        robot.addOnReposeStatusChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnRobotReadyListener(this);
        robot.removeOnLocationsUpdateListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeOnDistanceToLocationChangedListener(this);
        robot.removeOnCurrentPositionChangedListener(this);
        robot.removeOnReposeStatusChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        robot.removeOnLoadMapStatusChangedListener(this);
        if(!executorService.isShutdown()){
            executorService.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String location,  String status, int descriptionId, @NotNull String description) {
        textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, "");
        switch (status) {
            case OnGoToLocationStatusChangedListener.START:
                textToSpeech.speak("Iniciando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.CALCULATING:
                textToSpeech.speak("Calculando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.GOING:
                textToSpeech.speak("Caminando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.COMPLETE:
                textToSpeech.speak("Completado", TextToSpeech.QUEUE_FLUSH, null, "");
                break;
            case OnGoToLocationStatusChangedListener.ABORT:
                textToSpeech.speak("Abortando", TextToSpeech.QUEUE_FLUSH, null, "");
                break;

        }
    }

    @Override
    public void onLocationsUpdated(@NotNull List<String> locations) {

    //    Toast.makeText(this, "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadMapStatusChanged(int status) {
      //  Toast.makeText(this,"load map status: "+status,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentPositionChanged(@NotNull Position position) {
    //Toast.makeText(this,"onCurrentPositionChanged"+position.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDistanceToLocationChanged(@NotNull Map<String, Float> distances) {
      /*  for (String location : distances.keySet()) {
            Toast.makeText(this, "onDistanceToLocation" + location + "location" + distances.get(location), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onReposeStatusChanged(int status, @NotNull String description) {
       // Toast.makeText(this, "repose status: " + status + "Description:" + description, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
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
        hideKeyboard();
    }
}
