package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.gamindungeon.gametest.engine.GameDisplay;

public abstract class GameObject {

    protected double health;
    protected double maxHealth;
    protected double strength;
    protected double positionX;
    protected double positionY;

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    //protected Bitmap sprite;
    protected Context context;
    protected Bitmap bitMapSprite;
    public boolean inCombat;

    public GameObject(Context context, double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;
        this.context = context;
    }


    public abstract void draw(Canvas canvas, GameDisplay gameDisplay);
    public abstract void move(String direction);
    public  abstract  void update();
    public abstract Context getContext();

    public abstract double getPositionX();
    public abstract double getPositionY();

    public abstract double getMaxHealth();
    public abstract double getHealth();

    //public abstract Rect getCollision();

    public abstract double getStrength();

    public abstract void setHealth(double health);
    @NonNull
    @Override
    public abstract  String toString();
}
