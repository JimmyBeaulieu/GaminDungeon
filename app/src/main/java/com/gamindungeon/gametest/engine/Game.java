package com.gamindungeon.gametest.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.activity.Bonus_Game_Activity;
import com.gamindungeon.gametest.activity.MainActivity;
import com.gamindungeon.gametest.graphics.UserInterface;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.manager.TileManager;
import com.gamindungeon.gametest.gamepanel.GameOver;
import com.gamindungeon.gametest.gamepanel.Performance;
import com.gamindungeon.gametest.object.collectable.Coin;
import com.gamindungeon.gametest.object.CoinMachine;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.Player;
import com.gamindungeon.gametest.object.Teleporter;
import com.gamindungeon.gametest.object.collectable.Food;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Game manages all objects in the game and is responsible for updating
// all states and render all objects oto the screen
public class Game extends SurfaceView implements SurfaceHolder.Callback{

    String saveFileName = "SaveFile";
    private Player player;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Coin> coinList = new ArrayList<Coin>();
    private List<CoinMachine> coinMachineList = new ArrayList<CoinMachine>();
    private List<Teleporter> teleporterList = new ArrayList<Teleporter>();
    private List<Food> foodList = new ArrayList<Food>();
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;
    private Music music;
    private Music sfx;

    TileManager tileManager;
    Score score;
    UserInterface ui;
    boolean firstTimePlaying = false;

    //TODO make the method work
    boolean fading = false;

    public Game(Context context, String bonusStr) {
        super(context);

        //Get surface holder and add callback
        //enable us to render on screen and handle touch input
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        initialization(surfaceHolder);

        //Adding bonus
        if(!bonusStr.equals("")){
            player.addBonusToPlayer(bonusStr);
        }
        player.setTileManager(tileManager);

//#######################################################################################################################################################

        //load();


//#######################################################################################################################################################
        //Map selector
        //loadMap(0);

        setFocusable(true);

    }

    private void initialization(SurfaceHolder surfaceHolder) {

        gameLoop = new GameLoop(this, surfaceHolder);

        //initialize game panel
        performance = new Performance(gameLoop, getContext());
        score = new Score(getContext());

        music = new Music(getContext());
        sfx = new Music(getContext());
        music.play(2);

        loadPlayer();



        //Initialize game display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels-176, displayMetrics.heightPixels-176, player);

        tileManager = new TileManager(getContext(), gameDisplay, player);

        loadEnemies();

        ui = new UserInterface(getContext(), score, player);
        gameOver = new GameOver(getContext(), ui);

        //generates entity depending on where they were placed on the map editor

        //Entities that don't change no matter the save
        //teleporter
        teleporterList.addAll(tileManager.getTeleporterOnMap());

        //coin machines
        coinMachineList.addAll(tileManager.getCoinMachineOnMap());


        if(firstTimePlaying){
            //loads the first map
            tileManager.loadMap(1);

            //generates entity depending on where they were placed on the map editor

            //save file dependant entity
            //enemies
            enemyList.addAll(tileManager.getEnemyOnMap());

            //coins
            coinList.addAll(tileManager.getCoinOnMap());

            //food
            foodList.addAll(tileManager.getFoodOnMap());
        }

        //if player was dead when they left last game
        if(player.getHealth() <= 0){
            player.setHealth(player.getMaxHealth());
            player.setPositionX(tileManager.getCurrentMapSpawnX());
            player.setPositionY(tileManager.getCurrentMapSpawnY());
            score.setExperience(0);
            score.setGold(0);
        }

        tileManager.cleanUp();
        Log.d("SAVEFILE", "after cleanup!");


    }


    private double gridPos(int i) {
        return (i)*176;
    }

