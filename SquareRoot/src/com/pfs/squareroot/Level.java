package com.pfs.squareroot;

import java.util.ArrayList;
import java.util.List;

import android.graphics.RectF;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class Level {
	int width, height;
	private Tile roottile;
	Object3D box;
	RectF boxrect;
	SimpleVector center;
	private List<Tile> tiles;
	
	public Level(int levelnum) {
		// TODO Auto-generated constructor stub
		createTiles(levelnum);
		createBox();
	}

	private void createBox() {
		boxrect = new RectF(0.99f,0.99f, 6.01f, 5.01f);
		center = new SimpleVector(boxrect.centerX(), boxrect.centerY(), 0);
	}

	private void createTiles(int levelnum) {
		tiles = new ArrayList<Tile>();
		if(levelnum == 1){
			width = 5;
			height = 4;
			roottile = Tile.createRootTile(2,2,1,2);
			tiles.add(roottile);
			tiles.add(Tile.create2x1Tile(1,1));
			tiles.add(Tile.create2x1Tile(3,1));
			tiles.add(Tile.create2x1Tile(1,4));
			tiles.add(Tile.create2x1Tile(3,4));
			
			tiles.add(Tile.create1x2Tile(3,2));

			tiles.add(Tile.create1x1Tile(5,1));
			tiles.add(Tile.create1x1Tile(4,2));
			tiles.add(Tile.create1x1Tile(4,3));
			tiles.add(Tile.create1x1Tile(5,4));
		}
	}

	public Tile getTile(SimpleVector position) {
		int size = tiles.size();
		for(int i=0; i<size; i++){
			Tile tile = tiles.get(i);
			if(tile.checkTouch(position.x, position.y))
				return tile;
		}
		return null;
	}

	public List<Tile> getIntersectingTiles(RectF rect, Tile intile) {
		List<Tile> outtiles = new ArrayList<Tile>();
		int size = tiles.size();
		for(int i=0; i<size; i++){
			Tile tile = tiles.get(i);
			if(tile != intile && tile.checkIntersect(rect))
				outtiles.add(tile);
		}
		return outtiles;
	}

}
