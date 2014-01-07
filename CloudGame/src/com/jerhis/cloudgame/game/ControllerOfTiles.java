package com.jerhis.cloudgame.game;

import java.util.ArrayList;

import com.jerhis.cloudgame.game.Tile.CollisionType;
import com.jerhis.cloudgame.game.Tile.Type;

public class ControllerOfTiles {
	
	public ArrayList<Tile> tiles;
	public int gameType;
	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	public int spawnHeight; //height where clouds first spawn
	
	public ControllerOfTiles(ArrayList<Tile> tilesDraw, int gT, ArrayList<GameObject> gos) {
		tiles = tilesDraw;
		gameObjects = gos;
		reset(gT);
	}
	
	public void reset(int gT) {
		spawnHeight = 40*12;
		gameObjects.clear();
		tiles.clear();
		gameType = gT;
		spawnStart();
	}
	
	public int updateTiles(float delta, float height) {
		int cloudsRemoved = 0;
		for (int k = 0; k < tiles.size(); k++) {       
			Tile t = tiles.get(k);                         //for each tile t
			if (t.y < height - 40) {                       //if it falls too low, remove it
				removeTile(t);
				k--;
			}
			else if (t.type == Tile.Type.Removable) {      //if it is removable (scenery), remove it
				removeTile(t);
				k--;
			}
			else if (t.type == Tile.Type.becomeScenery) {  //if it is becoming scenery
				int x1 = t.arrayX, y1 = t.arrayY;          
				removeTile(t);                                   //remove it
				addTile(x1,y1,scen);                             //add a scenery there
				gameObjects.add(new Raindrop(t.x, t.y));         //add a raindrop there
				k--;
				cloudsRemoved += 1;
			}
			else {                                          //otherwise, update it
				t.update(delta); 
			}
		}
		return cloudsRemoved;
	}
	
	public void updateCollisions(float delta, Guy guy) {
		for (Tile t: getAdjacentTiles((int)guy.x, (int)guy.y)) {
			CollisionType type = t.checkForCollision(guy);
			if (type != CollisionType.NONE) {
				boolean didHitNeighbors = t.collision(guy, type);
				if (didHitNeighbors) {
					hitNeighbors(t.arrayX,t.arrayY);
				}
			}
		}
	}
	
	private void hitNeighbors(int arrayX, int arrayY) {
		/* all plus 2
		 * 0 0 0 5 0 0 0
		 * 0 2 5 8 5 2 0
		 * 2 5 a x a 5 2
		 * 0 4 8 f 8 4 0
		 * 0 0 4 a 4 0 0
		 * 0 0 0 4 0 0 0
		 */
		for (Tile t: tiles) {
			if (t instanceof TileScenery) continue;
			if (t.arrayY == arrayY + 1) {
				if (t.arrayX == arrayX - 2) t.bit += 4; 
				else if (t.arrayX == arrayX - 1) t.bit += 7; 
				else if (t.arrayX == arrayX + 0) t.bit += 10; 
				else if (t.arrayX == arrayX + 1) t.bit += 7; 
				else if (t.arrayX == arrayX + 2) t.bit += 4; 
			}
			else if (t.arrayY == arrayY) {
				if (t.arrayX == arrayX - 3) t.bit += 4; 
				else if (t.arrayX == arrayX - 2) t.bit += 7; 
				else if (t.arrayX == arrayX - 1) t.bit += 12; 
				else if (t.arrayX == arrayX + 1) t.bit += 12; 
				else if (t.arrayX == arrayX + 2) t.bit += 7; 
				else if (t.arrayX == arrayX + 3) t.bit += 4; 
			}
			else if (t.arrayY == arrayY - 1) {
				if (t.arrayX == arrayX - 2) t.bit += 6; 
				else if (t.arrayX == arrayX - 1) t.bit += 10; 
				else if (t.arrayX == arrayX + 0) t.bit += 15; 
				else if (t.arrayX == arrayX + 1) t.bit += 10; 
				else if (t.arrayX == arrayX + 2) t.bit += 6; 
			}
			else if (t.arrayY == arrayY - 2) {
				if (t.arrayX == arrayX - 1) t.bit += 6;  
				else if (t.arrayX == arrayX + 0) t.bit += 10; 
				else if (t.arrayX == arrayX + 1) t.bit += 6;  
			}
			else if (t.arrayX == arrayX + 0 && t.arrayY == arrayY + 2) t.bit += 7; 
			else if (t.arrayX == arrayX + 0 && t.arrayY == arrayY - 3) t.bit += 5; 
			if (t.bit > 16) t.type = Type.becomeScenery;
		}
	}

