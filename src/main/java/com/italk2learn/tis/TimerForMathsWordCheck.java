package com.italk2learn.tis;

import java.util.Timer;
import java.util.TimerTask;

public class TimerForMathsWordCheck extends TimerTask {
	Analysis analysis;
	
	public void setAnalysis(Analysis elem){
		analysis = elem;
	}
	
	public void run() {
		analysis.checkForMathsWords();
	}

}