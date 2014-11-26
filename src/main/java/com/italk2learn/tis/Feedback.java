package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Feedback {
	
	public void sendEmotionsToTD(){
		
	}
	
	public void sendFeedback(StudentModel student, String message,  TISWrapper wrapper){
		int currentAffect = student.getAffect();
		wrapper.setMessage(message);
		if ((currentAffect == Affect.furstration) || (currentAffect == Affect.confusion)){
			wrapper.setPopUpWindow(true);
		}
		else {
			wrapper.setPopUpWindow(false);
		}
		
	}

	public void playSound(){
		
	}


}
