package chrisb.animaladjectives.content;

import android.content.Context;

public class Animal extends PossibleWord {

	private static String animalFileLocation = "listofanimals.txt";
	
	public Animal(Context context) {
		super(animalFileLocation, context);
	}

}
