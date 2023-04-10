package com.gamindungeon.gametest.manager;

import android.graphics.Bitmap;

public class Tile {

    private Bitmap bitmap;
    private boolean collision;


    public Tile(Bitmap bitmap, boolean collision) {
        this.bitmap = bitmap;
        this.collision = collision;
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

}
