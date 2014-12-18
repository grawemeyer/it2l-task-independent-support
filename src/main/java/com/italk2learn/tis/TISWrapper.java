package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class TISWrapper {
	public boolean popUpWindow = true;
	public String message = "";
	Analysis analysis = new Analysis();
	
	public TISWrapper(){
		analysis = new Analysis();
	}
	
	public void sendTDStoTIS(String feedbackMessage, String currentFeedbackType, String previousFeedbackType, boolean followed){
		System.out.println("feedbackMessage: "+feedbackMessage);
	}
	
	public void sendSpeechOutputToSupport(TaskIndependentSupportRequestVO request) {
		analysis.analyseWords(request.getWords(), request.getCheckMathKeywords(), this);
		
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
