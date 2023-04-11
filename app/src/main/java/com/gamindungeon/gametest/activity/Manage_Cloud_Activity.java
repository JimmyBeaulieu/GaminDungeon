package com.gamindungeon.gametest.activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gamindungeon.gametest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Manage_Cloud_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnUploadToCloud, btnDownloadFromCloud, btnReturnToLogin;

    DatabaseReference usersDatabase, usersChild;

    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        setContentView(R.layout.activity_manage_cloud);

        initialize();
    }

    private void initialize() {

        btnUploadToCloud = findViewById(R.id.btnUploadToCloud);
        btnDownloadFromCloud = findViewById(R.id.btnDownloadFromCloud);
        btnReturnToLogin = findViewById(R.id.btnReturnToLogin);

        btnUploadToCloud.setOnClickListener(this);
        btnDownloadFromCloud.setOnClickListener(this);
        btnReturnToLogin.setOnClickListener(this);

        username = getIntent().getStringExtra("username");

        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnUploadToCloud:
                uploadSave();
                break;
            case R.id.btnDownloadFromCloud:
                downloadSave();
                break;
            case R.id.btnReturnToLogin:
                finish();
                break;

        }

    }

    private void downloadSave() {

        try{

            usersChild = FirebaseDatabase.getInstance().getReference("users").child(username);

            usersChild.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        boolean isSuccessful = setSaveContent(snapshot.child("save").getValue().toString());

                        if(isSuccessful){
                            displayMessage("Success to download save");
                        }else {
                            displayMessage("Fail to download save");
                        }

                    }else {
                        displayMessage("FAILED to download save");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception ex){
            Toast.makeText(this,"YOO" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private boolean setSaveContent(String saveContent) {
        boolean result = false;

        try{

            File directory = this.getFilesDir();
            File[] files = directory.listFiles();

            if (files != null) {

                for (File file : files) {

                    file.delete();

                }
                File newSaveFile = new File(directory + "/SaveFile");
                FileWriter fw = new FileWriter(newSaveFile, false);
                fw.write(saveContent);
                fw.close();
                System.out.println("File is created with cloud save.");
                result = true;

            }

        } catch (Exception ex){
            System.out.println("Exception is" + ex.getMessage());
        }

        return result;
    }

    private void uploadSave() {

        try{

            usersChild = FirebaseDatabase.getInstance().getReference("users").child(username);

            usersChild.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        String content = getSaveFileContent();
                        usersDatabase.child(username).child("save").setValue(content);
                        displayMessage("Success to upload save");


                    }else {
                        displayMessage("FAILED to upload save");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception ex){
            Toast.makeText(this,"YOO" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private String getSaveFileContent() {
        String content = "";
        File directory = this.getFilesDir();
        File[] files = directory.listFiles();

        if (files != null) {

            for (File file : files) {

                BufferedReader reader;

                try {
                    reader = new BufferedReader(new FileReader(file));
                    String line = reader.readLine();

                    while (line != null) {
                        System.out.println(line);
                        content = content + line;

                        line = reader.readLine();
                    }

                    reader.close();

                } catch (Exception ex) {
                    System.out.println("Exception is" + ex.getMessage());
                }

            }

        }

        return content;
    }


    private void displayMessage(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}