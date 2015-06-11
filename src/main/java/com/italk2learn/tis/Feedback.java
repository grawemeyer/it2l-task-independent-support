package com.italk2learn.tis;


public class Feedback {
	
	
	public void sendFeedbackInStructuredExercise(StudentModel student, String message,  String type, TISWrapper wrapper){
		
		wrapper.saveLog("TIS.structuredExercise.feedback.presentation.input.type", type);
		wrapper.saveLog("TIS.structuredExercise.presentation.input.message", message);
		wrapper.saveLog("TIS.structuredExercise.presentation.popUp", "true");
		
		wrapper.setPopUpWindow(true);
		student.setHighMessage(wrapper.getPopUpWindow());
		wrapper.setMessage(message, true, type);
		Affect currentAffect = student.getCombinedAffect();
		String affectString = getCurrentAffectValueAsString(currentAffect);
		wrapper.setCurrentAffect(affectString);
		
		student.resetAffectWords();
	}
	
	public void playSound(){
		
	}
	
	public String getCurrentAffectValueAsString(Affect currentAffect){
		String confusion = "CONFUSION";
		String frustration = "FRUSTRATION";
		String boredom = "BOREDOM";
		String flow = "FLOW";
		String surprise = "SURPRISE";
		String result = flow;
		
		if (currentAffect.isFlow()){
			result = flow;
		}
		else if (currentAffect.isConfusion()){
			result = confusion;
		}
		else if (currentAffect.isFrustration()){
			result = frustration;
		}
		else if (currentAffect.isBoredom()){
			result = boredom;
		}
		else if (currentAffect.isSurprise()){
			result = surprise;
		}
		
		return result;
	}

	public void sendFeedback(StudentModel student, String message, String type, boolean followed, TISWrapper wrapper) {
		Affect currentAffect = student.getCombinedAffect();
		
		wrapper.saveLog("TIS.BN.feedback.presentation.input.message", message);
		wrapper.saveLog("TIS.BN.feedback.presentation.input.type", type);
		wrapper.saveLog("TIS.BN.feedback.presentation.input.followed", ""+followed);
		
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
			System.out.println(" FEEDBACK AFFIRMATION ");
			wrapper.setPopUpWindow(true);
			
			//not sure if we need to set this to stop checking spoken words as we need this for
			//the last reflective prompt.
			//student.setAtTheEnd(true);
		}
		else if (student.getFeedbackID().contains("E")){
			System.out.println(" FEEDBACK LAST REFLECTIVE PROMPT ");
			wrapper.setPopUpWindow(true);
		}
		else if (type.equals("TASK_NOT_FINISHED")){
			System.out.println(" FEEDBACK TASK_NOT_FINISHED ");
			wrapper.setPopUpWindow(true);
		}
		else if (student.getCurrentFeedbackType() == FeedbackData.talkAloud){
			System.out.println(" FEEDBACK TALK ALOUD PROMPT ");
			wrapper.setPopUpWindow(true);
		}
		else {
			System.out.println(" FEEDBACK NOT AFFIRMATION ");
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
					if (frustrationFollowedHigh >= frustrationFollowedLow){
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
		student.setHighMessage(wrapper.getPopUpWindow());
		System.out.println(" FEEDBACK high interruptive: "+wrapper.getPopUpWindow());
		student.resetAffectWords();
		wrapper.setMessage(message, wrapper.getPopUpWindow(), type);
		String affectString = getCurrentAffectValueAsString(currentAffect);
		System.out.println("<<<<<< affect: "+affectString+" followed: "+followed+" pop up: "+wrapper.getPopUpWindow()+" >>>>>>");
		
		wrapper.saveLog("TIS.BN.feedback.presentation.output.affect", affectString);
		wrapper.saveLog("TIS.BN.feedback.presentation.output.popUp", ""+wrapper.getPopUpWindow());
		
		wrapper.setCurrentAffect(affectString);
		wrapper.resetCurrentWordList();
	}


}