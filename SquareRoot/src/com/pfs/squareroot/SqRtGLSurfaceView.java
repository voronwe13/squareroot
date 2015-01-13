package com.pfs.squareroot;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SqRtGLSurfaceView extends GLSurfaceView {

	private final SqRtRenderer renderer;
	
	public SqRtGLSurfaceView(SquareRootActivity context) {
		super(context);
        setEGLContextClientVersion(2);
		renderer = new SqRtRenderer(this, context);
		setRenderer(renderer);
		
	}
	
	public boolean onTouchEvent(MotionEvent me) {
    	if(Controls.update(me))
    		return true;

		return super.onTouchEvent(me);
	}
	
    protected void onStop() {
        super.onPause();
        if (renderer != null) {
            renderer.onStop();
        }     
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (renderer != null) {
            renderer.onPause();
        }     
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (renderer != null) {
            renderer.onResume();
        }     
    }

}
