package com.jordansonatina.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
public class Game extends ApplicationAdapter {

	ShapeRenderer renderer;
	Wave wave;
	Blade blade;

	Texture backgroundTex;
	Sprite backgroundSprite;

	Texture playButtonTex;
	Sprite playButtonSprite;

	Texture arcadePlayButtonTex;
	Sprite arcadePlayButtonSprite;

	Texture gamemodesScreenTex;
	Sprite gamemodesScreenSprite;
	Texture cabbageTex;
	Sprite cabbageSprite;

	Texture bellPepperTex;
	Sprite bellPepperSprite;

	Texture potatoTex;
	Sprite potatoSprite;

	Texture artichokeTex;
	Sprite artichokeSprite;

	Texture eggplantTex;
	Sprite eggplantSprite;

	Texture frenzyEggplantTex;
	Sprite frenzyEggplantSprite;

	Texture pumpkinTex;
	Sprite pumpkinSprite;

	Texture menuTex;
	Sprite menuSprite;

	SpriteBatch batch;

	private boolean slicing;

	private int checkerForSlicing = 0;

	public static Sound throwSound;

	public static Sound combo3Sound;

	public static Sound criticalSound;
	public static Sound sliceSound;

	public static Sound frenzySound;
	public static Sound timeUpSound;


	public String directory;

	private int waveTimer;
	private int timeBetweenWaves;

	private int timeFrameForCombo;
	private int comboTimer;
	private int combo;

	private int lastCombo;
	private boolean comboStarted;

	private Vector2 previousMousePosition;

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
		// UI TEXTURES
		backgroundTex = new Texture(Gdx.files.internal(directory + "/ui/background.png"));
		backgroundSprite = new Sprite(backgroundTex);

		menuTex = new Texture(Gdx.files.internal(directory + "/ui/menuscreen.png"));
		menuSprite = new Sprite(menuTex);

		playButtonTex = new Texture(Gdx.files.internal(directory + "/ui/playbutton.png"));
		playButtonSprite = new Sprite(playButtonTex);

		arcadePlayButtonTex = new Texture(Gdx.files.internal(directory + "/ui/arcadePlayButton.png"));
		arcadePlayButtonSprite = new Sprite(arcadePlayButtonTex);

		gamemodesScreenTex = new Texture(Gdx.files.internal(directory + "/ui/gamemodes.png"));
		gamemodesScreenSprite = new Sprite(gamemodesScreenTex);

		// ----------------------

		cabbageTex = new Texture(Gdx.files.internal(directory + "/veggies/cabbage.png"));
		cabbageSprite = new Sprite(cabbageTex);

		bellPepperTex = new Texture(Gdx.files.internal(directory + "/veggies/bellpepper.png"));
		bellPepperSprite = new Sprite(bellPepperTex);

		potatoTex = new Texture(Gdx.files.internal(directory + "/veggies/potato.png"));
		potatoSprite = new Sprite(potatoTex);

		artichokeTex = new Texture(Gdx.files.internal(directory + "/veggies/artichoke.png"));
		artichokeSprite = new Sprite(artichokeTex);

		eggplantTex = new Texture(Gdx.files.internal(directory + "/veggies/eggplant.png"));
		eggplantSprite = new Sprite(eggplantTex);

