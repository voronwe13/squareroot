package com.pfs.squareroot;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class SqRtRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "SRRenderer";
	public static FrameBuffer fb = null;
	private World world = null;
	private Light sun;
	private RGBColor back = new RGBColor(170, 200, 255);
	private boolean iscreated;
	private Context context;
	private SqRtGLSurfaceView srglsv;
	private static SimpleVector utility = new SimpleVector();
	private double angle = 0;
	private final double radius = 6f;
	private final float lightz = -14f;
	private final double TWOPI = Math.PI * 2;
	
	
	public SqRtRenderer(SqRtGLSurfaceView srglsv, SquareRootActivity context) {
		this.srglsv = srglsv;
		this.context = context;
		iscreated = false;
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		fb.clear(back);
		angle += 0.002;
		if(angle > TWOPI)
			angle = 0;
		utility.set((float)(radius*Math.cos(angle))-3, (float) (radius*Math.sin(angle))-3, lightz);
		sun.setPosition(utility);
		Controls.execute();
		Game.GLupdate();
		UI.update();
		Shaders.updateShaders();
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
			sun = new Light(world);
			sun.setIntensity(255, 255, 240);
			sun.setPosition(new SimpleVector(-4,-4,-14));
			Shaders.setupShaders();
			Textures.setupTextures(fb);
			Camera cam = world.getCamera();
			Controls.setup(cam, context);
			Log.d(TAG, "width: "+w+", height: "+h);
			UI.setup(context, world, fb, w, h);
			Game.setup(world);
			Controls.setupLevel(Game.level, w, h);
			SquareRootActivity.activity.world = world;
			SquareRootActivity.rendererinitialized = true;
		} else {
			world = SquareRootActivity.activity.world;
			world.removeAllLights();
			sun = new Light(world);
			sun.setIntensity(255, 255, 240);
			sun.setPosition(new SimpleVector(-4,-4,-14));
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


}
