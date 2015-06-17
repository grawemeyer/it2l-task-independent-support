package com.italk2learn.tis;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;

/*import com.italk2learn.bo.inter.ILoginUserService;
import com.italk2learn.dao.inter.ITISLogDAO;
import com.italk2learn.exception.ITalk2LearnException;
import com.italk2learn.tis.inter.ITISWrapper;
import com.italk2learn.vo.ExerciseSequenceRequestVO;
import com.italk2learn.vo.HeaderVO;*/

@Service("TISWrapperService")
public class TISWrapper {//implements ITISWrapper {

	//@Autowired
	//public ILoginUserService loginUserService;
	//@Autowired
	//public ITISLogDAO tisLogDAO;

	public boolean popUpWindow = true;
	public String message = "";
	public String feedbackType = "";
	public String affectString = "";
	Analysis analysis = new Analysis(this);
	public byte[] audioStudent;
	private boolean fractionsLabInUse = false;
	Timer uploadCheckMathsWordsTimer;
	TimerTask timerSpeechMathsWords;
	boolean firstTime = false;
	String currentUser = "";
	String startUser = "";
	boolean doneButtonPressed = true;
	boolean languageEnglish = true;
	boolean languageSpanish = false;
	boolean languageGerman = false;
	boolean TDSfeedback = false;
	boolean checkForMathsVocab = false;
	String lastMessage = "";
	String lastType = "";
	TimerTask timerSpeechTask;
	Timer uploadCheckerTimer;

	public TISWrapper() {}
	
	public void startTIS(){
		startUser = "anonym";
		analysis = new Analysis(this); 
		analysis.setStudentModel();
		timerSpeechTask = new TimerForSpeechCheck();
		((TimerForSpeechCheck) timerSpeechTask).setAnalysis(analysis);
		uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(timerSpeechTask, 30 * 1000,
				60 * 1000);
	}

	public void stopTimers(){
		if (uploadCheckMathsWordsTimer != null) {
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			uploadCheckMathsWordsTimer = null;
		}
		if (timerSpeechMathsWords != null){
			timerSpeechMathsWords.cancel();
			timerSpeechMathsWords = null;
		}
		if (timerSpeechTask != null){
			timerSpeechTask.cancel();
			timerSpeechTask = null;
		}
		if (uploadCheckerTimer != null){
			uploadCheckerTimer.cancel();
			uploadCheckerTimer = null;
		}
	}
	
	public void setLanguageInTIStoEnglish() {
		saveLog("TIS.language", "english");
		languageEnglish = true;
		languageSpanish = false;
		languageGerman = false;
	}

	public void setLanguageInTIStoSpanish() {
		saveLog("TIS.language", "spanish");
		languageEnglish = false;
		languageSpanish = true;
		languageGerman = false;
	}

	public void setLanguageInTIStoGerman() {
		saveLog("TIS.language", "german");
		languageEnglish = false;
		languageSpanish = false;
		languageGerman = true;
	}

	public boolean isLanguageEnglish() {
		return languageEnglish;
	}

	public boolean isLanguageSpanish() {
		return languageSpanish;
	}

	public boolean isLanguageGerman() {
		return languageGerman;
	}

	public void sendDoneButtonPressedToTIS(boolean value) {
		doneButtonPressed = value;
		saveLog("TIS.TDS.doneButtonPressed", "" + value);
		if (!value) {
			saveLog("TIS.wieved.feedback", lastMessage);
			saveLog("TIS.wieved.type", lastType);
			checkMathsWords();
		}
	}

	public void sendTDStoTIS(String user, List<String> feedback, String type,
			String feedbackID, int level, boolean followed, boolean viewed) {
		System.out.println("::: TDStoTIS :::");
		System.out.println("::: feedback type ::: " + type);
		System.out.println("::: feedback id ::: " + feedbackID);
		System.out.println("followed: " + followed + " viewed: " + viewed);
		System.out.println("::: fractionsLabInUse::: " + fractionsLabInUse);

		currentUser = user;
		if (fractionsLabInUse) {
			analysis.analyseSound(audioStudent, this);
			if (firstTime) {
				followed = true;
				viewed = true;
				firstTime = false;
			}
			analysis.analyseInteractionAndSetFeedback(feedback, type,
					feedbackID, level, followed, viewed, this);
		}
	}

	public void sendSpeechOutputToSupport(String user, List<String> currentWords) {// ,
																					// TaskIndependentSupportRequestVO
																					// request)
																					// {
		//Database connection problem
		//saveLog("TIS.speech.user", user);
		currentUser = user;
		// analysis.analyseWords(request.getWords(), this);
		analysis.analyseWords(currentWords, this);
	}

	public void startNewExercise() {
		System.out.println(":: startNewExercise ::");
		analysis.resetVariablesForNewExercise(this);
		doneButtonPressed = true;
		if (uploadCheckMathsWordsTimer != null) {
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			timerSpeechMathsWords.cancel();
		}
		firstTime = true;
	}

	public void setFractionsLabinUse(boolean value) {
		saveLog("TIS.TDS.fractionsLab", "" + value);
		fractionsLabInUse = value;
	}

