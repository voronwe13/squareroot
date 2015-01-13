package com.pfs.squareroot;

public class RootTile extends Tile {
	//For now, there can only be one root, so this is set up as a singleton.
	private static RootTile roottile;
	
	private RootTile(float width, float height, float depth) {
		super(width, height, depth);
		// TODO Auto-generated constructor stub
		
	}
	
	public static RootTile getRootTile(float width, float height){
		if(roottile == null || width != roottile.width || height != roottile.height)
			roottile = new RootTile(width,height,0.1f);
		return roottile;
	}

}
