package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gamindungeon.gametest.R;

public class Teleporter {

    Bitmap sprite;
    double positionX;
    double positionY;
    Context context;

    public Teleporter(Context context, double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;
        this.context = context;
        sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.e_teleport), 176, 176, false);
    }

    public double getPositionX(){
        return positionX;
    }
    public double getPositionY(){
        return positionY;
    }
}
