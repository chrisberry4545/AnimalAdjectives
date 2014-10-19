package chrisb.animaladjectives.stringaccess;

import android.content.Context;
import chrisb.animaladjectives.R;

public class AddToFavouritesAccess extends IStringAccess {
	
	public AddToFavouritesAccess(Context context) {
		super(context);
	}
	
	@Override
	public String getString() {
		String[] stringArray = context.getResources().getStringArray(R.array.add_to_favs_array);
		return super.getRandomString(stringArray);
	}
}
