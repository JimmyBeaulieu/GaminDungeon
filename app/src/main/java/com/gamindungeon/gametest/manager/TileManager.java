package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.object.collectable.Coin;
//import com.gamindungeon.gametest.object.CoinMachine;
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
        tiles = new Tile[21];
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

            //structure
            tiles[0] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.aa_rock), true);
            tiles[1] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ab_water), true);
            tiles[2] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ac_wood), false);
            tiles[3] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ad_teleport), false);
            tiles[4] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ae_lava), false);
            tiles[5] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.af_stairs), false);
            tiles[6] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ag_door), false);
            tiles[7] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ah_spinsign), false);
            tiles[8] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ai_shopsign), false);

            //characters
            tiles[9] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ba_bat), false);
            tiles[10] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.bb_witch), false);
            tiles[11] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.bc_spirit), false);
            tiles[12] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.bd_eye), false);

            //collectables
            tiles[13] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ca_burger), false);
            tiles[14] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.cb_cake), false);
            tiles[15] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.cc_cone), false);
            tiles[16] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.cd_donut), false);
            tiles[17] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ce_drumstick), false);
            tiles[18] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.cf_potion), false);
            tiles[19] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.cg_coin), false);


            //addition I forgot, sorry I don't want to redo everything again
            tiles[20] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.da_downstairs), false);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(int map) {

        try {
            InputStream is = null;
            switch (map) {
                case 0:
                    is = context.getResources().openRawResource(R.raw.map0);
                    currentLoadedMap = 0;
                    break;
                case 1:
                    is = context.getResources().openRawResource(R.raw.map1);
                    currentLoadedMap = 1;
                    break;
                case 2:
                    is = context.getResources().openRawResource(R.raw.map2);
                    currentLoadedMap = 2;
                    break;
                case 3:
                    is = context.getResources().openRawResource(R.raw.map3);
                    currentLoadedMap = 3;
                    break;
                case 4:
                    //is = context.getResources().openRawResource(R.raw.map4);
                    //currentLoadedMap = 4;
                    break;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gameDisplay.getMaxScreenColumns() && row < gameDisplay.getMaxScreenRows()) {

                String line = br.readLine();

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
                if (mapTileNum[i][j] == 9) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "bat"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
                if (mapTileNum[i][j] == 10) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "witch"));
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
    public List<Coin> getCoinOnMap() {
        List<Coin> output = new ArrayList<Coin>();
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum[i].length; j++) {
                if (mapTileNum[i][j] == 19) {
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
                    case 13:
                        //apple
                        output.add(new Food(context, i * 176, j * 176, "burger"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 14:
                        output.add(new Food(context, i * 176, j * 176, "cake"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                    case 15:
                        output.add(new Food(context, i * 176, j * 176, "cone"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                    case 16:
                        output.add(new Food(context, i * 176, j * 176, "donut"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                    case 17:
                        output.add(new Food(context, i * 176, j * 176, "drumstick"));
                        tiles[mapTileNum[i][j]] = tiles[2];
                    case 18:
                        output.add(new Food(context, i * 176, j * 176, "potion"));
                        tiles[mapTileNum[i][j]] = tiles[2];

                }
            }
        }
        return output;
    }

    public void cleanUp() {
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum.length; j++) {
                if (mapTileNum[i][j] == 9 ||
                        mapTileNum[i][j] == 10 ||
                        mapTileNum[i][j] == 11 ||
                        mapTileNum[i][j] == 12 ||
                        mapTileNum[i][j] == 13 ||
                        mapTileNum[i][j] == 14 ||
                        mapTileNum[i][j] == 15 ||
                        mapTileNum[i][j] == 16) {
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
                output = 25 * 176;
                break;
            case 2:
                output = 9 * 176;
                break;
            case 3:
                output = 24 * 176;
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
                output = 32 * 176;
                break;
            case 2:
                output = 2 * 176;
                break;
            case 3:
                output = 46 * 176;
                break;
            case 4:
                output = 2 * 176;
                break;
        }
        return output;
    }
}
