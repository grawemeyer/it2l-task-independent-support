package com.italk2learn.tis;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;

public class Reasoner {
	
	
	
	public void affectiveStateReasoner(StudentModel student, List<String> feedback, String type, int level,  boolean followed, TISWrapper wrapper){
		updateBN(student, followed);
		
		Affect currentAffect = student.getCombinedAffect();
		
		/*
		 * BN values - affective state reasoner
		 * {affect_boosts, next_step, problem_solving, reflection}
		 * {{flowOr Enhancement: F, T}}
		 */
		int[][] feedbackValues = getfeedbackValues(student, followed);
		int[] feedbackTypes = getFeedbackWithHighestProbabilityForEnhancement(feedbackValues);
		String message = FeedbackData.reflectiveTask[0];
		
		String socratic = feedback.get(0);
		String guidance = feedback.get(1);
		String didacticConceptual = feedback.get(2);
		String didacticProcedural = feedback.get(3);
		
		if (type.equals("AFFIRMATION")){
			message = getFirstMessage(socratic, guidance, didacticConceptual, didacticProcedural);
		}
		else {
			for (int i = 0; i < feedbackTypes.length; i++){
				int currentFeedbackType = feedbackTypes[i];
				if (currentFeedbackType == FeedbackData.affectBoosts) {
					if (currentAffect.isConfusion()){
						message = getMessageFromArray(FeedbackData.affectBoostsForConfusion);
						student.setCurrentFeedbackType(FeedbackData.affectBoosts);
						i = feedbackTypes.length;
					}
					else if (currentAffect.isFrustration()){
						message = getMessageFromArray(FeedbackData.affectBoostsForFrustration);
						student.setCurrentFeedbackType(FeedbackData.affectBoosts);
						i = feedbackTypes.length;
					}
					else if (currentAffect.isBoredom()){
						message = getMessageFromArray(FeedbackData.affectBoostsForBoredom);
						student.setCurrentFeedbackType(FeedbackData.affectBoosts);
						i = feedbackTypes.length;
					}
				}
				else if (currentFeedbackType == FeedbackData.nextStep){
					if (type.equals("NEXT_STEP") || type.equals("PROBLEM_SOLVING")){
						if (level ==1){
							message = getFirstMessage (didacticConceptual, didacticProcedural, guidance);
							student.setCurrentFeedbackType(FeedbackData.nextStep);
							i= feedbackTypes.length;
						}
						else if (level==2){
							message = getFirstMessage (didacticProcedural, didacticConceptual, guidance);
							student.setCurrentFeedbackType(FeedbackData.nextStep);
							i= feedbackTypes.length;
						}
						else {
							message = getFirstMessage (guidance, didacticConceptual, didacticProcedural);
							student.setCurrentFeedbackType(FeedbackData.nextStep);
							i= feedbackTypes.length;
						}	
					}
				}
				else if (currentFeedbackType == FeedbackData.problemSolving){
					if (type.equals("NEXT_STEP") || type.equals("PROBLEM_SOLVING")){
						if (containsMessage(socratic)){
							student.setCurrentFeedbackType(FeedbackData.problemSolving);
							message = socratic;
							i= feedbackTypes.length;
						}
					}
				}
				else if  (currentFeedbackType == FeedbackData.reflection){
					student.setCurrentFeedbackType(FeedbackData.reflection);
					if (type.equals("REFLECTION")){
						message = getFirstMessage(socratic, guidance, didacticConceptual, didacticProcedural);
						i= feedbackTypes.length;
					}
					else {
						if (currentAffect.isFlow()){
							message = FeedbackData.reflectiveForflow;
						}
						else if (currentAffect.isConfusion()){
							message = FeedbackData.reflectiveForConfusion;
						}
						else if (currentAffect.isFrustration()){
							message = FeedbackData.reflectiveForFrustration;
						}
						else {
							message = getMessageFromArray(FeedbackData.reflectiveTask);
						}
						i = feedbackTypes.length;
					}
				}	
			}
		}
		
		Feedback displayFeedback = new Feedback();
		displayFeedback.sendFeedback(student, message, type, followed, wrapper);
	}
	
	
	private String getFirstMessage(String first, String second, String third) {
		String message = "";
		if (containsMessage(first)){
			message = first;
		}
		else if (containsMessage(second)){
			message = second;
		}
		else if (containsMessage(third)){
			message = third;
		}
		return message;
	}


	private String getFirstMessage(String socratic, String guidance,
			String didacticConceptual, String didacticProcedural) {
		String message = "";
		if (containsMessage(socratic)){
			message = socratic;
		}
		else if (containsMessage(guidance)){
			message = guidance;
		}
		else if (containsMessage(didacticConceptual)){
			message = didacticConceptual;
		}
		else if (containsMessage(didacticProcedural)){
			message = didacticProcedural;
		}
		return message;
	}


