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
        finished = false;
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

    public int getGameTime() {return gameTime;}
    public void setGameTime(int n) {gameTime = n;}

    public boolean isFinished() {return finished;}
    public void setFinished(boolean n){finished = n;}
}
