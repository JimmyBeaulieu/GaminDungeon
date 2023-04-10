package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.gamindungeon.gametest.R;

public class Tutorial_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnReturnToOption;

    ImageView  imgTutorial, imgBackward, imgForward;

    int imgId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial);
        initialize();
    }


    @SuppressLint("ResourceType")
    private void initialize() {
        btnReturnToOption = findViewById(R.id.btnReturnToOption);
        btnReturnToOption.setOnClickListener(this);

        imgTutorial = findViewById(R.id.imgTutorial);
        imgBackward = findViewById(R.id.imgBackward);
        imgForward = findViewById(R.id.imgForward);


        imgForward.setOnClickListener(this);
        imgBackward.setOnClickListener(this);

        imgTutorial.setImageResource(R.raw.tutorial_move);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnReturnToOption:
                finish();
                break;
            case R.id.imgForward:
                changeImageTutorial(1);
                break;
            case R.id.imgBackward:
                changeImageTutorial(-1);
                break;
        }

    }

    @SuppressLint("ResourceType")
    private void changeImageTutorial(int i) {
        imgId = imgId + i;

        if(imgId <= 0){
            imgId = 1;

        }
        if(imgId >= 6){
            imgId = 6;


        }

        switch (imgId){
            case 1:
                imgTutorial.setImageResource(R.raw.tutorial_move);
                break;
            case 2:
                imgTutorial.setImageResource(R.raw.tutorial_teleport);
                break;
            case 3:
                imgTutorial.setImageResource(R.raw.tutorial_combat);
                break;
            case 4:
                imgTutorial.setImageResource(R.raw.tutorial_enemy);
                break;
            case 5:
                imgTutorial.setImageResource(R.raw.tutorial_collectables);
                break;
            case 6:
                imgTutorial.setImageResource(R.raw.tutorial_bonus);
                break;
        }

    }


}