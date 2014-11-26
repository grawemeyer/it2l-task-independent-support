package com.italk2learn.vo;

import java.util.List;

public class TaskIndependentSupportRequestVO extends RequestVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> words;
	
	private Boolean checkMathKeywords;
	
	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> words) {
		this.words = words;
	}
	public Boolean getCheckMathKeywords() {
		return checkMathKeywords;
	}
	public void setCheckMathKeywords(Boolean checkMathKeywords) {
		this.checkMathKeywords = checkMathKeywords;
	} 

}
