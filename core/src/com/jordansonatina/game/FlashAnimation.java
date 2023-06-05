package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;

public class FlashAnimation extends Animation{
    private int animationTimer = 0;
    private int animationLasts = 60;

    public Vector2 position;

    public boolean done;
    public FlashAnimation(Vector2 position)
    {
        super(position);
        done = false;
    }

    public int getAnimationTimer() {return animationTimer;}
    public int getAnimationLasts() {return animationLasts;}
    public boolean isDone() {return done;}
    public void time()
    {
        animationTimer++;
        if (animationTimer > animationLasts)
            done = true;
    }





}
