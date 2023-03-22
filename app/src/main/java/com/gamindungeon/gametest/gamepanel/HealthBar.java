package com.gamindungeon.gametest.gamepanel;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gamindungeon.gametest.engine.GameDisplay;
import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.object.GameObject;

public class HealthBar {

    private GameObject user;
    private int width;
    private int height;
    private int margin;
    private Paint borderPaint;
    private Paint healthPaint;

    public HealthBar(GameObject user){
        this.user = user;
        this.width = 100;
        this.height = 20;
        this.margin = 2;

        this.borderPaint = new Paint();
        borderPaint.setColor(ContextCompat.getColor(user.getContext(), R.color.yellow));
        healthPaint = new Paint();
        healthPaint.setColor(ContextCompat.getColor(user.getContext(), R.color.green));
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay){

        float x = (float) user.getPositionX();
        float y = (float) user.getPositionY();

        float distanceFromUser = 30;
        float healthPointPercentage = (float) (user.getHealth() / user.getMaxHealth());
        float borderLeft, borderTop, borderRight, borderBottom, healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;

        //draw border
        borderLeft = (x - 50)+88;
        borderRight = (x + 50)+88;
        borderBottom = y - distanceFromUser;
        borderTop = borderBottom - height;

        canvas.drawRect(
                (float)gameDisplay.gameToDisplayCoordinatesX(borderLeft),
                (float)gameDisplay.gameToDisplayCoordinatesY(borderTop),
                (float)gameDisplay.gameToDisplayCoordinatesX(borderRight),
                (float)gameDisplay.gameToDisplayCoordinatesY(borderBottom),
                borderPaint
        );

        //draw health
        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth * healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;

        canvas.drawRect(
                (float)gameDisplay.gameToDisplayCoordinatesX(healthLeft),
                (float)gameDisplay.gameToDisplayCoordinatesY(healthTop),
                (float)gameDisplay.gameToDisplayCoordinatesX(healthRight),
                (float)gameDisplay.gameToDisplayCoordinatesY(healthBottom),
                healthPaint
        );

    }
}
