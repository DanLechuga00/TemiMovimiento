package com.temi.app2;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicaEnAccion
{
    private MediaPlayer mediaPlayer;
    public MusicaEnAccion(Context context){
        mediaPlayer = MediaPlayer.create(context,R.raw.musica);
        mediaPlayer.setLooping(true);
    }
    public  void Pause(){
        mediaPlayer.pause();
    }
    public  void Start(){
        mediaPlayer.start();
    }
    public  void Stop(){
        mediaPlayer.stop();
    }


}
