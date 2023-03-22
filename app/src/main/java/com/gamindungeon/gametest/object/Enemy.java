package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.gamindungeon.gametest.GameDisplay;
import com.gamindungeon.gametest.gamepanel.HealthBar;
import com.gamindungeon.gametest.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends GameObject{
    private Player player;
    private HealthBar hpBar;
    private Sprite sprite;

//without sprite
    public Enemy(Context context, double positionX, double positionY, Bitmap bitMapSprite, Player player) {
        super(context, positionX, positionY);
        health = 7;
        maxHealth = health;
        strength = 2;
        this.player = player;
        //this.sprite = sprite;
        this.bitMapSprite = Bitmap.createScaledBitmap(bitMapSprite, 176, 176, false);
        hpBar = new HealthBar(this);
    }
    //with sprite
    /*
    WITH SPRITE
        public Enemy(Context context, double positionX, double positionY, Player player, Sprite sprite) {
        super(context, positionX, positionY);
        health = 7;
        maxHealth = health;
        strength = 2;
        this.player = player;
        this.sprite = sprite;
        hpBar = new HealthBar(this);
    }
     */

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        //sprite.draw(canvas, (int)gameDisplay.gameToDisplayCoordinatesX(getPositionX()), (int)gameDisplay.gameToDisplayCoordinatesY(getPositionY()));

        canvas.drawBitmap(
                bitMapSprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

        hpBar.draw(canvas, gameDisplay);
    }

    @Override
    public void setPosition(String direction) {
        oldPositionX = positionX;
        oldPositionY = positionY;
        if(direction.equals("up")) positionY -= 176;
        if(direction.equals("down")) positionY += 176;
        if(direction.equals("right")) positionX += 176;
        if(direction.equals("left")) positionX -= 176;
        if(health < maxHealth){
            health++;
        }
        if(health > maxHealth){
            health = maxHealth;
        }
    }

    @Override
    public void update() {
        //move();
    }

    boolean isFocused;
    boolean isHoming;
    int adhdLevel = 0;
    public void move() {

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
        switch (rand.nextInt(4)) {
            case 0:
                return;
            case 1:
                positionY += 176;
                break;
            case 2:
                positionY -= 176;
                break;
            case 3:
                positionX += 176;
                break;
            case 4:
                positionX -= 176;
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
        if(movementOptions.size() == 0){
        }
        else {
            int choice = rand.nextInt(movementOptions.size());
            if (choice < 0 || choice > 4) {
                choice = 0;
            }
            setPosition(movementOptions.get(choice));
            adhdLevel++;
        }
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

    @Override
    public double getPositionX() {
        return positionX;
    }

    @Override
    public double getPositionY() {
        return positionY;
    }

    @Override
    public double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public double getHealth() {
        return health;
    }
}
