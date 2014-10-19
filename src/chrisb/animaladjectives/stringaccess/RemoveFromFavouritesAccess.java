package chrisb.animaladjectives.stringaccess;

import android.content.Context;
import chrisb.animaladjectives.R;

public class RemoveFromFavouritesAccess extends IStringAccess {
	
	public RemoveFromFavouritesAccess(Context context) {
		super(context);
	}
	
	@Override
	public String getString() {
		String[] stringArray = context.getResources().getStringArray(R.array.remove_from_favs_array);
		return super.getRandomString(stringArray);
	}
}
