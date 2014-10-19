package chrisb.animaladjectives.stringaccess;

import chrisb.animaladjectives.R;
import android.content.Context;

public class PreviousStringAccess extends IStringAccess {
	
	public PreviousStringAccess(Context context) {
		super(context);
	}
	
	@Override
	public String getString() {
		String[] stringArray = context.getResources().getStringArray(R.array.previous_array);
		return super.getRandomString(stringArray);
	}
	
}
