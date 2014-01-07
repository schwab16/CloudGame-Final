package com.jerhis.cloudgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	 
    SpriteBatch batch;
	BitmapFont font;
	Preferences prefs;
	CloudGame g;
	boolean debug = false;
 
	public void create() {
		
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		font.setColor(new Color(0,0,0,1));
		font.setScale(2f);
		this.prefs = Gdx.app.getPreferences(".cloudgame");
		g = new CloudGame(this);
		this.setScreen(new SplashScreen(this));
	}
 
	public void render() {
		super.render(); // important!
	}
 
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
 
}