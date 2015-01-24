package com.pfs.squareroot;

import com.threed.jpct.World;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SquareRootActivity extends Activity {
	SqRtGLSurfaceView srglsv;
	public static SquareRootActivity activity;
	public World world;
	public static boolean rendererinitialized = false;
	public static TextView movetv, timertv;
	public static ViewGroup content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_square_root);
		ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
		srglsv = new SqRtGLSurfaceView(this);
		movetv = (TextView) findViewById(R.id.movecount);
		timertv = (TextView) findViewById(R.id.timertext);
		content.addView(srglsv,0);
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
        Game.pauseGame();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (srglsv != null) {
            srglsv.onResume();
        }
        movetv.setText("Moves: "+Game.movecount);
        Game.continueGame();
    }
    
    public void reset(View v){
    	Game.reset();
    }
    
    public void win(View v){
    	Game.win();
    }
    
    public void pauseGame(View v){
    	Button pausebutton = (Button) v;
    	if("Pause".equals(pausebutton.getText())){
    		Game.pauseGame();
    		pausebutton.setText("Resume");
    	} else {
    		Game.continueGame();
    		pausebutton.setText("Pause");    		
    	}
    }
    
}
