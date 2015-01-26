package com.pfs.squareroot;

import com.pfs.squareroot.Tile.TileType;

public class Tile2x1 extends Tile {

	public Tile2x1(float topleftx, float toplefty) {
		super(2,1,DEFAULTDEPTH, topleftx, toplefty);
		moveTo(topleftx, toplefty);
		tileobj.setTexture(Textures.wood2x1ti);
		tiletype = TileType.TILE2X1;
	}

}
