package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;
public class Veggie {

    private Vector2 position;
    private Vector2 velocity;

    private int radius;

    private int rightSpawnBounds;
    private int leftSpawnBounds;

    private String type;

    public Veggie()
    {
        leftSpawnBounds = 200;
        rightSpawnBounds = Constants.WIDTH - 200;
        radius = 50;
        newPosition();

    }
    private void newPosition()
    {
        position = new Vector2((int)(Math.random() * (rightSpawnBounds - leftSpawnBounds)+leftSpawnBounds), -radius/2);
        velocity = new Vector2(0, (int)(Math.random() * (23-18)+18));
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
