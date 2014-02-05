package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen, InputProcessor {
	
	final MyGdxGame game;
	OrthographicCamera camera;

	TextureAtlas textures;
	AtlasRegion bg, controlTilt, controlTouch;
	
	boolean leaving = false;
 
	public MainMenuScreen(final MyGdxGame gam) {
		game = gam;
		Gdx.input.setInputProcessor(this);
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		textures = new TextureAtlas("menuimages.txt");
		bg = textures.findRegion("menubg");
		controlTilt = textures.findRegion("controltilt");
		controlTouch = textures.findRegion("controltouch");
		
		//highScore = game.prefs.getInteger("best", 0);
		//game.prefs.putInteger("best", (int)GameDisplay.guy.currentScore);
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		game.batch.begin();
		//game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
		game.batch.draw(bg, 0, 0);
		if (game.tiltControls)
			game.batch.draw(controlTilt, 800-240, 0);
		else game.batch.draw(controlTouch, 800-240, 0);
		game.batch.end();
		
		if (leaving)
			game.setScreen(new GameScreen(game));
		
	}
 
	@Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void show() {
	}
 
	@Override
	public void hide() {
	}
 
	@Override
	public void pause() {
	}
 
	@Override
	public void resume() {
	}
 
	@Override
	public void dispose() {
		textures.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		if (x > 800-240 && y < 200 && y > 100) {
			game.setControls(true);
		}
		else if (x > 800-240 && y <= 100) {
			game.setControls(false);
		}
		else if (x < 800-250 || y > 250)
			leaving = true;
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
				
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
	