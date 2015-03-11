package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Feedback {
	
	
	public void sendFeedbackInStructuredExercise(StudentModel student, String message,  TISWrapper wrapper){
		wrapper.setMessage(message);
		wrapper.setPopUpWindow(true);
		student.resetAffectWords();
	}
	
	public void playSound(){
		
	}

	public void sendFeedback(StudentModel student, String message, String type, boolean followed, TISWrapper wrapper) {
		Affect currentAffect = student.getCombinedAffect();
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
				System.out.println(" FEEDBACK current affect FLOW and followed: "+followed);
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
				System.out.println(" FEEDBACK current affect CONFUSION and followed: "+followed);
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
				System.out.println(" FEEDBACK current affect FRUSTATION and followed: "+followed);
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
			else {
				if (currentAffect.isBoredom()) System.out.println(" FEEDBACK current affect BOREDOM and followed: "+followed);
				if (currentAffect.isSurprise()) System.out.println(" FEEDBACK current affect SURPRISE and followed: "+followed);
			
			}
		}
		System.out.println(" FEEDBACK high interruptive: "+wrapper.getPopUpWindow());
		student.resetAffectWords();
	}


}
