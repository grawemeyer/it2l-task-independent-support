package com.italk2learn.tis;

public class StudentModel {
	Affect currentAffectWords = new Affect();
	Affect currentAffectInteraction = new Affect();
	Affect currentAffectSound = new Affect();
	Affect combinedAffect = new Affect();
	Affect previousAffect = new Affect();
	
	/*
	 * BN values - affective state reasoner
	 * {affect_boosts, next_step, problem_solving, reflection}
	 * {{flowOr Enhancement: F, T}}
	 */
	int[][] flowNotFollowed = {{2,10},{2,10},{0,6},{1,15}};
	int[][] flowFollowed = {{2,28},{3,11},{3,6},{0,12}};
	int[][] confusionNotFollowed = {{3,2},{4,11},{19,7},{10,11}};
	int[][] confusionFollowed = {{3,11},{3,5},{6,7},{12,8}};
	int[][] frustrationNotFollowed = {{0,0},{1,3},{1,3},{0,3}};
	int[][] frustrationFollowed = {{1,3},{0,2},{1,4},{1,4}};
	int[][] boredomNotFollowed = {{0,0},{0,1},{0,0},{0,0}};
	int[][] boredomFollowed = {{0,0},{0,0},{1,0},{1,1}};
	int[][] surpriseNotFollowed = {{0,0},{0,0},{0,0},{0,1}};
	int[][] surpriseFollowed = {{0,0},{0,0},{0,0},{0,1}};
	
	int currentFeedbackType = 0;
	boolean followed = false;
	boolean highMessage = false;
	boolean viewed = false;
	boolean atTheEnd = false;
	
	String feedbackID = "";
	
	boolean isSpeaking = false;
	
	
	public StudentModel(){
		
	}
	
	public void setIsSpeaking(boolean value){
		isSpeaking = value;
	}
	
	public boolean getIsSpeaking(){
		return isSpeaking;
	}
	
	public void setAtTheEnd(boolean value){
		atTheEnd = value;
	}
	
	public boolean areWeAtTheEnd(){
		return atTheEnd;
	}
	
	public void setViewedMessage(boolean value){
		viewed = value;
	}
	
	public boolean viewedMessage(){
		return viewed;
	}
	
	public void setHighMessage(boolean value){
		highMessage = value;
	}
	
	public boolean getHighMessage(){
		return highMessage;
	}
	
	public void setFollowed(boolean value){
		followed = value;
	}
	
	public boolean getFollowed(){
		return followed;
	}
	
	public Affect getPreviousAffect(){
		return previousAffect;
	}
	
	public void setCurrentFeedbackType(int type){
		System.out.println("::: student model ::: ");
		System.out.println("::: setCurrentFeedbackType ::: "+type);
		currentFeedbackType = type;
	}
	
	public int getCurrentFeedbackType(){
		return currentFeedbackType;
	}
	
	public void setCombinedAffect(Affect affect){
		previousAffect = combinedAffect;
		combinedAffect = affect;
	}
	
