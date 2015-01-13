package com.pfs.squareroot;

import java.util.Random;

import android.graphics.Rect;
import android.graphics.RectF;

import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

public abstract class Tile {
	private static int texturecount = 0;
	public final static float DEFAULTDEPTH = 0.2f;
	private final static float offset = 0.0002f; //so tiles don't overlap
	private final static float snapdistance = 0.2f; //to snap tiles into grid	
	public final float width, height, depth;
	RectF positionrect;
	private float originz;
	
	public final Object3D tileobj;
	private String texturename;
	
	private static SimpleVector utility = new SimpleVector();
	
	public Tile(float width, float height, float depth){
		this.width = width;
		this.height = height;
		this.depth = depth;
		originz = depth/2;
		
		float widthoff = width-offset;
		float heightoff = height-offset;
		positionrect = new RectF(offset, offset, widthoff, heightoff);
		tileobj = new Object3D(20);
		SimpleVector x1y1t = new SimpleVector(offset,offset,0);
		SimpleVector x1y1b = new SimpleVector(offset,offset,depth);
		SimpleVector x2y1t = new SimpleVector(widthoff,offset,0);
		SimpleVector x2y1b = new SimpleVector(widthoff,offset,depth);
		SimpleVector x1y2t = new SimpleVector(offset,heightoff,0);
		SimpleVector x1y2b = new SimpleVector(offset,heightoff,depth);
		SimpleVector x2y2t = new SimpleVector(widthoff,heightoff,0);
		SimpleVector x2y2b = new SimpleVector(widthoff,heightoff,depth);
		
		float u0 = 0, u1 = 1, v0 = 0, v1 = 1;
		
		//top plane
		tileobj.addTriangle(x1y1t, u0, v0, x2y2t, u1, v1, x2y1t, u1, v0);
		tileobj.addTriangle(x1y1t, u0, v0, x1y2t, u0, v1, x2y2t, u1, v1);

		//bottom plane
		tileobj.addTriangle(x1y1b, u0, v0, x2y1b, u1, v0, x2y2b, u1, v1);
		tileobj.addTriangle(x1y1b, u0, v0, x2y2b, u1, v1, x1y2b, u0, v1);
		
		//left plane
		tileobj.addTriangle(x1y2t, u0, v0, x1y1b, u0, v1, x1y2b, u1, v1);
		tileobj.addTriangle(x1y2t, u0, v0, x1y1t, u1, v1, x1y1b, u1, v0);
		
		//right plane
		tileobj.addTriangle(x2y2b, u0, v0, x2y1b, u0, v1, x2y2t, u1, v1);
		tileobj.addTriangle(x2y1b, u0, v0, x2y1t, u1, v1, x2y2t, u1, v0);

		//front plane
		tileobj.addTriangle(x1y1t, u0, v0, x1y1b, u0, v1, x2y1t, u1, v1);
		tileobj.addTriangle(x1y1b, u0, v0, x2y1b, u1, v1, x2y1t, u1, v0);
		
		//back plane
		tileobj.addTriangle(x1y2t, u0, v0, x2y2t, u0, v1, x1y2b, u1, v1);
		tileobj.addTriangle(x2y2t, u0, v0, x2y2b, u1, v1, x1y2b, u1, v0);
		
//		int textcolor = texturecount*40;
//		texturename = "tiletexture" + texturecount;
//		texturecount++;
//		Texture texture = new Texture(1,1,new RGBColor(textcolor, 255 - textcolor, textcolor));
//		
//		TextureManager tm = TextureManager.getInstance();
//		if(tm.containsTexture(texturename))
//			tm.removeAndUnload(texturename, SqRtRenderer.fb);
//		tm.addTexture(texturename, texture);
		
		tileobj.setTexture("green");
		
		tileobj.build();
		
		Game.world.addObject(tileobj);
		
	}

	public static Tile createRootTile(float width, float height, float topleftx, float toplefty) {
		// TODO Auto-generated method stub
		RootTile roottile = RootTile.getRootTile(width, height);
		
		roottile.moveTo(topleftx, toplefty);
		return roottile;
	}

	public static Tile create2x1Tile(float topleftx, float toplefty) {
		return new Tile2x1(topleftx, toplefty);
	}

	public static Tile create1x2Tile(float topleftx, float toplefty) {
		return new Tile1x2(topleftx, toplefty);
	}

	public static Tile create1x1Tile(float topleftx, float toplefty) {
		return new Tile1x1(topleftx, toplefty);
	}
	
	public void moveTo(float topleftx, float toplefty) {
		float dx = topleftx - positionrect.top;
		float dy = toplefty - positionrect.left;
		positionrect.offset(dx, dy);
		
		utility.set(positionrect.left, positionrect.top, originz);
		tileobj.setOrigin(utility);
		
	}
	
	public boolean checkTouch(float x, float y){
		return positionrect.contains(x, y);
	}

	public boolean checkIntersect(RectF rect) {
		// TODO Auto-generated method stub
		return RectF.intersects(positionrect, rect);
	}

	public void slide(SimpleVector direction) {
		positionrect.offset(direction.x, direction.y);
		utility.set(positionrect.left, positionrect.top, originz);
		tileobj.setOrigin(utility);
	}

	public void snap() {
		float xdist = Math.round(positionrect.left) - positionrect.left;
		float ydist = Math.round(positionrect.top) - positionrect.top;
		if( Math.abs(xdist) > snapdistance){
			xdist = 0;
		}		
		if( Math.abs(ydist) > snapdistance){
			ydist = 0;
		}
		positionrect.offset(xdist, ydist);
		utility.set(positionrect.left, positionrect.top, originz);
		tileobj.setOrigin(utility);		
	}
	
}