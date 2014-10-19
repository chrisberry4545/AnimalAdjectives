package chrisb.animaladjectives.utils;

import chrisb.animaladjectives.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;

public class OrientationManager {

	public void enforceOrientation(Activity context) {
		if(context.getResources().getBoolean(R.bool.portrait_only)){
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
	}
	
}
