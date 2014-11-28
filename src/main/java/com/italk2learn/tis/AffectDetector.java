package com.italk2learn.tis;

import java.util.List;

public class AffectDetector {
	
	public AffectDetector(){
	}
	
	
	public int getAffect(List<String> words) {
		int defaultAffect = Affect.enjoyment;
		//hard, tricky, complicated
		for (int i = 0; i< words.size(); i++){
			String current = words.get(i);
			if (current.equals("hard") || current.equals("tricky")) {
				defaultAffect = Affect.furstrationFL;
				System.out.println("affect: frustration");
			}
			if (current.equals("complicated")){
				defaultAffect = Affect.furstration;
				System.out.println("affect: frustration");
			}
		}
		
		
		return defaultAffect;
	}

}
