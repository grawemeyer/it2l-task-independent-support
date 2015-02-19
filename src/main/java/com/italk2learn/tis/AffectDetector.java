package com.italk2learn.tis;

import java.util.List;

public class AffectDetector {
	private String[] flowBag = {"easy", "cake", "yes", "good", "enjpoyed", "OK", "interesting"};
	private String[] confusionBag = {"get", "hard", "no", "why", "dam", "bugger", "tricky", "don't", "get", "not", "sure", "complicated"};
	private String[] frustrationBag = {"god", "flip", "flipping", "bloody", "hell"};
	private String[] surpriseBag={"blimey"};
	private String[] boredomBag={"bored","boredom", "boring"};
	
	public AffectDetector(){
	}
	
	
	private int[] checkWordsInBag(List<String> words, String[] bag){
		int[] result = new int[bag.length];
		
		for (int i = 0; i< words.size(); i++){
			String currentWord = words.get(i);
			for (int j = 0; j < bag.length; j++){
				String affectWord = bag[j];
				if (currentWord.equals(affectWord)){
					result[j] = 1;
				}
				else {
					result[j] = 0;
				}
			}
		}
		return result;
	}
	
	
	private double caluclateProbabilityForWordsInBag(int[] affectValues){
		double result = 0.0;
		int numberOfWordsInBag = affectValues.length;
		int numberOfWordRecognised = 0;
		
		for (int i = 0; i < numberOfWordsInBag; i++){
			int currentValue = affectValues[i];
			if (currentValue == 1) numberOfWordRecognised +=1;
		}
		
		if (numberOfWordRecognised == 1) result = 0.7;
		else if (numberOfWordRecognised == 2) result = 0.85;
		else if (numberOfWordRecognised > 2) result = 0.95;
		
		return result;
	}
	
	private double caluclateProbability(double probabilityOfWordInAffect, double probabilityOfAffect, double probabilityOfWords){
		double result = 0.0;
				
		if ((probabilityOfWordInAffect != 0) && (probabilityOfAffect != 0) && (probabilityOfWords != 0)){
			result = ((probabilityOfWordInAffect * probabilityOfAffect) / probabilityOfWords);
		}
		return result;
	}

	
	public Affect getAffectFromWords(List<String> words) {
		Affect affect = new Affect();
		
		int[] flowValues = checkWordsInBag(words, flowBag);
		int[] confusionValues = checkWordsInBag(words, confusionBag);
		int[] frustrationValues = checkWordsInBag(words, frustrationBag);
		int[] surpriseValues = checkWordsInBag(words, surpriseBag);
		int[] boredomValues = checkWordsInBag(words, boredomBag);
		
		double affectProability = 1/5;

		double flowProbability = caluclateProbabilityForWordsInBag(flowValues);
		double confusionProbability = caluclateProbabilityForWordsInBag(confusionValues);
		double frustrationProbability = caluclateProbabilityForWordsInBag(frustrationValues);
		double surpriseProbability = caluclateProbabilityForWordsInBag(surpriseValues);
		double boredomProbability = caluclateProbabilityForWordsInBag(boredomValues);
		
		double probabilityOfWords = (flowProbability * affectProability) + (confusionProbability * affectProability) + 
				(frustrationProbability * affectProability) + (surpriseProbability * affectProability) + (boredomProbability * affectProability);
		
		double flow = caluclateProbability(flowProbability, affectProability, probabilityOfWords);
		double confusion = caluclateProbability(confusionProbability, affectProability, probabilityOfWords);
		double frustration = caluclateProbability(frustrationProbability, affectProability, probabilityOfWords);
		double surprise = caluclateProbability(surpriseProbability, affectProability, probabilityOfWords);
		double boredom = caluclateProbability(boredomProbability, affectProability, probabilityOfWords);
		
		System.out.println("flow: "+flow);
		System.out.println("confusion: "+confusion);
		System.out.println("frustration: "+frustration);
		System.out.println("surprise: "+surprise);
		System.out.println("boredom: "+boredom);
		affect.setFlowValue(flow);
		affect.setConfusionValue(confusion);
		affect.setFrustrationValue(frustration);
		affect.setSurpriseValue(surprise);
		affect.setBoredomValue(boredom);
		
		return affect;
	}
	
	public Affect getAffectFromInteraction(boolean feedbackFollowed, boolean messageViewed) {
		Affect affect = new Affect();
		
		double viewedFalseFollowedFalseConfused = 0.23;
		double viewedFalseFollowedFalseFlow = 0.615;
		double viewedFalseFollowedFalseFrustrated = 0.15;
		
		double viewedFalseFollowedTrueConfused = 0.0;
		double viewedFalseFollowedTrueFlow = 0.0;
		double viewedFalseFollowedTrueFrustrated = 0.0;
		
		double viewedTrueFollowedFalseConfused = 0.53;
		double viewedTrueFollowedFalseFlow = 0.45;
		double viewedTrueFollowedFalseFrustrated = 0.07;
		
		double viewedTrueFollowedTrueConfused = 0.23;
		double viewedTrueFollowedTrueFlow = 0.76;
		double viewedTrueFollowedTrueFrustrated = 0.01;
		
		if (feedbackFollowed && messageViewed){
			affect.setConfusionValue(viewedTrueFollowedTrueConfused);
			affect.setFlowValue(viewedTrueFollowedTrueFlow);
			affect.setFrustrationValue(viewedTrueFollowedTrueFrustrated);
		}
		else if (!feedbackFollowed && messageViewed){
			affect.setConfusionValue(viewedTrueFollowedFalseConfused);
			affect.setFlowValue(viewedTrueFollowedFalseFlow);
			affect.setFrustrationValue(viewedTrueFollowedFalseFrustrated);
		}
		else if (!feedbackFollowed &&  !messageViewed){
			affect.setConfusionValue(viewedFalseFollowedFalseConfused);
			affect.setFlowValue(viewedFalseFollowedFalseFlow);
			affect.setFrustrationValue(viewedFalseFollowedFalseFrustrated);
		}
	
		return affect;
	}
	
	

}
