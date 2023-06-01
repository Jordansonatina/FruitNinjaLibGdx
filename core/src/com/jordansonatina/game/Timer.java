package com.jordansonatina.game;

public class Timer {
    private int gameTime;

    private boolean finished;

    private boolean stopped;

    private int tick;

    public Timer(int gameTime)
    {
        this.gameTime = gameTime;
        stopped = false;
        tick = 0;
        finished = false;
    }

    public void countDown()
    {
        if (!stopped)
        {
            if (gameTime == -1) {
                finished = true;
                Game.timeUpSound.play();
                gameTime = 0;
            }

            tick++;

            if (tick == 60 && !finished) {
                gameTime--;
                tick = 0;
            }
        }
    }

    public int getGameTime() {return gameTime;}
    public void setGameTime(int n) {gameTime = n;}

    public void setStopped(boolean n) {stopped = n;}

    public boolean isFinished() {return finished;}
    public void setFinished(boolean n){finished = n;}
}
