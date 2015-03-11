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
	List<String> currentWords;
	List<String> currentWordsFromLastMinute;
	String currentUser;
	private StudentModel student;

	public Analysis() {
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
	
	public void analyseInteractionAndSetFeedback(List<String> feedback, String type, int level, boolean followed, boolean viewed, TISWrapper wrapper){
		AffectDetector detector = new AffectDetector();
		Affect interactionAffect = detector.getAffectFromInteraction(followed, viewed);
		
		if (student == null) student = new StudentModel();
		student.setAffectInteraction(interactionAffect);
		Affect combinedAffect = detector.getCombinedAffect(student, viewed);
		student.setCombinedAffect(combinedAffect);
		student.setFollowed(followed);
		student.setViewedMessage(viewed);
		
		Reasoner reasoner = new Reasoner();
		reasoner.affectiveStateReasoner(student, feedback, type, level, followed, wrapper);
		
	}
	
	public void checkIfSpeaking(TISWrapper wrapper){
		System.out.println("!!!!!");
		System.out.println("!!!!! check if student is speaking !!!!!");
		System.out.println("!!!!!");
		//check currentWordsFromLastMinute
		
		if (currentWordsFromLastMinute != null){
			Reasoner reasoner = new Reasoner();
			if (student == null) student = new StudentModel();
			reasoner.checkSpokenWords(currentWordsFromLastMinute, student, wrapper);
			currentWordsFromLastMinute.clear();
		}
		
	}
	
	

	public void analyseWords(List<String> currentWords, TISWrapper wrapper) {
		//check if the current student is speaking or not.
		//if not then check for how long and then send message
		//else detect affect.
		
		if (currentWordsFromLastMinute == null){
			currentWordsFromLastMinute = new ArrayList<String>();
		}
		currentWordsFromLastMinute.addAll(currentWords);
		
		AffectDetector detector = new AffectDetector();
		Affect currentAffect = detector.getAffectFromWords(currentWords);
		
		if (student == null) student = new StudentModel();
		student.addAffectWords(currentAffect);
		
		Reasoner reasoner = new Reasoner();
		
		boolean checkMathsKeywords = false;
		//check if this needs to be set after a particular time or when student stops
		if ((student.getCurrentFeedbackType() == FeedbackData.reflection) && (student.getHighMessage() ||
				((!student.getHighMessage()) && student.viewedMessage()))){
			checkMathsKeywords = true;
		}
		else {
			checkMathsKeywords = false;
		}
		if (checkMathsKeywords){
			MathsVocabDetector mathsDetector = new MathsVocabDetector();
			boolean includesMathsWords = mathsDetector.includesMathsWords(currentWords);
			System.out.println("::TIS:: includes maths words: "+includesMathsWords);
			reasoner.checkMathsWords(student, includesMathsWords, wrapper);
		}
		else if (!wrapper.getFractionsLabInUse()) {
			reasoner.startFeedbackForStructuredExercise(student, wrapper);
		}

	}
}
