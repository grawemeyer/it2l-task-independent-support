package com.italk2learn.tis;

import java.util.List;

public class AffectDetector {
	private String[] flowBag = {"easy", "cake", "yes", "good", "enjoyed", "OK", "interesting"};
	private String[] confusionBag = {"get", "hard", "no", "why", "damn", "bugger", "tricky", "don't", "not", "sure", "complicated", "difficult"};
	private String[] frustrationBag = {"god", "flip", "flipping", "bloody", "hell"};
	private String[] surpriseBag={"wow"};
	private String[] boredomBag={"bored","boredom", "boring"};
	
	private String[] germanFlowBag = {"achja", "genau", "achso", "ja", "hier", "ah", "klar", "OK", "okidoki", "kapier", "kapiert", "einfach", "geschafft", "Spaß", "Kopf", "verstehe", "benutzt"};
	private String[] germanConfusionBag = {"Hinweis","irritiert","verguckt","verschrieben","verzählt","vertan","tippe","anstrengen","nachdenken","gerechnet","verrechnet","schwieriger","Hilfe","nochmal","erklären","merke","äh", "achso", "wie", "jetzt", "nein", "moment", "weiß", "nicht", "überlegen", "warte", "gar", "nee", "ne","versteh", "nicht", "kapier", "quatsch", "ups", "vergessen", "aufschreiben", "egal", "dünn", "hä", "so", "weg", "fällt", "Problem", "machen", "mach", "macht", "überlege", "rechnen", "wieso", "warum", "was", "falsch"};
	private String[] germanFrustrationBag = {"leck", "verarschen", "schitte", "kacke", "Hölle", "fuck", "Gott", "nein", "los","kapier", "weiß", "lange", "her", "pack", "doof", "doofes", "wütend", "nervig", "weglassen", "überspringe", "furchtbar", "nicht", "ach", "kann", "viel","noch","gemein","hasse", "voll","scheiße", "scheiß", "falsch","verdammt", "Pause", "shit", "blöd","boah"};
	private String[] germanSurpriseBag={"wow"};
	private String[] germanBoredomBag = {"vertauscht", "hasse", "kenne", "hatten","bla", "noch", "gleiche", "wieder", "nochmal", "gelangweilt", "langweilt", "langweilig"};
	
	boolean languageEnglish = true;
	boolean languageGerman = false;
	boolean languageSpanish = false;
	
