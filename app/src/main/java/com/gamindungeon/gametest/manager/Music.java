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
        if(Option.isMusicOn) {
            switch (songNumber) {
                case 0:
                    mp = MediaPlayer.create(context, R.raw.mikmiclea);
                    break;
                case 1:
                    mp = MediaPlayer.create(context, R.raw.dungeon1);
                    break;
                case 2:
                    mp = MediaPlayer.create(context, R.raw.song1);
                case 3:
                    mp = MediaPlayer.create(context, R.raw.exploration);
                    break;
                case 4:
                    mp = MediaPlayer.create(context, R.raw.spin);
                    break;
                case 5:
                    mp = MediaPlayer.create(context, R.raw.spintheme);
                    break;
                case 6:
                    mp = MediaPlayer.create(context, R.raw.gameover);
                    break;
                case 7:
                    mp = MediaPlayer.create(context, R.raw.shopmusic);
            }
            if (!mp.isPlaying()) {
                mp.start();
                song = songNumber;
                mp.setLooping(true);
            }
        }
    }
    public void playSFX(int sfwNumber){

        if(Option.isSoundOn) {
            switch (sfwNumber) {
                case 0:
                    //pick up coin
                    mp = MediaPlayer.create(context, R.raw.pickupcoin);
                    break;
                case 1:
                    //fighting
                    mp = MediaPlayer.create(context, R.raw.hithurt);
                    break;
                case 2:
                    //eat food
                    mp = MediaPlayer.create(context, R.raw.eat);
                    break;
                case 3:
                    //level up
                    mp = MediaPlayer.create(context, R.raw.levelup);
                    break;
                case 4:
                    mp = MediaPlayer.create(context, R.raw.teleport);
                    break;
                case 5:
                    //entering the shop
                    mp = MediaPlayer.create(context, R.raw.shopenter);
                    break;
                case 6:
                    //trying to buy something too expensive
                    mp = MediaPlayer.create(context, R.raw.deny);
                    break;
                case 7:
                    //buy something
                    mp = MediaPlayer.create(context, R.raw.purchase);
                    break;
            }
            mp.start();
        }
    }

    public void pause(){
        mp.pause();
    }
    public void stop(){
        mp.stop();
    }
}
