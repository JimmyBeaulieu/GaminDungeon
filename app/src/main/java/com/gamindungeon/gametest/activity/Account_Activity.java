package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gamindungeon.gametest.R;

public class Account_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister, btnLogin, btnReturnToOptionFromRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_account);
        initialize();
    }

    private void initialize() {
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnReturnToOptionFromRegister = findViewById(R.id.btnReturnToOptionFromRegister);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnReturnToOptionFromRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.btnRegister:
                goToRegister();
                break;
            case R.id.btnLogin:
                goToLogin();
                break;
            case R.id.btnReturnToOptionFromRegister:
                finish();
                break;
        }

    }

    private void goToLogin() {
        Intent i =  new Intent(this, Login_Activity.class);
        this.startActivity(i);
    }

    private void goToRegister() {
        Intent i =  new Intent(this, Register_Activity.class);
        this.startActivity(i);
    }
}