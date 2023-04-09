package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;

public class Teleporter {

    Bitmap sprite;
    double positionX;
    double positionY;
    Context context;

    public Teleporter(Context context, double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;
        this.context = context;
        sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.ad_teleport), 176, 176, false);
    }

    public double getPositionX(){
        return positionX;
    }
    public double getPositionY(){
        return positionY;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        canvas.drawBitmap(
                sprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX)+20,
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);
    }
}
