package com.gamindungeon.gametest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gamindungeon.gametest.object.GameObject;

public class TileManager extends GameObject {

    Tile[] tiles;
    Context context;
    public TileManager(Context context){
        super(context, 20, 0);
        this.context = context;
        tiles = new Tile[3];

        getTileImage();
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

    public void draw(Canvas canvas, GameDisplay gameDisplay){

        canvas.drawBitmap(Bitmap.createScaledBitmap(
                tiles[0].bitmap, 176, 176, false),
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);

        canvas.drawBitmap(Bitmap.createScaledBitmap(
                        tiles[1].bitmap, 176, 176, false),
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY + 176),
                null);

        canvas.drawBitmap(Bitmap.createScaledBitmap(
                        tiles[2].bitmap, 176, 176, false),
                (float)gameDisplay.gameToDisplayCoordinatesX(positionX + 176),
                (float)gameDisplay.gameToDisplayCoordinatesY(positionY),
                null);
    }

    @Override
    public void setPosition(String direction) {

    }

    @Override
    public void update() {

    }

    @Override
    public void afterClash() {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public double getPositionX() {
        return 0;
    }

    @Override
    public double getPositionY() {
        return 0;
    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public double getHealth() {
        return 0;
    }
}
