package com.jordansonatina.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

	ShapeRenderer renderer;
	Wave wave;

	Texture backgroundTex;
	Sprite backgroundSprite;
	Texture cabbageTex;
	Sprite cabbageSprite;

	SpriteBatch batch;

	public String directory;

	private int tick;
	private int timeBetweenWaves;

	private void loadTextures()
	{
		backgroundTex = new Texture(Gdx.files.internal(directory + "/background.jpeg"));
		backgroundSprite = new Sprite(backgroundTex);

		cabbageTex = new Texture(Gdx.files.internal(directory + "/veggies/newCabbage.png"));
		cabbageSprite = new Sprite(cabbageTex);
	}

	@Override
	public void create () {
		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		wave = new Wave();
		tick = 0;
		timeBetweenWaves = 300;

		directory = System.getProperty("user.dir");

		loadTextures();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);



		if (wave.isFinished()) {
			tick(); // count down for next wave
			if (timeForNextWave()) {
				wave.resetWave();
			}
		}
		else {
			wave.throwFruit();
		}


		batch.begin();
		backgroundSprite.draw(batch); // draw background
		updateAndDrawFruit();
		batch.end();




	}
	private void tick()
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
		for (Veggie f : wave.getVeggie()) {
			f.update();
			//float distanceFromCenter = (float)Math.sqrt(Math.pow(Constants.WIDTH/2f-f.getPos().x, 2) + Math.pow(Constants.HEIGHT/2f-f.getPos().y, 2));
			//float normalizedDistance = 1f - distanceFromCenter / (1280/2);
			float distance = (Constants.HEIGHT/2 - f.getPos().y)/(Constants.HEIGHT/2);


			// CABBAGES ___________________________
			// cast shadow on wall
			cabbageSprite.setPosition(f.getPos().x+15, f.getPos().y-15);
			cabbageSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
			cabbageSprite.draw(batch);

			cabbageSprite.setColor(1f-distance+0.2f, 1f-distance+0.2f, 1f-distance+0.2f, 1f);
			cabbageSprite.setSize(f.getRadius()*2, f.getRadius()*2);
			cabbageSprite.setPosition(f.getPos().x, f.getPos().y);
			cabbageSprite.draw(batch);
			// ______________________________________

		}
	}

	@Override
	public void dispose () {
		renderer.dispose();
		batch.dispose();
	}
}
