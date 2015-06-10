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
		List<String> words = new ArrayList<String>();
		words.add("test");
		try {
			tis.sendSpeechOutputToSupport("student2", words);
			Assert.assertTrue(true);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
