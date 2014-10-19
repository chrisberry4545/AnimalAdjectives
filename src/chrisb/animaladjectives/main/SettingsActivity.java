package chrisb.animaladjectives.main;

import chrisb.animaladjectives.R;
import chrisb.animaladjectives.notifications.MyReceiver;
import chrisb.animaladjectives.utils.OrientationManager;
import chrisb.animaladjectives.utils.SettingsManager;
import chrisb.animaladjectives.utils.ToastManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class SettingsActivity extends ActionBarActivity implements OnItemSelectedListener {

	private SettingsManager settingsManager = new SettingsManager(this);
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;

        OrientationManager orientationManager = new OrientationManager();
        orientationManager.enforceOrientation(this);
        
        setContentView(R.layout.activity_settings);
        this.instantsiateUI();
    }
	
	private void instantsiateUI() {
		setUpSpinner();
		setUpOkButton();
		setUpAllowNotificationsButtons();
	}
	
	private void setUpOkButton() {
		View okButton = getOkButton();
		okButton.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			okButtonClick();
    		}
    	});
	}
	
	private void okButtonClick() {
		this.finish();
	}
	
	private void setUpAllowNotificationsButtons() {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.settings_allowNotificationsRadioGroup);
	    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	switch(checkedId) {
	        	case R.id.settingsAllowNotificationsNo : allowNotifications(false);
	        	break;
	        	case R.id.settingsAllowNotificationsYes : allowNotifications(true);
	        	break;
	        	}
	        }
	    });
    	boolean useNotifications = settingsManager.getUseNotifications();
    	RadioButton rButton;
    	if (useNotifications) {
    		rButton = (RadioButton)findViewById(R.id.settingsAllowNotificationsYes);
    	} else {
    		rButton = (RadioButton)findViewById(R.id.settingsAllowNotificationsNo);
    	}
		rButton.setChecked(true);
	}
	
	private void allowNotifications(boolean allow) {
		settingsManager.setUseNotificaitons(allow);
		MyReceiver receiver = new MyReceiver();
		if (allow) {
			receiver.runNotificationService(this.getApplicationContext());
			getNotificationIntervalSpinner().setEnabled(true);
		} else {
			receiver.clearAlarmManager(this.getApplicationContext());
			getNotificationIntervalSpinner().setEnabled(false);
		}
	}
	
	private void setUpSpinner() {

		//TODO::Set default value of spinner.
		Spinner spinner = getNotificationIntervalSpinner();
		// Create an ArrayAdapter using the string array and a default spinner layout	
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.time_between_notifications_options, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
    	int intervalPosition = settingsManager.getCurrentNotificationPosition();
    	spinner.setSelection(intervalPosition);
    	boolean useNotifications = settingsManager.getUseNotifications();
    	if (!useNotifications) {
    		spinner.setEnabled(false);
    	}
	}
	
	private int itemSelectedCount = 0;
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		if (itemSelectedCount != 0) {
			int minutesToUse = settingsManager.getNotificationIntervalAtPoint(pos);
			settingsManager.setNotificationInterval(minutesToUse);
			MyReceiver receiver = new MyReceiver();
			receiver.runNotificationService(this.getApplicationContext());
		} else {
			itemSelectedCount++;
		}
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
		ToastManager toaster = new ToastManager(this);
		toaster.CreateToast("NO item selected..");
    }
    
    private Spinner getNotificationIntervalSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.settings_timeBetweenNotificationsSpinner);
		return spinner;
    }
	
    private View getOkButton() {
    	return this.findViewById(R.id.settings_okButton);
    }
}
