package com.jerhis.cloudgame.game;

public class Raindrop extends GameObject{
	
	public float x,y,yVel;
	
	public Raindrop(float xx, float yy) {
		super(GameObject.ObjectType.Raindrop);
		x=xx;
		y=yy;
		yVel=0;
	}
	
	public void update(float delta) {
		yVel += delta * 10;
		y -= yVel;
		if (y<-40) gone = true;
	}
	
}
