package com.gamindungeon.gametest.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.graphics.UserInterface;

public class GameOver {
    private Context context;
    private boolean isGameOver = false;
    private UserInterface ui;

    public GameOver(Context context, UserInterface ui){
        this.context = context;
        this.ui = ui;
    }
    public void draw(Canvas canvas) {

        if(isGameOver){

        }
        String died = "You Died!";
        String underDied = "Press the screen twice";
        float x = 800;
        float y = 400;
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.GameOver);
        float textSize = 150;

        canvas.drawRect(500,200,1800,1000, paint);

        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.drawText(died, x, y, paint);
        paint.setTextSize(100);
        canvas.drawText(underDied, 600, y+300, paint);
        canvas.drawText( "to return to the main menu", 600, y+400, paint);
    }

    public boolean getGameOverState(){
        return isGameOver;
    }
    public void setGameOverState(boolean state){
        isGameOver = state;
    }
}
