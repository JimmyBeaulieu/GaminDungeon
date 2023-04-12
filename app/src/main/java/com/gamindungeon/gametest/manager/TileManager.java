package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.object.collectable.Coin;
//import com.gamindungeon.gametest.object.CoinMachine;
import com.gamindungeon.gametest.object.Enemy;
import com.gamindungeon.gametest.object.Player;
import com.gamindungeon.gametest.object.collectable.Food;
import com.gamindungeon.gametest.object.collectable.foodType;

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
    Music music;

    public TileManager(Context context, GameDisplay gameDisplay, Player player, Music mp) {
        this.context = context;
        tiles = new Tile[40];
        this.gameDisplay = gameDisplay;
        mapTileNum = new int[gameDisplay.getMaxScreenColumns()][gameDisplay.getMaxScreenRows()];
        this.player = player;
        getTileImage();
        loadMap(0);
        currentLoadedMap = 0;
        music = mp;
    }

    public Tile getTiles(int index) {
        return tiles[index];
    }

    public void getTileImage() {

        try {

            //structure
            tiles[0] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.aa_rock), true);
            tiles[1] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ab_water), true);
            tiles[2] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ac_floor), false);
            tiles[3] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ad_teleport), false);
            tiles[4] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ae_lava), true);
            tiles[5] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.af_stairs), false);
            tiles[6] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ag_door), false);
            tiles[7] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ah_spinsign), true);
            tiles[8] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ai_shopsign), true);

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


            //addition I forgot, I'm so sorry I didn't want to redo everything again, I'm so, so sorry, please forgive me :((((
            tiles[20] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.da_downstairs), false);
            tiles[21] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.db_help), false);

            tiles[22] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ea_floor), false);
            tiles[23] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.eb_floor), false);
            tiles[24] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ec_floor), false);
            tiles[25] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ed_rock), true);
            tiles[26] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ee_floor), false);
            tiles[27] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.ef_floor), false);
            tiles[28] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.eg_floor), false);
            tiles[29] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.eh_floor), false);




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
                    music.play(0);
                    currentLoadedMap = 0;
                    break;
                case 1:
                    is = context.getResources().openRawResource(R.raw.map1);
                    music.play(1);
                    currentLoadedMap = 1;
                    break;
                case 2:
                    is = context.getResources().openRawResource(R.raw.map2);
                    music.play(2);
                    currentLoadedMap = 2;
                    break;
                case 3:
                    is = context.getResources().openRawResource(R.raw.map3);
                    music.play(3);
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
                if (mapTileNum[i][j] == 4) {
                    output.add(new Enemy(context, i * 176, j * 176, player, this, "lava"));
                    tiles[mapTileNum[i][j]] = tiles[2];
                }
            }
        }
        return output;
    }
    public List<Coin> getCoinOnMap() {
        List<Coin> output = new ArrayList<Coin>();

        if(getCurrentMapSpawnY() == player.getPositionY() && getCurrentMapSpawnX() == player.getPositionX()) {
            for (int i = 0; i < mapTileNum.length; i++) {
                for (int j = 0; j < mapTileNum[i].length; j++) {
                    if (mapTileNum[i][j] == 19) {
                        output.add(new Coin(context, i * 176, j * 176));
                        tiles[mapTileNum[i][j]] = tiles[2];
                    }
                }
            }
        }
        return output;
    }

    public List<Food> getFoodOnMap() {
        List<Food> output = new ArrayList<Food>();
        if(getCurrentMapSpawnY() == player.getPositionY() && getCurrentMapSpawnX() == player.getPositionX()) {
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum.length; j++) {
                switch (mapTileNum[i][j]) {
                    case 13:
                        output.add(new Food(context, i * 176, j * 176, foodType.BURGER));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 14:
                        output.add(new Food(context, i * 176, j * 176, foodType.CAKE));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 15:
                        output.add(new Food(context, i * 176, j * 176, foodType.CONE));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 16:
                        output.add(new Food(context, i * 176, j * 176, foodType.DONUT));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 17:
                        output.add(new Food(context, i * 176, j * 176, foodType.DRUMSTICK));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;
                    case 18:
                        output.add(new Food(context, i * 176, j * 176, foodType.POTION));
                        tiles[mapTileNum[i][j]] = tiles[2];
                        break;

                }

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
                        mapTileNum[i][j] == 12 ||
                        mapTileNum[i][j] == 13 ||
                        mapTileNum[i][j] == 14 ||
                        mapTileNum[i][j] == 15 ||
                        mapTileNum[i][j] == 16 ||
                        mapTileNum[i][j] == 17 ||
                        mapTileNum[i][j] == 18 ||
                        mapTileNum[i][j] == 19) {
                    mapTileNum[i][j] = 2;
                }
                if(mapTileNum[i][j] == 11){
                    mapTileNum[i][j] = 23;
                }
            }

        }
    }

    public double getCurrentMapSpawnX() {
        double output = 0;
        switch (currentLoadedMap){
            case 0:
                output = 3 * 176;
                break;
            case 1:
                output = 3 * 176;
                break;
            case 2:
                output = 9 * 176;
                break;
            case 3:
                output = 25 * 176;
                break;

        }
        return output;
    }

    public double getCurrentMapSpawnY() {
        double output = 0;
        switch (currentLoadedMap){
            case 0:
                output = 4 * 176;
                break;
            case 1:
                output = 7 * 176;
                break;
            case 2:
                output = 3 * 176;
                break;
            case 3:
                output = 47 * 176;
                break;

        }
        return output;
    }
}
