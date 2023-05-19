package com.jordansonatina.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

	Fruit f;
	ShapeRenderer renderer;

	@Override
	public void create () {
		renderer = new ShapeRenderer();
		f = new Fruit();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		f.update();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		drawFruit();
		renderer.end();

	}

	private void drawFruit()
	{
		renderer.circle(f.getPos().x, f.getPos().y, f.getRadius());
	}

	@Override
	public void dispose () {
		renderer.dispose();
	}
}
