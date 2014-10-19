package chrisb.animaladjectives.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;
import chrisb.animaladjectives.content.FullWord;
import chrisb.animaladjectives.main.MainActivity;

public class FavouritesManager {
	
	private String favouritesStore= "animalAdjectivesFavouritesStoreString";
	
	private String favouritesStringDivide = "-----";

	private Context context;
	
	public FavouritesManager(Context con) {
		this.context = con;
	}
	
	private FullWord constructWord(String storedText) {
		String[] words = storedText.split(favouritesStringDivide);
		FullWord fullWord = new FullWord();
		fullWord.setFirstPart(words[0]);
		fullWord.setSecondPart(words[1]);
		return fullWord;
	}
	
	private String constructStringStoreText(FullWord fullWord) {
		return fullWord.getFirstPart() + favouritesStringDivide + fullWord.getLastPart();
	}
	
	public void addToFavourites(FullWord fullWord) {
		ArrayList<String> allFavs = getAllFavourites();
		allFavs.add(this.constructStringStoreText(fullWord));
        commitToStringSet(allFavs);
	}
	
	private void commitToStringSet(ArrayList<String> set) {
//		Set<String> setCopy = new HashSet<String>();
//		setCopy.addAll(set);
		setStringArrayPref(context.getApplicationContext(), favouritesStore, set);
//		SharedPreferences prefs = getAppSharedPreferences();
//		SharedPreferences prefs = context.getSharedPreferences(MainActivity.sharedPreferencesStore, 0);
//        SharedPreferences.Editor mEditor = prefs.edit();
//        mEditor.putStringSet(favouritesStore, setCopy).commit();
	}
	
	private ArrayList<String> getAllFavourites() {
		return getStringArrayPref(context.getApplicationContext(), favouritesStore);
//		SharedPreferences prefs = getAppSharedPreferences();
////		SharedPreferences prefs = context.getSharedPreferences(MainActivity.sharedPreferencesStore, 0);
//        return prefs.getStringSet(favouritesStore, new HashSet<String>());
	}
	
	public FullWord getFavourite(int number) {
		ArrayList<String> favourites = getAllFavourites();
		if (favourites.size() > number) {
			String[] favsArray = favourites.toArray(new String[favourites.size()]);
			FullWord fullWord = this.constructWord(favsArray[number]);
			return fullWord;
		} else {
			return null;
		}
	}
	
	public void removeFavourite(FullWord favourite) {
		ArrayList<String> allFavs = this.getAllFavourites();
		String stringToRemove = this.constructStringStoreText(favourite);
		
		if (allFavs.contains(stringToRemove)) {
			allFavs.remove(stringToRemove);
		}
		
		commitToStringSet(allFavs);
	}
	
	public int getLastFavouriteNumber() {
		return this.getAllFavourites().size() - 1;
	}
	
	public boolean isFavourite(FullWord query) {
		ArrayList<String> allFavs = this.getAllFavourites();
		String stringVersion = this.constructStringStoreText(query);
		if (allFavs.contains(stringVersion)) {
			return true;
		}
		return false;
	}
	
	public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    JSONArray a = new JSONArray();
	    for (int i = 0; i < values.size(); i++) {
	        a.put(values.get(i));
	    }
	    if (!values.isEmpty()) {
	        editor.putString(key, a.toString());
	    } else {
	        editor.putString(key, null);
	    }
	    editor.commit();
	}

	public static ArrayList<String> getStringArrayPref(Context context, String key) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    String json = prefs.getString(key, null);
	    ArrayList<String> urls = new ArrayList<String>();
	    if (json != null) {
	        try {
	            JSONArray a = new JSONArray(json);
	            for (int i = 0; i < a.length(); i++) {
	                String url = a.optString(i);
	                urls.add(url);
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	    return urls;
	}
	
}
