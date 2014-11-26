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
			if (current.equals("hard")) defaultAffect = Affect.furstration;
			if (current.equals("tricky")) defaultAffect = Affect.furstration;
			if (current.equals("complicated")) defaultAffect = Affect.furstration;
		}
		
		
		return defaultAffect;
	}

}
