package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;
public class Veggie {

    private Vector2 position;
    private Vector2 velocity;

    private final int radius;

    private final int rightSpawnBounds;
    private final int leftSpawnBounds;

    private int maxVelocity;
    private int minVelocity;

    private int isCritical;

    private String type;

    public Veggie(String type)
    {
        maxVelocity = 17;
        minVelocity = 14;
        this.type = type;
        leftSpawnBounds = 200;
        rightSpawnBounds = Constants.WIDTH - 200;
        radius = 50;

        if (!type.equals("Pumpkin")) {

            newPosition();
            if (Math.random() < 0.05)
                isCritical = 1;
            else
                isCritical = 0;

        } else {
            velocity = new Vector2(0, 15);
            position = new Vector2(Constants.WIDTH / 2 - radius, -radius);
            isCritical = 0;
        }

    }
    private void newPosition()
    {
        int velocityX = 0;

        if (Wave.isFrenzyTime)
        {
            if (Math.random() < 0.5) {
                position = new Vector2(-10, (int) (Math.random() * (Constants.HEIGHT/2-100)+100));
                velocityX = 8;
            }
            else {
                position = new Vector2(Constants.WIDTH + 10, (int) (Math.random() * (Constants.HEIGHT/2-100)+100));
                velocityX = -8;
            }

            velocity = new Vector2(velocityX, (int)(Math.random() * (13-8)+8));

            return;
        }

        if (type.equals("Bomb"))
        {
            position = new Vector2((int)(Math.random() * (rightSpawnBounds-100 - leftSpawnBounds+100)+leftSpawnBounds), -radius);
            if (position.x < Constants.WIDTH/2)
                velocityX = (int)(Math.random()*(3)-3);
            else
                velocityX = (int)(Math.random()*3);
            velocity = new Vector2(velocityX, (int)(Math.random() * (maxVelocity+1-minVelocity)+minVelocity));
            return;
        }

        position = new Vector2((int)(Math.random() * (rightSpawnBounds - leftSpawnBounds)+leftSpawnBounds), -radius);
        // if veggie spawns to the right make velocity negative and vice versa so the veggies bundle in the middle
        if (position.x > Constants.WIDTH/2)
            velocityX = (int)(Math.random()*(3)-3);
        else
            velocityX = (int)(Math.random()*3);
        velocity = new Vector2(velocityX, (int)(Math.random() * (maxVelocity+1-minVelocity)+minVelocity));

    }

    public void update()
    {
        position.add(velocity);
        velocity.add(new Vector2(0, Constants.GRAVITY));

    }

    public Vector2 getPos() {return position;}
    public Vector2 getVel() {return velocity;}
    public void setVel(Vector2 n) {velocity = n;}
    public int getRadius() {return radius;}

    public String getType() {return type;}
    public void setType(String n){ type = n;}

    public int isCritical() {return isCritical;}
}