    float startX = 0;
    float startY = 0;
    float deltaX = 0;
    float deltaY = 0;
    int combatTimer = 0;
    int passDialogHitCount = 0;
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
                    passDialogHitCount++;
                    if (passDialogHitCount == 2) {
                        passDialogHitCount = 0;
                        Intent i = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(i);
                    }
                }
                if(ui.isInDialog()){
                    passDialogHitCount++;
                    if(passDialogHitCount == 3){
                        passDialogHitCount = 0;
                        ui.setInDialog(false);
                    }
                }

                break;

                //user stop touching the screen
            case MotionEvent.ACTION_UP:
                //!checkCollision() &&
                if(player.getHealth() > 0 && !ui.isInDialog()) {

                    //calculates the difference between the point where the user originally touches the screen vs the point where the user stops
                    deltaX = event.getX() - startX;
                    deltaY = event.getY() - startY;

                    System.out.println("startX:" + startX + "| startY:" + startY + "| deltaX:" + deltaX + "| deltaY:" + deltaY);
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (deltaX > MIN_DISTANCE) {
                            move = "right";
                        } else if (deltaX < MIN_DISTANCE * -1) {
                            move = "left";
                        }
                    } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
                        if (deltaY > MIN_DISTANCE) {
                            move = "down";
                        } else if (deltaY < MIN_DISTANCE * -1) {
                            move = "up";
                        }
                    }
                }

                player.setOldX(player.getPositionX());
                player.setOldY(player.getPositionY());
                player.move(move);

                save();

                for(Enemy enemy:enemyList){
                    enemy.statusBranch();
                }

                if (combatTimer > 0) {
                    combatTimer--;
                }
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

        for(Teleporter teleporter : teleporterList){
            teleporter.draw(canvas, gameDisplay);
        }

        for(CoinMachine machine : coinMachineList){
            machine.draw(canvas, gameDisplay);
        }

        for(Coin coin : coinList){
            coin.draw(canvas, gameDisplay);
        }

        for(Food food : foodList){
            food.draw(canvas, gameDisplay);
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
        if(!gameOver.getGameOverState()) {
            ui.draw(canvas);
        }
        //TODO make it work
        //fadeBlack(canvas);

    //******************************************************************************************
        // TO BE DEACTIVATED FOR FULL RELEASE

        drawDebug(canvas);

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
        canvas.drawText("Combat Timer: " + combatTimer, 100, 500, paint);

    }

    public void update() {
        //Stop updating the game if the player is dead
        if(player.getHealth() <= 0){
            player.setLastKnownMove("up");
            return;
        }
        //update game state
        player.update();

        if(combatTimer == 0){
            player.setIsInCombat(false);
        }

        checkForCollision();
        mapEvent(tileManager.getCurrentLoadedMap(), player.getPositionX(), player.getPositionY());
        gameDisplay.update();
    }
