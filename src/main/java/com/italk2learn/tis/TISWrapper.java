package com.italk2learn.tis;

import java.util.List;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class TISWrapper {
	public boolean popUpWindow = true;
	public String message = "";
	Analysis analysis = new Analysis();
	public byte[] audioStudent;
	private boolean fractionsLabInUse = false;
	
	public TISWrapper(){
		analysis = new Analysis();
	}
	
	public void sendTDStoTIS(List<String> feedback, String type, int level, boolean followed, boolean viewed){
		System.out.println("followed: "+followed+" viewed: "+viewed);
		analysis.analyseSound(audioStudent);
		analysis.analyseInteractionAndSetFeedback(feedback, type, level, followed, viewed, this);
	}
	
	public void sendSpeechOutputToSupport(TaskIndependentSupportRequestVO request) {
		analysis.analyseWords(request.getWords(), this);
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
