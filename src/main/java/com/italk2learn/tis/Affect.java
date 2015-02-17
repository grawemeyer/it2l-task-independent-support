package com.italk2learn.tis;

public class Affect {
	
	public double flow = 0;
	public double surprise = 0;
	public double boredom = 0;
	public double confusion = 0;
	public double frustration = 0;
	
	public Affect(){
		
	}
	
	public boolean isFlow(){
		if ((flow > surprise) &&
				(flow > boredom) &&
				(flow > confusion) &&
				(flow > frustration)){
			return true;
		}
		return false;
	}
	
	public boolean isSurprise(){
		if ((surprise > flow) &&
				(surprise > boredom) &&
				(surprise > confusion) &&
				(surprise > frustration)){
			return true;
		}
		return false;
	}
	
	public boolean isBoredom(){
		if ((boredom > flow) &&
				(boredom > surprise) &&
				(boredom > confusion) &&
				(boredom > frustration)){
			return true;
		}
		return false;
	}
	
	public boolean isConfusion(){
		if ((confusion > flow) &&
				(confusion > surprise) &&
				(confusion > boredom) &&
				(confusion > frustration)){
			return true;
		}
		return false;
	}
	
	public boolean isFrustration(){
		if ((frustration > flow) &&
				(frustration > surprise) &&
				(frustration > boredom) &&
				(frustration > confusion)){
			return true;
		}
		return false;
	}
	
	
	public void setFlowValue(double value) {
		flow = value;
	}
	
	public double getFlowValue(){
		return flow;
	}
	
	public void setSurpriseValue(double value) {
		surprise = value;
	}
	
	public double getSurpriseValue(){
		return surprise;
	}
	
	public void setBoredomValue(double value) {
		boredom = value;
	}
	
	public double getBoredomValue(){
		return boredom;
	}
	
	public void setConfusionValue(double value) {
		confusion = value;
	}
	
	public double getConfusionValue(){
		return confusion;
	}
	
	public void setFrustrationValue(double value) {
		frustration = value;
	}
	
	public double getFrustrationValue(){
		return frustration;
	}

}
