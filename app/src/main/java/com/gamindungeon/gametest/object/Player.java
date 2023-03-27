package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.graphics.Sprite;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.TileManager;

import java.util.Arrays;
import java.util.List;

public class Player extends GameObject{
    private Sprite sprite;

    private double oldPositionX;
    private double oldPositionY;
    private double hunger;

    private String lastKnownMove = "";
    private TileManager tm;
    private Music mp;

    public Player(Context context, Music music){
        super(context, 176 * 3, 176 * 4);
        health = 100;
        maxHealth = health;
        strength = 20;
        mp=music;
        hunger = 100;

        this.bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.protag), 176, 176, false);
        oldPositionX = 0;
        oldPositionY = 0;

    }

    public void setTileManager(TileManager tm){
        this.tm = tm;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        canvas.drawBitmap(
                bitMapSprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX)+20,
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.magenta));
        paint.setTextSize(50);
        canvas.drawText("Health: " + health + "/" + maxHealth, 100, 100, paint);

    }


    public void update() {

    }

    @Override
    public Context getContext() {
        return context;
    }

    public void move(String direction) {

//row
        int gridXPos = ((int)positionX / 176);
//column
        int gridYPos = (((int)positionY / 176));

        int tileUp = tm.mapTileNum[gridXPos][gridYPos-1];
        int tileDown = tm.mapTileNum[gridXPos][gridYPos+1];
        int tileLeft = tm.mapTileNum[gridXPos-1][gridYPos];
        int tileRight = tm.mapTileNum[gridXPos+1][gridYPos];

        switch(direction) {
            case "up":
                if (!tm.getTiles(tileUp).getCollision()) {
                    setPositionY(getPositionY() - 176);
                    if (getPositionY() < 0) {
                        setPositionY(0);
                    }
                }
                break;
            case "down":
                if (!tm.getTiles(tileDown).getCollision()) {
                    //set new position for player
                    setPositionY(getPositionY() + 176);
                    //if out of bounds, brings player back in-bound
                    if (getPositionY() > 49 * 176) {
                        setPositionY(49 * 176);
                    }
                }
                break;
            case "left":
                if (!tm.getTiles(tileLeft).getCollision()) {
                    setPositionX(getOldPositionX() - 176);
                    if (getPositionX() < 0) {
                        setPositionX(0);
                    }
                }
                    break;
            case "right":
                        if (!tm.getTiles(tileRight).getCollision()) {
                            setPositionX(getPositionX() + 176);
                            if (getPositionX() > 49 * 176) {
                                setPositionX((49 * 176));
                            }
                        }
                        break;
                }
                if(health < maxHealth && hunger > 0 && !inCombat){
                    health+=5;
                    hunger-=10;
                }
        lastKnownMove = direction;
    }

    public double getHealth(){
        return health;
    }
    @Override
    public double getStrength() {
        return strength;
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
    public void setPositionY(double newPosition) {
        positionY = newPosition;
    }

    public void setPositionX(double newPosition) {
        positionX = newPosition;
    }

    public double getOldPositionX(){
        return oldPositionX;
    }
    public double getOldPositionY(){
        return oldPositionY;
    }

    public String getLastKnownMove(){
        return lastKnownMove;
    }
    public double getHunger(){
        return hunger;
    }
    public void setHunger(double hunger){
        this.hunger = hunger;
        if(hunger > 100){
            this.hunger = 100;
        }
    }

    public void addBonusToPlayer( String bonusStr) {
        String[] listOfBonus = bonusStr.split("\\s*\n\\s*");

        for (String str: listOfBonus) {
            switch (str){
                case "Prize 1":
                    this.setMaxHealth( this.getMaxHealth() + 1);
                    break;
                case "Prize 2":
                    this.setMaxHealth( this.getMaxHealth() - 2);
                    break;
                case "Prize 3":
                    this.setMaxHealth( this.getMaxHealth() + 3);
                    break;
                case "Prize 4":
                    this.setStrength( this.getStrength() + 1);
                    break;
                case "Prize 5":
                    this.setStrength( this.getStrength() - 2);
                    break;
                case "Prize 6":
                    this.setStrength( this.getStrength() + 3);
                    break;
                case "Prize 7":
                    this.setMaxHealth( this.getMaxHealth() + 10);
                    break;
            }

        }

    }


    public void setOldX(double positionX) {
         oldPositionX = positionX;
    }
    public void setOldY(double positionY) {
        oldPositionY = positionY;
    }
}
