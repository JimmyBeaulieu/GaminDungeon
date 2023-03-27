package com.gamindungeon.gametest.object.collectable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.Game;
import com.gamindungeon.gametest.engine.GameDisplay;

public class Coin {
    Bitmap sprite;
    private double positionX;
    private double positionY;

    public Coin(Context context, double positionX, double positionY){
        sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.d_coin), 176, 176, false);
        this.positionX = positionX;
        this.positionY = positionY;

    }
    public void draw(Canvas canvas, GameDisplay gameDisplay){
        canvas.drawBitmap(
                sprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX)+20,
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);
    }
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    @Override
    @NonNull
    public String toString(){
        return positionX + "|" +positionY + "||";
    }
}
