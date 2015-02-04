package com.pfs.squareroot;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.threed.jpct.World;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SquareRootActivity extends Activity {
	private static final String TAG = "SRActivity";
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
    	//Button pausebutton = (Button) v;
    	//if("Pause".equals(pausebutton.getText())){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout pausell = (LinearLayout) inflater.inflate(R.layout.pausedialog, null);
		builder.setView(pausell).setPositiveButton("Resume", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   Game.continueGame();
	           }
	       });
		builder.setCancelable(false);
		Game.pauseGame();
		//pausebutton.setText("Resume");
		
		AdView adview = (AdView) pausell.findViewById(R.id.adview);
		int dialogwidth = (3*UI.screenwidth)/4;
		dialogwidth = dialogwidth>320?dialogwidth:320;
		int dialogheight = (3*UI.screenheight)/4;
		dialogheight = dialogheight>250?dialogheight:250;
		adview.setLayoutParams(new LinearLayout.LayoutParams(dialogwidth, dialogheight));
		 
		// Request for Ads
		AdRequest adrequest = new AdRequest.Builder()
		 
		// Add a test device to show Test Ads
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice("99D6CF3CCEB4360655A3B068428D91A6")
		.build();
		 
		// Load ads into Banner Ads
		adview.loadAd(adrequest);
		AlertDialog pausedialog = builder.create();
		pausedialog.show();

//		Log.d(TAG, "setting dialog width: "+dialogwidth+", height: "+dialogheight);
//		pausedialog.getWindow().setLayout(dialogwidth, dialogheight);
//    	} else {
//    		Game.continueGame();
//    		pausebutton.setText("Pause");    		
//    	}
    }
    
    public void showScores(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout scoretextll = (LinearLayout) inflater.inflate(R.layout.windialog, null);
		builder.setView(scoretextll).setPositiveButton("Close", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	       });
		builder.setNegativeButton("Reset Scores", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				Game.resetScores();
				showScores(null);
			}
			
		});
		AlertDialog scoredialog = builder.create();
		TextView maintext = (TextView) scoretextll.findViewById(R.id.wintext1);
		maintext.setText("Your best scores:");
		String movestring = "Best # of moves: ";
		if(Game.bestmovecount<0)
			movestring += "not set";
		else
			movestring += Game.bestmovecount;
		TextView movestext = (TextView) scoretextll.findViewById(R.id.wintext2);
		movestext.setText(movestring);
		TextView timertext = (TextView) scoretextll.findViewById(R.id.wintext3);
		String timestring = "Best time: ";
		if(Game.besttime<0)
			timestring += "not set";
		else {
			int bestsecs = (int) (Game.besttime/1000);
			int bestmins = bestsecs/60;
			bestsecs = bestsecs%60;
			timestring += String.format("%d:%02d", bestmins, bestsecs);
		}

		timertext.setText(timestring);
		scoredialog.show();
    }
    
    public void showCredits(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout creditstextll = (LinearLayout) inflater.inflate(R.layout.windialog, null);
		builder.setView(creditstextll).setPositiveButton("Close", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	       });
		AlertDialog creditsdialog = builder.create();
		TextView maintext = (TextView) creditstextll.findViewById(R.id.wintext1);
		maintext.setText("Credits");
		TextView progcredtext = (TextView) creditstextll.findViewById(R.id.wintext2);
		progcredtext.setText("Programming: Peter Schatz\nSound: Peter Schatz");
		TextView soundcredtext = (TextView) creditstextll.findViewById(R.id.wintext3);
		soundcredtext.setText("Graphics powered by jPCT");
		creditsdialog.show();
    }
    
    public void showHowTo(View v){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		ImageView howtoimg = (ImageView) inflater.inflate(R.layout.howto, null);
		int dialogwidth = (2*UI.screenwidth)/3;
		dialogwidth = dialogwidth>320?dialogwidth:320;
		int dialogheight = (3*UI.screenheight)/4;
		dialogheight = dialogheight>250?dialogheight:250;
		Log.d(TAG, "setting dialog width: "+dialogwidth+", height: "+dialogheight);

		//howtoimg.setScaleType(ScaleType.CENTER_INSIDE);
		builder.setView(howtoimg).setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	       });
		AlertDialog howtodialog = builder.create();
		WindowManager.LayoutParams lp =  new WindowManager.LayoutParams();
	    lp.copyFrom(howtodialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		howtodialog.show();
		howtodialog.getWindow().setAttributes(lp);
    }
}
