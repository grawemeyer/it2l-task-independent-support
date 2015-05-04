package com.italk2learn.tis;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;

//import com.italk2learn.tis.inter.ITISWrapper;
//import com.italk2learn.vo.TaskIndependentSupportRequestVO;

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
	boolean firstTime = false;
	String currentUser = "";
	String startUser="";
	boolean doneButtonPressed = true;
	boolean languageEnglish = true;
	boolean languageSpanish = false;
	boolean languageGerman = false;
	
	public TISWrapper(){
		startUser = "anonym";
		analysis = new Analysis();
		TimerTask timerSpeechTask = new TimerForSpeechCheck();
		((TimerForSpeechCheck) timerSpeechTask).setAnalysis(analysis);
		((TimerForSpeechCheck) timerSpeechTask).setWrapper(this);
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(timerSpeechTask, 30 * 1000, 60 * 1000);
	}
	
	public void setLanguageInTIStoEnglish(){
		languageEnglish = true;
		languageSpanish = false;
		languageGerman = false;
	}
	
	public void setLanguageInTIStoSpanish(){
		languageEnglish = false;
		languageSpanish = true;
		languageGerman = false;
	}
	
	public void setLanguageInTIStoGerman(){
		languageEnglish = false;
		languageSpanish = false;
		languageGerman = true;
	}
	
	public boolean isLanguageEnglish(){
		return languageEnglish;
	}
	
	public boolean isLanguageSpanish(){
		return languageSpanish;
	}
	
	public boolean isLanguageGerman(){
		return languageGerman;
	}
	
	public void sendDoneButtonPressedToTIS(boolean value){
		doneButtonPressed = value;
	}
	
	public void sendTDStoTIS(String user, List<String> feedback, String type, String feedbackID, int level, boolean followed, boolean viewed){
		System.out.println("::: TDStoTIS :::");
		System.out.println("::: feedback type ::: "+type);
		System.out.println("::: feedback id ::: "+feedbackID);
		System.out.println("followed: "+followed+" viewed: "+viewed);
		System.out.println("::: fractionsLabInUse::: "+fractionsLabInUse);
		currentUser = user;
		if (fractionsLabInUse){
			analysis.analyseSound(audioStudent);
			if (firstTime){
				followed = true;
				viewed = true;
				firstTime = false;
			}
			analysis.analyseInteractionAndSetFeedback(feedback, type, feedbackID, level, followed, viewed, this);
		}
	}
	
	
	public void sendSpeechOutputToSupport(String user, List<String> currentWords){//, TaskIndependentSupportRequestVO request) {
		currentUser = user;
		//analysis.analyseWords(request.getWords(), this);
		analysis.analyseWords(currentWords, this);
	}
	
	
	public void startNewExercise(){
		System.out.println(":: startNewExercise ::" );
		analysis.resetVariablesForNewExercise(this);
		if (uploadCheckMathsWordsTimer != null){
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			timerSpeechMathsWords.cancel();
		}
		firstTime = true;
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
		String result ="";
		if (message.length() > 0) {
			result = message;
		}
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
	
	private void checkMathsWordsTimer(){
		if (uploadCheckMathsWordsTimer != null){
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			timerSpeechMathsWords.cancel();
		}
	}
	
	public void setMessage(String value, boolean popUpWindow) {
		if (fractionsLabInUse){
			if (doneButtonPressed) {
				System.out.println(":::::: setMessage :::: "+value);
				message = value;
				checkMathsWordsTimer();
				if (popUpWindow){
					doneButtonPressed = false;
				}
			}
		}
		else {
			System.out.println(":::::: setMessage :::: "+value);
			message = value;
			checkMathsWordsTimer();
		}
	}
	
	public void resetMessage(){
		message = "";
		checkMathsWordsTimer();
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
			
			//this needs to get checked if it stops after displaying it only once..
			uploadCheckMathsWordsTimer.scheduleAtFixedRate(timerSpeechMathsWords, 3*6000, 0);
			
		}
	}

	public void setCurrentAffect(String value) {
		affectString = value;
	}

	public void resetCurrentWordList() {
		analysis.resetCurrentWordList();
	}
}