	private ArrayList<Tile> getAdjacentTiles(int x, int y)
    {
        ArrayList<Tile> closeTiles = new ArrayList<Tile>();
        
        for (Tile t: tiles) {
        	if (Math.sqrt((t.x - x)*(t.x-x) + (t.y-y)*(t.y-y)) < 75)
        		closeTiles.add(t);
        }

        return closeTiles;
    }
	
	public void updateGameObjects(float delta) {
		for (int k = 0; k < gameObjects.size(); k++) {
			GameObject go = gameObjects.get(k);
			go.update(delta);
			if (go.gone) {
				gameObjects.remove(k);
				k--;
			}
		}
	}
	
	//  0 is end, 9-10 is middle, 19 is end
	private void spawnStart() {
		switch (gameType) {
		
		}
		spawnCloud(10,1,0,0); //spawns base cloud
		spawnCloud(rng(7,9),6,rng(1,2),rng(2,3)); //spawns first clouds
		spawnCloud(rng(10,12),6,rng(1,2),rng(2,3));
	}
	
	public void spawn(float totalTime, float height) {
		
		int section = 0;
		if (height > 1000) section = 1;
		if (height > 2000) section = 2;
		if (height > 3000) section = 3;
		if (height > 4000) section = 4;
		if (height > 5000) section = 5;
		if (height > 6000) section = 6;
		if (height > 7000) section = 7;
		
		if (height + 480 > spawnHeight) {
			int numClouds = rng(NUM_CLOUDS[section]);
			while (numClouds > 0) {
				int y = spawnHeight/40;
				int cloudType = rng(CLOUD_TYPE[section]);
				int supers = rng(SUPERS[section]);
				int x = rng(XLOCATION[section]);
				spawnCloud(x, y, cloudType, supers);
				numClouds--;
			}
			spawnHeight += 40 * rng(INTERVALS[section]);
		}
		
	}
	
	private void spawnCloud(int x, int y, int cloudShape, int supers) {
		float[][] cloud;
		boolean flip = cloudShape == 0 ? false : Math.random() < 0.5;
		cloud = getCloudShape(cloudShape);
		
		while (supers > 0) {
			boolean stillNeed = true;
			while (stillNeed) {
				int randA = rng(1,4);
				int randB = rng(1,7);
				if (cloud[randA][randB] > 0) 
					if (Math.random() < cloud[randA][randB]) {
						cloud[randA][randB] = SUPE;
						supers--;
						stillNeed = false;
					}
			}
		}
		
		for (int qY = 0; qY < 6; qY++)
			for (int qX = 0; qX < 9; qX++) {
				int k = flip ? -qX + 4 : qX - 4;
				addTile(x + k, y + 4 - qY, (int)cloud[qY][qX]);
			}
	}
	
