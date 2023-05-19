package com.jordansonatina.game;

import java.util.ArrayList;

public class Wave {
    private ArrayList<Fruit> fruits;
    private int size;

    private int timeBetweenThrow;
    private int tick;

    private boolean finished;

    public Wave()
    {
        finished = false;
        tick = 0;
        timeBetweenThrow = 45;
        size = (int)(Math.random() * (6-2)+2);
        fruits = new ArrayList<>();
    }

    public ArrayList<Fruit> getFruit() {return fruits;}
    public boolean isFinished() {return finished;}

    public void throwFruit()
    {
        if (fruits.size() == size)
            finished = true;

        tick++;
        if (tick > timeBetweenThrow)
            tick = 0;

        if (!finished && tick == timeBetweenThrow-1)
            fruits.add(new Fruit());

    }

    private void removeFruit()
    {
        for (int i = 0; i < fruits.size(); i++)
        {
            fruits.clear();
        }
    }

    public void resetWave()
    {
        removeFruit();
        finished = false;
        tick = 0;
        size = (int)(Math.random() * (6-2)+2);
    }
}
