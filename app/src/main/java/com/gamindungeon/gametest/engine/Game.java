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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.manager.TileManager;
import com.gamindungeon.gametest.gamepanel.GameOver;
import com.gamindungeon.gametest.gamepanel.Performance;
import com.gamindungeon.gametest.graphics.SpriteSheet;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.GameObject;
import com.gamindungeon.gametest.object.Player;

import java.util.ArrayList;
import java.util.Arrays;
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
    Score score;
    boolean mapLoaded = false;



    public Game(Context context, String bonusStr) {
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
        score = new Score(context);



        music = new Music(context);
        music.play(context, 2);
        player = new Player(getContext(), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protag), music);
        //Adding bonus
        if(!bonusStr.equals("")){
            player.addBonusToPlayer(bonusStr);
        }

        //using bitmap
        //------------------------------------------col---------row------------------------------------------------------------------------image
/*
        enemyList.add(new Enemy(getContext(), gridPos(9), gridPos(4), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player, tileManager));
        enemyList.add(new Enemy(getContext(), gridPos(17), gridPos(5), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player, tileManager));
        enemyList.add(new Enemy(getContext(), gridPos(21), gridPos(6), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player, tileManager));
        enemyList.add(new Enemy(getContext(), gridPos(23), gridPos(11), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player, tileManager));
        enemyList.add(new Enemy(getContext(), gridPos(21), gridPos(11), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player, tileManager));
        enemyList.add(new Enemy(getContext(), gridPos(12), gridPos(18), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.protagbig), player, tileManager));
*/
        //Initialize game display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels-176, displayMetrics.heightPixels-176, player);

        tileManager = new TileManager(context, gameDisplay, player);
        player.setTileManager(tileManager);

        for(Enemy enemy : enemyList){
            enemy.setTileManager(tileManager);
        }

        setFocusable(true);

    }




    private double gridPos(int i) {
        return (i)*176;
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
                if(!checkCollision() && player.getHealth() > 0) {

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
                if(!checkCollision()){
                    player.move(move);
                    for(Enemy enemy:enemyList){
                        enemy.statusBranch();
                    }
                }

                //gradually regenerate health when not in combat
                if (combatTimer == 0 && player.getHealth() >0) {
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

        tileManager.draw(canvas);

        player.draw(canvas, gameDisplay);

        //enemy.draw(canvas, gameDisplay);

        for(Enemy enemy:enemyList){
            enemy.draw(canvas, gameDisplay);
        }

        //Draw Game Over if (player's hp <= 0)
        if(player.getHealth() <= 0){
            gameOver.draw(canvas);
        }
        drawGrid(canvas);
        score.draw(canvas);


        //******************************************************************************************
        // TO BE DEACTIVATED FOR FULL RELEASE

        //drawDebug(canvas);

        //******************************************************************************************

        //
        //COLLISION CHECKER#########################################################################START
        //
        if(checkCollision()){

            //brings the player back to their old position so that the two objects colliding don't stay on top of each other
            player.setPositionX(player.getOldPositionX());
            player.setPositionY(player.getOldPositionY());

            System.out.println("New player's positionX=" + player.getPositionX() + "| position Y:" + player.getPositionY());

            for(int i = 0; i<enemyList.size();i++){

                if(enemyList.get(i).inCombat){
                    combat(enemyList.get(i), player);
                    enemyList.get(i).setPositionY(enemyList.get(i).getOldPositionY());
                    enemyList.get(i).setPositionX(enemyList.get(i).getOldPositionX());
                    Log.d("ENEMY_COMBAT_STATUS", "Enemy #" +i);
                    break;
                }
            }
            for(int i =0; i< enemyList.size();i++){
                if(enemyList.get(i).getHealth() <= 0){
                    enemyList.remove(enemyList.get(i));
                    score.setExperience(score.getExperience() + 1);
                }
            }

        }

        //checks if two enemies intersects, if so, prevent movement for one enemy for one turn
        for(int i = 0; i<enemyList.size();i++){
            for(int j = 0; j<enemyList.size(); j++){
                if(j == i){
                    break;
                }
                else if(enemyList.get(i).getCollision().intersect(enemyList.get(j).getCollision())){
                    enemyList.get(i).setPositionX(enemyList.get(i).getOldPositionX());
                    enemyList.get(i).setPositionY(enemyList.get(i).getOldPositionY());
                }
            }
        }
        //
        //COLLISION CHECKER#########################################################################END
        //


    }
    private void drawDebug(Canvas canvas) {
        String playerHealth = Double.toString(player.getHealth());

        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.yellow);

        //DRAWS UPS(update per seconds) AND FPS(frames per seconds)
        performance.draw(canvas);

        //DRAWS PLAYER'S CURRENT HP
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Current Health: " + playerHealth, 100, 300, paint);

        //DRAWS COMBAT TIMER
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Combat Timer: " + combatTimer, 100, 400, paint);

        //DRAWS ENEMY ADHDLEVEL
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("ADHD Level: " + enemy.getAdhdLevel(), 100, 500, paint);

        //DRAWS ENEMY FOCUS STATUS
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Focused: " + enemy.isFocused(), 100, 600, paint);

        //DRAWS ENEMY COLLISION BOXES
        paint.setColor(0xffff0000);
        for(Enemy enemy:enemyList){
            canvas.drawRect(enemy.getCollision(), paint);
        }
        //draws player collision box
        canvas.drawRect(player.getCollision(), paint);

    }
    private void drawGrid(Canvas canvas) {
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();

        int gridSize = 176;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);


        //offset to middle
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

                combatTimer = 3;
                enemy.setIsInCombat(true);
                flag = true;
            }

        }
        return flag;
    }
    private void combat(Enemy enemy, Player player) {
        enemy.setHealth(enemy.getHealth()-player.getStrength());
        player.setHealth(player.getHealth() -enemy.getStrength());

        System.out.println("Enemy Health: " + enemy.getHealth() + "//" + enemy.getMaxHealth());
        enemy.setIsInCombat(false);
    }


    private void addBonusToPlayer(Player player, String bonusStr) {
        List<String> listOfBonus = Arrays.asList(bonusStr.split("\\s*\n\\s*"));

        for (String str: listOfBonus) {
            switch (str){
                case "Prize 1":
                    player.setMaxHealth( player.getMaxHealth() + 1);
                    break;
                case "Prize 2":
                    player.setMaxHealth( player.getMaxHealth() - 2);
                    break;
                case "Prize 3":
                    player.setMaxHealth( player.getMaxHealth() + 3);
                    break;
                case "Prize 4":
                    player.setStrength(player.getStrength() + 1);
                    break;
                case "Prize 5":
                    player.setStrength(player.getStrength() - 2);
                    break;
                case "Prize 6":
                    player.setStrength(player.getStrength() + 3);
                    break;
                case "Prize 7":
                    player.setMaxHealth( player.getMaxHealth() + 10);
                    break;
            }

        }

    }
}
