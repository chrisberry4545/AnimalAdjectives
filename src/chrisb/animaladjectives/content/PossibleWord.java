package chrisb.animaladjectives.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

public abstract class PossibleWord {

	private static Random random = new Random();
	
	private String word;
	
	public PossibleWord(String fileLocation, Context context) {
		this.word = GetRandomWord(fileLocation, context);
	}
	
	public String GetWord() {
		return this.word;
	}
	
	private String GetRandomWord(String fileLocation, Context context) {
		List<String> fullFileLines = null;
		try 
		{
			fullFileLines = readLines(fileLocation, context);
		}
		catch (IOException e) {
		}
		
		if (fullFileLines != null) {
			int lineToUse = random.nextInt(fullFileLines.size());
			return fullFileLines.get(lineToUse);
		} else {
			return "File not found..";
		}
		
		
	}
	
	public static List<String> readLines(String fileLocation, Context context) throws IOException {
	    InputStreamReader fileReader = 
	    		new InputStreamReader(context.getResources().getAssets()
	    				.open(fileLocation));
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    List<String> lines = new ArrayList<String>();
	    String line = null;
	    while ((line = bufferedReader.readLine()) != null) {
	        lines.add(line);
	    }
	    bufferedReader.close();
	    return lines;
	}
	
}
