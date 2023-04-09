package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.object.collectable.Coin;
import com.gamindungeon.gametest.object.CoinMachine;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.Player;
import com.gamindungeon.gametest.object.Teleporter;
import com.gamindungeon.gametest.object.collectable.Food;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {

    Tile[] tiles;
    Context context;
    private int[][] mapTileNum;
    GameDisplay gameDisplay;
    Player player;
    int currentLoadedMap;

    public TileManager(Context context, GameDisplay gameDisplay, Player player) {
        this.context = context;
        tiles = new Tile[40];
        this.gameDisplay = gameDisplay;
        mapTileNum = new int[gameDisplay.getMaxScreenColumns()][gameDisplay.getMaxScreenRows()];
        this.player = player;
        getTileImage();
        loadMap(0);
        currentLoadedMap = 0;
    }

    public Tile getTiles(int index) {
        return tiles[index];
    }

    public void getTileImage() {

        try {

            tiles[0] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.a_rock), true, "wall");
            tiles[1] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.b_water), true, "wall");
            tiles[2] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.c_wood), false, "floor");
            tiles[3] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.d_coin), false, "coin");
            tiles[4] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.e_teleport), false, "teleporter");
            tiles[5] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.f_coinmachine), false, "coinmachine");
            tiles[6] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.g_bat), false, "bat");
            tiles[7] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.h_turkeyleg), false, "food");
            tiles[8] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.i_apple), false, "food");



//            tiles[0] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile000), true, "background");
//            tiles[1] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile001), true, "wall");
//            tiles[2] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile002), true, "wall");
//            tiles[3] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile003), true, "wall");
//            tiles[4] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile004), false, "floor");
//            tiles[5] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile005), false, "floor");
//            tiles[6] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile006), false, "floor");
//            tiles[7] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile007), false, "floor");
//            tiles[8] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile008), false, "floor");
//            tiles[9] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile009), false, "floor");
//            tiles[10] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile010), false, "floor");
//            tiles[11] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile011), false, "floor");
//            tiles[12] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile012), false, "floor");
//            tiles[13] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile013), false, "floor");
//            tiles[14] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile014), false, "floor");
//            tiles[15] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile015), false, "floor");
//            tiles[16] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile016), false, "floor");
//            tiles[20] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile020), false, "floor");
//            tiles[21] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile021), false, "floor");
//            tiles[22] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile022), false, "floor");
//            tiles[23] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile023), false, "floor");
//            tiles[24] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile024), false, "floor");
//            tiles[25] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile025), false, "floor");
//            tiles[26] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile026), false, "floor");
//            tiles[27] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile027), false, "floor");
//            tiles[28] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile028), false, "floor");
//            tiles[29] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile029), false, "floor");
//            tiles[30] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile030), false, "floor");
//            tiles[31] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile031), false, "floor");
//            tiles[32] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.tile032), false, "floor");
//            tiles[33] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.pizza), false, "food");
//            tiles[34] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.drumstick), false, "food");

