package com.gamindungeon.gametest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    Button btnRegister, btnReturnToOptionFromRegister;
    EditText edRegisterEmail, edRegisterPassword,edRegisterRetypePassword, edUsername;

    DatabaseReference usersDatabase, usersChild;

    String username;
    String email;
    String password;
    String encryptPass;

    boolean isUsernameUnique = true;
    boolean isEmailUnique = true;

    String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_register);

        initialize();
    }


    private void initialize() {
        btnRegister = findViewById(R.id.btnRegister);
        btnReturnToOptionFromRegister = findViewById(R.id.btnReturnToOptionFromRegister);

        edRegisterEmail = findViewById(R.id.edRegisterEmail);
        edRegisterPassword = findViewById(R.id.edRegisterPassword);
        edRegisterRetypePassword = findViewById(R.id.edRegisterRetypePassword);
        edUsername = findViewById(R.id.edUsername);

        btnRegister.setOnClickListener(this);
        btnReturnToOptionFromRegister.setOnClickListener(this);

        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnReturnToOptionFromRegister:
                finish();
                break;
            case R.id.btnRegister:
                registerNewUser();
        }


    }

    private void registerNewUser() {

        try {
            username= edUsername.getText().toString();
            email = edRegisterEmail.getText().toString();
            password = edRegisterPassword.getText().toString();
            encryptPass = encryptString(password);


            if(!password.equals(edRegisterRetypePassword.getText().toString()) || password.isEmpty()){
                Toast.makeText(this,"Passwords do not match or empty.", Toast.LENGTH_LONG).show();
                return;
            }

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if(!matcher.matches()){
                Toast.makeText(this,"Email does not match email pattern.(aka -> name@test.com)", Toast.LENGTH_LONG).show();
                return;
            }

            checkIfEmailAndUsernameExistsToAdd(email, username);

        } catch (Exception ex){

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();

        }

    }


    private void checkIfEmailAndUsernameExistsToAdd(String uEmail, String uUsername) {

        try {

            usersChild = FirebaseDatabase.getInstance().getReference("users");

            usersChild.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        isEmailUnique = true;
                        isUsernameUnique = true;

                        for (DataSnapshot ch: snapshot.getChildren()) {

                            if (uEmail.equals(ch.child("email").getValue().toString())){
                                displayMessage( "Email Exists in database. FAIL to Register");
                                isEmailUnique = false;
                            }

                            if (uUsername.equals(ch.child("username").getValue().toString())){
                                displayMessage( "Username Exists in database. FAIL to Register");
                                isUsernameUnique = false;
                            }

                        }


                        if (isUsernameUnique == true && isEmailUnique == true){

                            User user = new User(username, email, encryptPass, "NoSave");
                            usersDatabase.child(username).setValue(user);
                            clearFields();

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


    private void displayMessage(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void clearFields() {
        edUsername.setText("");
        edRegisterEmail.setText("");
        edRegisterPassword.setText("");
        edRegisterRetypePassword.setText("");

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}