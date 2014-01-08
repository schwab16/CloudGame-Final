package com.jerhis.cloudgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.jerhis.cloudgame.game.*;

public class GameScreen implements Screen, InputProcessor {
	
	final MyGdxGame game;
	OrthographicCamera camera;

	TextureAtlas textures;//, textures2;
	AtlasRegion bg, ready, finish, pause, pauseIcon, blackOverlay, rain, 
		basic0, super0, redChaser, sceneryClouds[] = new AtlasRegion[16], 
		blue[] = new AtlasRegion[31]; //yellow[] = new AtlasRegion[10];
	State state;
	//Texture splash;
	 
	public GameScreen(final MyGdxGame gam) {
		game = gam;
		Gdx.input.setInputProcessor(this);
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		textures = new TextureAtlas("gameimages.txt");
		bg = textures.findRegion("background");
		pause = textures.findRegion("pause");
		pauseIcon = textures.findRegion("pauseicon");
		finish = textures.findRegion("finish");
		ready = textures.findRegion("ready");
		blackOverlay= textures.findRegion("blackoverlay");
		rain= textures.findRegion("rain");
		
		basic0 = textures.findRegion("basic0");
		super0 = textures.findRegion("super");
		redChaser = textures.findRegion("guyred");
		
		for (int k = 1; k < 16; k++) {
			sceneryClouds[k] = textures.findRegion("clsc" + k);
		}
		sceneryClouds[0] = pauseIcon;
		//sceneryClouds[1] = pauseIcon;
		
		for (int k = 1; k < 31; k++) {
			blue[k] = textures.findRegion("blue" + k + " copy");
			//yellow[k] = textures.findRegion("yellow" + k);
		}
		//textures2 = new TextureAtlas("gameimages2.txt");
		//splash = new Texture(Gdx.files.internal("splash.png"));
		
		state = State.Ready;
		
		//highScore = game.prefs.getInteger("best", 0);
		//game.prefs.putInteger("best", 100);
		//game.prefs.flush(); <--- IMPORTANT!
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.86f, 0.86f, 0.86f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		stateRender(delta);
		stateDraw(delta);
		//game.setScreen(new GenericScreen(game));
	}
 
	private void stateRender(float delta) {
		switch (state) {
		case Finished:
			break;
		case Paused:
			break;
		case Ready:
			break;
		case Running:
			state = game.g.update(delta);
			break;
		default:
			break;
		}
	}
	
	private void stateDraw(float delta) {
		game.batch.begin();
		game.batch.draw(bg, game.g.bg1, 0);
		game.batch.draw(bg, game.g.bg2, 0);
		
		//public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
		//float scaleX, float scaleY, float rotation) {
		//0, 0, 10, 10, 4, 4, 0

		for (Tile tile: game.g.tiles) {
			switch (tile.type) {
			case Scenery: 
			case becomeScenery:	
				if (tile.bit > 0) game.batch.draw(sceneryClouds[(int) tile.bit], tile.x - 20, tile.y - 20 - game.g.height); break;
			case Default: 
				game.batch.draw(basic0, tile.x - 21, tile.y - 21 - game.g.height); 
				if (tile.bit > 0 && tile.bit <= 30) game.batch.draw(blue[(int) (tile.bit)], tile.x - 20, tile.y - 20 - game.g.height); break;
			case Super: 
				game.batch.draw(super0, tile.x - 21, tile.y - 21 - game.g.height); 
				if (tile.bit > 0 && tile.bit <= 30) game.batch.draw(blue[(int) (tile.bit)], tile.x - 20, tile.y - 20 - game.g.height); break;
			//game.batch.draw(ready, oS(tile.x - 20), tile.y - 20); 
			//default: game.batch.draw(pauseIcon, tile.x - 20, tile.y - 20 - game.g.height); break;
			case Removable: break;
			}
		}
		
		for (GameObject go: game.g.gameObjects) {
			switch (go.objectType) {
			case Raindrop: 
				Raindrop r = (Raindrop)go; 
				game.batch.draw(rain, r.x-20, r.y-20 - game.g.height);
				break;
			}
			
		}
		
		game.batch.draw(redChaser, game.g.guy.x - 15, game.g.guy.y - 15- game.g.height);
		
		game.font.draw(game.batch, "Score: " + (int)game.g.score, 10, 470);
		game.font.draw(game.batch, "Best: " + game.g.highScore, 10, 440);
		if (game.debug) game.font.draw(game.batch, "FPS: " + (int)(1/game.g.lastDelta) + " H: " + (int)game.g.height/1000, 10, 410);
		
		switch (state) {
		case Running:
			game.batch.draw(pauseIcon,800-50,480-50);
			break;
		case Finished:
			game.batch.draw(finish, 0, 0);
			break;
		case Paused:
			game.batch.draw(pause, 0, 0);
			break;
		case Ready:
			game.batch.draw(ready, 0, 0, 0, 0, 200, 120, 4, 4, 0);
			break;
		default:
			break;
		}
		game.batch.end();
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
		if (state == State.Running)
			state = State.Paused;
	}
 
	@Override
	public void resume() {
	}
 
	@Override
	public void dispose() {
		textures.dispose();
		//textures2.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		switch (state) {
		case Finished:
			game.g.clear();
			state = State.Ready;
			break;
		case Paused:
			state = State.Running;
			break;
		case Ready:
			state = State.Running;
			break;
		case Running:
			if (x > 800-50 && y > 480 - 50)
				state = State.Paused;
			else game.g.touchDown(x, y, pointer);
			break;
		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		switch (state) {
		case Finished:
			break;
		case Paused:
			if (x < 50 && y > 430) game.debug = !game.debug;
			break;
		case Ready:
			break;
		case Running:
			game.g.touchUp(x, y, pointer);
			break;
		default:
			break;
		}
				
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		camera.unproject(pos);
		int x = (int) pos.x, y = (int) pos.y;
		
		switch (state) {
		case Finished:
			break;
		case Paused:
			break;
		case Ready:
			break;
		case Running:
			game.g.touchDragged(x, y, pointer);
			break;
		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {return false;}
	@Override
	public boolean keyUp(int keycode) {return false;}
	@Override
	public boolean keyTyped(char character) {return false;}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	@Override
	public boolean scrolled(int amount) {return false;}
	
	enum State {
		Running, Paused, Finished, Ready
	}
	
}
