package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.gamepanel.HealthBar;
import com.gamindungeon.gametest.graphics.Sprite;
import com.gamindungeon.gametest.manager.TileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends GameObject{
    private Player player;
    private HealthBar hpBar;
    private Sprite sprite;
    public boolean inCombat;
    double oldPosX = 0;
    double oldPosY = 0;
    TileManager tm;
    boolean isFocused;
    boolean isHoming;
    int adhdLevel = 0;

    public Enemy(Context context, double positionX, double positionY, Player player, TileManager tm, String type) {
        super(context, positionX, positionY);

        switch(type){
            case "bat":
                health = 70;
                strength = 20;
                this.bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.g_bat), 176, 176, false);
                break;

        }

        maxHealth = health;
        this.player = player;
        hpBar = new HealthBar(this);
        this.tm = tm;

    }

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        canvas.drawBitmap(
                bitMapSprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX)+20,
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

        hpBar.draw(canvas, gameDisplay);


    }

    @Override
    public void move(String direction) {

        //column
        int gridXPos = (int)positionX / 176;
        //row
        int gridYPos = (int)positionY / 176;

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
    }


    @Override
    public void update() {
        //move();
    }

    //decides if the enemy is randomly roaming or going for the player
    public void statusBranch() {
        oldPosX = positionX;
        oldPosY = positionY;

        if(!inCombat) {
            if (adhdLevel >= 5) {
                isFocused = false;
                isHoming = false;
            }
            if (adhdLevel == 0) {
                isFocused = true;
            }

            //if player is within two tiles of this object use HOMING MOVEMENT AI:
            if (Math.abs(player.positionX - positionX) <= 352 && Math.abs(player.positionY - positionY) <= 352 && isFocused) {
                System.out.println("Enemy is Focused on you!!!");
                isHoming = true;
            } else {
                //random();
                System.out.println("Enemy is just wandering around");
            }

            if (isHoming) homing();
            else random();

        }
    }

    private void random() {
        if(adhdLevel > 0){
            adhdLevel--;
        }
        Random rand = new Random();
        switch (rand.nextInt(5 )) {
            case 0:
                return;
            case 1:
                move("down");
                break;
            case 2:
                move("up");
                break;
            case 3:
                move("right");
                break;
            case 4:
                move("left");
                break;
        }

    }

    private void homing() {
        List<String> movementOptions = new ArrayList<String>();

        if (positionX < player.positionX) {
            //wanna go right
            movementOptions.add("right");
        } else if (positionX > player.positionX) {
            //wanna go left
            movementOptions.add("left");
        }
        if (positionY < player.positionY) {
            //wanna go down
            movementOptions.add("down");
        } else if (positionY > player.positionY) {
            //wanna go up
            movementOptions.add("up");
        }

        Random rand = new Random();
        if(movementOptions.size() > 0){

            int choice = rand.nextInt(movementOptions.size());
            if (choice > 4) {
                choice = 0;
            }
            move(movementOptions.get(choice));
            adhdLevel++;
        }
    }

    @Override
    public Context getContext() {
        return context;
    }
    @Override
    public double getMaxHealth() {
        return maxHealth;
    }
    @Override
    public double getHealth() {
        return health;
    }
    public void setHealth(double health) {
        this.health = health;
    }
    @Override
    public double getStrength() {
        return strength;
    }

    public void setIsInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }
    @Override
    public double getPositionX() {
        return positionX;
    }
    @Override
    public double getPositionY() {
        return positionY;
    }
    public void setPositionX(double newPos){
        positionX = newPos;
    }
    public void setPositionY(double newPos){
        positionY = newPos;
    }

    public double getOldPositionX() {
        return oldPosX;
    }

    public double getOldPositionY() {
        return oldPosY;
    }

    public void setTileManager(TileManager tileManager) {
        tm = tileManager;
    }
    public int getAdhdLevel(){
        return  adhdLevel;
    }
    public boolean isFocused(){
        return  isFocused;
    }
}
