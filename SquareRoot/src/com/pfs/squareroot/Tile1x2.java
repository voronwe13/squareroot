package com.pfs.squareroot;

public class Tile1x2 extends Tile {

	public Tile1x2(float topleftx, float toplefty) {
		super(1,2,DEFAULTDEPTH, topleftx, toplefty);
		moveTo(topleftx, toplefty);
		tileobj.setTexture(Textures.wood1x2ti);
	}

}
