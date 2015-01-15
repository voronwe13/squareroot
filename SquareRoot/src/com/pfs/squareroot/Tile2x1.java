package com.pfs.squareroot;

public class Tile2x1 extends Tile {

	public Tile2x1(float topleftx, float toplefty) {
		super(2,1,DEFAULTDEPTH, topleftx, toplefty);
		moveTo(topleftx, toplefty);
		tileobj.setTexture(Textures.wood2x1ti);
	}

}
