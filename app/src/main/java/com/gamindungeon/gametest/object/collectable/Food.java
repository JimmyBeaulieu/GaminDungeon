package com.gamindungeon.gametest.object.collectable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;

public class Food {
    double positionX;
    double positionY;
    Bitmap sprite;
    double hungerRestoreAmount;
    double calories;
    String type;

    public Food(Context context, double positionX, double positionY, String type){
        this.type = type;
        switch(type){
            case "donut":
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cd_donut), 176, 176, false);
                hungerRestoreAmount = 10;
                calories = 300;
                break;
            case "drumstick":
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.ce_drumstick), 176, 176, false);
                hungerRestoreAmount = 50;
                calories = 150;
            case "burger":
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.ca_burger), 176, 176, false);
                hungerRestoreAmount = 25;
                calories = 295;
            case "cake":
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cb_cake), 176, 176, false);
                hungerRestoreAmount = 60;
                calories = 500;
            case "cone":
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cc_cone), 176, 176, false);
                hungerRestoreAmount = 50;
                calories = 417;
            case "potion":
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cf_potion), 176, 176, false);
                hungerRestoreAmount = 100;
                calories = 10;
        }
        this.positionX = positionX;
        this.positionY=positionY;

    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        canvas.drawBitmap(
                sprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);
    }

    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }

    public double getHunger() {
        return hungerRestoreAmount;
    }

    @Override
    @NonNull
    public String toString(){
        return positionX + "|" +positionY + "|" + type + "|" +  "food||";
    }

    public double getCalorie() {
        return calories;
    }
}
