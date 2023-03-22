package com.gamindungeon.gametest.engine;

import com.gamindungeon.gametest.object.GameObject;

public class GameDisplay {
    private double gameToDisplayCoordinateOffsetX;
    private double gameToDisplayCoordinateOffsetY;

    public double getDisplayCenterX() {
        return displayCenterX;
    }

    public double getDisplayCenterY() {
        return displayCenterY;
    }

    private double displayCenterX;
    private double displayCenterY;
    private double gameCenterX;
    private double gameCenterY;
    private GameObject centerObject;
    private final int maxScreenColumns = 50;
    private final int maxScreenRows = 50;
    private final int worldWidth = 176 * maxScreenColumns;
    private final int worldHeight = 176 * maxScreenRows;

    public GameDisplay(int widthPixels, int heightPixels, GameObject centerObject){
        this.centerObject = centerObject;

        displayCenterX = widthPixels/2.0;
        displayCenterY = heightPixels/2.0;
    }

    public void update(){
        gameCenterX = centerObject.getPositionX();
        gameCenterY = centerObject.getPositionY();

        gameToDisplayCoordinateOffsetX = displayCenterX - gameCenterX;
        gameToDisplayCoordinateOffsetY = displayCenterY - gameCenterY;

    }
    public double gameToDisplayCoordinatesX(double x) {
        return x + gameToDisplayCoordinateOffsetX;
    }

    public double gameToDisplayCoordinatesY(double y) {
        return y + gameToDisplayCoordinateOffsetY;
    }
    public int getMaxScreenColumns(){
        return maxScreenColumns;
    }
    public int getMaxScreenRows(){
        return  maxScreenRows;
    }
    public int getWorldWidth(){
        return worldWidth;
    }
    public int getWorldHeight(){
        return worldHeight;
    }
}
