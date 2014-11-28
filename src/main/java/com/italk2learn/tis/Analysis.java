package com.italk2learn.tis;

import java.util.List;

import com.italk2learn.vo.TaskIndependentSupportRequestVO;
import com.italk2learn.vo.TaskIndependentSupportResponseVO;

public class Analysis {
	boolean includeAffect = true;
	boolean presentAccordingToAffect = true;
	List<String> currentWords;
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

	public void analyseWords(List<String> currentWords, boolean checkMathsKeywords, TISWrapper wrapper) {
		//for (int i = 0; i < currentWords.size(); i++) {
		//	System.out.println(currentWords.get(i) + " ");
		//}
		
		AffectDetector detector = new AffectDetector();
		int currentAffect = detector.getAffect(currentWords);
		
		if (student == null) student = new StudentModel();
		student.setAffect(currentAffect);
		
		Reasoner reasoner = new Reasoner();
		reasoner.start(student, wrapper);
		
		if (checkMathsKeywords){
			MathsVocabDetector mathsDetector = new MathsVocabDetector();
			boolean includesMathsWords = mathsDetector.includesMathsWords(currentWords);
			System.out.println("includes maths words: "+includesMathsWords);
			reasoner.checkMathsWords(student, includesMathsWords, wrapper);
		}
		
		
		
	}
}
