package com.pfs.squareroot;

import com.threed.jpct.World;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SquareRootActivity extends Activity {
	SqRtGLSurfaceView srglsv;
	public static SquareRootActivity activity;
	public World world;
	public static boolean rendererinitialized = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_square_root);
		ViewGroup vg = (ViewGroup) findViewById(android.R.id.content);
		srglsv = new SqRtGLSurfaceView(this);
		vg.addView(srglsv);
		Controls.activitySetup(this);
	}
	
    @Override
    public void onStart() {
        super.onStart();
        if(activity == null)
        	activity = this;
    }
	
    protected void onStop() {
        super.onPause();
        if (srglsv != null) {
            srglsv.onStop();
        }     
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (srglsv != null) {
            srglsv.onPause();
        }     
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (srglsv != null) {
            srglsv.onResume();
        }     
    }
}
