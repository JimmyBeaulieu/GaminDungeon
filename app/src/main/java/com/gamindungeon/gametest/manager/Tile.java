package com.gamindungeon.gametest.manager;

import android.graphics.Bitmap;

public class Tile {

    private Bitmap bitmap;
    private boolean collision;
    private String type;

    public Tile(Bitmap bitmap, boolean collision) {
        this.bitmap = bitmap;
        this.collision = collision;
        //this.type = type;

    }
    public boolean getCollision(){
        return collision;
    }
    public void setCollision(boolean isCollidiable){
        collision = isCollidiable;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public String getType(){
        return type;
    }
}
