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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager{

    Tile[] tiles;
    Context context;
    int mapTileNum[][];
    GameDisplay gameDisplay;
    public TileManager(Context context, GameDisplay gameDisplay){
        this.context = context;
        tiles = new Tile[3];
        this.gameDisplay = gameDisplay;
        mapTileNum = new int[gameDisplay.getMaxScreenColumns()][gameDisplay.getMaxScreenRows()];

        getTileImage();
        loadMap();
    }

    public void getTileImage(){

        try{
            tiles[0] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.drawable.rock), true);
            tiles[1] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.drawable.water), true);
            tiles[2] = new Tile(BitmapFactory.decodeResource(context.getResources(), R.drawable.wood), false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(){
        try{
            InputStream is = context.getResources().openRawResource(R.raw.testmap);
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

            canvas.drawBitmap(Bitmap.createScaledBitmap(
                            tiles[tileNum].getBitmap(), 176, 176, false),
                    (float) gameDisplay.gameToDisplayCoordinatesX(x) + 20,
                    (float) gameDisplay.gameToDisplayCoordinatesY(y),
                    null);
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

}
