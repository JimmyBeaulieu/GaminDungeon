package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;

import java.io.Serializable;

public class Score implements Serializable {
    public static int gold;
    public static int experience;
    public static int music;
    Context context;

    public Score(Context context){
        gold = 0;
        experience = 0;
        music = 0;
        this.context = context;
    }

}