	private String getMessageFromArray(String[] messages){
		String result = messages[0];
		int length = messages.length;
		
		Random rn = new Random();
		int randomNumber = rn.nextInt(length);
		result = messages[randomNumber];
		
		return result;
	}
	
	private boolean containsMessage(String text) {
		if (text.equals("")){
			return false;
		}
		return true;
	}


	private int[] getFeedbackWithHighestProbabilityForEnhancement(int[][] feedbackValues) {
		int[] affectBoosts = feedbackValues[0];
		int[] nextStep = feedbackValues[1];
		int[] problemSolving = feedbackValues[2];
		int[] reflection = feedbackValues[3];
		int[] result = {FeedbackData.nextStep, FeedbackData.problemSolving, FeedbackData.reflection, FeedbackData.affectBoosts} ;
		
		double affectBoostsMultiplicator = 1/(affectBoosts[0] + affectBoosts[1]);
		double affectBoostsTrue = affectBoosts[1] * affectBoostsMultiplicator;
		
		double nextStepMultiplicator = 1/(nextStep[0] + nextStep[1]);
		double nextStepTrue = nextStep[1] * nextStepMultiplicator;
		
		double problemSolvingMultiplicator = 1/(problemSolving[0] + problemSolving[1]);
		double problemSolvingTrue = problemSolving[1] * problemSolvingMultiplicator;
		
		double reflectionMultiplicator = 1/(reflection[0] + reflection[1]);
		double reflectionTrue = reflection[1] * reflectionMultiplicator;
		
		double[] valuesForTrueValue = {affectBoostsTrue, nextStepTrue, problemSolvingTrue, reflectionTrue};
		double[] sortedValues = valuesForTrueValue;
		Arrays.sort(sortedValues);
		
		for (int i = 0; i < sortedValues.length; i++){
			double value = sortedValues[i];
			int feedbackType = FeedbackData.nextStep;
			
			for (int j=0; j < valuesForTrueValue.length; j++){
				double unsortedValue = valuesForTrueValue[j];
				if (value == unsortedValue){
					if (j==0) feedbackType = FeedbackData.affectBoosts;
					else if (j==1) feedbackType = FeedbackData.nextStep;
					else if (j==2) feedbackType = FeedbackData.problemSolving;
					else if (j==3) feedbackType= FeedbackData.reflection;
					j = valuesForTrueValue.length;
				}
			}
			result[i] = feedbackType;
		}
		return result;
	}


	public void startFeedbackForStructuredExercise(StudentModel student, TISWrapper wrapper) {
		Feedback feedback = new Feedback();
		
		Affect currentAffect = student.getAffectWords();
		if (currentAffect.isFrustration()){
			String message = getMessageFromArray(FeedbackData.affectBoostsForFrustration);
			feedback.sendFeedbackInStructuredExercise(message, wrapper);
		}
		else if (currentAffect.isConfusion()){
			String message = getMessageFromArray(FeedbackData.affectBoostsForConfusion);
			feedback.sendFeedbackInStructuredExercise(message, wrapper);
		}
		else if (currentAffect.isBoredom()){
			String message = getMessageFromArray(FeedbackData.affectBoostsForBoredom);
			feedback.sendFeedbackInStructuredExercise(message, wrapper);
		}
	}

