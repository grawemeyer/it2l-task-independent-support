package com.italk2learn.tis;

import java.util.List;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class TISWrapper {
	public boolean popUpWindow = true;
	public String message = "";
	Analysis analysis = new Analysis();
	public byte[] audioStudent;
	
	public TISWrapper(){
		analysis = new Analysis();
	}
	
	public void sendTDStoTIS(List<String> feedback, String type, int level, boolean followed, boolean viewed){
		System.out.println("followed: "+followed+" viewed: "+viewed);
		analysis.analyseSound(audioStudent);
		analysis.analyseInteractionAndSetFeedback(feedback, type, level, followed, viewed, this);
	}
	
	public void sendSpeechOutputToSupport(TaskIndependentSupportRequestVO request) {
		analysis.analyseWords(request.getWords(), request.getCheckMathKeywords(), this);
		
	}
	
	public byte[] getAudio(){
		return audioStudent;
	}
	
	public void setAudio(byte[] currentAudioStudent){
		audioStudent = currentAudioStudent;
	}

	public String getMessage(){
		return message;
	}
	
	public boolean getPopUpWindow(){
		return popUpWindow;
	}
	
	public void setMessage(String value) {
		message = value;
	}

	public void setPopUpWindow(boolean value){
		popUpWindow = value;
	}
	
}
