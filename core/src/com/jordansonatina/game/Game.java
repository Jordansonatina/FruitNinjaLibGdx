package com.jordansonatina.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

	ShapeRenderer renderer;
	Wave wave;

	Texture backgroundTex;
	Sprite backgroundSprite;
	Texture cabbageTex;
	Sprite cabbageSprite;

	Texture bellPepperTex;
	Sprite bellPepperSprite;

	Texture menuTex;
	Sprite menuSprite;

	SpriteBatch batch;

	public static Sound throwSound;

	public String directory;

	private int tick;
	private int timeBetweenWaves;

	public static int GAMESTATE;

	public Vector2 mousePos;

	private void loadTextures()
	{
		backgroundTex = new Texture(Gdx.files.internal(directory + "/background.jpeg"));
		backgroundSprite = new Sprite(backgroundTex);

		cabbageTex = new Texture(Gdx.files.internal(directory + "/veggies/newCabbage.png"));
		cabbageSprite = new Sprite(cabbageTex);

		bellPepperTex = new Texture(Gdx.files.internal(directory + "/veggies/bellpepper.png"));
		bellPepperSprite = new Sprite(bellPepperTex);

		menuTex = new Texture(Gdx.files.internal(directory + "/MenuScreen.jpg"));
		menuSprite = new Sprite(menuTex);
	}

	private void loadSounds()
	{
		throwSound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/throw.wav"));
	}

	@Override
	public void create () {
		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		wave = new Wave();
		tick = 0;
		timeBetweenWaves = 300;


		directory = System.getProperty("user.dir");

		GAMESTATE = Constants.MENU;

		loadTextures();
		loadSounds();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());


		if (GAMESTATE == Constants.PLAYING) {
			if (wave.isFinished()) {
				tick(); // count down for next wave
				if (timeForNextWave()) {
					wave.resetWave();
				}
			} else {
				wave.throwFruit();
			}


			batch.begin();
			backgroundSprite.draw(batch); // draw background
			updateAndDrawFruit();
			batch.end();
		} else if (GAMESTATE == Constants.MENU)
		{
			boolean hovering = false;
			// check to see if player hits play
			if (mousePos.x >= 807 && mousePos.x <= 1046 && mousePos.y >= 272 && mousePos.y <= 348 )
				hovering = true;

			if (hovering && Gdx.input.justTouched())
				GAMESTATE = Constants.PLAYING;


			batch.begin();
			menuSprite.draw(batch);
			batch.end();
		}




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

			float distanceFromCursorToFruit = (float)Math.sqrt(Math.pow((f.getPos().x+f.getRadius()) - mousePos.x, 2) + Math.pow((f.getPos().y+f.getRadius()) - (720-mousePos.y), 2));

			if (Gdx.input.isButtonPressed(0) && distanceFromCursorToFruit <= f.getRadius())
				wave.slice(f);


			//float distanceFromCenter = (float)Math.sqrt(Math.pow(Constants.WIDTH/2f-f.getPos().x, 2) + Math.pow(Constants.HEIGHT/2f-f.getPos().y, 2));
			//float normalizedDistance = 1f - distanceFromCenter / (1280/2);
			float distanceY = ((float) Constants.HEIGHT /2 - f.getPos().y)/((float) Constants.HEIGHT /2);

			if (f.getType().equals("Cabbage")) {
				// CABBAGES ___________________________
				// cast shadow on wall
				cabbageSprite.setSize(f.getRadius() * 2, f.getRadius() * 2);
				cabbageSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				cabbageSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				cabbageSprite.draw(batch);

				cabbageSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				cabbageSprite.setPosition(f.getPos().x, f.getPos().y);
				cabbageSprite.draw(batch);
				// ______________________________________
			} else if (f.getType().equals("BellPepper")) {
				// CABBAGES ___________________________
				// cast shadow on wall
				bellPepperSprite.setSize(f.getRadius() * 2, f.getRadius() * 2);
				bellPepperSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				bellPepperSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				bellPepperSprite.draw(batch);

				bellPepperSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				bellPepperSprite.setPosition(f.getPos().x, f.getPos().y);
				bellPepperSprite.draw(batch);
			}

		}
	}

	@Override
	public void dispose () {
		renderer.dispose();
		batch.dispose();
	}
}
