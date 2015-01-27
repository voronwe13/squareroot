package com.pfs.squareroot;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager implements SoundPool.OnLoadCompleteListener {
	//class for handling sounds.  Add new sounds to loadSounds(), and add a public int
	//with a descriptive name.  Play the sound by getting an instance and using playSound
	//with the int for the sound, eg. sm.playSound(sm.endwin);
	private static SoundManager sm;
	SoundPool soundpool;
	private float maxvol;
	private AudioManager audman;
	MediaPlayer menumusicplayer, creditsmusicplayer, currentmusicplayer;
	private Activity context;
	private int loadedsong;
	public float soundvolume, musicvolume, fxpercent, musicpercent;
	private HashMap<String, SoundEffect> sounds;// = new HashMap<String, SoundEffect>();
	
	
	
	public static void setup(Activity context){
		if(sm == null){
			sm = new SoundManager(context);
			sm.soundpool.setOnLoadCompleteListener(sm);
		}
		sm.soundvolume = 1f;
		sm.musicvolume = 1f;
		sm.fxpercent = 1f;
		sm.musicpercent = 1f;
		if(sm.sounds == null)
			sm.sounds = new HashMap<String, SoundEffect>();
        sm.loadSounds();
	}
	
	public static SoundManager getInstance(){
		if(sm == null)
			throw new IllegalStateException("SoundManager has not been set up yet.");
		return sm;
	}
	
	private SoundManager(Activity context){
		this.context = context;
        soundpool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
        context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        loadedsong = menusong;
//        currentmusicplayer = menumusicplayer;
        audman = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxvol = (float) audman.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        
	}
	
	/**
	 * Adds a sound effect to the sound manager.  If there is a sound effect with
	 * the given name already in the sound manager, it will add the sound as an
	 * option to that sound, i.e. when that sound is called, it will randomly
	 * choose from among the sounds loaded for the given name.
	 * 
	 * @param resourceid	the android resource id (e.g. R.raw.soundname)
	 * @param name			a name to reference the sound by.
	 */
	public void addSound(String name, int resourceid){
		
		SoundEffect sound = sounds.get(name);
		if(sound == null){
			sound = new SoundEffect();
			sound.addSound(soundpool.load(context, resourceid, 1));
			sounds.put(name, sound);
		} else {
			sound.addSound(soundpool.load(context, resourceid, 1));
		}
		
	}
	
	private void loadSounds(){
		addSound("woodclick", R.raw.woodclick1);
		addSound("woodclick", R.raw.woodclick2);
		addSound("woodclick", R.raw.woodclick3);
		addSound("woodslide", R.raw.woodslide1);
		
//		menumusicplayer = MediaPlayer.create(context, menusong);
//		menumusicplayer.setLooping(true);
//		creditsmusicplayer = MediaPlayer.create(context, creditsmusic);
//		creditsmusicplayer.setLooping(true);
	}
	
    public void playSound(String name){
    	SoundEffect sound = sounds.get(name);
    	float currentvol = (float) audman.getStreamVolume(AudioManager.STREAM_MUSIC);
    	soundvolume = fxpercent * currentvol / maxvol;
    	sound.play();
    }
    
    public void playSoundLoop(String name, int looptimes){
    	SoundEffect sound = sounds.get(name);
    	float currentvol = (float) audman.getStreamVolume(AudioManager.STREAM_MUSIC);
    	soundvolume = fxpercent * currentvol / maxvol;
    	sound.playLoop(looptimes);
    }
    
    public void playSong(int song){
    	if(loadedsong == song)
    		currentmusicplayer.start();
    	else{
    		switch(song){

    			case R.raw.creditsmusic: currentmusicplayer = creditsmusicplayer; break;
    			
    		}
    		currentmusicplayer.start();
    		loadedsong = song;
    	}
    }
    
    public void endSong(){
    	currentmusicplayer.pause();
    }
    
    public void endSound(String name){
    	SoundEffect sound = sounds.get(name);
    	sound.stop();
    }

	@Override
	public void onLoadComplete(SoundPool pool, int sound, int status) {
		
	}
    
}

