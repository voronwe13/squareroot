package com.pfs.squareroot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;

public class Textures {
	static TextureInfo wood1x1ti, wood1x2ti, wood2x1ti, wood2x2ti;
	
	public static void setupTextures(FrameBuffer fb){
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
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		
		Bitmap text1x1bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.wood1x1text256_3, options);
		Texture wood1x1 = new Texture(text1x1bmp);
		tm.addTexture("wood1x1", wood1x1);
		
		Bitmap text1x2bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.wood1x2text256_2, options);
		Texture wood1x2 = new Texture(text1x2bmp);
		tm.addTexture("wood1x2", wood1x2);
		
		Bitmap text2x2bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.wood2x2text256, options);
		Texture wood2x2 = new Texture(text2x2bmp);
		tm.addTexture("wood2x2", wood2x2);
		
		Bitmap text2x1bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.wood2x1text256, options);
		Texture wood2x1 = new Texture(text2x1bmp);
		tm.addTexture("wood2x1", wood2x1);

		Bitmap normal1x1bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.normalmap2, options);
		Texture normal1x1 = new Texture(normal1x1bmp);
		tm.addTexture("normal1x1", normal1x1);
		
		Bitmap normal1x2bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.normalmap21x2, options);
		Texture normal1x2 = new Texture(normal1x2bmp);
		tm.addTexture("normal1x2", normal1x2);
		
		Bitmap normal2x1bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.normalmap22x1, options);
		Texture normal2x1 = new Texture(normal2x1bmp);
		tm.addTexture("normal2x1", normal2x1);
		
		Bitmap normal2x2bmp = BitmapFactory.decodeResource(SquareRootActivity.activity.getResources(), R.drawable.normalmap22x2, options);
		Texture normal2x2 = new Texture(normal2x2bmp);
		tm.addTexture("normal2x2", normal2x2);
		
		wood1x1ti = new TextureInfo(tm.getTextureID("wood1x1"));
		wood1x1ti.add(tm.getTextureID("normal1x1"), TextureInfo.MODE_BLEND);
		
		wood1x2ti = new TextureInfo(tm.getTextureID("wood1x2"));
		wood1x2ti.add(tm.getTextureID("normal1x2"), TextureInfo.MODE_BLEND);
		
		wood2x2ti = new TextureInfo(tm.getTextureID("wood2x2"));
		wood2x2ti.add(tm.getTextureID("normal2x2"), TextureInfo.MODE_MODULATE);
		
		wood2x1ti = new TextureInfo(tm.getTextureID("wood2x1"));
		wood2x1ti.add(tm.getTextureID("normal2x1"), TextureInfo.MODE_MODULATE);
		
//		wood1x1ti = new TextureInfo(tm.getTextureID("normal1x1"));
//		wood1x1ti.add(tm.getTextureID("wood1x1"), TextureInfo.MODE_BLEND);
//		
//		wood1x2ti = new TextureInfo(tm.getTextureID("normal1x2"));
//		wood1x2ti.add(tm.getTextureID("wood1x2"), TextureInfo.MODE_BLEND);
//		
//		wood2x2ti = new TextureInfo(tm.getTextureID("normal2x2"));
//		wood2x2ti.add(tm.getTextureID("wood2x2"), TextureInfo.MODE_BLEND);
//		
//		wood2x1ti = new TextureInfo(tm.getTextureID("normal2x1"));
//		wood2x1ti.add(tm.getTextureID("wood2x1"), TextureInfo.MODE_BLEND);
	}
}
