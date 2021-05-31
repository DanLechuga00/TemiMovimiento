package com.temi.app2;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.sequence.SequenceModel;

import java.util.List;

public class SecuenciaDeMovimiento extends AppCompatActivity {
    private final Robot robot;
    private volatile List<SequenceModel> allSequence;

    public SecuenciaDeMovimiento() {
        this.robot = Robot.getInstance();


    }

    public void playSequence(boolean withPlayer) {
        getAllSequence();
        if (allSequence != null && !allSequence.isEmpty()) {
            robot.playSequence(allSequence.get(0).getId(), withPlayer);
        }
    }

    private void getAllSequence() {
        new Thread(() -> {
            allSequence = robot.getAllSequences();
runOnUiThread(() ->{
    for (SequenceModel sequenceModel : allSequence) {
        if (sequenceModel == null) {
            continue;
        }
        Log.d("Sequence","The sequence: "+sequenceModel.toString());
    }
});
        }).start();
    }
}
