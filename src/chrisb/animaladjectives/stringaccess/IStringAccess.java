package chrisb.animaladjectives.stringaccess;

import java.util.Random;

import chrisb.animaladjectives.R;
import android.content.Context;

public abstract class IStringAccess {
	
	protected Context context;
	
	public IStringAccess(Context context) {
		this.context = context;
	}

	public abstract String getString();
	
	private Random random = new Random();
	
	protected String getRandomString(String[] strings) {
		int numberToChoose = random.nextInt(strings.length);
		String strToReturn = strings[numberToChoose];
		return strToReturn;
	}
	
	protected String getPlaceholderString() {
		return context.getString(R.string.placeholderString);
	}
	
	public String replacePlaceholder(String fullStr, String stringToAddIn) {
		String replacerStr = getPlaceholderString();
		if (fullStr.contains(replacerStr)) {
			fullStr = fullStr.replace(replacerStr, stringToAddIn);
		}
		return fullStr;
	}
	
}