	public Affect getCombinedAffect(){
		return combinedAffect;
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

	public void resetAffectWords(){
		currentAffectWords = new Affect();
	}
	
	public void setAffectWords(Affect affect) {
		currentAffectWords = affect;
	}
	
	public Affect getAffectWords(){
		return currentAffectWords;
	}
	
	public void addAffectWords(Affect affect){
		double flow = affect.getFlowValue();
		double surprise = affect.getSurpriseValue();
		double boredom = affect.getBoredomValue();
		double confusion = affect.getConfusionValue();
		double frustration = affect.getFrustrationValue();
		
		double currentFlow = currentAffectWords.getFlowValue();
		double currentSurprise = currentAffectWords.getSurpriseValue();
		double currentBoredom = currentAffectWords.getBoredomValue();
		double currentConfusion = currentAffectWords.getConfusionValue();
		double currentFrustration = currentAffectWords.getFrustrationValue();
		
		Affect addedAffect = new Affect();
		addedAffect.setFlowValue(flow+currentFlow);
		addedAffect.setSurpriseValue(surprise+currentSurprise);
		addedAffect.setBoredomValue(boredom+currentBoredom);
		addedAffect.setConfusionValue(confusion+currentConfusion);
		addedAffect.setFrustrationValue(frustration+currentFrustration);
		
		currentAffectWords = addedAffect;
	}
	
		
	public int[][] getFlowFollowedValues() {
		return flowFollowed;
	}

	public int[][] getFlowNotFollowedValues() {
		return flowNotFollowed;
	}

	public int[][] getConfusionFollowedValues() {
		return confusionFollowed;
	}

	public int[][] getConfusionNotFollowedValues() {
		return confusionNotFollowed;
	}

	public int[][] getFrustrationFollowedValues() {
		return frustrationFollowed;
	}

	public int[][] getFrustrationNotFollowedValues() {
		return frustrationNotFollowed;
	}

	public int[][] getBoredomFollowedValues() {
		return boredomFollowed;
	}

	public int[][] getBoredomNotFollowedValues() {
		return boredomNotFollowed;
	}

	public int[][] getSurpriseFollowedValues() {
		return surpriseFollowed;
	}

	public int[][] getSurpriseNotFollowedValues() {
		return surpriseNotFollowed;
	}


	private int[] addValues(int[] enhancement, int falseValue, int trueValue){
		int currentFalse = enhancement[0];
		int currentTrue = enhancement[1];
		int[] result = {currentFalse+falseValue, currentTrue+trueValue};
		return result;
	}
	
	public void updateFlowFollowedAffectBoosts(int falseValue, int trueValue) {
		flowFollowed[0] = addValues(flowFollowed[0], falseValue, trueValue);
	}

	public void updateFlowNotFollowedAffectBoosts(int falseValue, int trueValue) {
		flowNotFollowed[0] = addValues(flowNotFollowed[0], falseValue, trueValue);
		
	}

	public void updateConfusionFollowedAffectBoosts(int falseValue, int trueValue) {
		confusionFollowed[0] = addValues(confusionFollowed[0], falseValue, trueValue);
	}

	public void updateConfusionNotFollowedAffectBoosts(int falseValue, int trueValue) {
		confusionNotFollowed[0] = addValues(confusionNotFollowed[0], falseValue, trueValue);
	}

	public void updateFlowFollowedNextStep(int falseValue, int trueValue) {
		flowFollowed[1] = addValues(flowFollowed[1], falseValue, trueValue);
	}

	public void updateFlowFollowedProblemSolving(int falseValue, int trueValue) {
		flowFollowed[2] = addValues(flowFollowed[2], falseValue, trueValue);
	}

	public void updateFlowFollowedReflection(int falseValue, int trueValue) {
		flowFollowed[3] = addValues(flowFollowed[3], falseValue, trueValue);
	}

	public void updateFlowNotFollowedNextStep(int falseValue, int trueValue) {
		flowNotFollowed[1] = addValues(flowNotFollowed[1], falseValue, trueValue);
	}

	public void updateFlowNotFollowedProblemSolving(int falseValue, int trueValue) {
		flowNotFollowed[2] = addValues(flowNotFollowed[2], falseValue, trueValue);	
	}

	public void updateFlowNotFollowedReflection(int falseValue, int trueValue) {
		flowNotFollowed[3] = addValues(flowNotFollowed[3], falseValue, trueValue);
	}

	public void updateConfusionFollowedNextStep(int falseValue, int trueValue) {
		confusionFollowed[1] = addValues(confusionFollowed[1], falseValue, trueValue);
	}

	public void updateConfusionFollowedProblemSolving(int falseValue, int trueValue) {
		confusionFollowed[2] = addValues(confusionFollowed[2], falseValue, trueValue);
	}

	public void updateConfusionFollowedReflection(int falseValue, int trueValue) {
		confusionFollowed[3] = addValues(confusionFollowed[3], falseValue, trueValue);
	}

	public void updateConfusionNotFollowedNextStep(int falseValue, int trueValue) {
		confusionNotFollowed[1] = addValues(confusionNotFollowed[1], falseValue, trueValue);
	}

	public void updateConfusionNotFollowedProblemSolving(int falseValue, int trueValue) {
		confusionNotFollowed[2] = addValues(confusionNotFollowed[2], falseValue, trueValue);
	}

	public void updateConfusionNotFollowedReflection(int falseValue, int trueValue) {
		confusionNotFollowed[3] = addValues(confusionNotFollowed[3], falseValue, trueValue);
	}

	public void updateFrustrationFollowedAffectBoosts(int falseValue, int trueValue) {
		frustrationFollowed[0] = addValues(frustrationFollowed[0], falseValue, trueValue);
	}

	public void updateFrustrationFollowedNextStep(int falseValue, int trueValue) {
		frustrationFollowed[1] = addValues(frustrationFollowed[1], falseValue, trueValue);
	}

	public void updateFrustrationFollowedProblemSolving(int falseValue, int trueValue) {
		frustrationFollowed[2] = addValues(frustrationFollowed[2], falseValue, trueValue);
	}

	public void updateFrustrationFollowedReflection(int falseValue, int trueValue) {
		frustrationFollowed[3] = addValues(frustrationFollowed[3], falseValue, trueValue);
	}

	public void updateFrustrationNotFollowedAffectBoosts(int falseValue, int trueValue) {
		frustrationNotFollowed[0] = addValues(frustrationNotFollowed[0], falseValue, trueValue);
	}

	public void updateFrustrationNotFollowedNextStep(int falseValue, int trueValue) {
		frustrationNotFollowed[1] = addValues(frustrationNotFollowed[1], falseValue, trueValue);
	}

	public void updateFrustrationNotFollowedProblemSolving(int falseValue, int trueValue) {
		frustrationNotFollowed[2] = addValues(frustrationNotFollowed[2], falseValue, trueValue);
	}

	public void updateFrustrationNotFollowedReflection(int falseValue, int trueValue) {
		frustrationNotFollowed[3] = addValues(frustrationNotFollowed[3], falseValue, trueValue);
	}

	public void updateBoredomFollowedAffectBoosts(int falseValue, int trueValue) {
		boredomFollowed[0] = addValues(boredomFollowed[0], falseValue, trueValue);
	}

	public void updateBoredomFollowedNextStep(int falseValue, int trueValue) {
		boredomFollowed[1] = addValues(boredomFollowed[1], falseValue, trueValue);
	}

	public void updateBoredomFollowedProblemSolving(int falseValue, int trueValue) {
		boredomFollowed[2] = addValues(boredomFollowed[2], falseValue, trueValue);
	}

	public void updateBoredomFollowedReflection(int falseValue, int trueValue) {
		boredomFollowed[3] = addValues(boredomFollowed[3], falseValue, trueValue);
	}

	public void updateBoredomNotFollowedAffectBoosts(int falseValue, int trueValue) {
		boredomNotFollowed[0] = addValues(boredomNotFollowed[0], falseValue, trueValue);
	}

	public void updateBoredomNotFollowedNextStep(int falseValue, int trueValue) {
		boredomNotFollowed[1] = addValues(boredomNotFollowed[1], falseValue, trueValue);
	}

	public void updateBoredomNotFollowedProblemSolving(int falseValue, int trueValue) {
		boredomNotFollowed[2] = addValues(boredomNotFollowed[2], falseValue, trueValue);
	}

	public void updateBoredomNotFollowedReflection(int falseValue, int trueValue) {
		boredomNotFollowed[3] = addValues(boredomNotFollowed[3], falseValue, trueValue);
	}

	public void updateSurpriseFollowedAffectBoosts(int falseValue, int trueValue) {
		surpriseFollowed[0] = addValues(surpriseFollowed[0], falseValue, trueValue);
	}

	public void updateSurpriseFollowedNextStep(int falseValue, int trueValue) {
		surpriseFollowed[1] = addValues(surpriseFollowed[1], falseValue, trueValue);
	}

	public void updateSurpriseFollowedProblemSolving(int falseValue, int trueValue) {
		surpriseFollowed[2] = addValues(surpriseFollowed[2], falseValue, trueValue);
	}

	public void updateSurpriseFollowedReflection(int falseValue, int trueValue) {
		surpriseFollowed[3] = addValues(surpriseFollowed[3], falseValue, trueValue);
	}

	public void updateSurpriseNotFollowedAffectBoosts(int falseValue, int trueValue) {
		surpriseNotFollowed[0] = addValues(surpriseNotFollowed[0], falseValue, trueValue);
	}

	public void updateSurpriseNotFollowedNextStep(int falseValue, int trueValue) {
		surpriseNotFollowed[1] = addValues(surpriseNotFollowed[1], falseValue, trueValue);
	}

	public void updateSurpriseNotFollowedProblemSolving(int falseValue, int trueValue) {
		surpriseNotFollowed[2] = addValues(surpriseNotFollowed[2], falseValue, trueValue);
	}

	public void updateSurpriseNotFollowedReflection(int falseValue, int trueValue) {
		surpriseNotFollowed[3] = addValues(surpriseNotFollowed[3], falseValue, trueValue);
	}

	public void setFeedbackID(String value) {
		feedbackID = value;
		
	}
	
	public String getFeedbackID(){
		return feedbackID;
	}

	
}