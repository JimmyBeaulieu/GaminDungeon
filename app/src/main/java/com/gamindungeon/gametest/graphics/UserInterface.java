package com.gamindungeon.gametest.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.object.Player;

import java.util.ArrayList;
import java.util.List;

public class UserInterface {
    private Score score;
    private Context context;
    private boolean inDialog = false;
    private Player player;
    private String dialogText="";
    private boolean isMultiline = false;
    List<String>multiLineText = new ArrayList<String>();
    public UserInterface(Context context,Score score, Player player){
        this.player = player;
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
        canvas.drawText(stringGold + "$", 100, 300, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.magenta));
        paint.setTextSize(50);
        canvas.drawText(stringExp + " exp", 100, 375, paint);

        if(inDialog){
            drawDialog(canvas);
        }


    }

    public void createDialog(String text){

        inDialog = true;
        dialogText = "";
        if(multiLineText.size() > 0){
            multiLineText = new ArrayList<>();
        }

        if(text.length() > 50){
            isMultiline = true;
            int index = 0;
            while (index < text.length()) {
                multiLineText.add(text.substring(index, Math.min(index + 50,text.length())));
                index += 50;
            }
        }
        else {
            isMultiline = false;
            dialogText = text;
        }
    }
    private void drawDialog(Canvas canvas){
        Paint paint = new Paint();

        paint.setColor(ContextCompat.getColor(context, R.color.dialogBox));
        paint.setTextSize(50);

        canvas.drawRect(250,650,1850,1050, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawRect(300,700,1800,1000, paint);

        if(isMultiline){
            int position = 750;
            for(String sentence : multiLineText){
                paint.setColor(ContextCompat.getColor(context, R.color.dialogBox));
                canvas.drawText(sentence, 325, position, paint);
                position += 50;
                Log.d("MAP", String.valueOf(position));
            }
        }
        else {
            paint.setColor(ContextCompat.getColor(context, R.color.dialogBox));
            //canvas.drawText(this.dialogText, 325, 800, paint);
            canvas.drawText(this.dialogText, 325, 800, paint);
        }
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
