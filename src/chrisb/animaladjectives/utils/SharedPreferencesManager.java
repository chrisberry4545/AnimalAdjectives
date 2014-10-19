package chrisb.animaladjectives.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

	private Context context;
	private static final String sharedPreferencesStore = "chrisbanimaladjectivessharedpreferences";
	
	public SharedPreferencesManager(Context context) {
		this.context = context;
	}
	
	public SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(sharedPreferencesStore, 0);
	}
	
	
}
