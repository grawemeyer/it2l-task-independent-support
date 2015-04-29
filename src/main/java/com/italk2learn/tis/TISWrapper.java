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
	
	public TISWrapper(){
		startUser = "anonym";
		analysis = new Analysis();
		TimerTask timerSpeechTask = new TimerForSpeechCheck();
		((TimerForSpeechCheck) timerSpeechTask).setAnalysis(analysis);
		((TimerForSpeechCheck) timerSpeechTask).setWrapper(this);
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(timerSpeechTask, 30 * 1000, 60 * 1000);
	}
	
	public void sendTDStoTIS(String user, List<String> feedback, String type, int level, boolean followed, boolean viewed){
		System.out.println("::: TDStoTIS :::");
		System.out.println("::: feedback type ::: "+type);
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
			analysis.analyseInteractionAndSetFeedback(feedback, type, level, followed, viewed, this);
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
		LdapUserDetailsImpl user = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		startUser=user.getUsername();
		String result ="";
		if (message.length() > 0) {
			result = "start user: "+startUser+" user: "+currentUser+" message: "+message;
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
	
	public void setMessage(String value) {
		System.out.println(":::::: setMessage :::: "+value);
		message = value;
		if (uploadCheckMathsWordsTimer != null){
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			timerSpeechMathsWords.cancel();
		}
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
			uploadCheckMathsWordsTimer.scheduleAtFixedRate(timerSpeechMathsWords, 6000, 6*1000);
		}
	}

	public void setCurrentAffect(String value) {
		affectString = value;
	}

	public void resetCurrentWordList() {
		analysis.resetCurrentWordList();
	}
}