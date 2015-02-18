package com.italk2learn.tis;

public class StudentModel {
	Affect currentAffectWords = new Affect();
	Affect currentAffectInteraction = new Affect();
	Affect currentAffectSound = new Affect();
	
	public StudentModel(){
		
	}
	
	public void setAffectSound(Affect affect) {
		currentAffectSound = affect;
	}
	
	public Affect getAffectSound(){
		return currentAffectSound;
	}
	
	public void setAffectInteraction(Affect affect) {
		currentAffectInteraction = affect;
	}
	
	public Affect getAffectInteraction(){
		return currentAffectInteraction;
	}

	public void setAffectWords(Affect affect) {
		currentAffectWords = affect;
	}
	
	public Affect getAffectWords(){
		return currentAffectWords;
	}
}
