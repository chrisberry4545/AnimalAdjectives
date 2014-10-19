package chrisb.animaladjectives.notifications;
 
import java.util.Calendar;

import chrisb.animaladjectives.utils.SettingsManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class MyReceiver extends BroadcastReceiver
{
    
	  @Override
	  public void onReceive(Context context, Intent intent)
	  {
	  	runNotificationService(context.getApplicationContext());
	  }   
  
	public void runNotificationService(Context context)
	{
    	SettingsManager settingsManager = new SettingsManager(context);
    	if (settingsManager.getUseNotifications()) {
        	int minutesBetweenNotifications = settingsManager.getCurrentNotificationInterval();
            int interval = 60 * 1000 * minutesBetweenNotifications;
            
        	Calendar calendar = Calendar.getInstance();
        	calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutesBetweenNotifications);
        	PendingIntent pendingIntent = setUpPendingIntent(context);
            //Once a day
//            int interval = 1 * 60 * 60 * 24 * 1000;

            //Every 5 minutes
//            int interval = 1 * 60 * 5 * 1000;
            
            //Every 2 hours
//            int interval = 1 * 60 * 60 * 2 * 1000;
            
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), interval, pendingIntent);
    	}
	}
	
	public void clearAlarmManager(Context context) {
    	PendingIntent pendingIntent = setUpPendingIntent(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
	}
	
	private PendingIntent setUpPendingIntent(Context context) {
        Intent myIntent = new Intent(context, AlertServiceWrapper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,0);
        return pendingIntent;
		
	}
}