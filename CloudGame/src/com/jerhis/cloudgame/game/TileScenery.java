package com.jerhis.cloudgame.game;


public class TileScenery extends Tile {

	public TileScenery(int x, int y) {
		super(x, y, 0, Tile.Type.Scenery);
	}

	@Override
	public void update(float delta) {
		if (neighborScore == 0 || neighborScore == 1)
			type = Tile.Type.Removable;
		else bit = neighborScore;
	}

	@Override
	public boolean collision(Guy guy, CollisionType type) {
		return false;
	}

	@Override
	public void setNeighborScore(int score) {
		neighborScore = score;
		bit = neighborScore;
	}

}
