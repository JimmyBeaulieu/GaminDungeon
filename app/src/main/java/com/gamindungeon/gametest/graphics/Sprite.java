package com.gamindungeon.gametest.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.gamindungeon.gametest.R;

public class Sprite {

    private final Rect rect;
    private final SpriteSheet spriteSheet;

    public Sprite(SpriteSheet spriteSheet, Rect rect) {
        this.spriteSheet = spriteSheet;
        this.rect = rect;
    }

    public void draw(Canvas canvas, int x, int y) {

        Bitmap scaledSprite = Bitmap.createScaledBitmap(spriteSheet.getBitmap(), 176, 176, false);

        canvas.drawBitmap(
                scaledSprite,
                rect,
                new Rect(x, y, x+getWidth(), y+getHeight()),
                null
        );
    }

    public int getHeight() {
        return rect.height();
    }
    public int getWidth() {
        return rect.width();
    }
}
