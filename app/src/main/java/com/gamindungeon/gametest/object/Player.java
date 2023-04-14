package com.gamindungeon.gametest.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.gamepanel.HealthBar;
import com.gamindungeon.gametest.graphics.Sprite;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.manager.TileManager;
import com.gamindungeon.gametest.object.collectable.powerUpType;

import java.util.Arrays;
import java.util.List;

public class Player extends GameObject{
    private Sprite sprite;

    private double oldPositionX;
    private double oldPositionY;
    public static double hunger;
    public static double health;
    public static int strengthBonus;
    public static int lifeBonus;
    public static int goldBonus;
    private static boolean hasPowerUp;
    private static int powerUpTimer;
    private int levelUpText=0;
    private double bmi;
    private double calories;

    private String lastKnownMove = "";
    private TileManager tm;
    private Music mp;
    float healthPointPercentage;
    float hungerPointPercentage;
    private int level;
    public Player(Context context, Music music, double positionX, double positionY, double health,
                  double maxHealth, double strength, double hunger, double oldPositionX, double oldPositionY, String lastKnownMove, int level){

        super(context, positionX, positionY);
        this.health = health;
        this.maxHealth = maxHealth;
        this.strength = strength;
        this.hunger = hunger;
        this.oldPositionX = oldPositionX;
        this.oldPositionY = oldPositionY;
        this.lastKnownMove = lastKnownMove;
        this.level = level;
        hasPowerUp = false;
        lifeBonus = 0;
        goldBonus = 0;
        strengthBonus = 0;
        bmi = 0;

        mp=music;

        this.bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainfront), 176, 176, false);
    }

    public void setTileManager(TileManager tm){
        this.tm = tm;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {

        switch(lastKnownMove){
            case "up":
                bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainback), 176, 176, false);
                break;
            case "down":
                if(calories <= 1000) {
                    bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainfront), 176, 176, false);
                }
                else{
                    bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainfront_bigger), 176, 176, false);
                }
                break;
            case "left":
                if(calories <= 1000) {
                    bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainleft), 176, 176, false);
                }
                else{
                    bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainleft_bigger), 176, 176, false);
                }
                break;
            case "right":
                if(calories <= 1000) {
                    bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainright), 176, 176, false);
                }
                else{
                    bitMapSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.mainright_bigger), 176, 176, false);
                }
                break;
        }
        canvas.drawBitmap(
                bitMapSprite,
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX)+20,
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

        //health bar
        Paint paint = new Paint();

        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTextSize(50);
        canvas.drawText("Life", 100, 100, paint);
        //background bar
        paint.setColor(ContextCompat.getColor(context, R.color.gray));
        canvas.drawRect(100, 100, 500, 150, paint);

        if(getHealth() > 51) {
            paint.setColor(ContextCompat.getColor(context, R.color.green));
        }
        if(getHealth() <= 51 && getHealth() >= 20){
            paint.setColor(ContextCompat.getColor(context, R.color.yellow));
        }
        if(getHealth() < 20){
            paint.setColor(ContextCompat.getColor(context, R.color.red));
        }
        if(Player.strengthBonus > 0){
            paint.setColor(ContextCompat.getColor(context, R.color.blue));
        }
        //foreground bar
        canvas.drawRect(110, 110, healthPointPercentage + 110, 140, paint);

        //hunger bar

        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTextSize(50);
        canvas.drawText("Hunger", 100, 190, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.gray));
        //background bar
        canvas.drawRect(100, 200, 500, 250, paint);

        if(getHunger() > 51) {
            paint.setColor(ContextCompat.getColor(context, R.color.green));
        }
        if(getHunger() <= 51 && getHunger() >= 20){
            paint.setColor(ContextCompat.getColor(context, R.color.yellow));
        }
        if(getHunger() < 20){
            paint.setColor(ContextCompat.getColor(context, R.color.red));
        }
        //foreground bar
        canvas.drawRect(110, 210, hungerPointPercentage + 110, 240, paint);

        if(levelUpText > 0){
            paint.setColor(ContextCompat.getColor(context, R.color.red));
            canvas.drawText("Level up!!", 950, 430, paint);
            paint.setColor(ContextCompat.getColor(context, R.color.red));
            canvas.drawText("+20 Health Points", 900, 640, paint);
            canvas.drawText("+5 Strength", 900, 700, paint);
        }
    }


    public void update() {

        healthPointPercentage =(float) ((getHealth() + Player.lifeBonus) / getMaxHealth());
        healthPointPercentage = healthPointPercentage * 380;
        //if(healthPointPercentage < 110){healthPointPercentage = 110;}

        hungerPointPercentage = (float) (getHunger() / 100);
        hungerPointPercentage = hungerPointPercentage * 380;
       //if(getHunger() == 0 ){hungerPointPercentage = 110;}

        if(Score.experience >= 100){
            levelUp();
            Score.experience = 0;
        }
        if(health > maxHealth){
            health = maxHealth;
        }
        if(hunger > 100){
            hunger = 100;
        }
        if(hunger < 0){
            hunger = 0;
        }
        if(!hasPowerUp){
            lifeBonus = 0;
            goldBonus = 0;
            strengthBonus = 0;
        }


    }

    @Override
    public Context getContext() {
        return context;
    }

    public void move(String direction) {


        if(levelUpText >= 1){
            levelUpText--;
        }

        if(hasPowerUp){
            powerUpTimer--;
            if(powerUpTimer <= 0){
                hasPowerUp = false;
                powerUpTimer = 0;
                strengthBonus = 0;
                lifeBonus = 0;
                goldBonus = 0;
            }
        }

//row
        int gridXPos = ((int)positionX / 176);
//column
        int gridYPos = (((int)positionY / 176));

        int tileUp = tm.getMapTileNum()[gridXPos][gridYPos-1];
        int tileDown = tm.getMapTileNum()[gridXPos][gridYPos+1];
        int tileLeft = tm.getMapTileNum()[gridXPos-1][gridYPos];
        int tileRight = tm.getMapTileNum()[gridXPos+1][gridYPos];

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
                    health+=10;
                    hunger-=7;
                }
                if(!direction.equals("")) {
                    lastKnownMove = direction;
                }

    }
    public void levelUp(){
        maxHealth += 20;
        strength += 5;
        mp.playSFX(3);
        levelUpText = 3;
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

    public void setOldX(double positionX) {
         oldPositionX = positionX;
    }
    public void setOldY(double positionY) {
        oldPositionY = positionY;
    }

    @NonNull
    @Override
    public String toString(){
        /*
        location
        health
        maxHealth
        strength
        hunger
        oldPositionX = 0;
        oldPositionY = 0;
        lastknownmove
         */

        return positionX + "|" + positionY  + "|" + health  + "|" + maxHealth  + "|" + strength +
                "|" +hunger + "|" + oldPositionX + "|" +oldPositionY + "|" + lastKnownMove + "|" + level + "||";
    }

    public void setLastKnownMove(String up) {
        lastKnownMove = up;
    }
    public static void givePowerUp(powerUpType type, int amount){
        hasPowerUp = true;

        switch(type){
            case LIFE:
                lifeBonus = amount;
                powerUpTimer = 20;
                break;
            case STRENGTH:
                strengthBonus = amount;
                powerUpTimer = 20;
                break;
            case COIN:
                goldBonus = amount;
                powerUpTimer = 50;
                break;
        }
    }
    public boolean getPowerUpState(){
        return hasPowerUp;
    }

    public void addCalorie(double calorie) {
        this.calories += calorie;
    }
    public void substractCalorie(double calorieLost){
        calories -= calorieLost;
        if(calories<0){
            calories = 0;
        }
    }

}
