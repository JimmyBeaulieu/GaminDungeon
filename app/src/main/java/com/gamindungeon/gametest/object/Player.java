package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.GameDisplay;
import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.graphics.Sprite;

public class Player extends GameObject{
    private Sprite sprite;

    public Player(Context context, Bitmap bitMapSprite){
        super(context, 0, 0);
        health = 10;
        maxHealth = health;
        strength = 3;

        this.bitMapSprite = Bitmap.createScaledBitmap(bitMapSprite, 176, 176, false);
    }
//using sprite
    /*
    public Player(Context context, Sprite sprite){
        super(context, 0, 0);
        health = 10;
        maxHealth = health;
        strength = 3;
        this.sprite = sprite;
    }
     */
    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        /*
        sprite.draw(
                canvas,
                (int)gameDisplay.gameToDisplayCoordinatesX(getPositionX()) - sprite.getWidth()/2,
                (int)gameDisplay.gameToDisplayCoordinatesY(getPositionY()) - sprite.getHeight()/2
        );
*/


        canvas.drawBitmap(
                bitMapSprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

    }


    public void update() {

    }

    @Override
    public void afterClash() {
        positionX = oldPositionX;
        positionY = oldPositionY;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setPosition(String direction) {
        oldPositionX = positionX;
        oldPositionY = positionY;
        if(direction.equals("up")){
            positionY -= 176;
        }
        if(direction.equals("down")){
            positionY += 176;
        }
        if(direction.equals("right")){
            positionX += 176;
        }
        if(direction.equals("left")){
            positionX -= 176;
        }
    }

    public double getHealth(){
        return health;
    }
    public double getMaxHealth(){
        return  maxHealth;
    }
    public void setIsInCombat(boolean inCombat){
        this.inCombat = inCombat;
    }
    public boolean isInCombat(){
        return inCombat;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getPositionX() {
        return  positionX;
    }
}
