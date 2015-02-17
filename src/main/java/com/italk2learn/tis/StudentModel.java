package com.italk2learn.tis;

public class StudentModel {
	Affect currentAffect = new Affect();
	
	public StudentModel(){
		
	}

	public void setAffect(Affect affect) {
		currentAffect = affect;
	}
	
	public Affect getAffect(){
		return currentAffect;
	}
}
