package com.gamindungeon.gametest.engine;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
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
import com.gamindungeon.gametest.object.Coin;
import com.gamindungeon.gametest.object.CoinMachine;
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
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Coin> coinList = new ArrayList<Coin>();
    private List<CoinMachine> coinMachineList = new ArrayList<CoinMachine>();
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;
    private Music music;
    private Music sfx;

    TileManager tileManager;
    Score score;
    boolean fading = false;

    //TO BE REMOVED
    //boolean mapLoaded = false;
    //TO BE REMOVED
    //private Enemy enemy;



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

        //TO BE REMOVED
        //SpriteSheet spriteSheet = new SpriteSheet(context);

        score = new Score(context);

        music = new Music(context);
        sfx = new Music(context);
        music.play(2);

        player = new Player(getContext(), music);

        //Adding bonus
        if(!bonusStr.equals("")){
            player.addBonusToPlayer(bonusStr);
        }

        //Initialize game display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels-176, displayMetrics.heightPixels-176, player);

        tileManager = new TileManager(context, gameDisplay, player);
        player.setTileManager(tileManager);

        //Map selector
        switch (tileManager.getCurrentLoadedMap()){
            case 0:
                //enemies
                enemyList.add(new Enemy(getContext(), gridPos(9), gridPos(4), player, tileManager));
                enemyList.add(new Enemy(getContext(), gridPos(17), gridPos(5), player, tileManager));
                enemyList.add(new Enemy(getContext(), gridPos(21), gridPos(6), player, tileManager));
                enemyList.add(new Enemy(getContext(), gridPos(23), gridPos(11), player, tileManager));
                enemyList.add(new Enemy(getContext(), gridPos(21), gridPos(11), player, tileManager));
                enemyList.add(new Enemy(getContext(), gridPos(12), gridPos(18),  player, tileManager));

                //coin machines
                coinMachineList.add(new CoinMachine(context, gridPos(40), gridPos(18)));

                //coins
                coinList.add(new Coin(context, gridPos(12), gridPos(19)));


                break;
            case 1:

                break;

        }

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
    int gameoverHitCount = 0;
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

                if(gameOver.getGameOverState()) {
                    gameoverHitCount++;
                    if (gameoverHitCount == 2) {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(i);
                    }
                }

                break;

                //user stop touching the screen
            case MotionEvent.ACTION_UP:
                //!checkCollision() &&
                if(player.getHealth() > 0) {

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
                //if(!checkCollision()){
                player.setOldX(player.getPositionX());
                player.setOldY(player.getPositionY());
                player.move(move);

                    for(Enemy enemy:enemyList){
                        enemy.statusBranch();
                    }
                //}

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

            for(CoinMachine machine : coinMachineList){
                machine.draw(canvas, gameDisplay);
            }

            for(Coin coin : coinList){
                coin.draw(canvas, gameDisplay);
            }

            player.draw(canvas, gameDisplay);
            //enemy.draw(canvas, gameDisplay);
            for (Enemy enemy : enemyList) {
                enemy.draw(canvas, gameDisplay);
            }
            //Draw Game Over if (player's hp <= 0)
            if (player.getHealth() <= 0) {
                gameOver.draw(canvas);
                gameOver.setGameOverState(true);
                music.stop();
            }
            drawGrid(canvas);
            score.draw(canvas);

            //TODO make it work
            //fadeBlack(canvas);

        //******************************************************************************************
            // TO BE DEACTIVATED FOR FULL RELEASE

            //drawDebug(canvas);

            //******************************************************************************************

    }

    private void drawDebug(Canvas canvas) {

        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.yellow);

        //DRAWS UPS(update per seconds) AND FPS(frames per seconds)
        performance.draw(canvas);

        //DRAWS COMBAT TIMER
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Combat Timer: " + combatTimer, 100, 400, paint);

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
    for (int i = 88; i < screenWidth; i += gridSize) {
        //draws vertical lines X
        canvas.drawLine(i, 0, i, screenHeight, paint);
    }
    for (int i = 100; i < screenHeight; i += gridSize) {
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

        checkForCollision();
        checkForTeleport();

        if(combatTimer == 0){
            player.setIsInCombat(false);
        }

        gameDisplay.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    private void combat(Enemy enemy, Player player) {

        sfx.playSFX(1);
        player.setPositionX(player.getOldPositionX());
        player.setPositionY(player.getOldPositionY());

        enemy.setHealth(enemy.getHealth() - player.getStrength());
        player.setHealth(player.getHealth() - enemy.getStrength());

        System.out.println("Enemy Health: " + enemy.getHealth() + "//" + enemy.getMaxHealth());


    }

    private void checkForCollision() {

        //check for coins, if it finds one, pick it up and add it to total coin score
        for(Coin coin : coinList){
            if (player.getPositionX() == coin.getPositionX() &&
                    player.getPositionY() == coin.getPositionY()) {
                score.setGold(score.getGold()+1);
                sfx.playSFX(0);
                coinList.remove(coin);
            }
        }

        //check for coin machines, if it collides with one, relocate player to old position and start coin minigame if player at least have one coin
        for(CoinMachine machine : coinMachineList){
            if (player.getPositionX() == machine.getPositionX() &&
                    player.getPositionY() == machine.getPositionY()) {
                player.setPositionY(player.getOldPositionY());
                player.setPositionX(player.getOldPositionX());

                if(score.getGold() > 0){
                    score.setGold(score.getGold() - 1);
                    Random rand = new Random();
                    switch(rand.nextInt(10)+1){
                        case 1:
                            score.setGold(score.getGold() + 1);
                            break;
                        case 2:
                            score.setGold(score.getGold() + 3);
                            break;
                        case 3:
                            score.setGold(score.getGold() + 5);
                            break;
                        case 4:
                            score.setGold(score.getGold() * 2);
                            break;
                        case 5:
                            score.setGold(score.getGold() * 3);
                            break;
                        case 6:
                            score.setGold(score.getGold() * 5);
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                    }
                }
            }
        }

        for (Enemy enemy : enemyList) {
            if (player.getPositionX() == enemy.getPositionX() &&
            player.getPositionY() == enemy.getPositionY()) {
                combatTimer = 3;
                combat(enemy, player);
            }
        }

        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).getHealth() <= 0) {
                Random random = new Random();
                int chance = random.nextInt(10)+1;
                if(chance >= 5){
                    coinList.add(new Coin(getContext(), enemyList.get(i).getPositionX(), enemyList.get(i).getPositionY()));
                }

                enemyList.remove(enemyList.get(i));
                score.setExperience(score.getExperience() + 1);
            }
        }
            //checks if two enemies intersects, if so, prevent movement for one enemy for one turn
        for(int i = 0; i<enemyList.size();i++){
            for(int j = 0; j<enemyList.size(); j++){
                if(j == i){
                    break;
                }
                else if(enemyList.get(i).getPositionX() == enemyList.get(j).getPositionX() &&
                enemyList.get(i).getPositionY() == enemyList.get(j).getPositionY()){
                    enemyList.get(i).setPositionX(enemyList.get(i).getOldPositionX());
                    enemyList.get(i).setPositionY(enemyList.get(i).getOldPositionY());
                }
            }
        }
    }

    private void checkForTeleport() {
        //X = Col
        //Y = Row

        //will be used for hardcoded teleporter, basically:
        //IF player is on specific X and Y, THEN change X and Y position to newX and newY

        //first teleporter sending to second teleporter
        if(player.getPositionX() == gridPos(12) && player.getPositionY() == gridPos(17)){ teleport(39, 24); }

        //second teleporter sending to first teleporter
        if(player.getPositionX() == gridPos(38) && player.getPositionY() == gridPos(24)){ teleport(13, 17); }


    }

    private void teleport(int x, int y) {
        //fading =true;
        player.setPositionX(gridPos(x));
        player.setPositionY(gridPos(y));

    }

    //TODO
    //make it work properly
    /*
    private void fadeBlack(Canvas canvas){
        if(fading) {
            Rect rect = new Rect(0, 0, getWidth(), getHeight());
            Paint paint = new Paint(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            Handler handler = new Handler(Looper.getMainLooper());


            ValueAnimator animator = ValueAnimator.ofInt(0, 255);
            animator.setDuration(1000); // 1 second duration
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int alpha = (int) animation.getAnimatedValue();
                    paint.setAlpha(alpha);

                    canvas.drawRect(rect, paint);

                    invalidate(); // Request a redraw
                }
            });
            handler.post(new Runnable() {
                @Override
                public void run() {
                    animator.start();
                    fading = false;
                }

            });
        }
    }

     */

}
