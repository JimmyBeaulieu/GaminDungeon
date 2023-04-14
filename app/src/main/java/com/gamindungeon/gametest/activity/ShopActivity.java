package com.gamindungeon.gametest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamindungeon.gametest.R;
import com.gamindungeon.gametest.manager.Music;
import com.gamindungeon.gametest.manager.Score;
import com.gamindungeon.gametest.object.Player;
import com.gamindungeon.gametest.object.collectable.Food;
import com.gamindungeon.gametest.object.collectable.Powerup;
import com.gamindungeon.gametest.object.collectable.foodType;
import com.gamindungeon.gametest.object.collectable.powerUpType;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvGold;
    Button[] buyButtonSlot = new Button[3];
    Button leaveButton;
    private ImageView[] imageSlot;
    private TextView[] textSlot;
    private TextView[] priceSlot;
    Food[] foodList;
    Powerup[] powerupList;

    Object[] allItems;
    Object[] availableToBuy;

    int[] price;

    private int coins;
    Music music;
    Music sfx;

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
        music = new Music(this);
        sfx = new Music(this);
        sfx.playSFX(5);
        music.play(7);

        price = new int[3];

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

        allItems = new Object[]{

                new Food(this, 0, 0, foodType.DONUT),       //0
                new Food(this, 0, 0, foodType.DRUMSTICK),   //1
                new Food(this, 0, 0, foodType.BURGER),      //2
                new Food(this, 0, 0, foodType.CAKE),        //3
                new Food(this, 0, 0, foodType.CONE),        //4
                new Food(this, 0, 0, foodType.POTION),      //5
                new Food(this, 0, 0, foodType.PACZKI),      //6
                new Food(this, 0, 0, foodType.BIGCAKE),      //7

                new Powerup(this, 10, powerUpType.LIFE),                //8
                new Powerup(this, 20, powerUpType.LIFE),                //9
                new Powerup(this, 30, powerUpType.LIFE),                //10
                new Powerup(this, 40, powerUpType.LIFE),                //11
                new Powerup(this, 50, powerUpType.LIFE),                //12

                new Powerup(this, 10, powerUpType.STRENGTH),            //13
                new Powerup(this, 20, powerUpType.STRENGTH),            //14
                new Powerup(this, 30, powerUpType.STRENGTH),            //15
                new Powerup(this, 40, powerUpType.STRENGTH),            //16
                new Powerup(this, 50, powerUpType.STRENGTH),            //17

                new Powerup(this, 2, powerUpType.COIN),                 //18
                new Powerup(this, 4, powerUpType.COIN),                 //19
                new Powerup(this, 6, powerUpType.COIN),                 //20
                new Powerup(this, 8, powerUpType.COIN),                 //21
                new Powerup(this, 10, powerUpType.COIN)                 //22
        };

        updateText();
        leaveButton = findViewById(R.id.leaveButton);
        leaveButton.setOnClickListener(this);

        randomSelection();
    }

    private void updateText(){
        String gold = "Gold: " + String.valueOf(Score.gold);
        tvGold.setText(gold);
    }

    private void randomSelection() {

        Random rand = new Random();

        availableToBuy = new Object[3];

        List<Integer>alreadyInIt = new ArrayList<Integer>();

        for(int i = 0; i<3 ;i++){

            int choice = rand.nextInt(23);

            for(Integer num : alreadyInIt){
                while(choice == num){
                    choice = rand.nextInt(23);
                }
            }
            availableToBuy[i] = allItems[choice];
            alreadyInIt.add(i);

        }

        for(int i = 0; i<3 ; i++){
            if(availableToBuy[i].getClass() == Food.class){

                imageSlot[i].setImageBitmap(((Food)availableToBuy[i]).getSprite());
                textSlot[i].setText(((Food)availableToBuy[i]).getName());
                price[i] = ((Food)availableToBuy[i]).getShopValue();
                String text = String.valueOf(price[i]) + " $";
                priceSlot[i].setText(text);
            }

            if(availableToBuy[i].getClass() == Powerup.class){


                imageSlot[i].setImageBitmap(((Powerup)availableToBuy[i]).getSprite());
                String text = "";
                if(((Powerup)availableToBuy[i]).getType() == powerUpType.COIN){
                    text = ((Powerup) availableToBuy[i]).getName() + " X " + ((Powerup) availableToBuy[i]).getBonus();
                }
                else {
                    text = ((Powerup) availableToBuy[i]).getName() + " + " + ((Powerup) availableToBuy[i]).getBonus();
                    ;
                }

                textSlot[i].setText(text);
                price[i] = ((Powerup)availableToBuy[i]).getShopValue();
                text = String.valueOf(price[i]) + " $";
                priceSlot[i].setText(text);
            }

        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.imageSlot0:
                //set the appropriate button visible
                textSlot[0].setVisibility(View.VISIBLE);
                priceSlot[0].setVisibility(View.VISIBLE);
                priceSlot[0].setTextColor(Color.parseColor("#e39950"));
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
                priceSlot[1].setTextColor(Color.parseColor("#e39950"));
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

                priceSlot[2].setTextColor(Color.parseColor("#e39950"));

                buyButtonSlot[2].setVisibility(View.VISIBLE);

                //set all the other button invisible
                textSlot[0].setVisibility(View.INVISIBLE);
                buyButtonSlot[0].setVisibility(View.INVISIBLE);
                priceSlot[0].setVisibility(View.INVISIBLE);
                textSlot[1].setVisibility(View.INVISIBLE);
                priceSlot[1].setVisibility(View.INVISIBLE);
                buyButtonSlot[1].setVisibility(View.INVISIBLE);

                break;

            case R.id.buySlot0:


                if(Score.gold >= price[0]){

                    sfx.playSFX(7);

                    Score.gold -= price[0];

                    if(availableToBuy[0].getClass() == Food.class) {
                        Player.hunger += ((Food)availableToBuy[0]).getHunger();
                        Player.health += ((Food)availableToBuy[0]).getHealthRestoreAmount();
                    }
                    if(availableToBuy[0].getClass() == Powerup.class) {
                        Player.givePowerUp(((Powerup)availableToBuy[0]).getType(), ((Powerup)availableToBuy[0]).getBonus());
                    }


                    imageSlot[0].setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.raw.sold), 176, 176, false));
                    textSlot[0].setVisibility(View.INVISIBLE);
                    priceSlot[0].setVisibility(View.INVISIBLE);
                    buyButtonSlot[0].setVisibility(View.INVISIBLE);
                    updateText();


                }
                else{
                    music.playSFX(6);
                    priceSlot[0].setTextColor(Color.RED);
                }

                break;
            case R.id.buySlot1:

                if(Score.gold >= price[1]){
                    sfx.playSFX(7);
                    Score.gold -= price[1];

                    if(availableToBuy[1].getClass() == Food.class) {
                        Player.hunger += ((Food)availableToBuy[1]).getHunger();
                        Player.health += ((Food)availableToBuy[1]).getHealthRestoreAmount();
                    }
                    if(availableToBuy[1].getClass() == Powerup.class) {
                        Player.givePowerUp(((Powerup)availableToBuy[1]).getType(), ((Powerup)availableToBuy[1]).getBonus());
                    }

                    imageSlot[1].setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.raw.sold), 176, 176, false));
                    textSlot[1].setVisibility(View.INVISIBLE);
                    priceSlot[1].setVisibility(View.INVISIBLE);
                    buyButtonSlot[1].setVisibility(View.INVISIBLE);
                    updateText();
                }
                else{
                    music.playSFX(6);
                    priceSlot[1].setTextColor(Color.RED);
                }

                break;

            case R.id.buySlot2:

                if(Score.gold >= price[2]){
                    sfx.playSFX(7);
                    Score.gold -= price[2];

                    if(availableToBuy[2].getClass() == Food.class) {
                        Player.hunger += ((Food)availableToBuy[2]).getHunger();
                        Player.health += ((Food)availableToBuy[2]).getHealthRestoreAmount();
                    }
                    if(availableToBuy[2].getClass() == Powerup.class) {

                        Player.givePowerUp(((Powerup)availableToBuy[2]).getType(), ((Powerup)availableToBuy[2]).getBonus());
                    }

                    imageSlot[2].setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.raw.sold), 176, 176, false));
                    textSlot[2].setVisibility(View.INVISIBLE);
                    priceSlot[2].setVisibility(View.INVISIBLE);
                    buyButtonSlot[2].setVisibility(View.INVISIBLE);
                    updateText();
                }
                else{
                    music.playSFX(6);
                    priceSlot[2].setTextColor(Color.RED);
                }

                break;

            case R.id.leaveButton:
                sfx.playSFX(5);
                sfx.stop();
                music.stop();
                finish();
        }
    }
}