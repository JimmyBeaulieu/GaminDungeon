package com.gamindungeon.gametest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Option;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    final int REQUEST_CODE = 1;
    Button btnPlay, btnOptions;
    Music mp;

    String bonusStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set window to fullscreen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);
        initialize();
        

    }

    private void initialize() {
        btnPlay = findViewById(R.id.btnPlay);
        btnOptions = findViewById(R.id.btnOptions);

        btnPlay.setOnClickListener(this);
        btnOptions.setOnClickListener(this);

        //setting up music for menu
        mp = new Music(this);


    }

    @Override
    public void onClick(View view) {

        int id  = view.getId();


        switch (id){
            case R.id.btnPlay:
                mp.stop();
                goToGamePlay();
                break;
            case R.id.btnOptions:
                goToOptions();
                break;
        }


    }

    private void goToOptions() {
        Intent i =  new Intent(this, Option_Activity.class);
        this.startActivity(i);
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
    protected void onResume() {
        super.onResume();
        if(Option.isMusicOn){ mp.play(1);}
        else{ mp.stop(); }
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