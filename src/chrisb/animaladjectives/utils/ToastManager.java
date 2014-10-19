package chrisb.animaladjectives.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {
	
	private Context context;
	
	public ToastManager(Context context) {
		this.context = context;
	}

	public void CreateToast(String text) {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
}
