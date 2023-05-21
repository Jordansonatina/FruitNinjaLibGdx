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

    private String[] types = {"Cabbage", "BellPepper", "Potato"};


    private boolean finished;

    private boolean isPumpkinTime;

    private int theThrows;

    private int slices;

    public Wave()
    {
        finished = false;
        tick = 0;
        timeBetweenThrow = 45;
        slices = 0;

        pumpkinMaxTime = 100;
        pumpkinTimer = 0;
        isPumpkinTime = false;

        maxThrowTime = 40;
        minThrowTime = 20;

        theThrows = 0;

        maxSize = 20;
        minSize = 10;

        pumpkinOut = false;

        size = (int)(Math.random() * ((maxSize+1)-minSize)+minSize);
        veggies = new ArrayList<>();
    }

    public ArrayList<Veggie> getVeggie() {return veggies;}
    public boolean isFinished() {return finished;}
    public boolean isPumpkinTime() {return isPumpkinTime;}

    public void setPumpkinTime(boolean n) {isPumpkinTime = n;}


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

    private void freezePumpkin()
    {
        veggies.get(0).setVel(Vector2.Zero);
    }

    public void throwFruit()
    {
        if (theThrows >= size) {
            finished = true;
        }

        // throw pumpkin at the very end of the game
        if (Game.timer.isFinished() && !pumpkinOut && finished)
        {
            removeVeggies();
            pumpkinOut = true;
            veggies.add(new Veggie("Pumpkin"));
        }


        tick++;
        if (tick > timeBetweenThrow)
            tick = 0;

        // actually throwing the fruit
        if (!finished && tick == timeBetweenThrow-1)
        {
            theThrows++;
            Game.throwSound.play();
            int randomType = (int)(Math.random() * types.length);
            veggies.add(new Veggie(types[randomType]));
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
    }
}
