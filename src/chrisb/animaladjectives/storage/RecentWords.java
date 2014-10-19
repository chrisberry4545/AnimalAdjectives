package chrisb.animaladjectives.storage;

import android.content.Context;
import android.content.SharedPreferences;
import chrisb.animaladjectives.main.MainActivity;

public class RecentWords {
	   
	   public void saveWords(Context context, String word1, String word2)
	   {
			SharedPreferences prefs = context.getSharedPreferences(MainActivity.sharedPreferencesStore, 0);
		    SharedPreferences.Editor mEditor = prefs.edit();
		    mEditor.putString(MainActivity.word1Store, word1).commit();
		    mEditor.putString(MainActivity.word2Store, word2).commit();
	   }
	
}
