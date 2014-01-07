package com.jerhis.cloudgame.game;

public class Guy {
	
	public float x,y;
	public float velY, velX;
	public boolean jumping= true, dead = false;
	
	public final float JUMPVELOCITY = 6, MOMENTUM = 1f, GRAVITY = 9,
			MAXMOMENTUM = 7, RESISTANCE = 0.5f, OFFSETSPEED = 10f,
			REGULAR_JUMP = 4.0f, SUPER_JUMP = 10.5f;
	
	public Guy(float x, float y){
		this.x = x; 
		this.y = y;
	}
	
	public Guy(){
		this(400,150);
	}
	
	public void update(float delta, boolean right, boolean left, float height) {
		velY -= delta * GRAVITY;
		if (!right && !left)
			velX = velX > 0 ? velX - RESISTANCE + 0.01f : velX + RESISTANCE - 0.01f;
		velX = velX >= -RESISTANCE && velX <= RESISTANCE ? 0 : velX;
		
		velX = right ? velX + MOMENTUM : velX;
		velX = left ? velX - MOMENTUM : velX;
		
		velX = velX > MAXMOMENTUM ? MAXMOMENTUM : velX;
		velX = velX < -MAXMOMENTUM ? -MAXMOMENTUM : velX;
		
		x += velX;
		y += velY;
		
		if (x < 0) x = 800;
		if (x > 800) x = 0;
				
		if (y < -40 + height) dead = true;
	}

}
