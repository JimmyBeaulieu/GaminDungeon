package com.gamindungeon.gametest.object.collectable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.Game;
import com.gamindungeon.gametest.engine.GameDisplay;

public class Coin {
    Bitmap sprite;
    private double posistionX;
    private double posistionY;

    public Coin(Context context, double positionX, double positionY){
        sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.d_coin), 176, 176, false);
        this.posistionX = positionX;
        this.posistionY = positionY;

    }
    public void draw(Canvas canvas, GameDisplay gameDisplay){
        canvas.drawBitmap(
                sprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(posistionX)+20,
                (float)gameDisplay.gameToDisplayCoordinatesY(posistionY),
                null);
    }

    public double getPositionX() {
        return posistionX;
    }
    public double getPositionY() {
        return posistionY;
    }
}
