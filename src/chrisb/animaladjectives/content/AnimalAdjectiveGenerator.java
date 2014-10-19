package chrisb.animaladjectives.content;

import android.content.Context;

public class AnimalAdjectiveGenerator {

	private Context context;
	
	public AnimalAdjectiveGenerator(Context context)
	{
		this.context = context;
	}
    
    public String GetWord1() {
    	return new Adjective(context).GetWord();
    }
    
    public String GetWord2() {
    	return new Animal(context).GetWord();
    }
}
