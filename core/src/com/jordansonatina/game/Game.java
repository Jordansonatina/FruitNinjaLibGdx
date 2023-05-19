package com.jordansonatina.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

	ShapeRenderer renderer;

	Wave wave;

	private int tick;
	private int timeBetweenWaves;

	@Override
	public void create () {
		renderer = new ShapeRenderer();
		wave = new Wave();
		tick = 0;
		timeBetweenWaves = 300;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		clock();

		wave.removeOutOfBoundsFruit();

		if (wave.isFinished() && timeForNextWave())
			wave.resetWave();
		else
			wave.throwFruit();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		updateAndDrawFruit();
		renderer.end();

	}

	private void clock()
	{
		tick++;
		if (tick > timeBetweenWaves)
			tick = 0;
	}

	private boolean timeForNextWave()
	{
		return (tick == timeBetweenWaves-1);
	}

	private void updateAndDrawFruit()
	{
		for (Fruit f : wave.getFruit()) {
			f.update();
			renderer.circle(f.getPos().x, f.getPos().y, f.getRadius());
		}
	}

	@Override
	public void dispose () {
		renderer.dispose();
	}
}
