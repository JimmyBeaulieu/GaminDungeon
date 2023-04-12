package com.gamindungeon.gametest.object.collectable;

public class Powerup {

    int strengthBonus;
    int lifeBonus;
    int coinBonus;

    powerUpType powerUpType;

    public Powerup(int bonus, powerUpType powerUpType){
        switch (powerUpType){
            case LIFE:
                    lifeBonus = bonus;
                break;
            case COIN:
                    coinBonus = bonus;
                break;
            case STRENGTH:
                    strengthBonus = bonus;
                break;
        }
    }

}
