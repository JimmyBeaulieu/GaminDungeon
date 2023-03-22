package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;

public class Score {
    private int gold;
    private int experience;
    Context context;

    public Score(Context context){
        gold = 0;
        experience = 0;
        this.context = context;
    }

    public void draw(Canvas canvas){

        String stringGold = Integer.toString(gold);
        String stringExp = Integer.toString(experience);
        Paint paint = new Paint();

        paint.setColor(ContextCompat.getColor(context, R.color.magenta));
        paint.setTextSize(50);
        canvas.drawText("Gold: " + stringGold, 100, 200, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.magenta));
        paint.setTextSize(50);
        canvas.drawText("Experience: " +stringExp, 100, 300, paint);
    }
    public void setGold(int gold){
        this.gold = gold;
    }
    public int getGold(){
        return gold;
    }
    public void setExperience(int experience){
        this.experience = experience;
    }
    public int getExperience(){
        return experience;
    }

}