//            tiles[35] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.e_teleport), false, "teleporter");
//            tiles[36] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.f_coinmachine), false, "coinmachine");
//            tiles[37] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.g_bat), false, "bat");
//            tiles[38] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.h_turkeyleg), false, "food");
//            tiles[39] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.i_apple), false, "food");

            tiles[9] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.j_witch), false, "witch");

            //Kamil
            tiles[10] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.k_lava), false, "lava");
            tiles[11] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.l_spirit), false, "spirit");
            tiles[12] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.m_eye), false, "eye");

            //stairs
            tiles[13] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.n_stairs), false, "floor");
            tiles[15] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.o_door), false, "floor");
            tiles[20] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.p_spinsign), true, "wall");
            tiles[21] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.q_shopsign), true, "wall");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(int map) {

        try {
            InputStream is = null;
            switch (map) {
                case 0:
                    is = context.getResources().openRawResource(R.raw.testmap);
                    currentLoadedMap = 0;
                    //player.setPositionX(getCurrentMapSpawnX());
                    //player.setPositionY(getCurrentMapSpawnY());
                    break;
                case 1:
                    is = context.getResources().openRawResource(R.raw.lvl1_v2);
                    currentLoadedMap = 1;
                    //player.setPositionX(getCurrentMapSpawnX());
                    //player.setPositionY(getCurrentMapSpawnY());
                    break;
                case 2:
                    is = context.getResources().openRawResource(R.raw.testmap_k2);
                    currentLoadedMap = 2;
                   //player.setPositionX(getCurrentMapSpawnX());
                    //player.setPositionY(getCurrentMapSpawnY());
                    break;
                case 3:
                    is = context.getResources().openRawResource(R.raw.testmap_k3);
                    currentLoadedMap = 3;
                    //player.setPositionX(getCurrentMapSpawnX());
                    //player.setPositionY(getCurrentMapSpawnY());
                    break;
                case 4:
                    is = context.getResources().openRawResource(R.raw.testmap_k4);
                    currentLoadedMap = 4;
                    //player.setPositionX(getCurrentMapSpawnX());
                    //player.setPositionY(getCurrentMapSpawnY());
                    break;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gameDisplay.getMaxScreenColumns() && row < gameDisplay.getMaxScreenRows()) {

                String line = br.readLine();
                Log.d("Map", line);
                while (col < gameDisplay.getMaxScreenColumns()) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gameDisplay.getMaxScreenColumns()) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {

        }
    }

    public void draw(Canvas canvas) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gameDisplay.getMaxScreenColumns() && row < gameDisplay.getMaxScreenRows()) {

            int tileNum = mapTileNum[col][row];
            //Log.d("Map", String.valueOf(tileNum));
            int worldX = col * 176;
            int worldY = row * 176;

            if (worldX + 352 > player.getPositionX() - gameDisplay.getDisplayCenterX() &&
                    worldX - 352 < player.getPositionX() + gameDisplay.getDisplayCenterX() &&
                    worldY + 352 > player.getPositionY() - gameDisplay.getDisplayCenterY() &&
                    worldY - 352 < player.getPositionY() + gameDisplay.getDisplayCenterY()) {

                canvas.drawBitmap(Bitmap.createScaledBitmap(
                                tiles[tileNum].getBitmap(), 176, 176, false),
                        (float) gameDisplay.gameToDisplayCoordinatesX(x) + 20,
                        (float) gameDisplay.gameToDisplayCoordinatesY(y),
                        null);
            }


            col++;
            x += 176;
            if (col == gameDisplay.getMaxScreenColumns()) {
                col = 0;
                x = 0;
                row++;
                y += 176;
            }

        }
    }

    public int getCurrentLoadedMap() {
        return currentLoadedMap;
    }

    public int[][] getMapTileNum() {
        return mapTileNum;
    }

    public List<Enemy> getEnemyOnMap() {
        List<Enemy> output = new ArrayList<Enemy>();

        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum.length; j++) {
                if (mapTileNum[i][j] == 6) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "bat"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }

                if (mapTileNum[i][j] == 9) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "witch"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
                if (mapTileNum[i][j] == 10) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "lava"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
                if (mapTileNum[i][j] == 11) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "spirit"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
                if (mapTileNum[i][j] == 12) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "eye"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }

            }
        }
        return output;
    }

    public List<Teleporter> getTeleporterOnMap() {
        List<Teleporter> output = new ArrayList<Teleporter>();
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum[i].length; j++) {
                if (mapTileNum[i][j] == 4) {
                    output.add(new Teleporter(context, i * 176, j * 176));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
            }
        }
        return output;
    }


    public List<CoinMachine> getCoinMachineOnMap() {
        List<CoinMachine> output = new ArrayList<CoinMachine>();
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum[i].length; j++) {
                if (mapTileNum[i][j] == 5) {
                    output.add(new CoinMachine(context, i * 176, j * 176));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
            }
        }
        return output;
    }

    public List<Coin> getCoinOnMap() {
        List<Coin> output = new ArrayList<Coin>();
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum[i].length; j++) {
                if (mapTileNum[i][j] == 3) {
                    output.add(new Coin(context, i * 176, j * 176));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
            }
        }
        return output;
    }

    public List<Food> getFoodOnMap() {
        List<Food> output = new ArrayList<Food>();

        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum.length; j++) {
                switch (mapTileNum[i][j]) {
                    case 8:
                        //apple
                        output.add(new Food(context, i * 176, j * 176, "apple"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 7:
                        output.add(new Food(context, i * 176, j * 176, "turkeyleg"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                }
            }
        }
        return output;
    }

    public void cleanUp() {
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum.length; j++) {
                if (mapTileNum[i][j] == 3 ||
                        mapTileNum[i][j] == 4 ||
                        mapTileNum[i][j] == 5 ||
                        mapTileNum[i][j] == 6 ||
                        mapTileNum[i][j] == 7 ||
                        mapTileNum[i][j] == 8 ||
                        mapTileNum[i][j] == 9 ||
                        mapTileNum[i][j] == 10 ||
                        mapTileNum[i][j] == 11 ||
                        mapTileNum[i][j] == 12 ) {
                    mapTileNum[i][j] = 2;
                }
            }

        }
    }

    public double getCurrentMapSpawnX() {
        double output = 0;
        switch (currentLoadedMap){
            case 0:
                output =  4*176;
                break;
            case 1:
                output = 5 * 176;
                break;
            case 2:
                output = 25 * 176;
                break;
            case 3:
                output = 3 * 176;
                break;
            case 4:
                output = 9 * 176;
                break;
        }
        return output;
    }

    public double getCurrentMapSpawnY() {
        double output = 0;
        switch (currentLoadedMap){
            case 0:
                output =  4*176;
                break;
            case 1:
                output = 15 * 176;
                break;
            case 2:
                output = 48 * 176;
                break;
            case 3:
                output = 7 * 176;
                break;
            case 4:
                output = 2 * 176;
                break;
        }
        return output;
    }
}
