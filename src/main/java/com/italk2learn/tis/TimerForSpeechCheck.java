package com.italk2learn.tis;

import java.util.TimerTask;

public class TimerForSpeechCheck extends TimerTask {
	Analysis analysis;
	
	public void setAnalysis(Analysis elem){
		analysis = elem;
	}
	
	public void run() {
		analysis.checkIfSpeaking();
	}
}