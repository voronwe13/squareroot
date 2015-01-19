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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * UI class is not being used yet
 * 
 * 
 * @author Voronwe
 *
 */
public class UI {
	protected static final String TAG = "UI";
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
	
	public static void updateMoveCount(){
		SquareRootActivity.activity.runOnUiThread(new Runnable(){
	
			@Override
			public void run() {

				SquareRootActivity.activity.tv.setText("Moves: "+Game.movecount);
				Log.d(TAG, "trying to set text to "+SquareRootActivity.activity.tv.getText());
			}
			
		});
		
	}

	public static void winGame() {
		//TODO: do something better on a win...
		SquareRootActivity.activity.runOnUiThread(new Runnable(){
			
			@Override
			public void run() {	
				Toast.makeText(Controls.context, "You won!", Toast.LENGTH_LONG).show();
			}
			
		});
	}
	
}