	public boolean getFractionsLabInUse() {
		return fractionsLabInUse;
	}

	public byte[] getAudio() {
		return audioStudent;
	}

	public void setAudio(byte[] currentAudioStudent) {
		audioStudent = currentAudioStudent;
	}

	public String getMessage() {
		String result = "";
		if (message.length() > 0) {
			result = message;
			saveLog("TIS.provideMessage", message);
		}
		message = "";
		return result;
	}

	public String getFeedbackType() {
		String result = feedbackType;
		feedbackType = "";
		return result;
	}

	public String getCurrentAffect() {
		String result = affectString;
		affectString = "";
		return result;
	}

	public boolean getPopUpWindow() {
		return popUpWindow;
	}

	private void checkMathsWordsTimer() {
		if (uploadCheckMathsWordsTimer != null) {
			uploadCheckMathsWordsTimer.cancel();
			uploadCheckMathsWordsTimer.purge();
			timerSpeechMathsWords.cancel();
		}
	}

	public void saveLog(String name, String value) {
		//ExerciseSequenceRequestVO request = new ExerciseSequenceRequestVO();
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			LdapUserDetailsImpl user = (LdapUserDetailsImpl) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			//request.setHeaderVO(new HeaderVO());
			//request.getHeaderVO().setLoginUser(user.getUsername());
			try {
				//getTisLogDAO().storeDataTIS(
				//		getLoginUserService().getIdUserInfo(
				//				request.getHeaderVO()), name, value);
			} catch (Exception e) {//(ITalk2LearnException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void popUpOpen(){
		doneButtonPressed = false;
	}

	public void popUpClosed(){
		doneButtonPressed = true;
	}

	public void setMessage(String value, boolean popUpWindow, String type) {
		saveLog("TIS.currentUser", currentUser);
		if (fractionsLabInUse) {
			if (doneButtonPressed) {
				System.out.println(":::::: setMessage :::: " + value);
				message = value;
				setType(type);
				checkMathsWordsTimer();
				saveLog("TIS.set.message", message);
				saveLog("TIS.set.type", type);

				// need to check if that is the correct value for viewed
				// feedback when done button is set to false
				lastMessage = value;
				lastType = type;

				if (popUpWindow) {
					saveLog("TIS.popUp", "true");
					saveLog("TIS.wieved.feedback", message);
					saveLog("TIS.wieved.type", type);
					doneButtonPressed = false;
					checkMathsWords();
				} else {
					saveLog("TIS.popUp", "false");
				}
			}
		} else {
			saveLog("TIS.set.message", message);
			saveLog("TIS.set.type", type);
			saveLog("TIS.wieved.feedback", message);
			saveLog("TIS.wieved.type", type);
			System.out.println(":::::: setMessage :::: " + value);
			message = value;
			setType(type);
			checkMathsWordsTimer();
		}
	}

	public void resetMessage() {
		message = "";
		setType("");
		checkMathsWordsTimer();
	}

	public void setPopUpWindow(boolean value) {
		popUpWindow = value;
	}

	private void setType(String value) {
		feedbackType = value;
		System.out.println("teeeeeeeesT"+value);
		if (value.equals("NEXT_STEP") || value.equals("PROBLEM_SOLVING")) {
			saveLog("TIS.feedback.is.TDS.maleRobot", "true");
			System.out.println("teeeeeeeesT TDS: true");
			setTDSfeedback(true);
		} else {
			System.out.println("teeeeeeeesT TDS: false");
			saveLog("TIS.feedback.is.TDS.maleRobot", "false");
			setTDSfeedback(false);
		}

		if (value.equals("REFLECTION")) {
			checkForMathsVocab = true;
		} else {
			checkForMathsVocab = false;
		}
	}

	private void checkMathsWords() {
		if (checkForMathsVocab) {
			timerSpeechMathsWords = new TimerForMathsWordCheck();
			((TimerForMathsWordCheck) timerSpeechMathsWords)
					.setAnalysis(analysis);
			uploadCheckMathsWordsTimer = new Timer(true);

			// this needs to get checked if it stops after displaying it only
			// once..
			uploadCheckMathsWordsTimer.scheduleAtFixedRate(
					timerSpeechMathsWords, 3 * 6000, 0);
		}
	}

	private void setTDSfeedback(boolean value) {
		System.out.println("teeeeeeeesT setTDSfeedback: "+value);
		TDSfeedback = value;
	}

	public boolean getTDSfeedback() {
		if (fractionsLabInUse){
			System.out.println("teeeeeeeesT GET value: "+TDSfeedback);
			return TDSfeedback;
		}
		else {
			System.out.println("teeeeeeeesT GET value: "+false);
			return false;
		}
	}

	public void setCurrentAffect(String value) {
		affectString = value;
	}

	public void resetCurrentWordList() {
		analysis.resetCurrentWordList();
	}

	/*public ILoginUserService getLoginUserService() {
		return loginUserService;
	}
	public ITISLogDAO getTisLogDAO() {
		return tisLogDAO;
	}*/
}