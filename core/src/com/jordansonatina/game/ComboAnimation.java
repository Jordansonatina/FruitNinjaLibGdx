package com.jordansonatina.game;

import com.badlogic.gdx.math.Vector2;

public class ComboAnimation {

    public int type;
    public int animationTimer = 0;
    public int animationLasts = 30;

    public Vector2 position;

    public boolean done;
    public ComboAnimation(Vector2 position)
    {
        done = false;
        this.position = position;
    }

    public void time()
    {
        animationTimer++;
        if (animationTimer > animationLasts)
            done = true;
    }

    public int getAnimationTimer() {return animationTimer;}
    public boolean isDone() {return done;}
    public Vector2 getPos() {return position;}

}
