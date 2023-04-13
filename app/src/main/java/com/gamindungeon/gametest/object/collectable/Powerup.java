package com.gamindungeon.gametest.object.collectable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.object.Player;

public class Powerup {


    powerUpType powerUpType;
    Bitmap sprite;
    Context context;
    int bonus;

    public Powerup(Context context, int bonus, powerUpType powerUpType){
        this.bonus = bonus;
        this.powerUpType = powerUpType;

        switch (powerUpType){
            case LIFE:
                //Player.lifeBonus = bonus;
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.zb_lifebonus), 176, 176, false);
                break;
            case COIN:
                //Player.goldBonus = bonus;
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.za_coinbonus), 176, 176, false);
                break;
            case STRENGTH:
                //Player.strengthBonus = bonus;
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.zc_strengthbonus), 176, 176, false);
                break;
        }
    }

    public Bitmap getSprite(){
        return sprite;
    }

    public String getName() {
        String output = "";
        switch (this.powerUpType){
            case STRENGTH:
                output = "Strength Bonus";
                break;
            case LIFE:
                output = "Life Bonus";
                break;
            case COIN:
                output = "Coin Bonus";
                break;
        }
        return output;
    }

    public int getShopValue() {
        int output = 0;
        switch(powerUpType){

            case STRENGTH:
                output = bonus;
                break;
            case LIFE:
                output = bonus + 5;
                break;
            case COIN:
                output = bonus * 10;
                break;
        }
        return output;
    }

    public powerUpType getType(){
        return powerUpType;
    }
    public int getBonus(){
        return bonus;
    }
}
