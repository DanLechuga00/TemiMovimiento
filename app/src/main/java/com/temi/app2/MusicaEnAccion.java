package com.temi.app2;

import android.media.MediaPlayer;

public class MusicaEnAccion extends MainActivity
{
    private MediaPlayer mediaPlayer;
    public MusicaEnAccion(){
        mediaPlayer = MediaPlayer.create(this,R.raw.musica);
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
