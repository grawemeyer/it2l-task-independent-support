package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Reasoner {
	
	public void start(StudentModel student, TISWrapper wrapper) {
		Feedback feedback = new Feedback();
		
		Affect currentAffect = student.getAffect();
		if (currentAffect.isFrustration()){
			//display affect boosts or reflective prompt in pop-up window
			String message= FeedbackData.reflective2;	
			feedback.sendFeedback(student, message, wrapper);
		}
	}

	public void checkMathsWords(StudentModel student, boolean includesMathsWords, TISWrapper wrapper) {
		Feedback feedback = new Feedback();
		
		if (!includesMathsWords){
			//feedback for maths words
			String message = FeedbackData.mathsReminder1;
			
			//for demo
			Affect affect = new Affect();
			affect.setFrustrationValue(0.5);
			student.setAffect(affect);
			
			feedback.sendFeedback(student, message, wrapper);
		}
	}

}
