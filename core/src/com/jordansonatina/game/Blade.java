package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;

public class Blade {
    private Vector2[] previousPosition;

    private int length;

    public Blade()
    {
        length = 500;
        previousPosition = new Vector2[length];
    }

    public Vector2 getPreviousPosition(int index) {return previousPosition[index];}

    public Vector2[] getPreviousPositions() {return previousPosition;}

    public void setPreviousPosition(Vector2 value, int index) {previousPosition[index] = value;}

    public int getLength() {return length;}


}
