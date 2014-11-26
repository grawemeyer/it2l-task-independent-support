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

import com.italk2learn.tis.TISWrapper;
import com.italk2learn.vo.HeaderVO;
import com.italk2learn.vo.SpeechRecognitionRequestVO;
import com.italk2learn.vo.SpeechRecognitionResponseVO;
import com.italk2learn.vo.TaskIndependentSupportRequestVO;

@RunWith(value = SpringRunner.class)
@Configuration(locations = { "web-application-config.xml" })
public class TaskIndependentSupportTest {
	
	private static final int ARRAY_SIZE = 500000;
	private static final int NUM_SECONDS = 5 * 1000;
	
	@Dependency
	private RestTemplate restTemplate;
	
	private SpeechRecognitionRequestVO request= new SpeechRecognitionRequestVO();
	private SpeechRecognitionResponseVO liveResponse=new SpeechRecognitionResponseVO();
	private final TISWrapper response = new TISWrapper();
	private List<byte[]> audioChunks=new ArrayList<byte[]>();
	private int counter=0;
	
	
	private static final Logger LOGGER = Logger
	.getLogger(TaskIndependentSupportTest.class);
	
	@Test
	public void sendSpeechToSupport() throws Exception{
		LOGGER.info("TESTING sendSpeechToSupport");
		TaskIndependentSupportRequestVO request= new TaskIndependentSupportRequestVO();
		String words[]={"hello","this","is","a","test"};
		boolean testOk = false;
		request.setHeaderVO(new HeaderVO());
		request.getHeaderVO().setLoginUser("student1");
		//request.setWords(words);
		try {
			//response.sendSpeechOutputToSupport(request.getWords(), request.getCheckMathKeywords());
			testOk = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
		Assert.assertTrue(testOk);
	}
	
	
	@Test
	public void sendRealSpeechToSupport() throws Exception{
		final int dataSize = (int) (Runtime.getRuntime().maxMemory());
		System.out.println("Max amount of memory is: "+dataSize);
		final TISWrapper an = new TISWrapper();
		request.setHeaderVO(new HeaderVO());
		request.getHeaderVO().setLoginUser("student1");
		request.setInstance(1);
		boolean testOk = false;
		File f=new File("Hard-example.wav");
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
		int initialChunk=0;
		int finalChunk=(int)l/(int)numChunks;
		for (int i=0;i<numChunks;i++){
			audioChunks.add(Arrays.copyOfRange(b, initialChunk, finalChunk));
			System.out.println("Chunk "+i+" starts at "+initialChunk+" bytes and finish at "+finalChunk+" bytes");
			initialChunk=finalChunk+1;
			finalChunk=finalChunk+((int)l/(int)numChunks);
		}
		if (initialChunk<l){
			System.out.println("Last chunk starts at "+initialChunk+" bytes and finish at "+l+" bytes");
			audioChunks.add(Arrays.copyOfRange(b, initialChunk, (int)l-1));
		}
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("user", "student1");
		vars.put("instance", "1");
		vars.put("server", "localhost");
		vars.put("language", "en_ux");
		vars.put("model", "base");
		try {
			//Call initEngineService of an available instance
			Boolean isOpen=this.restTemplate.getForObject("http://193.61.29.166:8081/italk2learnsm/speechRecognition/initEngine?user={user}&instance={instance}&server={server}&language={language}&model={model}",Boolean.class, vars);
			if (isOpen){
				//an.sendSpeechOutputToSupport(liveResponse.getResponse());
				Timer timer = new Timer();
				//JLF:Send chunk each NUM_SECONDS
				timer.scheduleAtFixedRate(new SpeechTask(), NUM_SECONDS,NUM_SECONDS);
			}
			while (true){}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
		Assert.assertTrue(testOk);
	}
	
	  class SpeechTask extends TimerTask {
		    public void run() {
		    	if (counter<audioChunks.size()) {
		    		TaskIndependentSupportRequestVO req= new TaskIndependentSupportRequestVO();
		    		int aux=counter+1;
		    		System.out.println("Sending chunk: " + aux);
		    		System.out.println("the chunk is " + audioChunks.get(counter).length + " bytes long");
		    		request.setData(audioChunks.get(counter));
		    		liveResponse=restTemplate.postForObject("http://193.61.29.166:8081/italk2learnsm/speechRecognition/sendData", request, SpeechRecognitionResponseVO.class);
		    		req.setCheckMathKeywords(false);
		    		req.setWords(liveResponse.getLiveResponse());
		    		response.sendSpeechOutputToSupport(req);
		    		
		    		counter++;
			    }
		    	else {
		    		String response=restTemplate.getForObject("http://193.61.29.166:8081/italk2learnsm/speechRecognition/closeEngine?instance={instance}",String.class, "1");
		    		if (response!=null){
		    			System.out.println(response);
		    			Assert.assertTrue(true);
		    		}
		    		System.out.println("All jobs finished");
		    		System.exit(0);	
		    	}
		    }
		  }
	

}
