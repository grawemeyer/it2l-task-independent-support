package com.italk2learn.tis;

import java.util.ArrayList;
import java.util.List;

import ptdFromAmplitudesTIS.CreateWavTIS;
import ptdFromAmplitudesTIS.PtdFromAmplitudesTIS;

public class Analysis {
	
	//524288 each 5 seconds
	//12 times per minute
	private static final int SIZE_AUDIO_1MINUTES = 1 * 11 * 524288;
	
	
	boolean includeAffect = true;
	boolean presentAccordingToAffect = true;
	List<String> currentWordList;
	List<String> currentWordsFromLastMinute;
	String currentUser;
	private StudentModel student;

	public Analysis() {
	}

	public void resetVariablesForNewExercise(TISWrapper wrapper){
		student.setAtTheEnd(false);
		wrapper.resetMessage();
		wrapper.setType("");
	}
	
	public void startSupport(boolean start) {
		student = new StudentModel();
	}

	public void sendCurrentUserToSupport(String user) {
		currentUser = user;
	}
	
	public void analyseSound(byte[] audioByteArray){
		int result;
		if (audioByteArray!=null && audioByteArray.length>SIZE_AUDIO_1MINUTES) {
			String wavname;
	        List<byte[]> exampleChunks = new ArrayList<byte[]>();
	        exampleChunks.add(audioByteArray);
	
	        int numberOfChunksToCombine = exampleChunks.size();
	
	        CreateWavTIS wavcreation = new CreateWavTIS();
	        for (int i = 0; i < numberOfChunksToCombine; i++) {
	            wavcreation.addChunk(exampleChunks.get(i));
	        }
	
	        // Initialize ptd classifier:
	        PtdFromAmplitudesTIS ptdAmpl = new PtdFromAmplitudesTIS();
	
	        // get perceived task difficulty (ptd):
	
	        // Create wav from the last x (here numberOfChunksToCombine) chunks (x = seconds/5)
	        //wavname = wavcreation.createWavFileMonoOrStereo(numberOfChunksToCombine);
	        wavname = wavcreation.createWavFile(numberOfChunksToCombine);
	        
	        result = ptdAmpl.getPTD(wavname);
        
		} else {
			result=-1;
		}
        
        if (result == -1){
        	System.out.println("PTD: no result");
        }
        else if (result == 1){
        	System.out.println("PTD: overchallenged");
        }
        else if (result == 2){
        	System.out.println("PTD: flow");
        }
        else if (result == 3){
        	System.out.println("PTD: underchallenged");
        }
        		
        Affect affectSound = new Affect();
        affectSound.setPTD(result);
        if (student == null) student = new StudentModel();
		student.setAffectSound(affectSound);
	}
	
	public void analyseInteractionAndSetFeedback(List<String> feedback, String type, String feedbackID, int level, boolean followed, boolean TDSviewed, TISWrapper wrapper){
		if (student == null) student = new StudentModel();
		
		AffectDetector detector = new AffectDetector(wrapper.isLanguageEnglish(), wrapper.isLanguageGerman(), wrapper.isLanguageSpanish());
		student.setViewedMessage(TDSviewed);
		if (student.getHighMessage()) student.setViewedMessage(true);
		boolean viewed = student.viewedMessage();
		Affect interactionAffect = detector.getAffectFromInteraction(followed, viewed);
		
		student.setAffectInteraction(interactionAffect);
		Affect combinedAffect = detector.getCombinedAffect(student, viewed);
		student.setCombinedAffect(combinedAffect);
		student.setFollowed(followed);
		student.setFeedbackID(feedbackID);
		
		Reasoner reasoner = new Reasoner();
		reasoner.affectiveStateReasoner(student, feedback, feedbackID, type, level, followed, wrapper);
		
	}
	
	public void checkIfSpeaking(TISWrapper wrapper){
		//System.out.println("!!!!!");
		//System.out.println("!!!!! check if student is speaking !!!!!");
		//System.out.println("!!!!!");
		//check currentWordsFromLastMinute
		
		if ((currentWordsFromLastMinute != null) && ((student != null)&& (!student.areWeAtTheEnd()))){
			Reasoner reasoner = new Reasoner();
			if (student == null) student = new StudentModel();
			reasoner.checkSpokenWords(currentWordsFromLastMinute, student, wrapper);
			currentWordsFromLastMinute.clear();
		}
		
	}
	
	private void printCurrentFeedbackType(){
		if (student.getCurrentFeedbackType() == FeedbackData.reflection){
			System.out.println("last feedback REFLECTION");
		}
		else if (student.getCurrentFeedbackType() == FeedbackData.affectBoosts){
			System.out.println("last feedback AFFECT BOOSTS");
		}
		else if (student.getCurrentFeedbackType() == FeedbackData.nextStep){
			System.out.println("last feedback NEXT STEP");
		}
		else if (student.getCurrentFeedbackType() == FeedbackData.problemSolving){
			System.out.println("last feedback PROBLEM SOLVING");
		}
		else if (student.getCurrentFeedbackType() == FeedbackData.mathsVocabular){
			System.out.println("last feedback MATHS VOCAB");
		}
		else if (student.getCurrentFeedbackType() == FeedbackData.talkAloud){
			System.out.println("last feedback TALK ALOUD");
		}
	}
	
	public void checkForMathsWords(TISWrapper wrapper){
		System.out.println(":::: checkForMathsWords ::::");
		boolean checkMathsKeywords = false;
		if ((student.getCurrentFeedbackType() == FeedbackData.reflection) && (student.getHighMessage() ||
				((!student.getHighMessage()) && student.viewedMessage()))){
			checkMathsKeywords = true;
		}
		else {
			checkMathsKeywords = false;
		}
		if (checkMathsKeywords){
			MathsVocabDetector mathsDetector = new MathsVocabDetector(wrapper.isLanguageEnglish(), wrapper.isLanguageGerman(), wrapper.isLanguageSpanish());
			boolean includesMathsWords = mathsDetector.includesMathsWords(currentWordList);
			System.out.println("::TIS:: includes maths words: "+includesMathsWords);
			Reasoner reasoner = new Reasoner();
			reasoner.checkMathsWords(student, includesMathsWords, wrapper);
		}
	}
	
	public void resetCurrentWordList(){
		currentWordList = new ArrayList<String>();
	}

	public void analyseWords(List<String> currentWords, TISWrapper wrapper) {
		//check if the current student is speaking or not.
		//if not then check for how long and then send message
		//else detect affect.
		
		if (currentWordsFromLastMinute == null){
			currentWordsFromLastMinute = new ArrayList<String>();
		}
		currentWordsFromLastMinute.addAll(currentWords);
		
		if (currentWordList == null){
			currentWordList= new ArrayList<String>();
		}
		
		currentWordList.addAll(currentWords);
		
		AffectDetector detector = new AffectDetector(wrapper.isLanguageEnglish(), wrapper.isLanguageGerman(), wrapper.isLanguageSpanish());
		Affect currentAffect = detector.getAffectFromWords(currentWords);
		
		if (student == null) student = new StudentModel();
		student.addAffectWords(currentAffect);
		
		Reasoner reasoner = new Reasoner();
		printCurrentFeedbackType();
		
		if (!wrapper.getFractionsLabInUse()) {
			reasoner.startFeedbackForStructuredExercise(student, wrapper);
		}

	}
}