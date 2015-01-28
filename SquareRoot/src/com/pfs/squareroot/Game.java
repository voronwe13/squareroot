package com.pfs.squareroot;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.threed.jpct.Interact2D;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Game {
	private static final String TAG = "Game";
	static GameMode currentmode;
	static Level level;
	static int currentlevelnum, movecount;
	static World world;
	static boolean tileheld;
	static Tile currenttile;
	static SimpleVector newposition = new SimpleVector(), 
			direction = new SimpleVector(), 
			position = new SimpleVector();
	static final SimpleVector normal = new SimpleVector(0,0,-1);
	static RectF testrectx = new RectF(), testrecty = new RectF();
	private static boolean gamewon, timerstarted, slidesoundstarted, clicksounded;
	private static SimpleVector rtwinpos, winslide = new SimpleVector(0.1f, 0, 0);
	private static long starttime, pausetime = 0;
	static long currenttime, besttime;
	static int bestmovecount;
	static SoundManager sm;
	static float olddist;

	public static void setup(World world) {
		Game.world = world;
		if(!restoreState()){
			level = new Level(1);
			currenttime = 0;
			pausetime = 0;
			movecount = 0;
			besttime = -1;
			bestmovecount = -1;
		}
		currentlevelnum = level.levelnum;
		tileheld = false;
		currentmode = GameMode.ACTIVE;
		gamewon = false;
		timerstarted = false;
		SoundManager.setup(SquareRootActivity.activity);
		slidesoundstarted = false;
		olddist = 0;
	}
	
	public static void GLupdate() {
		// TODO Auto-generated method stub
		switch(currentmode){
		case ACTIVE:
			if(timerstarted){
				currenttime = pausetime + SystemClock.uptimeMillis() - starttime;
				
			}
			break;
		case PAUSED:
			break;
		case WON:
			if(level.roottile.positionrect.left < rtwinpos.x){
				level.roottile.slide(winslide);
				winslide.x = 1;
				Controls.camera.moveCamera(winslide, 0.13f);
				winslide.x = 0.1f;
				UI.winGameUpdate();
			} 
			break;
		default:
			break;
		}
	}
	
	public static void get3DFrom2D(float x, float y, SimpleVector tofill){
		//Log.d(TAG, "touch was at screen position: "+x+", "+y);
		Interact2D.reproject2D3DWS(Controls.camera, SqRtRenderer.fb, 
				Math.round(x), Math.round(y), direction);
		float denom = direction.calcDot(normal);
		if(denom == 0){
			Log.d(TAG, "got a weird ray here... no touch assumed.");
			tofill.set(-1,-1,-1);
		}
		float dist = -(Controls.campos.calcDot(normal))/denom;
		tofill.set(direction);
		tofill.scalarMul(dist);
		tofill.add(Controls.campos);
		//Log.d(TAG, "touch was at world position: "+tofill.toString());		
	}

	public static boolean checkTouch() {
		get3DFrom2D(Controls.xpos1, Controls.ypos1, position);
		currenttile = level.getTile(position);
		if(currenttile != null){
			tileheld = true;
			
		}
		return tileheld;
	}

	public static void handleTouch() {
		if(currentmode != GameMode.ACTIVE)
			return;
		if(tileheld){
			get3DFrom2D(Controls.xpos1, Controls.ypos1, newposition);
			direction.set(newposition.calcSub(position));
			boolean nomove = (direction.x == 0 && direction.y == 0);
			if(nomove){
				if(slidesoundstarted){
					SoundManager.getInstance().endSound("woodslide");
					slidesoundstarted = false;
				}
				return;
			}
			testrectx.set(currenttile.positionrect);
			testrectx.offset(direction.x, 0);
			testrecty.set(currenttile.positionrect);
			testrecty.offset(0, direction.y);
			List<Tile> tilesx = level.getIntersectingTiles(testrectx, currenttile);
			List<Tile> tilesy = level.getIntersectingTiles(testrecty, currenttile);
			if(!tilesx.isEmpty() || !level.boxrect.contains(testrectx))
				direction.x = 0;
			if(!tilesy.isEmpty() || !level.boxrect.contains(testrecty))
				direction.y = 0;
			if(direction.x != 0 || direction.y != 0){
				if(!slidesoundstarted){
					SoundManager.getInstance().playSoundLoop("woodslide", -1);
					slidesoundstarted = true;
					clicksounded = false;
				}
			} else {
				SoundManager sm = SoundManager.getInstance();
				sm.endSound("woodslide");
				if(!clicksounded && olddist > 0.05){
					sm.playSound("woodclick");
					clicksounded = true;
				}
				slidesoundstarted = false;
			}
			currenttile.slide(direction);
			position.set(newposition);
			float distx = Math.abs(direction.x);
			float disty = Math.abs(direction.y);
			olddist = distx > disty ? distx : disty;
		}
		
	}

	public static void handleRelease() {
		tileheld = false;
		if(currenttile != null){
			if(slidesoundstarted){
				SoundManager.getInstance().endSound("woodslide");
				slidesoundstarted = false;
			}
			currenttile.snap();
			clicksounded = false;
			if(currenttile == level.roottile)
				gamewon = level.checkWin();
			if(gamewon){
				win();
				
			}
		}
	}

	public static void win() {
		currentmode = GameMode.WON;
		rtwinpos = new SimpleVector(level.roottile.positionrect.left+5, level.roottile.positionrect.left, 0);
		//currenttime = SystemClock.uptimeMillis() - starttime;
		UI.winGame();
	}

	public static void reset(){
		level.reset();
		movecount = 0;
		UI.updateMoveCount();
		currentmode = GameMode.ACTIVE;
		Controls.moveToHome();
		gamewon = false;
		currenttime = 0;
		pausetime = 0;
		timerstarted = false;
	}

	public static void incMovecount() {
		movecount++;
		if(!timerstarted)
			startTimer();
		
	}

	private static void startTimer() {
		starttime = SystemClock.uptimeMillis();
		Log.d(TAG, "starting timer, starttime: "+starttime+", pausetime: "+pausetime+", currenttime: "+currenttime);
		timerstarted = true;
	}
	
	public static void pauseGame(){
		pausetime = currenttime;
		currentmode = GameMode.PAUSED;
		saveState();
	}
	
	public static void continueGame(){
		starttime = SystemClock.uptimeMillis();
		currentmode = GameMode.ACTIVE;
	}
	
	public static void saveState(){
		try {
			
			File fileDir = SquareRootActivity.activity.getFilesDir(); 
		    File datafile = new File(fileDir, "data.xml");
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document datadoc;
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    if(datafile.exists()){
		    	Log.d(TAG, "the data file exists");
			    datadoc = docBuilder.parse(datafile);
			    Element dataroot = datadoc.getDocumentElement();
			    Element tileroot = (Element) dataroot.getElementsByTagName("tiles").item(0);
			    level.saveTiles(tileroot);
			    Element time = (Element) dataroot.getElementsByTagName("time").item(0);
			    time.getAttributeNode("current").setNodeValue(Long.toString(currenttime));
			    time.getAttributeNode("best").setNodeValue(Long.toString(besttime));
			    Element moves = (Element) dataroot.getElementsByTagName("moves").item(0);
			    moves.getAttributeNode("current").setNodeValue(Integer.toString(movecount));
			    moves.getAttributeNode("best").setNodeValue(Integer.toString(bestmovecount));
		    } else {
		    	//speakerfile.createNewFile();
		    	//talksfile.createNewFile();
		    	Log.d(TAG, "datafile does not exist, creating it.");
		    	datadoc = docBuilder.newDocument();
		    	Element dataroot = datadoc.createElement("data");
		    	datadoc.appendChild(dataroot);
		    	Element tileroot = datadoc.createElement("tiles");
		    	dataroot.appendChild(tileroot);
		    	level.saveTiles(tileroot, datadoc);
			    Element time = datadoc.createElement("time");
			    time.setAttribute("current", Long.toString(currenttime));
			    time.setAttribute("best", Long.toString(besttime));
			    dataroot.appendChild(time);
			    Element moves = datadoc.createElement("moves");
			    moves.setAttribute("current", Integer.toString(movecount));
			    moves.setAttribute("best", Integer.toString(bestmovecount));
			    dataroot.appendChild(moves);
		    }
//		    // initialize StreamResult with File object to save to file
		    StreamResult result = new StreamResult(datafile);
		    DOMSource source = new DOMSource(datadoc);
		    transformer.transform(source, result);
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static boolean restoreState(){
		try {
			File fileDir = SquareRootActivity.activity.getFilesDir(); 
		    File datafile = new File(fileDir, "data.xml");			//fileDir.mkdirs();
		    if(!datafile.exists())
		    	return false;
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document datadoc = docBuilder.parse(datafile);
		    Element dataroot = datadoc.getDocumentElement();
		    Element tileroot = (Element) dataroot.getElementsByTagName("tiles").item(0);
		    level = new Level(tileroot);
		    Element time = (Element) dataroot.getElementsByTagName("time").item(0);
		    currenttime = Long.parseLong(time.getAttributeNode("current").getNodeValue());
		    pausetime = currenttime;
		    besttime = Long.parseLong(time.getAttributeNode("best").getNodeValue());
		    Element moves = (Element) dataroot.getElementsByTagName("moves").item(0);
		    movecount = Integer.parseInt(moves.getAttributeNode("current").getNodeValue());
		    bestmovecount = Integer.parseInt(moves.getAttributeNode("best").getNodeValue());
		    UI.updateMoveCount();
		} catch (Exception e) {
		    e.printStackTrace();
			File fileDir = SquareRootActivity.activity.getFilesDir(); 
		    File datafile = new File(fileDir, "data.xml");
		    if(datafile.exists()){
		    	datafile.delete();
		    	Log.d(TAG, "data.xml was bad, deleting");
		    }
		    return false;
		}
		return true;
	}

	public static boolean checkBestTime() {
		if(besttime < 0 || currenttime < besttime){
			besttime = currenttime;
			return true;
		}
		return false;
	}

	public static boolean checkBestMoves() {
		if(bestmovecount < 0 || movecount < bestmovecount){
			bestmovecount = movecount;
			return true;
		}
		return false;
	}

	public static void resetScores() {
		bestmovecount = -1;
		besttime = -1;
		saveState();
	}
}
