package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Score;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvGold;
    private Button btnItem1, btnItem2, btnItem3;
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initialize();
    }

    private void initialize() {

        tvGold = findViewById(R.id.tvGold);
        btnItem1 = findViewById(R.id.btnItem1);
        btnItem2 = findViewById(R.id.btnItem2);
        btnItem3 = findViewById(R.id.btnItem3);

        String gold = String.valueOf(Score.gold);
        tvGold.setText(gold);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnItem1:

                break;
            case R.id.btnItem2:

                break;
            case R.id.btnItem3:

                break;
        }
    }
}