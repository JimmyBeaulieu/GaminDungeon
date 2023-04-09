package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gamindungeon.gametest.R;

import java.io.File;

public class Option_Activity extends AppCompatActivity implements View.OnClickListener{
    Button deleteSaveFileButton, btnTutorial, btnReturnToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
      initialize();
    }

    private void initialize() {

        deleteSaveFileButton = findViewById(R.id.deleteSaveFileButton);
        deleteSaveFileButton.setOnClickListener(this);

        btnTutorial = findViewById(R.id.btnTutorial);
        btnTutorial.setOnClickListener(this);

        btnReturnToMenu = findViewById(R.id.btnReturnToMenu);
        btnReturnToMenu.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.deleteSaveFileButton:
                File directory = this.getFilesDir(); // assuming you want to delete files from app's internal storage
                File[] files = directory.listFiles();
                System.out.println("Before checking for null file");
                if (files != null) {
                    System.out.println("After checking for null file");
                    for (File file : files) {
                        System.out.println("Before deleting file");
                        if (file.delete()) {
                            System.out.println("after deleting file");
                            // File deleted successfully
                            Toast.makeText(this, "Save file deleted!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    Toast.makeText(this, "No save file found!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case R.id.btnTutorial:
                Intent i =  new Intent(this, Tutorial_Activity.class);
                this.startActivity(i);
                break;
            case R.id.btnReturnToMenu:
                finish();
                break;
        }


    }
}