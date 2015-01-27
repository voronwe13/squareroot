package com.pfs.squareroot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.media.AudioManager;
import android.media.SoundPool;

public class SoundEffect {
	private List<Integer> soundids;
	private SoundPool soundpool;
	private int numsounds, streamid;
	private SoundManager sm;
	Random randgen;
	boolean playing;
	
	public SoundEffect(){
		sm = SoundManager.getInstance();
		this.soundpool = sm.soundpool;
		randgen = new Random();
		numsounds = 0;
		soundids = new ArrayList<Integer>();
	}
	
	public void addSound(int soundid){
		soundids.add(soundid);
		numsounds++;
	}
	
    public void play(){
    	float volume = sm.soundvolume;
    	int sound = soundids.get(randgen.nextInt(numsounds));
    	streamid = soundpool.play(sound, volume, volume, 1, 0, 1f);
    	
    }
    
    public void playLoop(int looptimes){
    	float volume = sm.soundvolume;
    	int sound = soundids.get(randgen.nextInt(numsounds));
    	streamid = soundpool.play(sound, volume, volume, 1, looptimes, 1f);
    }

	public void stop() {
		soundpool.stop(streamid);
	}
}
