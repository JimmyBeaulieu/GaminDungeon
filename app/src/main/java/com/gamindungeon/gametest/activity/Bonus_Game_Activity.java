package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.object.Player;

public class Bonus_Game_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnSpin, btnReturn;

    ImageView imgWheel;

    TextView tvCurrentCoin;
    TextView result;
    String resultString = "";
    Music music;

    int spinAngle = 1080;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bonus_game);
        initialize();
    }


    private void initialize() {

        btnSpin = findViewById(R.id.btnSpin);
        btnSpin.setOnClickListener(this);

        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);

        tvCurrentCoin = findViewById(R.id.tvCurrentCoin);;

        imgWheel = findViewById(R.id.imgWheel);
        result = findViewById(R.id.result);
        result.setVisibility(View.INVISIBLE);

        music = new Music(this);
        music.stop();
        music.play(5);

        String coinShow = String.valueOf(Score.gold);
        tvCurrentCoin.setText(coinShow);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        result.setVisibility(View.VISIBLE);

        switch (id){
            case R.id.btnSpin:
                if( Score.gold > 4){
                    Score.gold =  Score.gold - 5;
                    String message = Score.gold + "";
                    tvCurrentCoin.setText(message);
                    spinWheel();

                }
                else {
                    music.play(Score.music);
                    finish();
                }
                break;
            case R.id.btnReturn:
                music.play(Score.music);
                finish();
                break;

        }

    }

    private void spinWheel() {

        int angle = getRandomAngle();

        imgWheel.animate().rotation(spinAngle + angle).setDuration(5000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
                //music.setVolume(1);
                music.play(4);
                btnSpin.setEnabled(false);
                btnReturn.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation finished
                // Display result and enable button here
                btnSpin.setEnabled(true);
                btnReturn.setEnabled(true);
                displayResult(angle);

                result.setText(resultString);
                String message = Score.gold + "";
                tvCurrentCoin.setText(message);
                music.stop();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Animation cancelled
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Animation repeated
            }
        }).start();
    }

    private void displayResult(int angle) {


        spinAngle = spinAngle + 1080;
        int result = findPrize(angle);
        switch(result){
            case 0:
            case 6:
                //nothing happens
                resultString = "Sorry no prize!!";
                break;
            case 1:
                Player.hunger = 100;
                resultString = "Your stomach was filled up!!";
                break;
            case 2:
                Player.health -= 10;
                resultString = "You lost 10 health!";
                if(Player.health <=0){
                    finish();
                }
                break;
            case 3:
                Score.gold *= 2;
                resultString = "Doubled your gold!!";
                break;
            case 4:
                Score.gold *= 0.5;
                resultString = "Halved your gold!!";
                break;
            case 5:
                Score.gold += 10;
                resultString = "You win 10 gold!";
                break;

        }
    }

    public int getRandomAngle(){
        int angle = 0;

        angle = (int) (Math.random() * 360);

        return angle;
    }

    public int findPrize(int angle){

        int prize = 0;

        //angle 0 to 53 are omitted since they would put prize at 0 anyways
        if (angle >= 53 && angle < 105){
            prize = 1;
        }
        else if (angle >= 105 && angle < 157){
            prize = 6;
        }
        else if (angle >= 157 && angle < 209){
            prize = 5;
        }
        else if (angle >= 209 && angle < 261){
            prize = 4;
        }
        else if (angle >= 261 && angle < 312){
            prize = 3;
        }
        else if (angle >= 312 && angle < 365){
            prize = 2;
        }

        Toast.makeText(this,"" + angle, Toast.LENGTH_LONG).show();

        return prize;
    }
}