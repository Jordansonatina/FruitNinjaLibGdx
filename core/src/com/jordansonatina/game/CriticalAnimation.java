package com.jordansonatina.game;

public class CriticalAnimation extends Animation{
    private int animationTimer = 0;
    private int animationLasts = 30;

    public boolean done;
    public CriticalAnimation()
    {
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
