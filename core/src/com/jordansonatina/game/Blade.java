package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;

public class Blade {
    private Vector2 position;
    private Vector2 previousPosition;

    public Blade()
    {
        position = Vector2.Zero;
        previousPosition = Vector2.Zero;
    }

    public void setPos(Vector2 n) {position = n;}
    public Vector2 getPos() {return position;}

    public void setPrevPos(Vector2 n) {previousPosition = n;}
    public Vector2 getPrevPos() {return previousPosition;}
}
