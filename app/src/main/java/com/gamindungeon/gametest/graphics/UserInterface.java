package com.gamindungeon.gametest.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Score;

public class UserInterface {
    private Score score;
    private Context context;
    private boolean inDialog = false;
    private String dialogText;
    public UserInterface(Context context,Score score){
        this.context = context;
        this.score = score;
    }


    public void draw(Canvas canvas){
            drawGrid(canvas);

            String stringGold = Integer.toString(score.getGold());
            String stringExp = Integer.toString(score.getExperience());
            Paint paint = new Paint();

            paint.setColor(ContextCompat.getColor(context, R.color.magenta));
            paint.setTextSize(50);
            canvas.drawText("Gold: " + stringGold, 100, 200, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.magenta));
            paint.setTextSize(50);
            canvas.drawText("Experience: " + stringExp, 100, 300, paint);

            if(inDialog){
                drawDialog(canvas);
            }


    }

    public void createDialog(String text){
        inDialog = true;
        dialogText = text;
    }
    private void drawDialog(Canvas canvas){
        Paint paint = new Paint();

        paint.setColor(ContextCompat.getColor(context, R.color.dialogBox));
        paint.setTextSize(50);

        canvas.drawRect(250,650,1850,1050, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawRect(300,700,1800,1000, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.dialogBox));
        canvas.drawText(this.dialogText, 325, 800, paint);
    }

    private void drawGrid(Canvas canvas) {
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();

        int gridSize = 176;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);


        //offset to middle
        for (int i = 88; i < screenWidth; i += gridSize) {
            //draws vertical lines X
            canvas.drawLine(i, 0, i, screenHeight, paint);
        }
        for (int i = 100; i < screenHeight; i += gridSize) {
            //draw horizontal lines Y
            canvas.drawLine(0, i, screenWidth, i, paint);
        }


    }
    public boolean isInDialog(){
        return inDialog;
    }
    public void setInDialog(boolean inDialog){
        this.inDialog = inDialog;
    }
}
