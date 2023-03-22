package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.graphics.Sprite;
import com.gamindungeon.gametest.manager.TileManager;

public class Player extends GameObject{
    private Sprite sprite;

    double oldPositionX;
    double oldPositionY;

    private String lastKnownMove = "";
    TileManager tm;

    public Player(Context context, Bitmap bitMapSprite){
        super(context, 176 * 3, 176 * 4);
        health = 10;
        maxHealth = health;
        strength = 3;


        this.bitMapSprite = Bitmap.createScaledBitmap(bitMapSprite, 176, 176, false);
        oldPositionX = 0;
        oldPositionY = 0;

//create a rectangle around the player to check for collision against other GameObject collision
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int rectWidth = 176;
        int rectHeight = 176;
        int rectLeft = (screenWidth - rectWidth) / 2;
        int rectTop = (screenHeight - rectHeight) / 2;
        int rectRight = rectLeft + rectWidth;
        int rectBottom = rectTop + rectHeight;

        collision = new Rect(rectLeft+20, rectTop, rectRight+20, rectBottom);
    }

    public void setTileManager(TileManager tm){
        this.tm = tm;
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
        //collision.set();

        canvas.drawBitmap(
                bitMapSprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.magenta));
        paint.setTextSize(50);
        canvas.drawText("Health: " + health + "/" + maxHealth, 100, 100, paint);

        //USED TO SEE THE COLLISION BOX
/*
        Paint paint = new Paint();
        paint.setColor(0xffff0000);
        canvas.drawRect(collision, paint);
*/

    }


    public void update() {

    }

    @Override
    public Context getContext() {
        return context;
    }

    public void move(String direction) {

        oldPositionX = positionX;
        oldPositionY = positionY;

//column
        int gridXPos = ((int)positionX / 176);
//row
        int gridYPos = (((int)positionY / 176));

        System.out.println("################################################################ up tile [" + gridYPos + "] [" + gridXPos + "]");

        int tileUp = tm.mapTileNum[gridXPos][gridYPos-1];
        int tileDown = tm.mapTileNum[gridXPos][gridYPos+1];
        int tileLeft = tm.mapTileNum[gridXPos-1][gridYPos];
        int tileRight = tm.mapTileNum[gridXPos+1][gridYPos];


        if(direction.equals("up")){
            if(!tm.getTiles(tileUp).getCollision()) {
                setPositionY(getPositionY() - 176);
                if (getPositionY() < 0) {
                    setPositionY(0);
                }
            }
        }
        if(direction.equals("down")){
            if(!tm.getTiles(tileDown).getCollision()) {
                setPositionY(getPositionY() + 176);
                if (getPositionY() > 49 * 176) {
                    setPositionY(49 * 176);
                }
            }
        }
        if(direction.equals("right")){
            if(!tm.getTiles(tileRight).getCollision()) {
                setPositionX(getPositionX() + 176);
                if (getPositionX() > 49 * 176) {
                    setPositionX((49 * 176));
                }
            }
        }
        if(direction.equals("left")){
            if(!tm.getTiles(tileLeft).getCollision()) {
                setPositionX(getOldPositionX() - 176);
                if (getPositionX() < 0) {
                    setPositionX(0);
                }
            }
        }
        /*
        tileUp = tm.mapTileNum[gridXPos][gridYPos];
        tileDown = tm.mapTileNum[(int)positionY /176][(int)positionX / 176];
        tileLeft = tm.mapTileNum[(int)positionY /176][(int)positionX / 176];
        tileRight = tm.mapTileNum[(int)positionY /176][(int)positionX / 176];
        */
        lastKnownMove = direction;
    }

    public double getHealth(){
        return health;
    }

    @Override
    public Rect getCollision() {
        return collision;
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


}
