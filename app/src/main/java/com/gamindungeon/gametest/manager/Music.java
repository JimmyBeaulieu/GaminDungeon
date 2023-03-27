package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.gamindungeon.gametest.R;

public class Music {
    private final Context context;
    MediaPlayer mp;
    int song;

    public Music(Context context){

        mp = new MediaPlayer();
        this.context = context;
    }

    public void play(int songNumber){

        switch(songNumber){
            case 0:
                mp = MediaPlayer.create(context, R.raw.dungeon1);
                break;
            case 1:
                mp = MediaPlayer.create(context, R.raw.exploration);
                break;
            case 2:
                mp = MediaPlayer.create(context, R.raw.song1);
                break;
        }
        if (!mp.isPlaying())
        {
            mp.start();
            song = songNumber;
            mp.setLooping(true);
        }

    }
    public void playSFX(int sfwNumber){
        switch(sfwNumber){
            case 0:
                //pick up coin
                mp= MediaPlayer.create(context, R.raw.pickupcoin);
                break;
            case 1:
                //fighting
                mp= MediaPlayer.create(context, R.raw.hithurt);
                break;
            case 2:
                //eat food
                mp = MediaPlayer.create(context, R.raw.eat);
                break;
        }
        mp.start();
    }

    public void pause(){
        mp.pause();
    }
    public void stop(){
        mp.stop();
    }
}
