package com.italk2learn.tis;

import java.util.*;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class TISWrapper {
	public boolean popUpWindow = true;
	public String message = "";
	public String feedbackType = "";
	public String affectString = "";
	Analysis analysis = new Analysis();
	public byte[] audioStudent;
	private boolean fractionsLabInUse = false;
	Timer uploadCheckMathsWordsTimer;
	TimerTask timerSpeechMathsWords;
	
	public TISWrapper(){
		analysis = new Analysis();
		TimerTask timerSpeechTask = new TimerForSpeechCheck();
		((TimerForSpeechCheck) timerSpeechTask).setAnalysis(analysis);
		((TimerForSpeechCheck) timerSpeechTask).setWrapper(this);
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(timerSpeechTask, 0, 60 * 1000);
	}
	
	public void sendTDStoTIS(List<String> feedback, String type, int level, boolean followed, boolean viewed){
		System.out.println("::: TDStoTIS :::");
		System.out.println("::: feedback type ::: "+type);
		System.out.println("followed: "+followed+" viewed: "+viewed);
		analysis.analyseSound(audioStudent);
		analysis.analyseInteractionAndSetFeedback(feedback, type, level, followed, viewed, this);
	}
	
	public void sendSpeechOutputToSupport(TaskIndependentSupportRequestVO request) {
		analysis.analyseWords(request.getWords(), this);
	}
	
	
	public void startNewExercise(){
		analysis.resetVariablesForNewExercise(this);
		if (uploadCheckMathsWordsTimer != null){
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			timerSpeechMathsWords.cancel();
		}
	}
	
	public void setFractionsLabinUse(boolean value){
		fractionsLabInUse = value;
	}
	
	public boolean getFractionsLabInUse(){
		return fractionsLabInUse;
	}
	
	public byte[] getAudio(){
		return audioStudent;
	}
	
	public void setAudio(byte[] currentAudioStudent){
		audioStudent = currentAudioStudent;
	}

	public String getMessage(){
		String result = message;
		message = "";
		return result;
	}
	
	public String getFeedbackType(){
		String result = feedbackType;
		feedbackType = "";
		return result;
	}
	
	public String getCurrentAffect(){
		String result = affectString;
		affectString = "";
		return result;
	}
	
	public boolean getPopUpWindow(){
		return popUpWindow;
	}
	
	public void setMessage(String value) {
		System.out.println(":::::: setMessage :::: "+value);
		message = value;
	}

	public void setPopUpWindow(boolean value){
		popUpWindow = value;
	}
	
	public void setType(String value){
		feedbackType = value;
		if (value.equals("REFLECTION")){
			timerSpeechMathsWords = new TimerForMathsWordCheck();
			((TimerForMathsWordCheck) timerSpeechMathsWords).setAnalysis(analysis);
			((TimerForMathsWordCheck) timerSpeechMathsWords).setWrapper(this);
			uploadCheckMathsWordsTimer = new Timer(true);
			uploadCheckMathsWordsTimer.scheduleAtFixedRate(timerSpeechMathsWords, 6, 6 * 1000);
		}
	}

	public void setCurrentAffect(String value) {
		affectString = value;
	}

	public void resetCurrentWordList() {
		analysis.resetCurrentWordList();
	}
	
	
	
}
