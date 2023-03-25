package com.gamindungeon.gametest.engine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.gamindungeon.gametest.Bonus_Game_Activity;
import com.gamindungeon.gametest.Game_Activity;
import com.gamindungeon.gametest.R;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    final int REQUEST_CODE = 1;
    Button btnPlay, btnBonus, btnOptions;

    String bonusStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        

    }

    private void initialize() {
        btnPlay = findViewById(R.id.btnPlay);
        btnBonus = findViewById(R.id.btnBonus);
        btnOptions = findViewById(R.id.btnOptions);

        btnPlay.setOnClickListener(this);
        btnBonus.setOnClickListener(this);
        btnOptions.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id  = view.getId();


        switch (id){
            case R.id.btnPlay:
                goToGamePlay();
                break;
            case R.id.btnBonus:
                goToBonusGame();
                break;
            case R.id.btnOptions:
                goToOptions();
                break;
        }


    }

    private void goToOptions() {
    }

    private void goToBonusGame() {

        try {
            //TODO GetNumOfCoinsFromSaveFile !!!
            int numCoins = 3;

            Intent intent = new Intent(this, Bonus_Game_Activity.class);

            intent.putExtra("coin", numCoins);

            startActivityForResult(intent, REQUEST_CODE);

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void goToGamePlay() {
        Intent i =  new Intent(this, Game_Activity.class);

        i.putExtra("bonus", bonusStr);
        this.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            String prize = data.getStringExtra("prize");

            int numCoin = data.getIntExtra("coin", -1);

            //TODO SetNumOfCoinsToSaveFile !!!
            String msg = "Player has " + numCoin + " coins!\n" + prize;

            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

            bonusStr = prize;

        }


    }
}