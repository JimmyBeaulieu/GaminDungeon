package com.gamindungeon.gametest.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Option;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    final int REQUEST_CODE = 1;
    Button btnPlay, btnOptions, deleteSaveFileButton, btnTutorial, btnReturnToMenu, musicToggle, sfxToggle, btnAccount;
    Button shopButton;
    Music mp;

    boolean isOptionUp;


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
        btnAccount = findViewById(R.id.btnAccount);

        btnPlay.setOnClickListener(this);
        btnOptions.setOnClickListener(this);
        btnAccount.setOnClickListener(this);

        //setting up music for menu
        mp = new Music(this);


        deleteSaveFileButton = findViewById(R.id.deleteSaveFileButton);
        deleteSaveFileButton.setOnClickListener(this);
        deleteSaveFileButton.setVisibility(View.INVISIBLE);


        btnTutorial = findViewById(R.id.btnTutorial);
        btnTutorial.setOnClickListener(this);
        btnTutorial.setVisibility(View.INVISIBLE);

        btnReturnToMenu = findViewById(R.id.btnReturnToMenu);
        btnReturnToMenu.setOnClickListener(this);
        btnReturnToMenu.setVisibility(View.INVISIBLE);

        musicToggle = findViewById(R.id.musicToggle);
        musicToggle.setOnClickListener(this);
        musicToggle.setVisibility(View.INVISIBLE);

        sfxToggle = findViewById(R.id.sfxToggle);
        sfxToggle.setOnClickListener(this);
        sfxToggle.setVisibility(View.INVISIBLE);

        updateText();

        shopButton = findViewById(R.id.shopTest);
        shopButton.setOnClickListener(this);


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
            case R.id.deleteSaveFileButton:
                File directory = this.getFilesDir(); // assuming you want to delete files from app's internal storage
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.delete()) {
                            // File deleted successfully
                            Toast.makeText(this, "Save file deleted!", Toast.LENGTH_LONG).show();
                        }
                    }
                    Toast.makeText(this, "No save file found!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnTutorial:
                Intent i =  new Intent(this, Tutorial_Activity.class);
                this.startActivity(i);
                break;
            case R.id.btnReturnToMenu:
                returnToMenu();
                break;

            case R.id.musicToggle:
                Option.isMusicOn = !Option.isMusicOn;
                updateText();
                break;

            case R.id.sfxToggle:
                Option.isSoundOn = !Option.isSoundOn;
                updateText();
                break;
            case R.id.btnAccount:
                goToAccount();
                break;
            case R.id.shopTest:
                i =  new Intent(this, ShopActivity.class);
                this.startActivity(i);
        }


    }

    private void goToAccount() {
        Intent i =  new Intent(this, Account_Activity.class);
        this.startActivity(i);
    }

    private void returnToMenu() {
        btnPlay.setVisibility(View.VISIBLE);
        btnOptions.setVisibility(View.VISIBLE);
        deleteSaveFileButton.setVisibility(View.INVISIBLE);
        btnTutorial.setVisibility(View.INVISIBLE);
        btnReturnToMenu.setVisibility(View.INVISIBLE);
        musicToggle.setVisibility(View.INVISIBLE);
        sfxToggle.setVisibility(View.INVISIBLE);
    }

    private void goToOptions() {

        btnPlay.setVisibility(View.INVISIBLE);
        btnOptions.setVisibility(View.INVISIBLE);
        deleteSaveFileButton.setVisibility(View.VISIBLE);
        btnTutorial.setVisibility(View.VISIBLE);
        btnReturnToMenu.setVisibility(View.VISIBLE);
        musicToggle.setVisibility(View.VISIBLE);
        sfxToggle.setVisibility(View.VISIBLE);


    }

    private void updateText(){
        if(Option.isMusicOn){ musicToggle.setText("Music ON"); }
        else { musicToggle.setText("Music OFF"); }

        if(Option.isSoundOn){ sfxToggle.setText("Sound Effects ON"); }
        else { sfxToggle.setText("Sound Effects OFF"); }
    }

    private void goToGamePlay() {
        Intent i =  new Intent(this, Game_Activity.class);
        this.startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.stop();
        if(Option.isMusicOn){ mp.play(1);}
    }
}