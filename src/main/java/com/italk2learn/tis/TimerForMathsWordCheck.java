package com.italk2learn.tis;

import java.util.Timer;
import java.util.TimerTask;

public class TimerForMathsWordCheck extends TimerTask {
	Analysis analysis;
	TISWrapper wrapper;
	
	public void setAnalysis(Analysis elem){
		analysis = elem;
	}
	
	public void setWrapper(TISWrapper elem){
		wrapper = elem;
	}

	
	public void run() {
		analysis.checkForMathsWords(wrapper);
	}

}
