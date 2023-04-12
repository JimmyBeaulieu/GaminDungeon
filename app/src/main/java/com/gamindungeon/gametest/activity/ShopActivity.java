package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.object.collectable.Food;
import com.gamindungeon.gametest.object.collectable.foodType;

import org.w3c.dom.Text;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvGold;
    Button[] buyButtonSlot = new Button[3];
    private ImageView[] imageSlot;
    private TextView[] textSlot;
    private TextView[] priceSlot;
    Food[] foodList;

    private int coins;

    //S
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
        setContentView(R.layout.activity_shop);
        initialize();
    }

    private void initialize() {

        tvGold = findViewById(R.id.tvGold);

        //initialize array of Button
        buyButtonSlot[0] = findViewById(R.id.buySlot0);
        buyButtonSlot[1] = findViewById(R.id.buySlot1);
        buyButtonSlot[2] = findViewById(R.id.buySlot2);

        //add OnClickListener to all buttons
        //set all the buttons invisible so only the image is shown at first, when the user click on the image, the button and text will appear
        for(Button button : buyButtonSlot){
            button.setOnClickListener(this);
            button.setVisibility(View.INVISIBLE);
        }

        //initialize array of ImageView
        imageSlot = new ImageView[3];
        imageSlot[0] = findViewById(R.id.imageSlot0);
        imageSlot[1] = findViewById(R.id.imageSlot1);
        imageSlot[2] = findViewById(R.id.imageSlot2);

        //add OnClickListener to all ImageView
        for(ImageView image : imageSlot){
            image.setOnClickListener(this);
        }

        //initialize array of TextView
        textSlot = new TextView[3];
        textSlot[0] = findViewById(R.id.textSlot0);
        textSlot[1] = findViewById(R.id.textSlot1);
        textSlot[2] = findViewById(R.id.textSlot2);

        //set the text invisible so only the image can appear at first
        for(TextView text : textSlot){
            text.setVisibility(View.INVISIBLE);
        }

        priceSlot = new TextView[3];
        priceSlot[0] = findViewById(R.id.priceSlot0);
        priceSlot[1] = findViewById(R.id.priceSlot1);
        priceSlot[2] = findViewById(R.id.priceSlot2);

        for(TextView text : priceSlot){
            text.setVisibility(View.INVISIBLE);
        }

        foodList = new Food[]{
                new Food(this, 0, 0, foodType.DONUT),
                new Food(this, 0,0,foodType.DRUMSTICK),
                new Food(this, 0,0, foodType.BURGER),
                new Food(this, 0,0, foodType.CAKE),
                new Food(this, 0,0, foodType.CONE),
                new Food(this, 0,0, foodType.POTION)
        };


        String gold = String.valueOf(Score.gold);
        tvGold.setText(gold);

        randomSelection();
    }

    private void randomSelection() {
        for()
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.imageSlot0:
                //set the appropriate button visible
                textSlot[0].setVisibility(View.VISIBLE);
                priceSlot[0].setVisibility(View.VISIBLE);
                buyButtonSlot[0].setVisibility(View.VISIBLE);

                //set all the other button invisible
                textSlot[1].setVisibility(View.INVISIBLE);
                buyButtonSlot[1].setVisibility(View.INVISIBLE);
                priceSlot[1].setVisibility(View.INVISIBLE);
                textSlot[2].setVisibility(View.INVISIBLE);
                buyButtonSlot[2].setVisibility(View.INVISIBLE);
                priceSlot[2].setVisibility(View.INVISIBLE);

                break;
            case R.id.imageSlot1:
                //set the appropriate button visible
                textSlot[1].setVisibility(View.VISIBLE);
                priceSlot[1].setVisibility(View.VISIBLE);
                buyButtonSlot[1].setVisibility(View.VISIBLE);

                //set all the other button invisible
                textSlot[0].setVisibility(View.INVISIBLE);
                buyButtonSlot[0].setVisibility(View.INVISIBLE);
                priceSlot[0].setVisibility(View.INVISIBLE);
                textSlot[2].setVisibility(View.INVISIBLE);
                buyButtonSlot[2].setVisibility(View.INVISIBLE);
                priceSlot[2].setVisibility(View.INVISIBLE);

                break;
            case R.id.imageSlot2:
                //set the appropriate button visible
                textSlot[2].setVisibility(View.VISIBLE);
                priceSlot[2].setVisibility(View.VISIBLE);
                buyButtonSlot[2].setVisibility(View.VISIBLE);

                //set all the other button invisible
                textSlot[0].setVisibility(View.INVISIBLE);
                buyButtonSlot[0].setVisibility(View.INVISIBLE);
                priceSlot[0].setVisibility(View.INVISIBLE);
                textSlot[1].setVisibility(View.INVISIBLE);
                priceSlot[1].setVisibility(View.INVISIBLE);
                buyButtonSlot[1].setVisibility(View.INVISIBLE);

                break;
        }
    }
}