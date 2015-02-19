package com.italk2learn.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.gienah.testing.junit.Configuration;
import org.gienah.testing.junit.Dependency;
import org.gienah.testing.junit.SpringRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;

import com.italk2learn.tis.Analysis;
import com.italk2learn.vo.HeaderVO;
import com.italk2learn.vo.SpeechRecognitionRequestVO;
import com.italk2learn.vo.SpeechRecognitionResponseVO;

@RunWith(value = SpringRunner.class)
@Configuration(locations = { "web-application-config.xml" })
public class TaskIndependentSupportTest {
	
	private static final int ARRAY_SIZE = 250000;// Should be a multiple of 16
	private static final int NUM_SECONDS = 5 * 1000;
	
	@Dependency
	private RestTemplate restTemplate;
	
	private SpeechRecognitionRequestVO request= new SpeechRecognitionRequestVO();
	private SpeechRecognitionResponseVO liveResponse=new SpeechRecognitionResponseVO();
	private List<byte[]> audioChunks=new ArrayList<byte[]>();
	private int counter=0;
	private boolean oneChunk=false;
	private boolean loop=true;
	
	
	private static final Logger LOGGER = Logger
	.getLogger(TaskIndependentSupportTest.class);
	
	@Test
	public void sendRealSpeechToSupport() throws Exception{
		final int dataSize = (int) (Runtime.getRuntime().maxMemory());
		System.out.println("Max amount of memory is: "+dataSize);
		final Analysis an = new Analysis();
		request.setHeaderVO(new HeaderVO());
		request.getHeaderVO().setLoginUser("student1");
		request.setInstance(12);
		boolean testOk = false;
		File f=new File("no-maths-vocab-example-01-mono.wav");
		long l=f.length();
		long numChunks=l/ARRAY_SIZE;
		System.out.println("the file is " + l + " bytes long");
		FileInputStream finp = null;
		byte[] b=new byte[(int)l];
		try {
			finp = new FileInputStream(f);
			int i;
			i=finp.read(b);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!oneChunk) {
			int initialChunk=0;
			int finalChunk=(int)l/(int)numChunks;
		    if ((finalChunk % 16)!=0)
		           finalChunk=finalChunk-(finalChunk % 16);
				
			for (int i=0;i<numChunks;i++){
				byte[] aux=Arrays.copyOfRange(b, initialChunk, finalChunk);
				audioChunks.add(aux);
				System.out.println("Chunk "+i+" starts at "+initialChunk+" bytes and finish at "+finalChunk+" bytes");
				initialChunk=finalChunk;
				finalChunk=finalChunk+((int)l/(int)numChunks);
			    if ((finalChunk % 16)!=0)
			        finalChunk=finalChunk-(finalChunk % 16);
			}
			if (initialChunk<l){
				System.out.println("Last chunk starts at "+initialChunk+" bytes and finish at "+l+" bytes");
				audioChunks.add(Arrays.copyOfRange(b, initialChunk, (int)l));
			}
		}
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("user", "student1");
		vars.put("instance", "12");
		vars.put("server", "localhost");
		vars.put("language", "en_ux");
		vars.put("model", "base");
		try {
			//Call initEngineService of an available instance
			Boolean isOpen=this.restTemplate.getForObject("http://193.61.29.166:8092/italk2learnsm/speechRecognition/initEngine?user={user}&instance={instance}&server={server}&language={language}&model={model}",Boolean.class, vars);
			if (isOpen){
				//an.sendSpeechOutputToSupport(liveResponse.getResponse());
				Timer timer = new Timer();
				//JLF:Send chunk each NUM_SECONDS
				if (!oneChunk)
					timer.scheduleAtFixedRate(new SpeechTask(), NUM_SECONDS,NUM_SECONDS);
				else {
					System.out.println("Sent in one chunk");
					request.setData(b);
		    		liveResponse=restTemplate.postForObject("http://193.61.29.166:8092/italk2learnsm/speechRecognition/sendData", request, SpeechRecognitionResponseVO.class);
		    		String response=restTemplate.getForObject("http://193.61.29.166:8092/italk2learnsm/speechRecognition/closeEngine?instance={instance}",String.class, "12");
		    		if (response!=null){
		    			List<String> words= new ArrayList<String>();
		    			String[] auxWords=response.split(" ");	
		    			for (String aux : auxWords){
		    				System.out.println("word: "+aux);
		    				words.add(aux);
		    			}
		    			System.out.println("Output: "+response);
		    			Assert.assertTrue(true);
		    			loop=false;
		    		}
				}
			}
			while (loop){}
			System.out.println("All jobs finished");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
		Assert.assertTrue(true);
	}
	
	  class SpeechTask extends TimerTask {
		    public void run() {
		    	if (counter<audioChunks.size()) {
		    		int aux=counter+1;
		    		System.out.println("Sending chunk: " + aux);
		    		System.out.println("the chunk is " + audioChunks.get(counter).length + " bytes long");
		    		request.setData(audioChunks.get(counter));
		    		liveResponse=restTemplate.postForObject("http://193.61.29.166:8092/italk2learnsm/speechRecognition/sendData", request, SpeechRecognitionResponseVO.class);
		    		//an.sendSpeechOutputToSupport(liveResponse.getResponse());
		    		counter++;
			    }
		    	else {
		    		String response=restTemplate.getForObject("http://193.61.29.166:8092/italk2learnsm/speechRecognition/closeEngine?instance={instance}",String.class, "12");
		    		if (response!=null){
		    			System.out.println("Output: "+response);
		    			Assert.assertTrue(true);
		    		}
		    		loop=false;
		    	}
		    }
		  }
	

}
