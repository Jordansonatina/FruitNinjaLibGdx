package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;
public class Fruit {

    private Vector2 position;
    private Vector2 velocity;

    private int radius;

    public Fruit()
    {
        radius = 50;
        position = new Vector2((int)(Math.random() * ((Constants.WIDTH-radius)-radius) + radius), -radius/2);
        velocity = new Vector2(0, (int)(Math.random() * (20-15)+15));
    }
    public void update()
    {
        position.add(velocity);
        velocity.add(new Vector2(0, Constants.GRAVITY));

    }

    public Vector2 getPos() {return position;}
    public Vector2 getVel() {return velocity;}
    public int getRadius() {return radius;}
}
