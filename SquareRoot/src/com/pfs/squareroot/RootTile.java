package com.pfs.squareroot;

public class RootTile extends Tile {
	//For now, there can only be one root, so this is set up as a singleton.
	private static RootTile roottile;
	
	private RootTile(float width, float height, float depth, float x, float y) {
		super(width, height, depth, x, y);
		tileobj.setTexture(Textures.wood2x2ti);
		
	}
	
	public static RootTile getRootTile(float width, float height, float x, float y){
		if(roottile == null || width != roottile.width || height != roottile.height
				|| x != roottile.originalx || y != roottile.originaly)
			roottile = new RootTile(width,height, DEFAULTDEPTH/2, x, y);
		roottile.moveTo(x, y);
		return roottile;
	}

}
