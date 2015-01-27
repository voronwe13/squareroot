package com.pfs.squareroot;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.World;
import com.threed.jpct.util.Overlay;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
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
	
	public static AlertDialog windialog;
	public static LinearLayout wintextll;

	public static World world;
	private static long oldseconds;
	
	public static void setup(Context context, World theworld, FrameBuffer fb, int w, int h){
		UI.context = context;
		world = theworld;

	}
	
public static void update(){
	long seconds = (Game.currenttime / 1000);
	if(seconds != oldseconds){
		//Log.d(TAG, "currenttime: "+Game.currenttime+", seconds: "+seconds);
		SquareRootActivity.activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				long seconds = (Game.currenttime / 1000);
				long minutes = seconds / 60;
				seconds     = seconds % 60;
			
				SquareRootActivity.activity.timertv.setText(String.format("Time: %d:%02d", minutes, seconds));
			}
		});
	}
	oldseconds = seconds;
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

				SquareRootActivity.activity.movetv.setText("Moves: "+Game.movecount);
				Log.d(TAG, "trying to set text to "+SquareRootActivity.activity.movetv.getText());
			}
			
		});
		
	}

	public static void winGame() {
		//TODO: do something better on a win...
		SquareRootActivity.activity.runOnUiThread(new Runnable(){
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {	
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				LayoutInflater inflater = (LayoutInflater)context.getSystemService
					      (Context.LAYOUT_INFLATER_SERVICE);
				wintextll = (LinearLayout) inflater.inflate(R.layout.windialog, null);
				builder.setView(wintextll).setPositiveButton("New Game", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               SquareRootActivity.activity.reset(null);
			           }
			       });
				windialog = builder.create();
				boolean beatmoves = Game.checkBestMoves();
				String movestring = "# of moves: "+Game.movecount+"     Best # of moves: "+Game.bestmovecount;
				if(beatmoves)
					movestring += "   New Record!";
				TextView movestext = (TextView) wintextll.findViewById(R.id.wintext2);
				movestext.setText(movestring);
				TextView timertext = (TextView) wintextll.findViewById(R.id.wintext3);
				boolean beattime = Game.checkBestTime();
				int seconds = (int) (Game.currenttime/1000);
				int minutes = seconds/60;
				seconds = seconds%60;
				int bestsecs = (int) (Game.besttime/1000);
				int bestmins = bestsecs/60;
				bestsecs = bestsecs%60;
				
				String timestring = String.format("Time finished in: %d:%02d", minutes, seconds)
									+ String.format("     Best time: %d:%02d", bestmins, bestsecs);
				if(beattime)
					timestring += "   New Record!";
				timertext.setText(timestring);
				wintextll.setAlpha(0);
				windialog.show();
			}
			
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void winGameUpdate(){
		if(wintextll != null){
			SquareRootActivity.activity.runOnUiThread(new Runnable(){
				
				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void run() {	
					float alpha = wintextll.getAlpha();
					if(alpha < 1){
						alpha += 0.1;
						wintextll.setAlpha(alpha);
					}
				}
			});
		}
	}
	
}
