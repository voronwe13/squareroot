package com.pfs.squareroot;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.World;
import com.threed.jpct.util.Overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * UI class is not being used yet
 * 
 * 
 * @author Voronwe
 *
 */
public class UI {
	public static GameMode mode, oldmode;
	public static Context context;
	public static Game gamestate;

	public static World world;
	
	public static void setup(Context context, World theworld, FrameBuffer fb, int w, int h){
		UI.context = context;
		world = theworld;

	}
	
	public static void update(){

	}
	

	public static void setMode(GameMode mode) {
		
		
	}
	

	public static void handleRelease(int x, int y) {
	
	}

	public static void handleFirstTouch(float xpos, float ypos) {

	}


	public static void handleTouching(float xpos, float ypos) {

	}

}
