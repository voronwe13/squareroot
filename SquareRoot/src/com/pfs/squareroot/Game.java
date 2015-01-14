package com.pfs.squareroot;

import java.util.List;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.threed.jpct.Interact2D;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Game {
	private static final String TAG = "Game";
	static Level level;
	static int currentlevelnum, movecount;
	static World world;
	static boolean tileheld;
	static Tile currenttile;
	static SimpleVector newposition = new SimpleVector(), 
			direction = new SimpleVector(), 
			position = new SimpleVector();
	static final SimpleVector normal = new SimpleVector(0,0,-1);
	static RectF testrectx = new RectF(), testrecty = new RectF();

	public static void setup(World world) {
		Game.world = world;
		currentlevelnum = 1;
		movecount = 0;
		level = new Level(currentlevelnum);
		Controls.camera.setPosition(3, 3, -8);
		Controls.camera.lookAt(new SimpleVector(3,3,0));
		tileheld = false;
	}
	
	public static void GLupdate() {
		// TODO Auto-generated method stub
		
	}
	
	public static void get3DFrom2D(float x, float y, SimpleVector tofill){
		Log.d(TAG, "touch was at screen position: "+x+", "+y);
		Interact2D.reproject2D3DWS(Controls.camera, SqRtRenderer.fb, 
				Math.round(x), Math.round(y), direction);
		float denom = direction.calcDot(normal);
		if(denom == 0){
			Log.d(TAG, "got a weird ray here... no touch assumed.");
			tofill.set(-1,-1,-1);
		}
		float dist = -(Controls.campos.calcDot(normal))/denom;
		tofill.set(direction);
		tofill.scalarMul(dist);
		tofill.add(Controls.campos);
		Log.d(TAG, "touch was at world position: "+tofill.toString());		
	}

	public static boolean checkTouch() {
		get3DFrom2D(Controls.xpos1, Controls.ypos1, position);
		currenttile = level.getTile(position);
		if(currenttile != null){
			tileheld = true;
			
		}
		return tileheld;
	}

	public static void handleTouch() {
		if(tileheld){
			get3DFrom2D(Controls.xpos1, Controls.ypos1, newposition);
			direction.set(newposition.calcSub(position));
			testrectx.set(currenttile.positionrect);
			testrectx.offset(direction.x, 0);
			testrecty.set(currenttile.positionrect);
			testrecty.offset(0, direction.y);
			List<Tile> tilesx = level.getIntersectingTiles(testrectx, currenttile);
			List<Tile> tilesy = level.getIntersectingTiles(testrecty, currenttile);
			if(!tilesx.isEmpty() || !level.boxrect.contains(testrectx))
				direction.x = 0;
			if(!tilesy.isEmpty() || !level.boxrect.contains(testrecty))
				direction.y = 0;
			currenttile.slide(direction);
			position.set(newposition);
		}
		
	}

	public static void handleRelease() {
		tileheld = false;
		if(currenttile != null)
			currenttile.snap();
	}

	public static void reset(){
		level.reset();
		movecount = 0;
		UI.updateMoveCount();
	}
	
}
