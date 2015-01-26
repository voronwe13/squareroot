package com.pfs.squareroot;

public class Tile1x1 extends Tile {

	public Tile1x1(float topleftx, float toplefty) {
		super(1,1,DEFAULTDEPTH, topleftx, toplefty);
		moveTo(topleftx, toplefty);
		tileobj.setTexture(Textures.wood1x1ti);
		tiletype = TileType.TILE1X1;
	}

}
