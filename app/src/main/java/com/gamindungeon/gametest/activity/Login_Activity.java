package com.gamindungeon.gametest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnReturnToOptionFromLogin;
    EditText edLoginUsername, edLoginPassword;
    DatabaseReference usersDatabase;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {

        edLoginUsername = findViewById(R.id.edLoginUsername);
        edLoginPassword = findViewById(R.id.edLoginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnReturnToOptionFromLogin = findViewById(R.id.btnReturnToOptionFromLogin);

        btnLogin.setOnClickListener(this);
        btnReturnToOptionFromLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();

        switch (id){
            case R.id.btnLogin:
                loginToManageSaveCloud();
                break;
            case R.id.btnReturnToOptionFromLogin:
                finish();
                break;
        }
    }

    private void loginToManageSaveCloud() {

        try{

            username = edLoginUsername.getText().toString();
            password = edLoginPassword.getText().toString();
            String encryptPass = encryptString(password);


            usersDatabase = FirebaseDatabase.getInstance().getReference("users").child(username);

            usersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {

                        displayMessage("Username or password is incorrect.");

                    }else {

                        if(!encryptPass.equals(snapshot.child("password").getValue().toString())){
                            displayMessage("Username or password is incorrect.");
                        }else {
                            goToManageCloud();
                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String encryptString (String input) throws NoSuchAlgorithmException {


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);


        return bigInt.toString(16);
    }

    private void goToManageCloud() {
        Intent i =  new Intent(this, Manage_Cloud_Activity.class);
        i.putExtra("username", username);
        this.startActivity(i);

    }

    private void displayMessage(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}