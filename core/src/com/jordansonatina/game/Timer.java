package com.jordansonatina.game;

public class Timer {
    private int gameTime;

    private boolean finished;

    private int tick;

    public Timer(int gameTime)
    {
        this.gameTime = gameTime;
        tick = 0;
        finished = false;
    }

    public void countDown()
    {
        if (gameTime == 0)
            finished = true;

        tick++;

        if (tick == 60 && !finished) {
            gameTime--;
            tick = 0;
        }
    }

    public int getGameTime() {return gameTime;}

    public boolean isFinished() {return finished;}
}
