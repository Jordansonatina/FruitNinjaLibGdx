package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Wave {
    private ArrayList<Veggie> veggies;
    private int size;

    private int timeBetweenThrow;
    private int tick;

    private int maxThrowTime;
    private int minThrowTime;

    private int maxSize;
    private int minSize;

    private boolean pumpkinOut;

    private int pumpkinMaxTime;
    private int pumpkinTimer;

    private String[] types = {"Cabbage", "BellPepper", "Potato", "Artichoke", "Eggplant"};


    private boolean finished;

    private boolean isPumpkinTime;

    private int theThrows;

    public static boolean isFrenzyTime;
    private boolean isFrenzyWave;

    private boolean isFrenzyEggplantThrown;
    private float chanceOfFrenzyPerWave;

    private int frenzyDuration;
    private int frenzyTimer;


    private int slices;

    public Wave()
    {
        finished = false;
        tick = 0;
        slices = 0;

        isFrenzyEggplantThrown = false;
        isFrenzyTime = false;
        chanceOfFrenzyPerWave = 0.01f;
        frenzyDuration = 60*6;


        pumpkinMaxTime = 100;
        pumpkinTimer = 0;
        isPumpkinTime = false;

        maxThrowTime = 40;
        minThrowTime = 20;
        timeBetweenThrow = (int)(Math.random() * ((maxThrowTime+1) - minThrowTime) + minThrowTime);

        theThrows = 0;

        maxSize = 15;
        minSize = 4;

        pumpkinOut = false;

        size = (int)(Math.random() * ((maxSize+1)-minSize)+minSize);
        veggies = new ArrayList<>();
    }

    public ArrayList<Veggie> getVeggie() {return veggies;}
    public boolean isFinished() {return finished;}
    public boolean isPumpkinTime() {return isPumpkinTime;}

    public void setIsFrenzyTime(boolean n) {isFrenzyTime= n;}
    public boolean isFrenzyTime() {return isFrenzyTime;}



    public void startPumpkinTimer()
    {
        if (pumpkinTimer == 0)
            freezePumpkin();

        pumpkinTimer++;
        isPumpkinTime = true;

        if (pumpkinTimer >= pumpkinMaxTime)
        {
            System.out.println(pumpkinTimer);
            pumpkinOut = false;
            removeVeggies();
        }
    }

    public void startFrenzyTimer()
    {
        frenzyTimer++;
        if (frenzyTimer >= frenzyDuration) {
            frenzyTimer = 0;
            isFrenzyTime = false;
        }
    }

    private void freezePumpkin()
    {
        veggies.get(0).setVel(Vector2.Zero);
    }

    public void throwFruit()
    {
        tick++;
        if (tick > timeBetweenThrow)
            tick = 0;

        // throw pumpkin at the very end of the game
        if (Game.timer.isFinished() && !pumpkinOut && finished)
        {
            removeVeggies();
            isFrenzyTime = false;
            pumpkinOut = true;
            veggies.add(new Veggie("Pumpkin"));
        }

        if (isFrenzyTime)
        {
            timeBetweenThrow = 5; // veggies are going to be coming a lot faster

            if (tick == timeBetweenThrow-1) {
                int randomType = (int) (Math.random() * types.length);
                veggies.add(new Veggie(types[randomType]));
            }

            return;
        }

        if (theThrows >= size) {
            finished = true;
        }



        // actually throwing the fruit
        if (!isFrenzyTime && !finished && tick == timeBetweenThrow-1)
        {
            theThrows++;
            Game.throwSound.play();

            // throw either a normal fruit or a special fruit
            if (Math.random() < chanceOfFrenzyPerWave && !isFrenzyEggplantThrown && !isFrenzyTime) {
                veggies.add(new Veggie("FrenzyEggplant"));
                isFrenzyEggplantThrown = true;
            }
            else {
                int randomType = (int)(Math.random() * types.length);
                veggies.add(new Veggie(types[randomType]));
            }
            timeBetweenThrow = (int)(Math.random() * ((maxThrowTime+1) - minThrowTime) + minThrowTime);
        }


    }

    public void slice(Veggie v)
    {
        slices++;
        v.setType("Sliced");
    }


    private void removeVeggies()
    {
        for (int i = 0; i < veggies.size(); i++)
        {
            veggies.clear();
        }
    }

    public void resetWave()
    {
        removeVeggies();
        theThrows = 0;
        finished = false;
        tick = 0;
        slices = 0;
        size = (int)(Math.random() * (maxSize+1-minSize)+minSize);
        isFrenzyEggplantThrown = false;

    }
}
