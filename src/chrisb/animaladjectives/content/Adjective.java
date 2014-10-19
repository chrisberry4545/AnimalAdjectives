package chrisb.animaladjectives.content;

import android.content.Context;

public class Adjective extends PossibleWord {

	private static String adjectiveFileLocation = "listofadjectives.txt";
	
	public Adjective(Context context) {
		super(adjectiveFileLocation, context);
	}

}
