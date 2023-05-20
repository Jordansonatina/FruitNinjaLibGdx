package com.jordansonatina.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {

	ShapeRenderer renderer;
	Wave wave;
	Blade blade;

	Texture backgroundTex;
	Sprite backgroundSprite;
	Texture cabbageTex;
	Sprite cabbageSprite;

	Texture bellPepperTex;
	Sprite bellPepperSprite;

	Texture menuTex;
	Sprite menuSprite;

	SpriteBatch batch;

	private boolean slicing;

	public static Sound throwSound;

	public static Sound combo3Sound;

	public String directory;

	private int waveTimer;
	private int timeBetweenWaves;

	private int timeFrameForCombo;
	private int comboTimer;
	private int combo;

	private int lastCombo;
	private boolean comboStarted;

	public static int score;

	public static int GAMESTATE;

	public Vector2 mousePos;

	FreeTypeFontGenerator generator;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter;

	BitmapFont font;

	ArrayList<ComboAnimation> comboAnimation;

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
		combo3Sound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/combo-3.wav"));
	}

	@Override
	public void create () {
		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		wave = new Wave();
		blade = new Blade();
		comboAnimation = new ArrayList<>();
		comboStarted = false;
		waveTimer = 0;
		timeBetweenWaves = 300;



		timeFrameForCombo = 30;
		comboTimer = 0;
		combo = 0;
		lastCombo = 0;

		score = 0;

		slicing = false;

		directory = System.getProperty("user.dir");

		generator = new FreeTypeFontGenerator(Gdx.files.internal(directory+"/fonts/go3v2.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 75;
		parameter.borderColor = Color.DARK_GRAY;
		parameter.borderWidth = 3;
		parameter.shadowColor = new Color(0f, 0f, 0f, 0.35f);
		parameter.shadowOffsetX = 10;
		parameter.shadowOffsetY = 10;

		font = generator.generateFont(parameter); // font size 12 pixels




		GAMESTATE = Constants.MENU;

		loadTextures();
		loadSounds();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());


		slicing = Gdx.input.isButtonPressed(0);

		//timeCombo();

		if (GAMESTATE == Constants.PLAYING) {
			if (wave.isFinished()) {
				waveTimer(); // count down for next wave
				if (timeForNextWave()) {
					wave.resetWave();
				}
			} else {
				wave.throwFruit();
			}
			// draw fruit background and score
			batch.begin();
			backgroundSprite.draw(batch);
			updateAndDrawFruit();
			font.draw(batch, "Score: " + score, 5, Constants.HEIGHT);

			// combo animations
			for (int i = 0; i < comboAnimation.size(); i++)
			{
				ComboAnimation temp = comboAnimation.get(i);

				if (!temp.isDone())
				{
					temp.time();
					font.setColor(Color.YELLOW);
					font.draw(batch, "COMBO " + lastCombo, temp.getPos().x-150, temp.getPos().y);
					font.setColor(Color.WHITE);
				} else {
					comboAnimation.remove(i);
					i--;
				}

			}

			batch.end();

		} else if (GAMESTATE == Constants.MENU)
		{
			boolean hovering = mousePos.x >= 807 && mousePos.x <= 1046 && mousePos.y >= 272 && mousePos.y <= 348;
			// check to see if player hits "play"

			if (hovering && Gdx.input.justTouched())
				GAMESTATE = Constants.PLAYING;


			batch.begin();
			menuSprite.draw(batch);
			batch.end();
		}

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		updateBladePositionAndDraw();
		renderer.end();


	}
	private void waveTimer()
	{
		waveTimer++;
		if (waveTimer > timeBetweenWaves)
			waveTimer = 0;
	}

	private void timeCombo()
	{
		comboTimer++;
		if (comboTimer > timeFrameForCombo)
			comboTimer = 0;
	}

	private boolean timeForNextWave()
	{
		return (waveTimer == timeBetweenWaves-1);
	}

	private void updateBladePositionAndDraw()
	{
			blade.setPrevPos(new Vector2(mousePos.x, Constants.HEIGHT - mousePos.y));
		blade.setPos(new Vector2(mousePos.x, Constants.HEIGHT - mousePos.y));

		renderer.rectLine(blade.getPrevPos(), blade.getPos(), 10);
	}


	private void updateAndDrawFruit()
	{
		if (comboStarted)
		{
			timeCombo();
			if (comboTimer > timeFrameForCombo - 1)
			{
				comboStarted = false;
				comboTimer = 0;
				combo = 0;
			}
		}

		for (Veggie f : wave.getVeggie()) {

			f.update();

			System.out.println(combo);

			float distanceFromCursorToFruit = (float)Math.sqrt(Math.pow((f.getPos().x+f.getRadius()) - mousePos.x, 2) + Math.pow((f.getPos().y+f.getRadius()) - (720-mousePos.y), 2));

			if (slicing && distanceFromCursorToFruit <= f.getRadius() && !f.getType().equals("Sliced"))
			{
				wave.slice(f);

				comboStarted = true;
				combo++;
				score++;

				switch (combo)
				{
					case 3:
						lastCombo = 3;
						comboAnimation.add(new ComboAnimation(new Vector2(mousePos.x, Constants.HEIGHT - mousePos.y)));
						//combo3Sound.play();

				}
			}

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
		generator.dispose();
	}
}
