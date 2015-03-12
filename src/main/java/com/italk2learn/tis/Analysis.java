package com.italk2learn.tis;

import java.util.ArrayList;
import java.util.List;

import ptdFromAmplitudes.CreateWav;
import ptdFromAmplitudes.PtdFromAmplitudes;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;
import com.italk2learn.vo.TaskIndependentSupportResponseVO;

public class Analysis {
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
		wrapper.setMessage("");
		wrapper.setType("");
	}
	
	public void startSupport(boolean start) {
		student = new StudentModel();
	}

	public void sendCurrentUserToSupport(String user) {
		currentUser = user;
	}
	
	public void analyseSound(byte[] audioByteArray){
		String wavname;
        List<byte[]> exampleChunks = new ArrayList<byte[]>();
        exampleChunks.add(audioByteArray);

        int numberOfChunksToCombine = exampleChunks.size();

        CreateWav wavcreation = new CreateWav();
        for (int i = 0; i < numberOfChunksToCombine; i++) {
            wavcreation.addChunk(exampleChunks.get(i));
        }

        // Initialize ptd classifier:
        PtdFromAmplitudes ptdAmpl = new PtdFromAmplitudes();

        // get perceived task difficulty (ptd):

        // Create wav from the last x (here numberOfChunksToCombine) chunks (x = seconds/5)
        wavname = wavcreation.createWavFileMonoOrStereo(numberOfChunksToCombine);
        
        int result = ptdAmpl.getPTD(wavname);
        
        
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
	
	public void analyseInteractionAndSetFeedback(List<String> feedback, String type, int level, boolean followed, boolean TDSviewed, TISWrapper wrapper){
		AffectDetector detector = new AffectDetector();
		student.setViewedMessage(TDSviewed);
		if (student.getHighMessage()) student.setViewedMessage(true);
		boolean viewed = student.viewedMessage();
		Affect interactionAffect = detector.getAffectFromInteraction(followed, viewed);
		
		if (student == null) student = new StudentModel();
		student.setAffectInteraction(interactionAffect);
		Affect combinedAffect = detector.getCombinedAffect(student, viewed);
		student.setCombinedAffect(combinedAffect);
		student.setFollowed(followed);
		
		
		Reasoner reasoner = new Reasoner();
		reasoner.affectiveStateReasoner(student, feedback, type, level, followed, wrapper);
		
	}
	
	public void checkIfSpeaking(TISWrapper wrapper){
		System.out.println("!!!!!");
		System.out.println("!!!!! check if student is speaking !!!!!");
		System.out.println("!!!!!");
		//check currentWordsFromLastMinute
		
		if ((currentWordsFromLastMinute != null) && (!student.areWeAtTheEnd())){
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
			MathsVocabDetector mathsDetector = new MathsVocabDetector();
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
		
		currentWordList.addAll(currentWords);
		
		AffectDetector detector = new AffectDetector();
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
