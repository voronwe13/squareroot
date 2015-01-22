package com.pfs.squareroot;

import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * This class handles all of the input.
 * 
 * 
 * @author Voronwe
 *
 */

public class Controls {

	private static final String TAG = "Controls";
	private static final SimpleVector home = new SimpleVector(3.3f,3,-6.5f);
	public static float xpos1, ypos1, xpos2, ypos2, xstart, ystart, xrelease, yrelease;
	private static int screenwidth, screenheight, moveboundl, moveboundr, moveboundt, moveboundb;
	private static int pointerid1, pointerid2;
	private static float movex, movey, maxx, maxy, maxz;
	private static float rotation, oldangle;
	private static float zoom;
	public static boolean touchstart1, touchstart2, touching, released, controllingcam, 
							topdown, rotated; 
	public static Camera camera;
	public static SimpleVector campos, center, camup, camdir, lookat, utility,
								touchvec, touchvecold, zaxis, yaxis, rightvec;
	private static ScaleGestureDetector SGD;
	private static ScaleListener scalelistener;
	private static Level level;
	static Context context;
	private static boolean cameradisabled;
	
	private static class ScaleListener extends ScaleGestureDetector.
	SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
		   zoom = detector.getCurrentSpan()-detector.getPreviousSpan();
		   //Log.d(TAG,"ScaleListener onScale called.");
		   return true;
		}
   }

	/**
	 * This updates all of the variables based on the input MotionEvent.
	 * Since this is called from the Activity/UI thread, this should not
	 * do any heavy calculations.
	 * 
	 * @param me	the MotionEvent from Android operating system
	 * @return		true if the input was processed by this method
	 */
	public static boolean update(MotionEvent me) {
		//Log.d(TAG, "update called");
		int pointerindex = me.getActionIndex();
		int action = me.getAction() & MotionEvent.ACTION_MASK;
		if(SGD!=null)
			SGD.onTouchEvent(me);
		if (action == MotionEvent.ACTION_DOWN) {
			pointerid1 = me.getPointerId(pointerindex);
			xpos1 = me.getX();
			ypos1 = me.getY();
			if(!touching){
				xstart = xpos1;
				ystart = ypos1;
				touching = true;
				touchstart1 = true;
				released = false;
			}
			return true;
		}
		if (action == MotionEvent.ACTION_POINTER_DOWN) {
			if(pointerid2 == -1)
				pointerid2 = me.getPointerId(pointerindex);
			xpos2 = me.getX();
			ypos2 = me.getY();
			touchstart2 = true;
			return true;
		}

		if (action == MotionEvent.ACTION_POINTER_UP) {
			if(pointerid2 == me.getPointerId(pointerindex))
				pointerid2 = -1;
			if(pointerid1 == me.getPointerId(pointerindex)){
				primaryFingerReleased();
			}
			touchvecold.x = 0;
			touchvecold.y = 0;
			return true;
		}
		
		if (action == MotionEvent.ACTION_UP) {
			if(pointerid1 == me.getPointerId(pointerindex)){
				primaryFingerReleased();
			}
			return true;
		}

		if (action == MotionEvent.ACTION_MOVE) {
			//TODO change to only move camera if outside center zone
			float xd = me.getX() - xpos1;
			float yd = me.getY() - ypos1;

			xpos1 = me.getX();
			ypos1 = me.getY();

			movex = xd; // -100f;
			movey = yd; // -100f;
			getRotation(me);
			return true;
		}
		return false;
	}
	
	private static void primaryFingerReleased(){
		xrelease = xpos1;
		yrelease = ypos1;
		xpos1 = -1;
		ypos1 = -1;
		movex = 0;
		movey = 0;
		touching = false;
		released = true;
	}

	private static void getRotation(MotionEvent me) {
		// TODO Auto-generated method stub
		if(me.getPointerCount() <= 1)
			return;
		int index1 = me.findPointerIndex(pointerid1);
		int index2 = me.findPointerIndex(pointerid2);
		touchvecold.set(touchvec);
		touchvec.x = me.getX(index2) - me.getX(index1);
		touchvec.y = me.getY(index2) - me.getY(index1);
		if(touchvecold.x == 0 && touchvecold.y == 0)
			touchvecold.set(touchvec);
		else
			rotated = true;
	}

	/**
	 * Make changes to the world and/or camera based on the latest input from Android.
	 * This is called from the Game/OpenGL rendering thread, so this is where heavy
	 * operations occur.
	 */
	public static void execute() {
		//camera.getPosition(campos);
		if(released){
			UI.handleRelease(Math.round(xrelease), Math.round(yrelease));
			Game.handleRelease();
			released = false;
		} else if(touching){
			UI.handleTouching(xpos1, ypos1);
		}
		if(touchstart1){
			Log.d(TAG, "in execute, first touch");
			//UI.handleFirstTouch(xstart, ystart);
			controllingcam = !Game.checkTouch();
			touchstart1 = false;
		}
		if(touchstart2){
			//ignore second touch for now...
			//UI.handleFirstTouch(xpos2, ypos2);
			
			touchstart2 = false;
		}
		if(camera == null) //if not setup yet
			return;
		
		if(!controllingcam){ //if moving a game piece
			Game.handleTouch();
		}
		if(cameradisabled)
			return;
		
		if(Math.abs(movex)>10){
			//Log.d("Controls", "movex: "+movex);
			movex = movex>0?10:-10;
		}	
		if(Math.abs(movey)>10){
			//Log.d("Controls", "movey: "+movey);
			movey = movey>0?10:-10;
		}	
		if (movex != 0) {
			camera.moveCamera(Camera.CAMERA_MOVERIGHT, -movex/40);
			//terrain.rotateY(touchTurn);
			movex = 0;
		}

		if (movey != 0) {
			camera.moveCamera(Camera.CAMERA_MOVEUP, movey/40);
			//terrain.rotateX(touchTurnUp);
			movey = 0;
		}
		if(rotated){
			//Log.d("Controls","touchvec = "+touchvec.toString()+", touchvecold = "+touchvecold.toString());
			rotation = touchvecold.calcAngle(touchvec);
			if(rotation == rotation){//if not NaN
				if(Math.abs(rotation)>10){
					Log.d(TAG, "rotation: "+rotation);
					rotation = rotation>0?10:-10;
				}
				rightvec.set(touchvecold.calcCross(zaxis));
				float sign=-Math.signum(rightvec.calcDot(touchvec));
				camera.getPosition(campos);
				if(!topdown){
					camera.getDirection(camdir);
					float oldy = camdir.y;
					lookat.set(campos);
					camdir.y = 0;
					float templen = camdir.length();
					lookat.add(camdir);
					camera.lookAt(lookat);
					camera.rotateY(2*sign*rotation);
					camera.getDirection(camdir);
					camdir.y = oldy/templen;
					lookat.set(campos);
					lookat.add(camdir);
					camera.lookAt(lookat);
				} else {
					camera.setPosition(Game.level.center);
					camera.rotateCameraZ(sign*rotation);
				}
				camera.setPosition(campos);
				
			}
			rotation = 0;
			rotated = false;
		}
//		if(zoom!=0)
//			System.out.println("zoom: "+zoom);
		if(Math.abs(zoom)>10){
			//Log.d("Controls", "zoom: "+zoom);
			zoom = zoom>0?10:-10;
		}
		camera.moveCamera(Camera.CAMERA_MOVEIN, zoom/8);
		camera.getPosition(campos);
		fixCamera();
		zoom = 0;
	}
	
	private static void fixCamera() {
		// TODO Auto-generated method stub
		if(campos.y > maxy)
			campos.y = maxy;
		if(campos.x > maxx)
			campos.x = maxx;
		if(campos.z < -maxz)
			campos.z = -maxz;
		if(campos.z > -3)
			campos.z = -3;
		if(campos.y < 0)
			campos.y = 0;
		if(campos.x < 0)
			campos.x = 0;
		camera.setPosition(campos);	
	}

	public static void activitySetup(Context context){
		scalelistener = new ScaleListener();
		SGD = new ScaleGestureDetector(context, scalelistener);
	}
	
	/**
	 * Since this is a static class, setup is for initializing variables before using the class.
	 * 
	 * @param context	the activity
	 * @param cam		the camera from the world.
	 * @param context 
	 */
	public static void setup(Camera cam, Context context){
		camera = cam;
		//campos = camera.getPosition();
		//camera.moveCamera(Camera.CAMERA_MOVEIN, 5);
		//camera.lookAt(new SimpleVector(3,0,0));
		Controls.context = context;
		controllingcam = false;
		cameradisabled = true;
		lookat = new SimpleVector();
		center = new SimpleVector();
		touchvec = new SimpleVector();
		touchvecold = new SimpleVector();
		zaxis = new SimpleVector(0,0,1);
		yaxis = new SimpleVector(0,1,0);
		camup = new SimpleVector();
		rightvec = new SimpleVector();
		camdir = new SimpleVector();
		utility = new SimpleVector();
		rotation = oldangle = 0;
		pointerid2 = -1;
		topdown = true;
		Log.d(TAG, "at end of Controls.setup, camera position: "+camera.getPosition().toString());
	}
	
	/**
	 * More setup after the level has been created/loaded. Also
	 * should be called if the screen dimensions change.  Could be useful
	 * if more than one level to the game.
	 * 
	 * @param currentlevel	the currently loaded level.
	 * @param width			the width of the screen in pixels
	 * @param height		the height of the screen in pixels
	 */
	public static void setupLevel(Level currentlevel, int width, int height) {
		//Call on start or if screen dimensions change.
		
		level = currentlevel;
		screenwidth = width;
		screenheight = height;
		moveboundl = width/8;
		moveboundr = 7*width/8;
		moveboundt = height/8;
		moveboundb = 7*height/8;
		zoom = 0;
		//controllingcam = true;
		touching = released = false;
		maxy = level.height+1;
		maxx = level.width+1;
		maxz = maxx+maxy;
		moveToHome();
		//camera.moveCamera(Camera.CAMERA_MOVEIN, width/2);
		//cam.moveCamera(Camera.CAMERA_MOVERIGHT, 20);
		//camera.moveCamera(Camera.CAMERA_MOVEUP, level.maxheight + 10);
		//camera.lookAt(level.terrainobj.getTransformedCenter());
		Log.d(TAG, "at end of Controls.setupLevel, camera position: "+camera.getPosition().toString());

	}
	
	
	public static void moveToHome(){
		camera.setPosition(home);
		campos = camera.getPosition();
	}
	
	public static void switchCameraMode(){

	}

}
