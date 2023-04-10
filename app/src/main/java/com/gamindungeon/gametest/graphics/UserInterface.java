package com.gamindungeon.gametest.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.object.Player;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class UserInterface {
    private Score score;
    private Context context;
    private boolean inDialog = false;
    private Player player;
    private String dialogText="";
    private boolean isMultiline = false;
    List<String>multiLineText = new ArrayList<String>();

    private Bitmap optionGear;
    private boolean isMenuOpen = false;

    public UserInterface(Context context,Score score, Player player){
        this.player = player;
        this.context = context;
        this.score = score;
        optionGear = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.optiongear), 88, 88, false);
    }


    public void draw(Canvas canvas){
        drawGrid(canvas);
        String stringGold = Integer.toString(Score.gold);
        String stringExp = Integer.toString(Score.experience);
        Paint paint = new Paint();

        if(isMenuOpen){

            paint.setColor(Color.argb(150, 0,0,0));
            canvas.drawRect(50, 50, 2000, 1000, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.red));
            canvas.drawRect(1590, 600, 1750, 750, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.black));
            paint.setTextSize(75);
            canvas.drawText("Quit", 1600, 700, paint);



            paint.setColor(ContextCompat.getColor(context, R.color.white));
            paint.setTextSize(50);

            //player stats
            canvas.drawText("Player Stat:", 200, 300, paint);

            String text = "Health Point: " + player.getHealth() + " / " + player.getMaxHealth();
            canvas.drawText(text, 200, 400, paint);

            text = "Hunger: " + player.getHunger();
            canvas.drawText(text, 200, 500, paint);

            text = "Strength: " + player.getStrength();
            canvas.drawText(text, 200, 600, paint);

            text = "Experience: " + Score.experience + " / 100";
            canvas.drawText(text, 200, 700, paint);

            text = "Money: " + Score.gold + " $";
            canvas.drawText(text, 200, 800, paint);

            text = "Total Calorie Intake: " + Score.caloriesIntake + " kcal";
            canvas.drawText(text, 200, 900, paint);

            //Enemy defeated stats
            canvas.drawText("Enemy Defeated:", 1000, 300, paint);

            Bitmap bat = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.ba_bat), 100, 100, false);
            Bitmap witch = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.bb_witch), 100, 100, false);
            Bitmap spirit = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.bc_spirit), 100, 100, false);
            Bitmap eye = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.bd_eye), 100, 100, false);


            canvas.drawBitmap(bat, 1200, 325, null);
            text = " X " + String.valueOf(Score.batDefeated);
            canvas.drawText(text, 1300, 400, paint);

            canvas.drawBitmap(witch, 1200, 425, null);
            text = " X " + String.valueOf(Score.witchDefeated);
            canvas.drawText(text, 1300, 500, paint);

            canvas.drawBitmap(spirit, 1200, 525, null);
            text = " X " + String.valueOf(Score.spiritDefeated);
            canvas.drawText(text, 1300, 600, paint);

            canvas.drawBitmap(eye, 1200, 625, null);
            text = " X " + String.valueOf(Score.eyeDefeated);
            canvas.drawText(text, 1300, 700, paint);

        }
        else {

            paint.setColor(ContextCompat.getColor(context, R.color.magenta));
            paint.setTextSize(50);
            canvas.drawText(stringGold + "$", 100, 300, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.magenta));
            paint.setTextSize(50);
            canvas.drawText(stringExp + " exp", 100, 375, paint);

            if (inDialog) {
                drawDialog(canvas);
            }

        }
        canvas.drawBitmap(optionGear, 1900, 100, null);

    }

    public void createDialog(String text){

        inDialog = true;
        dialogText = "";
        if(multiLineText.size() > 0){
            multiLineText = new ArrayList<>();
        }

        if(text.length() > 54){
            isMultiline = true;
            int index = 0;
            while (index < text.length()) {
                multiLineText.add(text.substring(index, Math.min(index + 54,text.length())));
                index += 54;
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
                paint.setColor(ContextCompat.getColor(context, R.color.white));
                canvas.drawText(sentence, 325, position, paint);
                position += 50;
            }
        }
        else {
            paint.setColor(ContextCompat.getColor(context, R.color.white));
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

    public void displayMenu() {
        isMenuOpen = !isMenuOpen;
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }
}
