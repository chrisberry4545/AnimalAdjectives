package chrisb.animaladjectives.notifications;

import chrisb.animaladjectives.R;
import chrisb.animaladjectives.content.AnimalAdjectiveGenerator;
import chrisb.animaladjectives.main.MainActivity;
import chrisb.animaladjectives.storage.RecentWords;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
                            
 
public class MyAlarmService extends Service 
{
      
   private NotificationManager mManager;
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
    }
 
   @SuppressWarnings("static-access")
   @Override
   public void onStart(Intent intent, int startId)
   {
       super.onStart(intent, startId);

	   	AnimalAdjectiveGenerator gen = new AnimalAdjectiveGenerator(this);
	   	String word1 = gen.GetWord1();
	   	String word2 = gen.GetWord2();
	   	
	   	String result = word1 + " " + word2;
	   	
	   	RecentWords storage = new RecentWords();
	   	storage.saveWords(this, word1, word2);
	   	
       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
       Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
     
       Notification notification = new Notification(R.drawable.ic_launcher,result, System.currentTimeMillis());
       intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
 
       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
       notification.flags |= Notification.FLAG_AUTO_CANCEL;
       notification.setLatestEventInfo(this.getApplicationContext(), "Animal Adjective:", result, pendingNotificationIntent);
 
       mManager.notify(0, notification);
    }
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}