		frenzyEggplantTex = new Texture(Gdx.files.internal(directory + "/veggies/frenzyeggplant.png"));
		frenzyEggplantSprite = new Sprite(frenzyEggplantTex);

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
		frenzySound = Gdx.audio.newSound(Gdx.files.internal(directory+"/sounds/Bonus-Banana-Frenzy.wav"));
	}

	private void createSaveFile()
	{
		try {
			File myObj = new File("highscore.txt");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
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

		checkerForSlicing = 0;

		previousMousePosition = Vector2.Zero;

		try {
			File myObj = new File("highscore.txt");
			Scanner myReader = new Scanner(myObj);
			highScore = Integer.parseInt(myReader.nextLine());
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}



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
		createSaveFile();

		arcadePlayButtonSprite.setPosition(Constants.WIDTH/2-400, 190);
		playButtonSprite.setPosition(Constants.WIDTH/2-120, 190);



	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

		checkerForSlicing++;
		if (checkerForSlicing >= 2)
		{
			checkerForSlicing = 0;
			previousMousePosition = mousePos;
		}

		System.out.println(slicing);


		// write the highscore to savefile as needed
		if (score > highScore) {
			try {
				FileWriter myWriter = new FileWriter("highscore.txt");
				myWriter.write(String.valueOf(score));
				myWriter.close();
				//System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			GAMESTATE = Constants.MENU;
			timer.countDown();
			//timer.setFinished(false);
			animations.clear();
			resetGame();
		}


		slicing = Gdx.input.isButtonPressed(0);

		if (GAMESTATE == Constants.PLAYINGARCADE) {
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
			font.draw(batch, String.valueOf(score), 120, Constants.HEIGHT-10);
			parameter.size = 2;
			//font.getData().setScale(0.5f);
			font.setColor(new Color(0f, 0.9f, 0f, 1f));
			font.draw(batch, "Best: " + highScore, 5, Constants.HEIGHT-100);

			// combo animations
			for (int i = 0; i < animations.size(); i++)
			{
				Animation temp = animations.get(i);

				if (!temp.isDone())
				{
					if (temp instanceof ComboAnimation)
					{
						temp.time();

						// font gets redder the higher the combo was
						font.setColor(new Color(1f, 1f - (float)(lastCombo-3)/5, 0, 1f-(temp.getAnimationTimer()/(float)temp.getAnimationLasts())));

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
			float deltaX = mousePos.x - (playButtonSprite.getX()+playButtonSprite.getWidth()/2);
			float deltaY = (720-mousePos.y) - (playButtonSprite.getY() + playButtonSprite.getHeight()/2);
			float distance = (float)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

			// check to see if player hits "play game"
			if (slicing && distance <= playButtonSprite.getWidth()/2) {
				sliceSound.play();
				GAMESTATE = Constants.GAMEMODES;
			}

			batch.begin();
			menuSprite.draw(batch);
			playButtonSprite.draw(batch);
			batch.end();
		} else if (GAMESTATE == Constants.GAMEMODES)
		{
			// mouse distance from arcade button
			float deltaXArcade = mousePos.x - (arcadePlayButtonSprite.getX()+arcadePlayButtonSprite.getWidth()/2);
			float deltaYArcade = (720-mousePos.y) - (arcadePlayButtonSprite.getY() + arcadePlayButtonSprite.getHeight()/2);
			float distanceArcade = (float)Math.sqrt(Math.pow(deltaXArcade, 2) + Math.pow(deltaYArcade, 2));

			// check to see if player hits "arcade mode"
			if (slicing && distanceArcade <= arcadePlayButtonSprite.getWidth()/2) {
				sliceSound.play();
				GAMESTATE = Constants.PLAYINGARCADE;
			}

			batch.begin();
			gamemodesScreenSprite.draw(batch);
			arcadePlayButtonSprite.draw(batch);
			batch.end();
		}

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		//updateBladePositionAndDraw();
		renderer.end();
	}

	private void resetGame()
	{
		timer.setGameTime(60);
		score = 0;
		wave.resetWave();
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
		for (int i = 0; i < blade.getLength()-1; i++)
		{
			blade.setPreviousPosition(blade.getPreviousPosition(i), i+1);
		}
		blade.setPreviousPosition(new Vector2(previousMousePosition.x, 720-previousMousePosition.y), blade.getLength()-1);

		for (Vector2 v : blade.getPreviousPositions())
		{
			if (v != null)
				renderer.circle(v.x, v.y, 10);
		}

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

		if (wave.isFrenzyTime())
		{
			wave.startFrenzyTimer();
		}

		for (Veggie f : wave.getVeggie()) {

			f.update();


			float distanceFromCursorToFruit = (float)Math.sqrt(Math.pow((f.getPos().x+f.getRadius()) - mousePos.x, 2) + Math.pow((f.getPos().y+f.getRadius()) - (720-mousePos.y), 2));

			if (slicing && distanceFromCursorToFruit <= f.getRadius()+20 && !f.getType().equals("Sliced"))
			{
				if (f.getType().equals("Pumpkin"))
				{
					score+=30;

					//wave.setPumpkinTime(true);

				} else if (f.getType().equals("FrenzyEggplant"))
				{
					wave.setIsFrenzyTime(true);
					frenzySound.play();
				}else {
					Constants.GRAVITY = -0.25f;

					if (f.isCritical()==1) {
						animations.add(new CriticalAnimation());
						criticalSound.play();
						score += 10;
					}
					else {
						score++;
					}

					comboStarted = true;
					comboTimer = 0;
					combo++;
				}
				sliceSound.play();
				wave.slice(f);
			}

			float distanceY = 0;//((float) Constants.HEIGHT /2 - f.getPos().y)/((float) Constants.HEIGHT /2);

			if (f.getType().equals("Cabbage")) {
				// CABBAGES ___________________________
				// cast shadow on wall
				cabbageSprite.setSize(f.getRadius() * 2f, f.getRadius() * 2f);
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
				bellPepperSprite.setSize(f.getRadius() * 3f, f.getRadius() * 3f);
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
				potatoSprite.setSize(f.getRadius() * 2.5f, f.getRadius() * 2.5f);
				potatoSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				potatoSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				potatoSprite.draw(batch);

				potatoSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				potatoSprite.setPosition(f.getPos().x, f.getPos().y);
				potatoSprite.draw(batch);
			} else if (f.getType().equals("Artichoke")) {
				// Artichoke ___________________________
				// cast shadow on wall
				artichokeSprite.setSize(f.getRadius() * 3.5f, f.getRadius() * 3.5f);
				artichokeSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				artichokeSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				artichokeSprite.draw(batch);

				artichokeSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				artichokeSprite.setPosition(f.getPos().x, f.getPos().y);
				artichokeSprite.draw(batch);
			} else if (f.getType().equals("Eggplant")) {
				// Eggplant ___________________________
				// cast shadow on wall
				eggplantSprite.setSize(f.getRadius() * 3, f.getRadius() * 3);
				eggplantSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				eggplantSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				eggplantSprite.draw(batch);

				eggplantSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				eggplantSprite.setPosition(f.getPos().x, f.getPos().y);
				eggplantSprite.draw(batch);
			} else if (f.getType().equals("FrenzyEggplant")) {
				// Eggplant ___________________________
				// cast shadow on wall
				frenzyEggplantSprite.setSize(f.getRadius() * 3, f.getRadius() * 3);
				frenzyEggplantSprite.setPosition(f.getPos().x + 15, f.getPos().y - 15);
				frenzyEggplantSprite.setColor(0.1f, 0.1f, 0.1f, 0.35f);
				frenzyEggplantSprite.draw(batch);

				frenzyEggplantSprite.setColor(1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f - distanceY + 0.2f, 1f);
				frenzyEggplantSprite.setPosition(f.getPos().x, f.getPos().y);
				frenzyEggplantSprite.draw(batch);
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
