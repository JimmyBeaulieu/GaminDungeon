package com.gamindungeon.gametest.manager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.R;

import java.io.Serializable;
import java.util.Hashtable;

public class Score{
    public static int gold;
    public static int experience;
    public static int music;
    public static double caloriesIntake;
    public static int batDefeated;
    public static int witchDefeated;
    public static int spiritDefeated;
    public static int eyeDefeated;
    public static String saveContent;
    Context context;

    public Score(Context context){
        gold = 0;
        experience = 0;
        music = 0;
        caloriesIntake = 0;
        batDefeated = 0;
        witchDefeated = 0;
        spiritDefeated = 0;
        eyeDefeated = 0;
        this.context = context;
    }

}
