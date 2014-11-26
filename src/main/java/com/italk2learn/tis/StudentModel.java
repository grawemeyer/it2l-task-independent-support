package com.italk2learn.tis;

public class StudentModel {
	int currentAffect = Affect.enjoyment;
	
	public StudentModel(){
		
	}

	public void setAffect(int affect) {
		currentAffect = affect;
	}
	
	public int getAffect(){
		return currentAffect;
	}
}
