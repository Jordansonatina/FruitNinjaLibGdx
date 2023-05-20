package com.jordansonatina.game;

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

    private String[] types = {"Cabbage", "BellPepper"};

    private boolean finished;

    private int theThrows;

    public Wave()
    {
        finished = false;
        tick = 0;
        timeBetweenThrow = 45;

        maxThrowTime = 60;
        minThrowTime = 30;

        theThrows = 0;

        maxSize = 8;
        minSize = 2;

        size = (int)(Math.random() * ((maxSize+1)-minSize)+minSize);
        veggies = new ArrayList<>();
    }

    public ArrayList<Veggie> getVeggie() {return veggies;}
    public boolean isFinished() {return finished;}




    public void throwFruit()
    {
        if (theThrows == size)
            finished = true;

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
        size = (int)(Math.random() * (6-2)+2);
    }
}
