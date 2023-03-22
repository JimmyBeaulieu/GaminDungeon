package com.gamindungeon.gametest.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;

public class GameOver {
    private Context context;

    public GameOver(Context context){
        this.context = context;
    }
    public void draw(Canvas canvas) {

        String text = "You Died!";
        float x = 800;
        float y = 200;
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.GameOver);
        float textSize = 150;
        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);
    }
}
