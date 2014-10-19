package chrisb.animaladjectives.utils;

import java.util.ArrayList;
import java.util.List;

import chrisb.animaladjectives.R;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

	private Context context;
	private static final String notificationsIntervalStore = "NOTIFICATION_INTERVAL";
	private static final String allowNotificationsStore = "ALLOW_NOTIFICATIONS";
	
	private SharedPreferencesManager sharedPrefsManager;
	
	public SettingsManager(Context context) {
		this.context = context;
		sharedPrefsManager = new SharedPreferencesManager(context); 
	}
	
	public void setNotificationInterval(int minutesBetweenNotifications) {
		SharedPreferences.Editor editor = sharedPrefsManager.getSharedPreferences().edit();
		editor.putInt(notificationsIntervalStore, minutesBetweenNotifications);
		editor.apply();
	}
	
	public int getCurrentNotificationInterval() {
		int defaultValue = context.getResources().getInteger(R.integer.default_notificationInterval);
		return sharedPrefsManager.getSharedPreferences().getInt(notificationsIntervalStore, defaultValue);
	}
	
	public int getCurrentNotificationPosition() {
		int currentInterval = getCurrentNotificationInterval();
		List<Integer> notificationsInterval =  getNotificationIntervals();
		return notificationsInterval.indexOf(currentInterval);
	}
	
	public int getNotificationIntervalAtPoint(int notificationIntervalPosition) {
		List<Integer> notificationsInterval =  getNotificationIntervals();
		if (notificationsInterval.size() > notificationIntervalPosition) {
			return notificationsInterval.get(notificationIntervalPosition);
		}
		return -1;
	}
	
	private List<Integer> getNotificationIntervals() {
		String[] intervals = context.getResources().getStringArray(R.array.time_between_notifications_mappings);
		ArrayList<Integer> intervalInts = new ArrayList<Integer>();
		for(String in : intervals) {
			intervalInts.add(Integer.parseInt(in));
		}
		return intervalInts;
	}
	
	public boolean getUseNotifications() {
		boolean defaultVal = context.getResources().getBoolean(R.bool.default_useNotifications);
		return sharedPrefsManager.getSharedPreferences().getBoolean(allowNotificationsStore, defaultVal);
	}
	
	public void setUseNotificaitons(boolean useNotifications) {
		SharedPreferences.Editor editor = sharedPrefsManager.getSharedPreferences().edit();
		editor.putBoolean(allowNotificationsStore, useNotifications);
		editor.apply();
	}
	
}