	public void checkMathsWords(StudentModel student, boolean includesMathsWords, TISWrapper wrapper) {
		Feedback feedback = new Feedback();
		
		if (!includesMathsWords){
			//feedback for maths words
			String message = FeedbackData.mathsReminder;
			
			//for demo
			Affect affect = new Affect();
			affect.setFrustrationValue(0.5);
			student.setAffectWords(affect);
			
			feedback.sendFeedbackInStructuredExercise(message, wrapper);
		}
	}
	
	
	private void updateBN(StudentModel student, boolean followed){
		Affect currentAffect = student.getCombinedAffect();
		Affect previousAffect = student.getPreviousAffect();
		int lastFeedback = student.getCurrentFeedbackType();
		
		if (previousAffect.isFlow()){
			if (currentAffect.isFlow() || currentAffect.isSurprise()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFlowFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFlowFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFlowFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFlowFollowedReflection(0,1);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFlowNotFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFlowNotFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFlowNotFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFlowNotFollowedReflection(0,1);
					}
				}
			}
			else if (currentAffect.isConfusion() || currentAffect.isFrustration() || currentAffect.isBoredom()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFlowFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFlowFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFlowFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFlowFollowedReflection(1,0);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFlowNotFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFlowNotFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFlowNotFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFlowNotFollowedReflection(1,0);
					}
				}
			}
		}
		else if (previousAffect.isConfusion()){
			if (currentAffect.isFlow() || currentAffect.isSurprise()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateConfusionFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateConfusionFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateConfusionFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateConfusionFollowedReflection(0,1);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateConfusionNotFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateConfusionNotFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateConfusionNotFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateConfusionNotFollowedReflection(0,1);
					}
				}
			}
			else if (currentAffect.isConfusion() || currentAffect.isFrustration() || currentAffect.isBoredom()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateConfusionFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateConfusionFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateConfusionFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateConfusionFollowedReflection(1,0);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateConfusionNotFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateConfusionNotFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateConfusionNotFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateConfusionNotFollowedReflection(1,0);
					}
				}
			}
		}
		
		else if (previousAffect.isFrustration()){
			if (currentAffect.isFlow() || currentAffect.isSurprise() || currentAffect.isConfusion()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFrustrationFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFrustrationFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFrustrationFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFrustrationFollowedReflection(0,1);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFrustrationNotFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFrustrationNotFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFrustrationNotFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFrustrationNotFollowedReflection(0,1);
					}
				}
			}
			else if (currentAffect.isFrustration() || currentAffect.isBoredom()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFrustrationFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFrustrationFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFrustrationFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFrustrationFollowedReflection(1,0);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateFrustrationNotFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateFrustrationNotFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateFrustrationNotFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateFrustrationNotFollowedReflection(1,0);
					}
				}
			}
		}
		
		else if (previousAffect.isBoredom()){
			if (currentAffect.isFlow() || currentAffect.isSurprise() ){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateBoredomFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateBoredomFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateBoredomFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateBoredomFollowedReflection(0,1);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateBoredomNotFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateBoredomNotFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateBoredomNotFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateBoredomNotFollowedReflection(0,1);
					}
				}
			}
			else if (currentAffect.isConfusion() || currentAffect.isFrustration() || currentAffect.isBoredom()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateBoredomFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateBoredomFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateBoredomFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateBoredomFollowedReflection(1,0);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateBoredomNotFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateBoredomNotFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateBoredomNotFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateBoredomNotFollowedReflection(1,0);
					}
				}
			}
		}
		
		else if (previousAffect.isSurprise()){
			if (currentAffect.isFlow()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateSurpriseFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateSurpriseFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateSurpriseFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateSurpriseFollowedReflection(0,1);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateSurpriseNotFollowedAffectBoosts(0,1);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateSurpriseNotFollowedNextStep(0,1);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateSurpriseNotFollowedProblemSolving(0,1);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateSurpriseNotFollowedReflection(0,1);
					}
				}
			}
			else if (currentAffect.isSurprise() || currentAffect.isConfusion() || currentAffect.isFrustration() || currentAffect.isBoredom()){
				if (followed){
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateSurpriseFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateSurpriseFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateSurpriseFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateSurpriseFollowedReflection(1,0);
					}
				}
				else {
					if (lastFeedback == FeedbackData.affectBoosts){
						student.updateSurpriseNotFollowedAffectBoosts(1,0);
					}
					else if (lastFeedback == FeedbackData.nextStep){
						student.updateSurpriseNotFollowedNextStep(1,0);
					}
					else if (lastFeedback == FeedbackData.problemSolving){
						student.updateSurpriseNotFollowedProblemSolving(1,0);
					}
					else if (lastFeedback == FeedbackData.reflection){
						student.updateSurpriseNotFollowedReflection(1,0);
					}
				}
			}
		}
		
	}
	
	private int[][] getfeedbackValues(StudentModel student, boolean followed){
		Affect currentAffect = student.getCombinedAffect();
		int[][] feedbackValues  = {{0,0},{0,0},{0,0},{0,0}};
		
		
		if (currentAffect.isFlow()){
			if (followed){
				feedbackValues = student.getFlowFollowedValues();
			}
			else {
				feedbackValues = student.getFlowNotFollowedValues();
			}
		}
		else if (currentAffect.isConfusion()){
			if (followed){
				feedbackValues = student.getConfusionFollowedValues();
			}
			else {
				feedbackValues = student.getConfusionNotFollowedValues();
			}
		}
		else if (currentAffect.isFrustration()){
			if (followed){
				feedbackValues = student.getFrustrationFollowedValues();
			}
			else {
				feedbackValues = student.getFrustrationNotFollowedValues();
			}
		}
		else if (currentAffect.isBoredom()){
			if (followed){
				feedbackValues = student.getBoredomFollowedValues();
			}
			else {
				feedbackValues = student.getBoredomNotFollowedValues();
			}
		}
		else if (currentAffect.isSurprise()){
			if (followed){
				feedbackValues = student.getSurpriseFollowedValues();
			}
			else {
				feedbackValues = student.getSurpriseNotFollowedValues();
			}
			
		}
		return feedbackValues;
		
	}
	

}
