package com.pfs.squareroot;

import java.util.ArrayList;
import java.util.List;

import android.graphics.RectF;
import android.util.Log;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class Level {
	private static final String TAG = "Level";
	int width, height;
	float winleft, wintop;
	RootTile roottile;
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
		float insideleft = 0.99f;
		float insideright = 6.01f;
		float insidetop = 0.99f;
		float insidebottom = 5.01f;
		float borderwidth = 0.15f;
		float depth = Tile.DEFAULTDEPTH + 0.1f;
		boxrect = new RectF(insideleft,insidetop, insideright, insidebottom);
		center = new SimpleVector(boxrect.centerX(), boxrect.centerY(), 0);
		Object3D boxbase = Tile.createBox(boxrect.width()+borderwidth*1.1f, boxrect.height()+borderwidth*2, 0.1f, 0);
		boxbase.setOrigin(new SimpleVector(insideleft - borderwidth, insidetop - borderwidth, 0.1));
		boxbase.setTexture("woodbox");
		boxbase.setShader(Shaders.phong);
		boxbase.setSpecularLighting(true);		
		boxbase.build();
		
		Object3D boxtop = Tile.createBox(boxrect.width()+borderwidth*2, borderwidth, depth, 0);
		boxtop.setOrigin(new SimpleVector(insideleft - borderwidth, insidetop - borderwidth, 0));
		boxtop.setTexture("woodbox");
		boxtop.setShader(Shaders.phong);
		boxtop.setSpecularLighting(true);		
		boxtop.build();
		
		Object3D boxbottom = Tile.createBox(boxrect.width()+borderwidth*2, borderwidth, depth, 0);
		boxbottom.setOrigin(new SimpleVector(insideleft - borderwidth, insidebottom, 0));
		boxbottom.setTexture("woodbox");
		boxbottom.setShader(Shaders.phong);
		boxbottom.setSpecularLighting(true);		
		boxbottom.build();
		
		Object3D boxleft = Tile.createBox(borderwidth, boxrect.height(), depth, 0);
		boxleft.setOrigin(new SimpleVector(insideleft - borderwidth, insidetop, 0));
		boxleft.setTexture("woodbox");
		boxleft.setShader(Shaders.phong);
		boxleft.setSpecularLighting(true);		
		boxleft.build();
		
		Object3D boxrightupper = Tile.createBox(borderwidth, boxrect.height(), depth-Tile.DEFAULTDEPTH/2, 0);
		boxrightupper.setOrigin(new SimpleVector(insideright, insidetop, -Tile.DEFAULTDEPTH/2));
		boxrightupper.setTexture("woodbox");
		boxrightupper.setShader(Shaders.phong);
		boxrightupper.setSpecularLighting(true);		
		boxrightupper.build();
		
		Object3D boxrightlowertop = Tile.createBox(borderwidth, boxrect.height()*0.5f - 1, Tile.DEFAULTDEPTH/2, 0);
		boxrightlowertop.setOrigin(new SimpleVector(insideright, insidetop, 0));
		boxrightlowertop.setTexture("woodbox");
		boxrightlowertop.setShader(Shaders.phong);
		boxrightlowertop.setSpecularLighting(true);		
		boxrightlowertop.build();
		
		Object3D boxrightlowerbottom = Tile.createBox(borderwidth, boxrect.height()*0.5f - 1, Tile.DEFAULTDEPTH/2, 0);
		boxrightlowerbottom.setOrigin(new SimpleVector(insideright, insidetop+3, 0));
		boxrightlowerbottom.setTexture("woodbox");
		boxrightlowerbottom.setShader(Shaders.phong);
		boxrightlowerbottom.setSpecularLighting(true);		
		boxrightlowerbottom.build();
		
		UI.world.addObject(boxbase);
		UI.world.addObject(boxbottom);
		UI.world.addObject(boxtop);
		UI.world.addObject(boxleft);
		UI.world.addObject(boxrightupper);
		UI.world.addObject(boxrightlowertop);
		UI.world.addObject(boxrightlowerbottom);
		
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
			
			winleft = 4;
			wintop = 2;
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
	
	public void reset(){
		int size = tiles.size();
		for(int i=0; i<size; i++){
			tiles.get(i).reset();
		}
	}

	public boolean checkWin() {
		// TODO Auto-generated method stub
		return roottile.checkAtPosition(winleft, wintop);
	}

}
