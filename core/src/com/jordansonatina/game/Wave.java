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

    private String[] types = {"Cabbage", "BellPepper", "Potato"};

    private boolean finished;

    private int theThrows;

    private int slices;

    public Wave()
    {
        finished = false;
        tick = 0;
        timeBetweenThrow = 45;
        slices = 0;

        maxThrowTime = 40;
        minThrowTime = 20;

        theThrows = 0;

        maxSize = 20;
        minSize = 10;

        size = (int)(Math.random() * ((maxSize+1)-minSize)+minSize);
        veggies = new ArrayList<>();
    }

    public ArrayList<Veggie> getVeggie() {return veggies;}
    public boolean isFinished() {return finished;}




    public void throwFruit()
    {
        if (theThrows >= size)
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
        slices++;
        v.setType("Sliced");
    }

    private boolean allHaveFallen()
    {
        int fallen = 0;
        for (Veggie v : veggies)
        {
            if (v.getVel().y < 0 && !v.getType().equals("Sliced") && v.getPos().y <= 30)
                fallen++;
        }
        if (fallen >= size)
            return true;
        else
            return false;
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