int dialogPass = 0; //Used to make sure dialog isn't repeated, basically, check for an increasing amount of dialogPass, then increase dialogPass by one each time
    private void mapEvent(int currentLoadedMap, double x, double y) {
        switch(currentLoadedMap){
            case 0:
                //mapTest
                ///////////////////////////////////////////////////////////////////////////////////

                //will be used for hardcoded teleporter, basically:
                //IF player is on specific X and Y, THEN change X and Y position to newX and newY

                //first teleporter sending to second teleporter
                if(x == gridPos(12) && y == gridPos(17)){ teleport(39, 24); }

                //second teleporter sending to first teleporter
                if(x == gridPos(38) && y == gridPos(24)){ teleport(13, 17); }


                if(x == gridPos(3) && y == gridPos(4) && dialogPass < 1) {
                    ui.createDialog("What is this, Minecraft?");
                    dialogPass++;
                }
                if(x == gridPos(9) && y == gridPos(4) && dialogPass < 2) {
                    ui.createDialog("I can't believe my eyes! what is this horrible, monster-filled place??");
                    dialogPass++;
                }

                break;
        }

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

                Intent i = new Intent(getContext(), Bonus_Game_Activity.class);
                getContext().startActivity(i);

/*
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
                        default:
                            break;
                    }
                }
*/

            }
        }

        for (Enemy enemy : enemyList) {
            if (player.getPositionX() == enemy.getPositionX() &&
            player.getPositionY() == enemy.getPositionY()) {
                player.setIsInCombat(true);
                combatTimer = 3;
                combat(enemy, player);
            }
        }

        for(int i = 0; i<foodList.size();i++){
            if(player.getPositionX() == foodList.get(i).getPositionX() &&
            player.getPositionY() == foodList.get(i).getPositionY()){
                player.setHunger(player.getHunger() + foodList.get(i).getHunger());
                sfx.playSFX(2);
                foodList.remove(foodList.get(i));
            }
        }

        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).getHealth() <= 0) {
                Random random = new Random();
                int chance = random.nextInt(10)+1;
                if(chance >= 8){
                    coinList.add(new Coin(getContext(), enemyList.get(i).getPositionX(), enemyList.get(i).getPositionY()));
                }
                if(chance <8 && chance >= 6){
                    foodList.add(new Food(getContext(), enemyList.get(i).getPositionX(), enemyList.get(i).getPositionY(), "apple"));
                }
                if(chance == 5){
                    foodList.add(new Food(getContext(), enemyList.get(i).getPositionX(), enemyList.get(i).getPositionY(), "turkeyleg"));
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
/*

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

 */

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

    public void save(){

        StringBuilder content;
        //Log.d("SAVEFILE", player.getLastKnownMove());

        content = new StringBuilder(player.toString());

        for(Enemy enemy : enemyList){
            content.append("enemy|").append(enemy);
        }

        for(Coin coin : coinList){
            content.append("coin|").append(coin);
        }

        for(Food food : foodList){
            content.append("food|").append(food);
        }

        content.append(dialogPass).append("||");

        //Log.d("SAVEFILE","map#|" + tileManager.getCurrentLoadedMap());
        content.append(tileManager.getCurrentLoadedMap()).append("||");



        try {
            File directory = getContext().getFilesDir();

            // Create a file object for the desired file name in the app's private storage
            File file = new File(directory, saveFileName);

            // Open a file output stream for writing to the file
            FileOutputStream stream = new FileOutputStream(file);

            // Write the content to the file
            stream.write(String.valueOf(content).getBytes());

            // Close the output stream
            stream.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the file.");
            e.printStackTrace();
        }

    }
    public void loadPlayer(){
        String content;
        try {

            File file = new File(getContext().getFilesDir(), saveFileName);
            if(file.exists()) {
                firstTimePlaying = false;
                Log.d("SAVEFILE", "Save found!");
                //found a save file on the system

                FileInputStream fis = getContext().openFileInput(saveFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                content = sb.toString();
                //takes the whole savefile into a String variables and split it each || character
                String[] contentArray = content.split("\\|\\|");
                Log.d("SAVEFILE", contentArray[0]);

//player START
                String[] playerInfo = contentArray[0].split("\\|");

                player = new Player
                        (
                                getContext(),
                                music,
                                Double.parseDouble(playerInfo[0]),  //positionX
                                Double.parseDouble(playerInfo[1]),  //positionY
                                Double.parseDouble(playerInfo[2]),  //health
                                Double.parseDouble(playerInfo[3]),  //maxHealth
                                Double.parseDouble(playerInfo[4]),  //strength
                                Double.parseDouble(playerInfo[5]),  //hunger
                                Double.parseDouble(playerInfo[6]),  //oldPositionX
                                Double.parseDouble(playerInfo[7]),  //oldPositionY
                                String.valueOf(playerInfo[8])       //lastKnownMove
                        );
//player END


            }
            else{
                //new save file

                //create a new player
                player = new Player(getContext(), music, gridPos(3), gridPos(4), 100,
                100, 30, 50, gridPos(3), gridPos(4), "up");

                //sets boolean for first time playing map initialization
                firstTimePlaying = true;

            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadEnemies(){

        String content;

        try {

            File file = new File(getContext().getFilesDir(), saveFileName);
            if(file.exists()) {
                firstTimePlaying = false;
                //Log.d("SAVEFILE", "Save found!");
                //found a save file on the system

                FileInputStream fis = getContext().openFileInput(saveFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                content = sb.toString();

                //takes the whole savefile into a String variables and split it each || character
                String[] contentArray = content.split("\\|\\|");

                int index = 0;

                for (int i = 1; i < contentArray.length; i++) {

                    String[] splitStr = contentArray[i].split("\\|");

                    if (splitStr[0].equals("enemy")) {
                        index++;
                        enemyList.add(new Enemy
                                (
                                        getContext(),
                                        Double.parseDouble(splitStr[1]),  //positionX
                                        Double.parseDouble(splitStr[2]),  //positionY
                                        player,                             //player
                                        tileManager,                        //tilemanager
                                        splitStr[3]                         //type
                                ));
                    }

                    Log.d("SAVEFILE", String.valueOf(index));

                    if (splitStr[0].equals("coin")) {
                        coinList.add(new Coin(
                                getContext(),
                                Double.parseDouble(splitStr[1]),
                                Double.parseDouble(splitStr[2])
                        ));
                    }

                    if (splitStr[0].equals("food")) {
                        //Log.d("SAVEFILE_FOOD", splitStr[3]);
                        foodList.add(new Food(
                                getContext(),
                                Double.parseDouble(splitStr[1]),
                                Double.parseDouble(splitStr[2]),
                                splitStr[3]
                        ));
                    }
                }
                dialogPass = Integer.parseInt(contentArray[contentArray.length - 2]);
                tileManager.loadMap(Integer.parseInt(contentArray[contentArray.length - 1]));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }

}
