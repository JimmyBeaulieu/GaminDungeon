package com.gamindungeon.gametest.manager;

import android.graphics.Bitmap;

public class Tile {

    public Bitmap bitmap;
    public Boolean collision;

    public Tile(Bitmap bitmap, boolean collision){
        this.bitmap = bitmap;
        this.collision = collision;
    }
}
