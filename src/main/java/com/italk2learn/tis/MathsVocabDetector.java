package com.italk2learn.tis;

import java.util.List;

public class MathsVocabDetector {
	
	public MathsVocabDetector(){
	}

	public boolean includesMathsWords(List<String> words) {
		//does this include numerator or denominator
		for (int i = 0; i< words.size(); i++){
			String current = words.get(i);
			if (current.equals("numerator") || current.equals("Numerator") ||
				current.equals("denominator") || current.equals("Denominator")) return true;
		}
		
		return false;
	}
	
	

}