	TISWrapper wrapper;
	
	
	public AffectDetector(boolean english, boolean german, boolean spanish, TISWrapper tisWrapper){
		languageEnglish = english;
		languageGerman = german;
		languageSpanish = spanish;
		wrapper = tisWrapper;
	}
	
	
	private int[] checkWordsInBag(List<String> words, String[] bag){
		int[] result = new int[bag.length];
	
		for (int j = 0; j < bag.length; j++){
			String affectWord = bag[j];
			for (int i = 0; i< words.size(); i++){
				String currentWord = words.get(i);
				currentWord.toLowerCase();
				if (currentWord.equals(affectWord)){
					System.out.println("::: word found: "+affectWord);
					wrapper.saveLog("TIS.affect.word.detected", affectWord);
					result[j] = 1;
					i = words.size();
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
		String[] thisFlowBag;
		String[] thisConfusionBag;
		String[] thisFrustrationBag;
		String[] thisSurpriseBag;
		String[] thisBoredomBag;
		
		if (languageGerman){
			thisFlowBag = germanFlowBag;
			thisConfusionBag = germanConfusionBag;
			thisFrustrationBag = germanFrustrationBag;
			thisSurpriseBag = germanSurpriseBag;
			thisBoredomBag = germanBoredomBag;
		}
		else {
			thisFlowBag = flowBag;
			thisConfusionBag = confusionBag;
			thisFrustrationBag = frustrationBag;
			thisSurpriseBag = surpriseBag;
			thisBoredomBag = boredomBag;
		}
		
		
		int[] flowValues = checkWordsInBag(words, thisFlowBag);
		int[] confusionValues = checkWordsInBag(words, thisConfusionBag);
		int[] frustrationValues = checkWordsInBag(words, thisFrustrationBag);
		int[] surpriseValues = checkWordsInBag(words, thisSurpriseBag);
		int[] boredomValues = checkWordsInBag(words, thisBoredomBag);
		
		double affectProability = 1.0/5.0;

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
		
		System.out.println("word flow: "+flow);
		System.out.println("word confusion: "+confusion);
		System.out.println("word frustration: "+frustration);
		System.out.println("word surprise: "+surprise);
		System.out.println("word boredom: "+boredom);
		wrapper.saveLog("TIS.affect.word.flow", ""+flow);
		wrapper.saveLog("TIS.affect.word.confusion", ""+confusion);
		wrapper.saveLog("TIS.affect.word.frustration", ""+frustration);
		wrapper.saveLog("TIS.affect.word.surprise", ""+surprise);
		wrapper.saveLog("TIS.affect.word.boredom", ""+boredom);
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
		else if (feedbackFollowed &&  !messageViewed){
			affect.setConfusionValue(viewedTrueFollowedTrueConfused);
			affect.setFlowValue(viewedTrueFollowedTrueFlow);
			affect.setFrustrationValue(viewedTrueFollowedTrueFrustrated);
		}
	
		System.out.println("interaction flow: "+affect.getFlowValue());
		System.out.println("interaction confusion: "+affect.getConfusionValue());
		System.out.println("interaction frustration: "+affect.getFrustrationValue());
		System.out.println("interaction surprise: "+affect.getSurpriseValue());
		System.out.println("interaction boredom: "+affect.getBoredomValue());
		wrapper.saveLog("TIS.affect.interaction.flow", ""+affect.getFlowValue());
		wrapper.saveLog("TIS.affect.interaction.confusion", ""+affect.getConfusionValue());
		wrapper.saveLog("TIS.affect.interaction.frustration", ""+affect.getFrustrationValue());
		wrapper.saveLog("TIS.affect.interaction.surprise", ""+affect.getSurpriseValue());
		wrapper.saveLog("TIS.affect.interaction.boredom", ""+affect.getBoredomValue());
		return affect;
	}
	
	public Affect getCombinedAffect(StudentModel student, boolean viewed){
		Affect affectWords = student.getAffectWords();
		Affect affectInteraction = student.getAffectInteraction();
		Affect affectSound = student.getAffectSound();
		Affect combinedAffect = new Affect();
		
		if (affectWords.isFlow()){
			combinedAffect.setFlowValue(affectWords.getFlowValue());
		}
		else if (affectWords.isConfusion()){
			combinedAffect.setConfusionValue(affectWords.getConfusionValue());
		}
		else if (affectWords.isFrustration()){
			combinedAffect.setFrustrationValue(affectWords.getFrustrationValue());
		}
		else if (affectWords.isBoredom()){
			combinedAffect.setBoredomValue(affectWords.getBoredomValue());
		}
		else if (affectWords.isSurprise()){
			combinedAffect.setSurpriseValue(affectWords.getSurpriseValue());
		}
		else if (affectSound.getPTD() == 1){
			if (affectInteraction.isConfusion()){
				combinedAffect.setConfusionValue(affectInteraction.getConfusionValue());
			}
			else if (affectInteraction.isFlow()){
				if (viewed){
					combinedAffect.setFlowValue(affectInteraction.getFlowValue());
				}
				else {
					combinedAffect.setFrustrationValue(0.5);
				}
			}
		}
		else if (affectSound.getPTD() == 2){
			if (affectInteraction.isConfusion()){
				combinedAffect.setConfusionValue(affectInteraction.getConfusionValue());
			}
			else if (affectInteraction.isFlow()){
				if (viewed){
					combinedAffect.setFlowValue(affectInteraction.getFlowValue());
				}
				else {
					combinedAffect.setFlowValue(affectInteraction.getFlowValue());
				}
			}
		}
		else if (affectSound.getPTD() == 3){
			if (affectInteraction.isConfusion()){
				combinedAffect.setConfusionValue(affectInteraction.getConfusionValue());
			}
			else if (affectInteraction.isFlow()){
				if (viewed){
					combinedAffect.setFlowValue(affectInteraction.getFlowValue());
				}
				else {
					combinedAffect.setBoredomValue(0.5);
				}
			}
		}
		else {
			combinedAffect = affectInteraction;
		}
		
		System.out.println("combined flow: "+combinedAffect.getFlowValue());
		System.out.println("combined confusion: "+combinedAffect.getConfusionValue());
		System.out.println("combined frustration: "+combinedAffect.getFrustrationValue());
		System.out.println("combined surprise: "+combinedAffect.getSurpriseValue());
		System.out.println("combined boredom: "+combinedAffect.getBoredomValue());
		wrapper.saveLog("TIS.affect.combined.flow", ""+combinedAffect.getFlowValue());
		wrapper.saveLog("TIS.affect.combined.confusion", ""+combinedAffect.getConfusionValue());
		wrapper.saveLog("TIS.affect.combined.frustration", ""+combinedAffect.getFrustrationValue());
		wrapper.saveLog("TIS.affect.combined.surprise", ""+combinedAffect.getSurpriseValue());
		wrapper.saveLog("TIS.affect.combined.boredom", ""+combinedAffect.getBoredomValue());
		
		return combinedAffect;
	}
	

}