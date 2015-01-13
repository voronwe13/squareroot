package com.pfs.squareroot;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;

public class SqRtRenderer implements GLSurfaceView.Renderer {

	public static FrameBuffer fb = null;
	private World world = null;
	private RGBColor back = new RGBColor(170, 200, 255);
	private boolean iscreated;
	private Context context;
	private SqRtGLSurfaceView srglsv;
	
	public SqRtRenderer(SqRtGLSurfaceView srglsv, SquareRootActivity context) {
		this.srglsv = srglsv;
		this.context = context;
		iscreated = false;
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		fb.clear(back);
		Controls.execute();
		Game.GLupdate();
		UI.update();
		world.renderScene(fb);
		world.draw(fb);
		fb.display();
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		// TODO Auto-generated method stub
		if (fb != null) {
			fb.dispose();
		}
		fb = new FrameBuffer(w, h);
		if(!SquareRootActivity.rendererinitialized){
			Texture.defaultToKeepPixels(true);
			world = new World();
			world.setAmbientLight(50, 50, 50);
			world.setClippingPlanes(1, 5000);
			setupTextures();
			Camera cam = world.getCamera();
			Controls.setup(cam);
			Game.setup(world);
			Controls.setupLevel(Game.level, w, h);
			SquareRootActivity.activity.world = world;
			SquareRootActivity.rendererinitialized = true;
		} else {
			world = SquareRootActivity.activity.world;
		}
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	public void onResume() {
		// TODO Auto-generated method stub
		
	}
	
	public void setupTextures(){
		TextureManager tm = TextureManager.getInstance();
		if(tm.containsTexture("green"))
			tm.removeAndUnload("green", fb);

		Paint paint = new Paint();
		Rect r = new Rect(4, 4, 60, 60);
		Bitmap tilebmp = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(tilebmp);
		// fill
	    paint.setStyle(Paint.Style.FILL);
	    paint.setColor(Color.GREEN); 
	    canvas.drawRect(r, paint);

	    // border
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setColor(Color.BLACK);
	    canvas.drawRect(r, paint);
		Texture greentex = new Texture(tilebmp);
		tm.addTexture("green", greentex);
	}

}
