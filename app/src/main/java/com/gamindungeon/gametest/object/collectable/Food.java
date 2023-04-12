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
    //String type;
    foodType type;
    int shopValue;

    public Food(Context context, double positionX, double positionY, foodType type){
        this.type = type;
        switch(type){
            case DONUT:
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cd_donut), 176, 176, false);
                hungerRestoreAmount = 10;
                calories = 300;
                shopValue = 5;
                break;
            case DRUMSTICK:
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.ce_drumstick), 176, 176, false);
                hungerRestoreAmount = 50;
                calories = 150;
                shopValue = 20;
                break;
            case BURGER:
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.ca_burger), 176, 176, false);
                hungerRestoreAmount = 25;
                calories = 295;
                shopValue = 10;
                break;
            case CAKE:
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cb_cake), 176, 176, false);
                hungerRestoreAmount = 60;
                calories = 500;
                shopValue = 15;
                break;
            case CONE:
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cc_cone), 176, 176, false);
                hungerRestoreAmount = 50;
                calories = 417;
                shopValue = 10;
                break;
            case POTION:
                sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.cf_potion), 176, 176, false);
                hungerRestoreAmount = 100;
                calories = 10;
                shopValue = 30;
                break;
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
    public Bitmap getSprite(){
        return sprite;
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
    public void setValue(int value){
        this.shopValue = value;
    }
    public int getShopValue(){ return shopValue; }
    public String getName(){
        String output = "";
        switch(type){
            case DONUT:
                output = "Donut";
                break;
            case DRUMSTICK:
                output = "Drumstick";
                break;
            case BURGER:
                output = "Burger";
                break;
            case CAKE:
                output = "Cake";
                break;
            case CONE:
                output = "Cone";
                break;
            case POTION:
                output = "Potion";
                break;
        }
        return output;
    }

}
