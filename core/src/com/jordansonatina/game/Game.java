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

	Texture potatoTex;
	Sprite potatoSprite;

	Texture pumpkinTex;
	Sprite pumpkinSprite;

	Texture menuTex;
	Sprite menuSprite;

	SpriteBatch batch;

	private boolean slicing;

	public static Sound throwSound;

	public static Sound combo3Sound;

	public static Sound criticalSound;
	public static Sound sliceSound;
	public static Sound timeUpSound;


	public String directory;

	private int waveTimer;
	private int timeBetweenWaves;

	private int timeFrameForCombo;
	private int comboTimer;
	private int combo;

	private int lastCombo;
	private boolean comboStarted;

	public static int score;
	public static int highScore;

	public static Timer timer;

	public static int GAMESTATE;

	public Vector2 mousePos;

	FreeTypeFontGenerator generator;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter;

	BitmapFont font;

	ArrayList<Animation> animations;

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

		potatoTex = new Texture(Gdx.files.internal(directory + "/veggies/potato.png"));
		potatoSprite = new Sprite(potatoTex);

		pumpkinTex = new Texture(Gdx.files.internal(directory + "/veggies/pumpkin.png"));
		pumpkinSprite = new Sprite(pumpkinTex);
	}

	private void loadSounds()
	{
		throwSound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/throw.wav"));
		combo3Sound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/combo-3.wav"));
		criticalSound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/Critical.wav"));
		sliceSound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/Clean-Slice-1.wav"));
		timeUpSound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/time-up.wav"));
	}

	@Override
	public void create () {
		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		wave = new Wave();
		blade = new Blade();
		animations = new ArrayList<>();
		comboStarted = false;
		waveTimer = 0;

		timer = new Timer(60);


		timeBetweenWaves = 200;

		timeFrameForCombo = 10;

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

		if (GAMESTATE == Constants.PLAYING) {
			timer.countDown();

			if (score > highScore)
				highScore = score;

										// game timer
			if (wave.isFinished() && !timer.isFinished()) {
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


			// UI for SCORING
			cabbageSprite.setSize(100, 100);
			cabbageSprite.setColor(1f, 1f, 1f, 1f);
			cabbageSprite.setPosition(0, Constants.HEIGHT-100);
			cabbageSprite.draw(batch);
			font.setColor(new Color(0.9f, 0.9f, 0.3f, 1f));
			font.draw(batch, String.valueOf(score), 100, Constants.HEIGHT-10);
			font.getData().setScale(0.5f);
			font.setColor(new Color(0f, 0.9f, 0f, 1f));
			font.draw(batch, String.valueOf(highScore), 110, Constants.HEIGHT-100);

			// combo animations
			for (int i = 0; i < animations.size(); i++)
			{
				Animation temp = animations.get(i);

				if (!temp.isDone())
				{
					if (temp instanceof ComboAnimation)
					{
						temp.time();
						if (lastCombo == 3)
							font.setColor(new Color(1f, 1f, 0, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));
						else if (lastCombo == 4)
							font.setColor(new Color(1f, 0.8f, 0, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));
						else if (lastCombo == 5)
							font.setColor(new Color(1f, 0.6f, 0, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));
						else if (lastCombo == 6)
							font.setColor(new Color(1f, 0.4f, 0, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));
						else if (lastCombo == 7)
							font.setColor(new Color(1f, 0.2f, 0, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));
						font.getData().setScale((0.001f+temp.getAnimationTimer())/(float)temp.getAnimationLasts());
						font.draw(batch, lastCombo + " veggie combo", Constants.WIDTH/2, Constants.HEIGHT/2);

					} else if (temp instanceof CriticalAnimation)
					{
						temp.time();
						font.setColor(new Color(0f, 1f, 1f, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));
						font.getData().setScale((0.001f+temp.getAnimationTimer())/(float)temp.getAnimationLasts());
						font.draw(batch, "critical \n   +10", Constants.WIDTH/2, Constants.HEIGHT/2);


					}

				} else {
					animations.remove(i);
					i--;
				}
			}

			// set everything back to normal for other font
			font.getData().setScale(1f);
			font.setColor(Color.WHITE);
			// draw the game time counting down
			drawTimer();

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

	private void drawTimer()
	{
		font.draw(batch, String.valueOf(timer.getGameTime()), Constants.WIDTH-100, Constants.HEIGHT-10);
	}

	private void determineCombo()
	{
		lastCombo = combo;
		score += combo * 2;
		animations.add(new ComboAnimation());
		combo3Sound.play();

	}

	private void updateAndDrawFruit()
	{
		if (comboStarted)
		{
			timeCombo();
			if (comboTimer > timeFrameForCombo - 1)
			{
				if (combo >= 3)
					determineCombo();
				comboStarted = false;
				comboTimer = 0;
				combo = 0;
			}
		}

		if (wave.isPumpkinTime())
		{
			Constants.GRAVITY = -0.002f;
			wave.startPumpkinTimer();
		}

		for (Veggie f : wave.getVeggie()) {

			f.update();


			float distanceFromCursorToFruit = (float)Math.sqrt(Math.pow((f.getPos().x+f.getRadius()) - mousePos.x, 2) + Math.pow((f.getPos().y+f.getRadius()) - (720-mousePos.y), 2));

			if (slicing && distanceFromCursorToFruit <= f.getRadius()+20 && !f.getType().equals("Sliced"))
			{
				if (f.getType().equals("Pumpkin"))
				{
					//score++;
					//sliceSound.play();
					wave.setPumpkinTime(true);

				} else {
					Constants.GRAVITY = -0.25f;
					wave.slice(f);

					if (f.isCritical()==1) {
						animations.add(new CriticalAnimation());
						criticalSound.play();
						score += 10;
					}
					else {
						sliceSound.play();
						score++;
					}

					comboStarted = true;
					comboTimer = 0;
					combo++;

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
			} else if (f.getType().equals("Potato"))
			{
				// Potato ___________________________
				// cast shadow on wall
				potatoSprite.setSize(f.getRadius() * 2, f.getRadius() * 2);
				potatoSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				potatoSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				potatoSprite.draw(batch);

				potatoSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				potatoSprite.setPosition(f.getPos().x, f.getPos().y);
				potatoSprite.draw(batch);
			} else if (f.getType().equals("Pumpkin"))
			{
				// Pumpkin ___________________________
				// cast shadow on wall
				pumpkinSprite.setSize(f.getRadius() * 3, f.getRadius() * 3);
				pumpkinSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				pumpkinSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				pumpkinSprite.draw(batch);

				pumpkinSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				pumpkinSprite.setPosition(f.getPos().x, f.getPos().y);
				pumpkinSprite.draw(batch);
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
