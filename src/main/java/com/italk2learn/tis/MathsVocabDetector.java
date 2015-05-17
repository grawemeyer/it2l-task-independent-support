package com.italk2learn.tis;

import java.util.List;

public class MathsVocabDetector {
	boolean languageEnglish = true;
	boolean languageGerman = false;
	boolean languageSpanish = false;
	
	public MathsVocabDetector(boolean english, boolean german, boolean spanish){
		languageEnglish = english;
		languageGerman = german;
		languageSpanish = spanish;
	}

	public boolean includesMathsWords(List<String> words) {
		//does this include numerator or denominator
		for (int i = 0; i< words.size(); i++){
			String current = words.get(i);
			
			if (languageEnglish){
				if (current.equals("numerator") || current.equals("Numerator") ||
						current.equals("denominator") || current.equals("Denominator")) return true;
			}
			if (languageGerman){
				if (current.equals("nenner") || current.equals("Nenner") ||
						current.equals("zähler") || current.equals("Zähler")) return true;
			}
		}
		
		return false;
	}
	
	

} 