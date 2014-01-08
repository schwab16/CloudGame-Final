package com.jerhis.cloudgame;

import java.util.ArrayList;

import com.jerhis.cloudgame.game.*;
import com.jerhis.cloudgame.game.Tile.CollisionType;

public class CloudGame {
	
	/*TODO:
	 * multiple raindrops
	 * spawning - actual values that are tested to be fun! or hard
	 * powerups? - point block, kill block, block destories cloud, 
	 *  - x2,super super jump, obstacles, invincibility 
	 */
	
	final MyGdxGame game;
	int gameType = 0; // there are no unique types yet
	float score; 
	int highScore;
	float bgOffSet = 0, bg1 = 0, bg2 = 0;
	float totalTime, height, lastDelta = 1.0f/60;
	boolean right, left;

	ArrayList<Tile> tiles = new ArrayList<Tile>();
	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	Guy guy;
	ControllerOfTiles controllerOfTiles = new ControllerOfTiles(tiles,gameType,gameObjects);
	
	public CloudGame(MyGdxGame gam) {
		game = gam;
		clear();
	}
	
	public GameScreen.State update(float delta) {
		totalTime += delta;
		lastDelta = delta;
		bgOffSet = ((totalTime * guy.OFFSETSPEED) % 840) - 40;
		bg1 = (int)((totalTime * 2 * 10) % 1600 - 800);
		bg2 = (int)((bg1 + 1600) % 1600 - 800);
		
		guy.update(delta, right, left, height);
		int buffer = 300;
		score = guy.y > height + buffer ? score + guy.y - height - buffer : score;
		height = guy.y > height + buffer ? guy.y - buffer : height;
		
		controllerOfTiles.updateCollisions(delta, guy);
		
		int cloudsRemoved = controllerOfTiles.updateTiles(delta, height);
		score += (cloudsRemoved)*(cloudsRemoved + 1) * 25; //combos are scored better
		controllerOfTiles.spawn(totalTime, height);
		
		controllerOfTiles.updateGameObjects(delta);
		
		if (!guy.dead)
			return GameScreen.State.Running;
		else return GameScreen.State.Finished;
	}
	
	public void touchDown(int x, int y, int pointer) {
		right = false;
		left = false;
		if (x >= 400)
			right = true;
		if (x < 400)
			left = true;
	}
	
	public void touchDragged(int x, int y, int pointer) {
		right = false;
		left = false;
		if (x >= 400) 
			right = true;
		if (x < 400)
			left = true;	
	}

	public void touchUp(int x, int y, int pointer) {
		right = false;
		left = false;
	}
	
	public void setMode(int type) {
		gameType = type;
	}
	
	public void clear() {
		highScore();
		score = 0;
		totalTime = 0;
		bgOffSet = 0;
		bg1 = 0; 
		bg2 = 0;
		height = 0;
		right = false;
		left = false;
		guy = new Guy();
		controllerOfTiles.reset(gameType);
	}
	
	public void highScore() {
		//sets high score to current highscore	
		if (score > highScore) game.prefs.putInteger(game.fileName + gameType, (int)score);
		game.prefs.flush();
		highScore = game.prefs.getInteger(game.fileName + gameType, 0);
	}

}
