package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Feedback {
	
	public void sendEmotionsToTD(){
		
	}
	
	public void sendFeedback(StudentModel student, String message,  TISWrapper wrapper){
		Affect currentAffect = student.getAffectWords();
		wrapper.setMessage(message);
		
		if (currentAffect.isFrustration() || currentAffect.isConfusion()){
			wrapper.setPopUpWindow(true);
		}
		else {
			wrapper.setPopUpWindow(false);
		}
	}
	
	public void playSound(){
		
	}


}
