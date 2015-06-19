package com.italk2learn.tis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class TISWrapperTest {

	@Test
	public void testSendSpeechOutputToSupport() {
		TISWrapper tis = new TISWrapper();
		tis.startTIS();	
		List<String> words = new ArrayList<String>();
		words.add("test");
		tis.sendSpeechOutputToSupport("student2", words);
	}
	
	@Test
	public void testStartTIS(){
		TISWrapper tis = new TISWrapper();
		tis.startTIS();	
		boolean studentModelIsnotNull = (tis.analysis.getStudentModel() != null);
		assertTrue(studentModelIsnotNull);
	}
}
