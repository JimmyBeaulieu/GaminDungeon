package com.gamindungeon.gametest;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.gamepanel.GameOver;
import com.gamindungeon.gametest.gamepanel.Performance;
import com.gamindungeon.gametest.graphics.SpriteSheet;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Game manages all objects in the game and is responsible for updating
// all states and render all objects oto the screen
public class Game extends SurfaceView implements SurfaceHolder.Callback{

    //private Enemy enemy;
    private Player player;
    private Enemy enemy;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;
    MediaPlayer mp;
    TileManager tileManager;



    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        //enable us to render on screen and handle touch input
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        //initialize game panel
        gameOver = new GameOver(context);
        performance = new Performance(gameLoop, context);

        //initialize player
        //GameObject(Context, X pos, Y pos, Sprite)
        SpriteSheet spriteSheet = new SpriteSheet(context);
        player = new Player(getContext(), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protag));
        //player = new Player(getContext(), spriteSheet.getPlayerSprite());
        //added player reference so the enemy can know where the player is

        //enemy = new Enemy(context, gridPos(6), gridPos(6), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player);

        //using bitmap
        enemyList.add(new Enemy(getContext(), gridPos(5), gridPos(5),BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player));
        enemyList.add(new Enemy(getContext(), gridPos(6), gridPos(3),BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player));
        enemyList.add(new Enemy(getContext(), gridPos(7), gridPos(4), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player));


        //using sprite
        /*
        enemyList.add(new Enemy(getContext(), gridPos(5), gridPos(5), player, spriteSheet.getPlayerSprite()));
        enemyList.add(new Enemy(getContext(), gridPos(6), gridPos(3), player, spriteSheet.getPlayerSprite()));
        enemyList.add(new Enemy(getContext(), gridPos(7), gridPos(4), player, spriteSheet.getPlayerSprite()));

         */



        //Initialize game display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels-176, displayMetrics.heightPixels-176, player);

        tileManager = new TileManager(context);

        playMusic(context);
        setFocusable(true);

    }

    public void playMusic(Context context){

        mp = MediaPlayer.create(context, R.raw.dungeon1);
        if (!mp.isPlaying())
        {
            mp.start();
            mp.setLooping(true);
        }

    }

    private double gridPos(int i) {
        return (i-1)*176;
    }

    float startX = 0;
    float startY = 0;
    float deltaX = 0;
    float deltaY = 0;
    int combatTimer = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println("Touch event");

        //handle touch event actions
        final float MIN_DISTANCE = 150f;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                deltaX = event.getX() - startX;
                deltaY = event.getY() - startY;

                System.out.println("startX:" + startX + "| startY:" + startY + "| deltaX:" + deltaX + "| deltaY:" + deltaY);
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX > MIN_DISTANCE) {
                        player.setPosition("right");
                        System.out.println("right");
                    } else if (deltaX < MIN_DISTANCE * -1) {
                        player.setPosition("left");
                        System.out.println("left");
                    }
                } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    if (deltaY > MIN_DISTANCE) {
                        player.setPosition("down");
                        System.out.println("down");
                    } else if (deltaY < MIN_DISTANCE * -1) {
                        player.setPosition("up");
                        System.out.println("up");
                    }
                }
                //enemy.move();

                for(Enemy enemy:enemyList){
                    enemy.move();
                }

                if(combatTimer == 0) {
                    if (player.getHealth() < player.getMaxHealth()) {
                        player.setHealth(player.getHealth() + 1);
                    }
                    if (player.getHealth() > player.getMaxHealth()) {
                        player.setHealth(player.getMaxHealth());
                    }
                }
                if(combatTimer > 0){
                    combatTimer--;
                }
                System.out.println("Player X:" + player.getPositionX() + " | Player Y:" + player.getPositionY());

        }

        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //instantiate the game loop
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
//used to draw things on the screen
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        tileManager.draw(canvas, gameDisplay);

        player.draw(canvas, gameDisplay);

        //enemy.draw(canvas, gameDisplay);

        for(Enemy enemy:enemyList){
            enemy.draw(canvas, gameDisplay);
        }


        //Draw Game Over if (player's hp <= 0)
        if(player.getHealth() <= 0){
            gameOver.draw(canvas);
        }

        performance.draw(canvas);
        drawGrid(canvas);
        drawHealth(canvas);

    }

    private void drawHealth(Canvas canvas) {
        String playerHealth = Double.toString(player.getHealth());

        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.yellow);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Current Health: " + playerHealth, 100, 300, paint);

        //TO BE REMOVED
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Combat Timer: " + combatTimer, 100, 400, paint);

        /*
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("ADHD Level: " + enemy.adhdLevel, 100, 500, paint);

        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Focused: " + enemy.isFocused, 100, 600, paint);
        */
    }

    private void drawGrid(Canvas canvas) {
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();

        int gridSize = 176;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        //original
/*
        for(int i = 0; i < screenWidth+176; i+= gridSize){
            //draws vertical lines
            canvas.drawLine(i, 0, i, screenHeight+88, paint);
        }

        for(int i =0; i< screenHeight+176;i+= gridSize){
            //draw horizontal lines
            canvas.drawLine(0, i, screenWidth, i, paint);
        }
*/

        //offset to middle
        //drawLine(float startX, float startY, float stopX, float stopY, Paint paint)
        //paint.setColor(ContextCompat.getColor(getContext(), R.color.green));
        for(int i = 88; i < screenWidth; i+= gridSize){
            //draws vertical lines X
            canvas.drawLine(i, 0, i, screenHeight, paint);
        }
        for(int i =100; i< screenHeight;i+= gridSize){
            //draw horizontal lines Y
            canvas.drawLine(0, i, screenWidth, i, paint);
        }


    }

    public void update() {

        //Stop updating the game if the player is dead

        if(player.getHealth() <= 0){
            return;
        }



        //update game state
        player.update();
        //checkCollision();
        if(combatTimer == 0){
            player.setIsInCombat(false);
        }

        gameDisplay.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    //NEED TO BE FIXED, PROBLEM WITH COLLISIONS
    /*
    private void checkCollision() {

        for(Enemy enemy: enemyList){
            if(isColliding(enemy, player)){
                System.out.println("Enemy current health: " + enemy.health);
                combatTimer = 3;
                combat(enemy, player);

                if(enemy.health <= 0){

                    enemyList.remove(enemy);
                }
            }
        }
    }


    private void combat(GameObject enemy, GameObject player) {
        enemy.inCombat = false;
        enemy.health -= player.strength;
        player.health -= enemy.strength;
        enemy.afterClash();
        player.afterClash();
    }

    private boolean isColliding(GameObject obj1, GameObject obj2) {
        return obj1.positionY == obj2.positionY && obj1.positionX == obj2.positionX;
    }
*/



}
