package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.object.GameObject;
import com.gamindungeon.gametest.object.Player;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager{

    Tile[] tiles;
    Context context;
    public int mapTileNum[][];
    GameDisplay gameDisplay;
    Player player;
    int currentLoadedMap;
    public TileManager(Context context, GameDisplay gameDisplay, Player player){
        this.context = context;
        tiles = new Tile[10];
        this.gameDisplay = gameDisplay;
        mapTileNum = new int[gameDisplay.getMaxScreenColumns()][gameDisplay.getMaxScreenRows()];
        this.player = player;
        getTileImage();
        loadMap(0);
        currentLoadedMap = 0;
    }
    public Tile getTiles(int index){
        return tiles[index];
    }

    public void getTileImage(){

        try{
            tiles[0] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.a_rock), true, "wall");
            tiles[1] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.b_water), true, "wall");
            tiles[2] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.c_wood), false, "floor");
            tiles[3] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.d_coin), false, "coin");
            tiles[4] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.raw.e_teleport), false, "teleport");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(int map){

        try{
            InputStream is = null;
            switch(map){
                case 0:
                    is = context.getResources().openRawResource(R.raw.testmap);
                    currentLoadedMap = 0;
                break;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gameDisplay.getMaxScreenColumns() && row < gameDisplay.getMaxScreenRows()){

                String line = br.readLine();
                System.out.println(line);
                while(col < gameDisplay.getMaxScreenColumns()){
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gameDisplay.getMaxScreenColumns()){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch(Exception e){

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

            if( worldX + 352 > player.getPositionX() - gameDisplay.getDisplayCenterX() &&
                worldX - 352 < player.getPositionX() + gameDisplay.getDisplayCenterX()&&
                worldY + 352 > player.getPositionY() - gameDisplay.getDisplayCenterY()&&
                worldY - 352 < player.getPositionY() + gameDisplay.getDisplayCenterY()){

                canvas.drawBitmap(Bitmap.createScaledBitmap(
                        tiles[tileNum].getBitmap(), 176, 176, false),
                    (float) gameDisplay.gameToDisplayCoordinatesX(x) + 20,
                        (float) gameDisplay.gameToDisplayCoordinatesY(y),
                        null);
            }


            col++;
            x+= 176;
            if(col == gameDisplay.getMaxScreenColumns()){
                col = 0;
                x = 0;
                row++;
                y += 176;
            }

        }
    }
    public int getCurrentLoadedMap(){
        return  currentLoadedMap;
    }


}
