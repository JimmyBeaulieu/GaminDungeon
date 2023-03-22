package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gamindungeon.gametest.GameDisplay;

public abstract class GameObject {

    protected double health;
    protected double maxHealth;
    protected double strength;
    protected double positionX;
    protected double positionY;
    protected double oldPositionX;
    protected double oldPositionY;
    //protected Bitmap sprite;
    protected Context context;
    protected Bitmap bitMapSprite;
    boolean inCombat;

    public GameObject(Context context, double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;
        this.context = context;
        //this.sprite = Bitmap.createScaledBitmap(sprite, 176, 176, false);
    }


    public abstract void draw(Canvas canvas, GameDisplay gameDisplay);
    public abstract void setPosition(String direction);
    public  abstract  void update();
    public abstract void afterClash();

    public abstract Context getContext();

    public abstract double getPositionX();
    public abstract double getPositionY();

    public abstract double getMaxHealth();
    public abstract double getHealth();
}
