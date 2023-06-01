package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;

public class ComboAnimation extends Animation{

    private int animationTimer = 0;
    private int animationLasts = 100;

    private Vector2 position;


    public boolean done;
    public ComboAnimation(Vector2 position)
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