	private void addTile(int x, int y, int type) {
		if (type == b___) return;
		
		Tile at = null, bt = null, lt = null, rt = null, nt;
		
		//make our new tile, nt
		switch (type) {
		case scen: nt = new TileScenery(x, y); break;
		case SUPE: nt = new TileSuper(x, y); break;
		default: case 0: case 1:
		case regu: nt = new TileBasic(x, y); break;
		}
		
		for (int k = 0; k < tiles.size(); k++) {
			Tile t = tiles.get(k);
			if (t.arrayX == x && t.arrayY == y) {
				if (t.type == Tile.Type.Scenery || t.type == Tile.Type.Removable) {
					tiles.remove(t);
					k--;
				}
				else return;
			}
			else if (t.arrayX == x + 1 && t.arrayY == y) 
				rt = t;
			else if (t.arrayX == x - 1 && t.arrayY == y) 
				lt = t;
			else if (t.arrayX == x && t.arrayY == y + 1) 
				at = t;
			else if (t.arrayX == x && t.arrayY == y - 1) 
				bt = t;
		}
		
		if (rt != null) {
			rt.leftTile = nt;
			onNeighborUpdate(rt);
		}
		if (lt != null) {
			lt.rightTile = nt;
			onNeighborUpdate(lt);
		}
		if (at != null) {
			at.belowTile = nt;
			onNeighborUpdate(at);
		}
		if (bt != null) {
			bt.aboveTile = nt;
			onNeighborUpdate(bt);
		}
		nt.initializeNeighbors(at, bt, rt, lt);
		onNeighborUpdate(nt);
		tiles.add(nt);
	}
	
	private void removeTile(Tile t) {
		if (t.aboveTile != null) {
			t.aboveTile.belowTile = null;
			onNeighborUpdate(t.aboveTile);
		}
		if (t.belowTile != null) {
			t.belowTile.aboveTile = null;
			onNeighborUpdate(t.belowTile);
		}
		if (t.leftTile != null) {
			t.leftTile.rightTile = null;
			onNeighborUpdate(t.leftTile);
		}
		if (t.rightTile != null) {
			t.rightTile.leftTile = null;
			onNeighborUpdate(t.rightTile);
		}
		tiles.remove(t);
	}
	
	public void onNeighborUpdate(Tile t) {
		int score = 0;
			//  1
			// 8X2
			//  4
		if (t.aboveTile != null && t.aboveTile.type != Tile.Type.Scenery) score += 1;
		if (t.belowTile != null && t.belowTile.type != Tile.Type.Scenery) score += 4;
		if (t.leftTile != null  && t.leftTile.type  != Tile.Type.Scenery) score += 8;
		if (t.rightTile != null && t.rightTile.type != Tile.Type.Scenery) score += 2;
		t.setNeighborScore(score);
	}
	
	//inclusive!
	private int rng(int a, int b) {
		return a + (int)(Math.random() * (b-a+1));
	}
	private int rng(int[] a) {
		return a[rng(0,a.length-1)];
	}
		
	
	private final int[][] INTERVALS = 
		  { {5,6}, //space between the clouds
			{5,6,7},
			{6,7},
			{6,7},
			{7},
			{7},
			{7},
			{7}} ;

