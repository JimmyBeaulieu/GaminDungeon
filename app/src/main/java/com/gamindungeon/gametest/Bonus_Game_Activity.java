package com.gamindungeon.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Bonus_Game_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnSpin, btnReturn;

    ImageView imgWheel;

    TextView tvCurrentCoin;

    int spinAngle = 1080;

    String listPrizes = "";

    int numCoins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        numCoins = getIntent().getIntExtra("coin", 0);
        tvCurrentCoin.setText(numCoins + "");

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnSpin:
                if( numCoins > 0){
                    spinWheel();
                    numCoins =  numCoins - 1;
                    tvCurrentCoin.setText(numCoins + "");
                }
                else {
                    Toast.makeText(this,"NO COINS!!! SORRY NO SPIN!",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnReturn:
                finishApp();
                break;

        }

    }

    private void finishApp() {

        Intent intent = new Intent();
        intent.putExtra("prize", listPrizes );
        intent.putExtra("coin", numCoins);
        setResult( RESULT_OK, intent);
        finish();
    }

    private void spinWheel() {
        int angle = getRandomAngle();
        Toast.makeText(this, "Angle is " + angle, Toast.LENGTH_LONG).show();

        imgWheel.animate().rotation(spinAngle + angle).setDuration(5000).start();
        spinAngle = spinAngle + 1080;

        String prize = findPrize(angle);

        Toast.makeText(this, "Prize is " + prize, Toast.LENGTH_LONG).show();

        listPrizes = listPrizes + prize + "\n";
    }

    public int getRandomAngle(){
        int angle = 0;

        angle = (int) (Math.random() * 360);

        return angle;
    }

    public String findPrize(int angle){
        String prize = "";

        if(angle >= 0 && angle < 53){
            prize = "Prize 1";
        }else if (angle >= 53 && angle < 105){
            prize = "Prize 2";
        }
        else if (angle >= 105 && angle < 157){
            prize = "Prize 7";
        }
        else if (angle >= 157 && angle < 209){
            prize = "Prize 6";
        }
        else if (angle >= 209 && angle < 261){
            prize = "Prize 5";
        }
        else if (angle >= 261 && angle < 312){
            prize = "Prize 4";
        }
        else if (angle >= 312 && angle < 365){
            prize = "Prize 3";
        }


        return prize;
    }
}