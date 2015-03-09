package com.italk2learn.tis;

import java.util.TimerTask;

public class TimerForSpeechCheck extends TimerTask {
	Analysis analysis;
	TISWrapper wrapper;
	
	public void setAnalysis(Analysis elem){
		analysis = elem;
	}
	
	public void setWrapper(TISWrapper elem){
		wrapper = elem;
	}
	
	
	public void run() {
		analysis.checkIfSpeaking(wrapper);
	}
}
