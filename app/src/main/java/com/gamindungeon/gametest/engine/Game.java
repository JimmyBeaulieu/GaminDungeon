package com.gamindungeon.gametest.engine;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.activity.Bonus_Game_Activity;
import com.gamindungeon.gametest.activity.ShopActivity;
import com.gamindungeon.gametest.gamepanel.GameOver;
import com.gamindungeon.gametest.gamepanel.Performance;
import com.gamindungeon.gametest.graphics.UserInterface;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.manager.TileManager;
import com.gamindungeon.gametest.object.collectable.foodType;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.Player;
import com.gamindungeon.gametest.object.collectable.Coin;
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

    int shadeExpAmount;
    int shadeGoldAmount;

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        //enable us to render on screen and handle touch input
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        initialization(surfaceHolder);

        player.setTileManager(tileManager);

        setFocusable(true);

    }

    private void initialization(SurfaceHolder surfaceHolder) {

        gameLoop = new GameLoop(this, surfaceHolder);

        //initialize game panel
        performance = new Performance(gameLoop, getContext());
        score = new Score(getContext());

        music = new Music(getContext());
        sfx = new Music(getContext());

        loadPlayer();

        //Initialize game display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels-176, displayMetrics.heightPixels-176, player);

        tileManager = new TileManager(getContext(), gameDisplay, player, music);


        loadEnemies();

        ui = new UserInterface(getContext(), score, player);
        gameOver = new GameOver(getContext(), ui);

        if(firstTimePlaying){
            //loads the first map
            tileManager.loadMap(0);
            reloadMap();
        }

        //if player was dead when they left last game
        if(player.getHealth() + Player.lifeBonus <= 0){
            player.setHealth(player.getMaxHealth());
            player.setPositionX(tileManager.getCurrentMapSpawnX());
            player.setPositionY(tileManager.getCurrentMapSpawnY());
            Score.experience = 0;
            Score.gold = Score.gold/2;
            reloadMap();
        }


        tileManager.cleanUp();
        //Log.d("SAVEFILE", "after cleanup!");


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

        //handle touch event actions
        final float MIN_DISTANCE = 150f;
        String move="";

        switch (event.getAction()) {

            // user touches the screen
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();

                //Log.d("option", "X : " + startX);
                //Log.d("option", "Y : " + startY);

                menuShenanigans(startX, startY);


                if(gameOver.getGameOverState()) {
                    passDialogHitCount++;
                    if (passDialogHitCount == 2) {
                        passDialogHitCount = 0;
                        respawn();
                    }
                }
                if(ui.isInDialog()){
                    passDialogHitCount++;
                    if(passDialogHitCount == 3){
                        passDialogHitCount = 0;
                        ui.setInDialog(false);
                    }
                }
                if(player.getPowerUpState()){

                }

                break;

                //user stop touching the screen
            case MotionEvent.ACTION_UP:
                if(player.getHealth() + Player.lifeBonus > 0 && !ui.isInDialog() && !ui.isMenuOpen()) {

                    //calculates the difference between the point where the user originally touches the screen vs the point where the user stops
                    deltaX = event.getX() - startX;
                    deltaY = event.getY() - startY;

                    //System.out.println("startX:" + startX + "| startY:" + startY + "| deltaX:" + deltaX + "| deltaY:" + deltaY);
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
                    player.substractCalorie(10);
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


        }


        return true;
    }

    private void respawn() {
        player.setPositionX(tileManager.getCurrentMapSpawnX());
        player.setPositionY(tileManager.getCurrentMapSpawnY());
        player.setHealth(player.getMaxHealth());
        gameOver.setGameOverState(false);
        //music.play(tileManager.getCurrentLoadedMap());
        reloadMap();
        shadeSpawn();
        Score.gold /=2;
        Score.experience /=2;
    }

    private void menuShenanigans(float startX, float startY) {
        //where the gear's at
        if(startX <= 1988 && startX >= 1900 && startY <= 188 && startY >= 100){
            ui.displayMenu();
        }
        //press quit
        if(ui.isMenuOpen() && startX <= 1800 && startX >= 1600 && startY <= 800 && startY >= 600){
            music.stop();
            save();
            ((Activity) getContext()).finish();
        }
        //press no
        if(ui.isMenuOpen() && startX <= 1500 && startX >= 1250 && startY <= 700 && startY >= 500){
            save();
            ui.displayMenu();
        }
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
        if (player.getHealth() + Player.lifeBonus <= 0) {
            player.setPositionX(tileManager.getCurrentMapSpawnX());
            player.setPositionY(tileManager.getCurrentMapSpawnY());
            gameOver.draw(canvas);
            gameOver.setGameOverState(true);
            music.stop();
        }

        if(!gameOver.getGameOverState()) {

            ui.draw(canvas);
        }

        //drawDebug(canvas);


    }

    private void shadeSpawn() {
        enemyList.add(new Enemy(getContext(), player.getOldPositionX(), player.getOldPositionY(), player, tileManager, "shade"));
        shadeExpAmount = Score.experience / 2;
        shadeGoldAmount = Score.gold / 2;
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
        if(player.getHealth() + Player.lifeBonus <= 0){
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
    //int dialogPass = 0; //Used to make sure dialog isn't repeated, basically, check for an increasing amount of dialogPass, then increase dialogPass by one each time

    List<String> dialogPass = new ArrayList<String>();

    private void mapEvent(int currentLoadedMap, double x, double y) {
        switch(currentLoadedMap){
            case 0:
                //first map, map0

                //go to next map
                if(x == gridPos(37) && y == gridPos(17)){
                    music.stop();
                    tileManager.setCurrentLoadedMap(1);
                    player.setPositionY(gridPos(7));
                    player.setPositionX(gridPos(3));
                    reloadMap();
                }
                ///////////////////////////////////////////////////////////////////////////////////

                //Shop room
                if (player.getPositionX() == gridPos(41) && player.getPositionY() == gridPos(22)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    shopRoom();
                }
                //spin room
                if (player.getPositionX() == gridPos(40) && player.getPositionY() == gridPos(16)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    spinRoom();
                }

                //help signs
                if(player.getPositionX() == gridPos(6) && player.getPositionY() == gridPos(3)){
                    player.setPositionX(player.getOldPositionX());
                    player.setPositionY(player.getOldPositionY());
                    ui.createDialog("If I walk into enemies, I can fight them and get      rewards!");
                }

                if(player.getPositionX() == gridPos(22) && player.getPositionY() == gridPos(3)){
                    player.setPositionX(player.getOldPositionX());
                    player.setPositionY(player.getOldPositionY());
                    ui.createDialog("Finding food here on the ground is kinda weird but it looks really tasty...");
                }

                //will be used for hardcoded teleporter, basically:
                //IF player is on specific X and Y, THEN change X and Y position to newX and newY

                //first teleporter sending to second teleporter
                if(x == gridPos(12) && y == gridPos(17)){ teleport(39, 24); }

                //second teleporter sending to first teleporter
                if(x == gridPos(38) && y == gridPos(24)){ teleport(13, 17); }



                //Cool ass dialogue system!
                if(x == gridPos(0) && y == gridPos(4)){
                    ui.createDialog("It took me ages to find this place, I am not going    back!!");
                    player.setPositionX(gridPos(1));
                }

                if(x == gridPos(3) && y == gridPos(4) && !dialogPass.contains("Finally I found it!")) {
                    ui.createDialog("Finally I found it! The famous Gamin's Dungeon...");
                    dialogPass.add("Finally I found it!");
                }
                if(x == gridPos(9) && y == gridPos(4) && !dialogPass.contains("Now where did Gamin")) {
                    ui.createDialog("Now where did Gamin put his famous recipe for paczki...");
                    dialogPass.add("Now where did Gamin");
                }
                if(x == gridPos(22) && y == gridPos(26) && !dialogPass.contains("Those eyes look dangerous")){
                    ui.createDialog("Those eyes look dangerous... I really don't want to   get stuck under one of them...");
                    dialogPass.add("Those eyes look dangerous");
                }


                break;
            case 1:

                //go back to previous map
                if(x == gridPos(2) && y == gridPos(7)){
                    music.stop();
                    tileManager.loadMap(0);
                    player.setPositionX(gridPos(38));
                    player.setPositionY(gridPos(17));
                    reloadMap();
                }

                //go to next map
                if(x == gridPos(5) && y == gridPos(34)){
                    music.stop();
                    tileManager.loadMap(2);
                    player.setPositionX(gridPos(9));
                    player.setPositionY(gridPos(3));
                    reloadMap();
                }
                ///////////////////////////////////////////////////////////////////////////////////

                if(x == gridPos(36) && y == gridPos(32)){
                    ui.createDialog("It's locked... no way for me to open this door...");
                    player.setPositionY(gridPos(31));
                }

                //spin room
                if (player.getPositionX() == gridPos(8) && player.getPositionY() == gridPos(47)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    spinRoom();
                }

                //shop room
                if (player.getPositionX() == gridPos(3) && player.getPositionY() == gridPos(44)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    shopRoom();
                }

                break;
            case 2:

                //go back to previous map
                if(x == gridPos(9) && y == gridPos(2)){
                    music.stop();
                    tileManager.loadMap(1);
                    player.setPositionX(gridPos(6));
                    player.setPositionY(gridPos(34));
                    reloadMap();
                }

                //go to next map
                if(x == gridPos(1) && y == gridPos(46)){
                    music.stop();
                    tileManager.loadMap(3);
                    player.setPositionX(gridPos(25));
                    player.setPositionY(gridPos(47));
                    reloadMap();
                }

                ///////////////////////////////////////////////////////////////////////////////////

                //spin room
                if ((player.getPositionX() == gridPos(1) && player.getPositionY() == gridPos(37)) ||
                    player.getPositionX() == gridPos(40) && player.getPositionY() == gridPos(5)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    spinRoom();
                }
                //shop room
                if (player.getPositionX() == gridPos(23) && player.getPositionY() == gridPos(38)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    shopRoom();
                }


                if( (x == gridPos(20) && y == gridPos(45) && !dialogPass.contains("That Lava looks dangerous."))) {
                    ui.createDialog("That Lava looks dangerous.");
                    dialogPass.add("That Lava looks dangerous.");
                }


                break;
            case 3:

                //go back to previous map
                if(x == gridPos(25) && y == gridPos(48)){
                    music.stop();
                    tileManager.loadMap(2);
                    player.setPositionX(gridPos(1));
                    player.setPositionY(gridPos(45));
                    reloadMap();
                }

                //spin room
                if ((player.getPositionX() == gridPos(26) && player.getPositionY() == gridPos(15)) ||
                        player.getPositionX() == gridPos(44) && player.getPositionY() == gridPos(26)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    spinRoom();
                }

                //shop room
                if (player.getPositionX() == gridPos(17) && player.getPositionY() == gridPos(15)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    shopRoom();
                }
                if (player.getPositionX() == gridPos(49) && player.getPositionY() == gridPos(26)) {
                    music.stop();
                    Score.music = tileManager.getCurrentLoadedMap();
                    shopRoom();
                }


                if(x == gridPos(28) && y == gridPos(46)){ teleport(45, 3); }
                if(x == gridPos(45) && y == gridPos(2)){ teleport(27, 46); }

                if( (x == gridPos(1) && y == gridPos(1))) {
                    ui.createDialog("For some reason I feel like this is the end for now...  I honestly don't know why but I feel like maybe someday it will be finished if anyone asks for it...");
                }

                break;

        }

    }

    private void shopRoom(){
        player.setPositionX(player.getOldPositionX());
        player.setPositionY(player.getOldPositionY());
        music.stop();
        Intent i = new Intent(getContext(), ShopActivity.class);
        startActivity(getContext(), i, null);
    }

    private void spinRoom() {
        player.setPositionX(player.getOldPositionX());
        player.setPositionY(player.getOldPositionY());
        if (Score.gold >= 5) {
            music.stop();
            Intent i = new Intent(getContext(), Bonus_Game_Activity.class);
            startActivity(getContext(), i, null);

        } else {
            ui.createDialog("GET OUT OF HERE YOU FILTHY BEGGAR, COME BACK WHEN YOU HAVE AT LEAST 5$!!");
        }
    }

    private void reloadMap() {

        //regenerate the array lists so there's no doubles
        enemyList = new ArrayList<Enemy>();
        coinList = new ArrayList<Coin>();
        foodList = new ArrayList<Food>();
        tileManager.loadMap(tileManager.getCurrentLoadedMap());

        //generates entity depending on where they were placed on the map editor

        //enemies
        enemyList.addAll(tileManager.getEnemyOnMap());

        //coins
        coinList.addAll(tileManager.getCoinOnMap());

        //food
        foodList.addAll(tileManager.getFoodOnMap());

    }

    private void combat(Enemy enemy, Player player) {

        sfx.playSFX(1);
        player.setPositionX(player.getOldPositionX());
        player.setPositionY(player.getOldPositionY());

        enemy.setHealth(enemy.getHealth() - (player.getStrength() + Player.strengthBonus));
        player.setHealth((player.getHealth() + Player.lifeBonus) - enemy.getStrength());

        if(player.getHealth() + Player.lifeBonus <= 0){
            //music.stop();
            sfx.play(6);
            //Log.d("gameover", "dead2 eletric boogallo!");
        }

    }
    private void checkForCollision() {

        //TODO make it more optimized!!

        //Kamil
        for(int i = 0 ; i < coinList.size() ;i++){
            Coin coin = coinList.get(i);
            if (player.getPositionX() == coin.getPositionX() &&
                    player.getPositionY() == coin.getPositionY()) {
                Score.gold = Score.gold + 1 + Player.goldBonus;
                sfx.playSFX(0);
                coinList.remove(coin);
            }
        }

        for (int i = 0 ; i < enemyList.size() ;i++){
            Enemy enemy = enemyList.get(i);
            if (player.getPositionX() == enemy.getPositionX() &&
            player.getPositionY() == enemy.getPositionY()) {
                player.setIsInCombat(true);
                combatTimer = 3;
                combat(enemy, player);
            }
            //
            if (enemyList.get(i).getHealth() <= 0) {
                String type = enemyList.get(i).getType();
                Random random = new Random();
                int chance = random.nextInt(10)+1;
                if(chance >= 8){
                    coinList.add(new Coin(getContext(), enemyList.get(i).getPositionX(), enemyList.get(i).getPositionY()));
                }
                if(chance <8 && chance >= 5){
                    foodList.add(getRandomFood(enemyList.get(i).getPositionX(), enemyList.get(i).getPositionY()));
                }
                switch(type){
                    case "bat":
                        Score.experience += 5;
                        Score.batDefeated += 1;
                        break;
                    case "witch":
                        Score.experience+=20;
                        Score.witchDefeated += 1;
                        break;
                    case "spirit":
                        Score.experience += 10;
                        Score.spiritDefeated += 1;
                        break;
                    case "eye":
                        Score.experience += 100;
                        Score.eyeDefeated += 1;
                        break;
                    case "shade":
                        Score.experience += shadeExpAmount;
                        Score.gold += shadeGoldAmount;
                        shadeGoldAmount = 0;
                        shadeExpAmount = 0;

                }
                enemyList.remove(enemyList.get(i));

            }
            //

        }

        for(int i = 0; i<foodList.size();i++){
            Food food = foodList.get(i);
            if(player.getPositionX() == food.getPositionX() &&
            player.getPositionY() == food.getPositionY()){
                player.setHunger(player.getHunger() + food.getHunger());
                player.setHealth(player.getHealth() + food.getHealthRestoreAmount());
                Score.caloriesIntake += food.getCalorie();
                player.addCalorie(food.getCalorie());
                sfx.playSFX(2);
                foodList.remove(food);
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

    private Food getRandomFood(double positionX, double positionY) {

        Random rand = new Random();
        Food food = new Food(getContext(), positionX, positionY, foodType.DONUT);;
        switch(rand.nextInt(7)){
            case 0:
                food = new Food(getContext(), positionX, positionY, foodType.DONUT);
                break;
            case 1:
                food = new Food(getContext(), positionX, positionY, foodType.DRUMSTICK);
                break;
            case 2:
                food = new Food(getContext(), positionX, positionY, foodType.BURGER);
                break;
            case 3:
                food = new Food(getContext(), positionX, positionY, foodType.CAKE);
                break;
            case 4:
                food = new Food(getContext(), positionX, positionY, foodType.CONE);
                break;
            case 5:
                food = new Food(getContext(), positionX, positionY, foodType.POTION);
                break;


        }
        return food;
    }

    private void teleport(int x, int y) {
        sfx.playSFX(4);
        player.setPositionX(gridPos(x));
        player.setPositionY(gridPos(y));

    }

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

        //content.append(dialogPass).append("||");

        content.append(Score.gold).append("||");
        content.append(Score.experience).append("||");
        content.append(Score.caloriesIntake).append("||");
        content.append(Score.batDefeated).append("||");
        content.append(Score.witchDefeated).append("||");
        content.append(Score.spiritDefeated).append("||");
        content.append(Score.eyeDefeated).append("||");

        content.append(tileManager.getCurrentLoadedMap()).append("||");

        Score.saveContent = content.toString();

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
                Score.gold = Integer.parseInt(contentArray[contentArray.length - 3]);
                //Log.d("SAVEFILE", (contentArray[contentArray.length - 2]));
                Score.experience = Integer.parseInt(contentArray[contentArray.length - 2]);

//player START
                String[] playerInfo = contentArray[0].split("\\|");

                player = new Player
                        (
                                getContext(),
                                sfx,
                                Double.parseDouble(playerInfo[0]),  //positionX
                                Double.parseDouble(playerInfo[1]),  //positionY
                                Double.parseDouble(playerInfo[2]),  //health
                                Double.parseDouble(playerInfo[3]),  //maxHealth
                                Double.parseDouble(playerInfo[4]),  //strength
                                Double.parseDouble(playerInfo[5]),  //hunger
                                Double.parseDouble(playerInfo[6]),  //oldPositionX
                                Double.parseDouble(playerInfo[7]),  //oldPositionY
                                String.valueOf(playerInfo[8]),
                                Integer.parseInt(playerInfo[9])       //lastKnownMove
                        );
//player END
            }
            else{
                //new save file

                //create a new player
                player = new Player(getContext(), sfx, gridPos(3), gridPos(4), 100,
                100, 30, 50, gridPos(3), gridPos(4), "down", 1);

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

                    //Log.d("SAVEFILE", String.valueOf(index));

                    if (splitStr[0].equals("coin")) {
                        coinList.add(new Coin(
                                getContext(),
                                Double.parseDouble(splitStr[1]),
                                Double.parseDouble(splitStr[2])
                        ));
                    }

                    if (splitStr[0].equals("food")) {
                        //Log.d("SAVEFILE_FOOD", splitStr[3]);

                        if(splitStr[3].equalsIgnoreCase("DONUT")){
                            foodList.add(new Food(
                                    getContext(),
                                    Double.parseDouble(splitStr[1]),
                                    Double.parseDouble(splitStr[2]),
                                    foodType.DONUT
                            ));
                        }
                        if(splitStr[3].equalsIgnoreCase("DRUMSTICK")){
                            foodList.add(new Food(
                                    getContext(),
                                    Double.parseDouble(splitStr[1]),
                                    Double.parseDouble(splitStr[2]),
                                    foodType.DRUMSTICK
                            ));
                        }
                        if(splitStr[3].equalsIgnoreCase("burger")){
                            foodList.add(new Food(
                                    getContext(),
                                    Double.parseDouble(splitStr[1]),
                                    Double.parseDouble(splitStr[2]),
                                    foodType.BURGER
                            ));
                        }
                        if(splitStr[3].equalsIgnoreCase("cake")){
                            foodList.add(new Food(
                                    getContext(),
                                    Double.parseDouble(splitStr[1]),
                                    Double.parseDouble(splitStr[2]),
                                    foodType.CAKE
                            ));
                        }
                        if(splitStr[3].equalsIgnoreCase("cone")){
                            foodList.add(new Food(
                                    getContext(),
                                    Double.parseDouble(splitStr[1]),
                                    Double.parseDouble(splitStr[2]),
                                    foodType.CONE
                            ));
                        }
                        if(splitStr[3].equalsIgnoreCase("potion")){
                            foodList.add(new Food(
                                    getContext(),
                                    Double.parseDouble(splitStr[1]),
                                    Double.parseDouble(splitStr[2]),
                                    foodType.POTION
                            ));
                        }


                    }
                }
                Score.gold = Integer.parseInt(contentArray[contentArray.length - 8]);
                Score.experience = Integer.parseInt(contentArray[contentArray.length - 7]);
                Score.caloriesIntake = Double.parseDouble(contentArray[contentArray.length - 6]);
                Score.batDefeated = Integer.parseInt(contentArray[contentArray.length - 5]);
                Score.witchDefeated = Integer.parseInt(contentArray[contentArray.length - 4]);
                Score.spiritDefeated = Integer.parseInt(contentArray[contentArray.length - 3]);
                Score.eyeDefeated = Integer.parseInt(contentArray[contentArray.length - 2]);
                tileManager.loadMap(Integer.parseInt(contentArray[contentArray.length - 1]));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

int x = 0;
    public void onResume() {
        if(x != 0) {
            music.play(tileManager.getCurrentLoadedMap());
        }
        x++;
        tileManager.cleanUp();
    }
    public void pause() {
        gameLoop.stopLoop();
        music.pause();
    }
}