	private final int[][] NUM_CLOUDS = 
		   {{1,1,2,2,2,2,3,3,3,3,3,3,3,3,3,3}, //a number > 0
			{1,1,1,2,2,2,2,2,3,3,3,3,3,3,3,3},
			{1,1,1,1,2,2,2,2,2,3,3,3,3,3,3,3},
			{1,1,1,1,1,2,2,2,2,2,3,3,3,3,3,3},
			{1,1,1,1,1,1,1,2,2,2,2,2,3,3,3,3},
			{1,1,1,1,1,1,1,1,1,2,2,2,2,3,3,3},
			{1,1,1,1,1,1,1,1,1,1,1,2,2,2,3,3},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,3}} ;

	private final int[][] CLOUD_TYPE = //1e 2e 3m 4ve 5em 6m 7em 8em 9mh 10ve 11m 12h 13mh 14h
		  { {1,2,4,10,5}, 
			{1,2,4,10,5,7,8},
			{5,5,7,7,8,8,4,3,6,9},
			{3,3,6,6,11,11,4,10,5,7,8},
			{3,6,11,1,2,4,10,5,7,8},
			{9,9,13,13,3,6,11,1,2,4,10,5,7,8},
			{9,13,3,6,11,1,2,4,10,5,7,8},
			{1,2,3,4,5,6,7,8,9,10,11,12,12,13,14,14}} ;

	private final int[][] SUPERS =    
		  { {2,3,3},  //4 is maximum!
			{2,2,3},
			{1,2,3},
			{1,2,2},
			{1,2},
			{1,1,2},
			{1,1,1,2},
			{1,1,1,1,2}} ;

	private final int[][] XLOCATION = 
		  { {7,8,9,9,10,10,11,12}, //3 min, 16 max
			{6,7,8,9,9,10,10,11,12,13},
			{6,7,8,9,10,11,12,13},
			{5,6,7,8,8,9,10,11,11,12,13,14},
			{5,6,7,7,8,9,10,11,12,12,13,14},
			{5,6,6,7,8,9,10,11,12,13,13,14},
			{4,5,5,6,6,7,8,9,10,11,12,13,13,14,14,15},
			{3,3,4,4,5,6,7,8,9,10,11,12,13,14,15,15,16,16}} ;
	
	
	private float[][] getCloudShape(int cloudShape) {
		switch (cloudShape) {
		case 0: return new float[][] { //starter
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,scen,b___,b___,b___,b___},
				{b___,b___,scen,SUPE,SUPE,scen,b___,b___,b___},
				{b___,b___,b___,scen,scen,b___,b___,b___,b___}};
		case 1: return new float[][] { //easy
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.3f,scen,b___,b___,b___},
				{b___,b___,scen,0.7f,1.0f,0.7f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		case 2: return new float[][] { //easy
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.3f,scen,b___,b___,b___},
				{b___,b___,scen,0.3f,0.8f,scen,b___,b___,b___},
				{b___,b___,scen,0.8f,1.0f,0.6f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		case 3: return new float[][] { //medium
				{b___,b___,b___,scen,b___,b___,b___,b___,b___},
				{b___,b___,scen,0.3f,scen,b___,b___,b___,b___},
				{b___,scen,0.4f,0.6f,0.3f,scen,b___,b___,b___},
				{b___,scen,0.7f,1.0f,0.7f,scen,b___,b___,b___},
				{b___,b___,scen,1.0f,1.0f,0.5f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		case 4: return new float[][] { //v easy
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,b___,scen,b___,b___},
				{b___,b___,scen,0.3f,scen,scen,0.3f,scen,b___},
				{b___,scen,0.6f,scen,0.5f,scen,0.7f,0.6f,scen},
				{b___,b___,scen,0.7f,1.0f,0.7f,1.0f,scen,b___},
				{b___,b___,b___,scen,scen,scen,scen,b___,b___}};
		case 5: return new float[][] { //easy - med
				{b___,b___,b___,b___,b___,b___,scen,b___,b___},
				{b___,b___,b___,b___,scen,scen,0.3f,scen,b___},
				{b___,b___,b___,scen,0.3f,0.5f,0.7f,0.5f,scen},
				{b___,b___,scen,0.6f,1.0f,scen,1.0f,scen,b___},
				{b___,b___,scen,1.0f,1.0f,0.6f,1.0f,scen,b___},
				{b___,b___,b___,scen,scen,scen,scen,b___,b___}};
		case 6: return new float[][] { //medium
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.5f,scen,b___,b___,b___},
				{b___,b___,scen,0.6f,1.0f,0.6f,scen,b___,b___},
				{b___,b___,scen,1.0f,1.0f,1.0f,0.6f,scen,b___},
				{b___,b___,b___,scen,scen,scen,scen,b___,b___}};
		case 7: return new float[][] { //easy - medium
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,b___,b___,b___,b___},
				{b___,b___,scen,0.5f,scen,scen,b___,b___,b___},
				{b___,scen,0.6f,1.0f,scen,0.5f,scen,b___,b___},
				{b___,scen,1.0f,1.0f,0.7f,1.0f,0.6f,scen,b___},
				{b___,b___,scen,scen,scen,scen,scen,b___,b___}};
		case 8: return new float[][] { //easy- medium
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,b___,b___,b___,b___},
				{b___,b___,scen,0.5f,scen,scen,b___,b___,b___},
				{b___,b___,scen,1.0f,0.7f,0.5f,scen,b___,b___},
				{b___,scen,0.7f,1.0f,1.0f,1.0f,0.7f,scen,b___},
				{b___,b___,scen,scen,scen,scen,scen,b___,b___}};
		case 9: return new float[][] { //med - hard
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,scen,b___,b___,b___,b___},
				{b___,b___,scen,0.5f,0.5f,scen,b___,b___,b___},
				{b___,scen,0.6f,1.0f,1.0f,0.6f,scen,b___,b___},
				{b___,scen,1.0f,1.0f,1.0f,1.0f,0.6f,scen,b___},
				{b___,b___,scen,scen,scen,scen,scen,b___,b___}};
		case 10: return new float[][] { //v easy
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,scen,b___,scen,b___,b___,b___},
				{b___,b___,scen,0.5f,scen,0.5f,scen,scen,b___},
				{b___,scen,0.6f,1.0f,0.8f,1.0f,0.6f,0.5f,scen},
				{b___,b___,scen,scen,scen,scen,scen,scen,b___}};
		case 11: return new float[][] { //medium
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,scen,b___,b___,b___},
				{b___,b___,scen,scen,0.5f,0.5f,scen,b___,b___},
				{b___,scen,0.5f,0.6f,1.0f,1.0f,scen,scen,b___},
				{b___,scen,1.0f,1.0f,1.0f,1.0f,0.7f,0.5f,scen},
				{b___,b___,scen,scen,scen,scen,scen,scen,b___}};
		case 12: return new float[][] { //hard
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,0.5f,scen,b___,b___,b___},
				{b___,b___,scen,0.6f,1.0f,scen,b___,b___,b___},
				{b___,b___,scen,1.0f,1.0f,0.7f,scen,scen,b___},
				{b___,scen,0.8f,1.0f,1.0f,1.0f,0.6f,0.5f,scen},
				{b___,b___,scen,scen,scen,scen,scen,scen,b___}};
		case 13: return new float[][] { //med - hard
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,scen,scen,b___,b___,b___,b___,b___},
				{b___,scen,0.5f,0.5f,scen,b___,scen,b___,b___},
				{b___,scen,1.0f,1.0f,0.6f,scen,0.5f,scen,b___},
				{scen,0.7f,1.0f,1.0f,1.0f,0.8f,1.0f,0.6f,scen},
				{b___,scen,scen,scen,scen,scen,scen,scen,b___}};
		case 14: return new float[][] { //hard
				{b___,b___,b___,scen,scen,scen,b___,b___,b___},
				{b___,b___,scen,0.5f,0.3f,0.5f,scen,b___,b___},
				{b___,scen,0.6f,1.0f,1.0f,1.0f,scen,scen,b___},
				{scen,0.6f,1.0f,1.0f,1.0f,1.0f,0.7f,0.5f,scen},
				{scen,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,scen},
				{b___,scen,scen,scen,scen,scen,scen,scen,b___}};
		case 15: return new float[][] { //easy
				{b___,b___,b___,b___,scen,scen,scen,b___,b___},
				{b___,b___,b___,scen,1.0f,1.0f,1.0f,scen,b___},
				{b___,b___,b___,b___,scen,1.0f,scen,b___,b___},
				{b___,b___,b___,scen,0.5f,scen,b___,b___,b___},
				{b___,b___,scen,0.7f,1.0f,0.7f,scen,b___,b___},
				{b___,b___,b___,scen,scen,scen,b___,b___,b___}};
		default: return new float[][] {
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,b___,b___,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___},
				{b___,b___,b___,scen,1.0f,scen,b___,b___,b___},
				{b___,b___,b___,b___,scen,b___,b___,b___,b___}};
		}
	}
	
	private final int b___ = -1;
	private final int SUPE = -2;
	private final int scen = -3;
	private final int regu = -4;

}
