package com.italk2learn.tis;

import java.util.List;

public class AffectDetector {
	private String[] enjoymentBag = {"easy", "cake", "yes", "good", "enjpoyed", "OK", "interesting"};
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
		
		if (numberOfWordRecognised > 0){
			result = numberOfWordsInBag/numberOfWordRecognised;
		}
		return result;
	}
	
	private double caluclateProbability(double probabilityOfWordInAffect, double probabilityOfAffect, double probabilityOfWords){
		double result = 0.0;
				
		if ((probabilityOfWordInAffect != 0) && (probabilityOfAffect != 0) && (probabilityOfWords != 0)){
			result = ((probabilityOfWordInAffect * probabilityOfAffect) / probabilityOfWords);
		}
		return result;
	}

	
	public int getAffectFromWords(List<String> words) {
		int defaultAffect = Affect.enjoyment;
		
		int[] enjoymentValues = checkWordsInBag(words, enjoymentBag);
		int[] confusionValues = checkWordsInBag(words, confusionBag);
		int[] frustrationValues = checkWordsInBag(words, frustrationBag);
		int[] surpriseValues = checkWordsInBag(words, surpriseBag);
		int[] boredomValues = checkWordsInBag(words, boredomBag);
		
		double affectProability = 1/5;

		double enjoymentProbability = caluclateProbabilityForWordsInBag(enjoymentValues);
		double confusionProbability = caluclateProbabilityForWordsInBag(confusionValues);
		double frustrationProbability = caluclateProbabilityForWordsInBag(frustrationValues);
		double surpriseProbability = caluclateProbabilityForWordsInBag(surpriseValues);
		double boredomProbability = caluclateProbabilityForWordsInBag(boredomValues);
		
		double probabilityOfWords = (enjoymentProbability * affectProability) + (confusionProbability * affectProability) + 
				(frustrationProbability * affectProability) + (surpriseProbability * affectProability) + (boredomProbability * affectProability);
		
		double enjoyment = caluclateProbability(enjoymentProbability, affectProability, probabilityOfWords);
		double confusion = caluclateProbability(confusionProbability, affectProability, probabilityOfWords);
		double frustration = caluclateProbability(frustrationProbability, affectProability, probabilityOfWords);
		double surprise = caluclateProbability(surpriseProbability, affectProability, probabilityOfWords);
		double boredom = caluclateProbability(boredomProbability, affectProability, probabilityOfWords);
		
		
		if ((enjoyment > confusion) && (enjoyment > frustration) && (enjoyment > surprise) && (enjoyment > boredom)){
			defaultAffect = Affect.enjoyment;
		}
		else if ((confusion > enjoyment) && (confusion > frustration) && (confusion > surprise) && (confusion > boredom)){
			defaultAffect = Affect.confusion;
		}
		else if ((frustration > enjoyment) && (frustration > confusion) && (frustration > surprise) && (frustration > boredom)){
			defaultAffect = Affect.frustration;
		}
		else if ((surprise > enjoyment) && (surprise > confusion) && (surprise > frustration) && (surprise > boredom)){
			defaultAffect = Affect.surprise;
		}
		else if ((boredom > enjoyment) && (boredom > confusion) && (boredom > frustration) && (boredom > surprise)){
			defaultAffect = Affect.boredom;
		}
		return defaultAffect;
	}

}
