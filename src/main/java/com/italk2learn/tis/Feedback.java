package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Feedback {
	
	
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

	public void sendFeedback(StudentModel student, String message, String type, boolean followed, TISWrapper wrapper) {
		Affect currentAffect = student.getAffectWords();
		wrapper.setMessage(message);
		wrapper.setPopUpWindow(false);
		
		double flowNotFollowedHigh = 0.8;
		double flowNotFollowedLow = 0.8;
		double flowFollowedHigh = 0.8;
		double flowFollowedLow = 0.8;
		double confusionNotFollowedHigh = 0.34;
		double confusionNotFollowedLow = 0.32;
		double confusionFollowedHigh = 0.56;
		double confusionFollowedLow = 0.34;
		double frustrationNotFollowedHigh = 0.5;
		double frustrationNotFollowedLow = 0.0;
		double frustrationFollowedHigh = 1.0;
		double frustrationFollowedLow = 1.0;
		
		if (type.equals("AFFIRMATION")){
			wrapper.setPopUpWindow(true);
		}
		else {
			if (currentAffect.isFlow()){
				if (followed){
					if (flowFollowedHigh > flowFollowedLow){
						wrapper.setPopUpWindow(true);
					}
					else {
						wrapper.setPopUpWindow(false);
					}
				}
				else {
					if (flowNotFollowedHigh > flowNotFollowedLow){
						wrapper.setPopUpWindow(true);
					}
					else {
						wrapper.setPopUpWindow(false);
					}
				}
			}
			else if (currentAffect.isConfusion()){
				if (followed){
					if (confusionFollowedHigh > confusionFollowedLow){
						wrapper.setPopUpWindow(true);
					}
					else {
						wrapper.setPopUpWindow(false);
					}
				}
				else {
					if (confusionNotFollowedHigh > confusionNotFollowedLow){
						wrapper.setPopUpWindow(true);
					}
					else {
						wrapper.setPopUpWindow(false);
					}
				}
			}
			else if (currentAffect.isFrustration()){
				if (followed){
					if (frustrationFollowedHigh > frustrationFollowedLow){
						wrapper.setPopUpWindow(true);
					}
					else {
						wrapper.setPopUpWindow(false);
					}
				}
				else {
					if (frustrationNotFollowedHigh >= frustrationNotFollowedLow){
						wrapper.setPopUpWindow(true);
					}
					else {
						wrapper.setPopUpWindow(false);
					}
				}
			}
		}
		
	}


}
