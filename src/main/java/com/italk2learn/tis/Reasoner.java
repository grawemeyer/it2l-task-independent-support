package com.italk2learn.tis;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Reasoner {
	
	public void start(StudentModel student, TISWrapper wrapper) {
		Feedback feedback = new Feedback();
		
		int currentAffect = student.getAffect();
		if (currentAffect == Affect.furstration){
			//display affect boosts or reflective prompt in pop-up window
			String message = FeedbackData.affectBoost1;
			feedback.sendFeedback(student, message, wrapper);
		}
	}

	public void checkMathsWords(StudentModel student, boolean includesMathsWords, TISWrapper wrapper) {
		if (!includesMathsWords){
			//feedback for maths words
			String message = FeedbackData.mathsReminder1;
			Feedback feedback = new Feedback();
			feedback.sendFeedback(student, message, wrapper);
		}
		
	}

}
