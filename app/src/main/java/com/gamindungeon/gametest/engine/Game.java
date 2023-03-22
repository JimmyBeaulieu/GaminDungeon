package com.gamindungeon.gametest.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.TileManager;
import com.gamindungeon.gametest.gamepanel.GameOver;
import com.gamindungeon.gametest.gamepanel.Performance;
import com.gamindungeon.gametest.graphics.SpriteSheet;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.GameObject;
import com.gamindungeon.gametest.object.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
    private Music music;
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
        music = new Music();
        Random rand = new Random();
        music.play(context, rand.nextInt(3));
        setFocusable(true);

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
        String move="";

        switch (event.getAction()) {

            // user touches the screen
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;

                //user stop touching the screen
            case MotionEvent.ACTION_UP:
                if(!checkCollision()) {

                    //calculates the difference between the point where the user originally touches the screen vs the point where the user stops
                    deltaX = event.getX() - startX;
                    deltaY = event.getY() - startY;

                    System.out.println("startX:" + startX + "| startY:" + startY + "| deltaX:" + deltaX + "| deltaY:" + deltaY);
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (deltaX > MIN_DISTANCE) {
                            move = "right";
                            System.out.println("right");
                        } else if (deltaX < MIN_DISTANCE * -1) {
                            move = "left";
                            System.out.println("left");
                        }
                    } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
                        if (deltaY > MIN_DISTANCE) {
                            move = "down";
                            System.out.println("down");
                        } else if (deltaY < MIN_DISTANCE * -1) {
                            move = "up";
                            System.out.println("up");
                        }
                    }
                }
                //check collision before enemy moves, so that way if the player moves on an enemy, it will start battle before the enemy can move away
               /*
                if (checkCollision()) {

                    System.out.println("COLLISISON!!!!");
                    player.setPositionX(player.getOldPositionX());
                    player.setPositionY(player.getOldPositionY());

                    for (Enemy enemy : enemyList) {
                        if (enemy.inCombat) {
                            combat(enemy, player);
                        }
                    }
                } else {
                    player.move(move);
                    for (Enemy enemy : enemyList) {
                        enemy.statusBranch();
                        if(checkCollision()){
                            player.setPositionX(player.getOldPositionX());
                            player.setPositionY(player.getOldPositionY());
                            combat(enemy, player);
                        }
                    }
                }
                */
                //enemy.move();

                if(!checkCollision()){
                    player.move(move);
                    for(Enemy enemy:enemyList){
                        enemy.statusBranch();
                    }
                }

                if (combatTimer == 0) {
                    if (player.getHealth() < player.getMaxHealth()) {
                        player.setHealth(player.getHealth() + 1);
                    }
                    if (player.getHealth() > player.getMaxHealth()) {
                        player.setHealth(player.getMaxHealth());
                    }
                }
                if (combatTimer > 0) {
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

        if(checkCollision()){
            int tries = 0;

                player.setPositionX(player.getOldPositionX());
                player.setPositionY(player.getOldPositionY());

                //player.setPositionX(player.getPositionX() + 176);
                //player.setPositionY(player.getPositionY() + 176);

                System.out.println("New player's positionX=" + player.getPositionX() + "| position Y:" + player.getPositionY());

            for(Enemy enemy:enemyList){
                if(enemy.inCombat){
                    combat(enemy, player);
                }
            }
        }


        //DEBUG PURPOSES, DRAWS COLLISION BOXES
/*
        Paint paint = new Paint();
        paint.setColor(0xffff0000);
        for(Enemy enemy:enemyList){
            canvas.drawRect(enemy.getCollision(), paint);
        }
        canvas.drawRect(player.getCollision(), paint);
*/

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


    private boolean checkCollision() {

        boolean flag = false;

        for (Enemy enemy : enemyList) {

            if (player.getCollision().intersect(enemy.getCollision())) {

                //System.out.println("COLLISION!");
                //System.out.println(player.getLastKnownMove());
                switch (player.getLastKnownMove()){
                    case "up":
                        player.setPositionY(player.getPositionY() + 176);

                        break;
                    case "down":
                        player.setPositionY(player.getPositionY() - 176);
                        break;
                    case "left":
                        player.setPositionX(player.getPositionX() + 176);
                        break;
                    case "right":
                        player.setPositionX(player.getPositionX() - 176);
                        break;
                }

                /*
                player.setPositionX(player.getOldPositionX());
                player.setPositionY(player.getOldPositionY());

                 */

                combatTimer = 3;
                enemy.setIsInCombat(true);
                flag = true;
            }
        }
        return flag;
    }


    private void combat(GameObject enemy, GameObject player) {
        enemy.inCombat = false;
        enemy.setHealth(enemy.getHealth()-player.getStrength());
        player.setHealth(player.getHealth() -enemy.getStrength());


        enemyList.removeIf(targetEnemy -> targetEnemy.getHealth() <= 0);

        System.out.println("Enemy Health: " + enemy.getHealth() + "//" + enemy.getMaxHealth());

    }

}
