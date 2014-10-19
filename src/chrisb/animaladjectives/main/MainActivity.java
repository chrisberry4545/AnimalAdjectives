package chrisb.animaladjectives.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import chrisb.animaladjectives.R;
import chrisb.animaladjectives.content.AnimalAdjectiveGenerator;
import chrisb.animaladjectives.content.FullWord;
import chrisb.animaladjectives.notifications.MyReceiver;
import chrisb.animaladjectives.socialmedia.FacebookShare;
import chrisb.animaladjectives.storage.FavouritesManager;
import chrisb.animaladjectives.storage.PreviousAndNextStorageManager;
import chrisb.animaladjectives.stringaccess.AddToFavouritesAccess;
import chrisb.animaladjectives.stringaccess.IStringAccess;
import chrisb.animaladjectives.stringaccess.PreviousStringAccess;
import chrisb.animaladjectives.stringaccess.RemoveFromFavouritesAccess;
import chrisb.animaladjectives.utils.ConnectionTest;
import chrisb.animaladjectives.utils.DownloadImageTask;
import chrisb.animaladjectives.utils.HTMLUtils;
import chrisb.animaladjectives.utils.OrientationManager;
import chrisb.animaladjectives.utils.ShakeDetector;
import chrisb.animaladjectives.utils.ToastManager;

public class MainActivity extends ActionBarActivity {

	private static final int FAVOURITES_ACTIVITY_RETURN = 0;
	
	public static final String sharedPreferencesStore = "chrisbanimaladjectivessharedpreferences";
	public static final String word1Store = "word1";
	public static final String word2Store = "word2";
	
	private String word1;
	private String word2;
	

	private FavouritesManager favouritesManager = new FavouritesManager(this);
	private ToastManager toaster = new ToastManager(this);
	private PreviousAndNextStorageManager previousAndNextManager = new PreviousAndNextStorageManager();
	
	private IStringAccess addToFavsStrings = new AddToFavouritesAccess(this);
	private IStringAccess removeFromFavsStrings = new RemoveFromFavouritesAccess(this);
	private IStringAccess previousStringsAccess = new PreviousStringAccess(this);
	
	private boolean actionLock = false;
	
	private SensorManager mSensorManager;
	private ShakeDetector mSensorListener;
	private boolean hasAccelerometer;
	
	private FacebookShare facebookManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
        
        OrientationManager orientationManager = new OrientationManager();
        orientationManager.enforceOrientation(this);
        
        setContentView(R.layout.activity_main);
        instantiateUI();
        showFirstResult();
        createAlarmManager();
        
        PackageManager manager = getPackageManager();
        hasAccelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        if (hasAccelerometer) {
        	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensorListener = new ShakeDetector();   

            mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {

              public void onShake() {
            	  nextWord();
              }
            });
        }
        facebookManager = new FacebookShare(savedInstanceState, this);
        
        
    }
    
    @Override
    protected void onResume() {
    	
    	facebookManager.onResume();
	      super.onResume();
	      if (hasAccelerometer) {
	          mSensorManager.registerListener(mSensorListener,
	                  mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	                  SensorManager.SENSOR_DELAY_UI);
	      }
	      String wordToUse = facebookManager.getFacebookData();
	      if (wordToUse != null) {
	    	  String[] splitWord = wordToUse.split(" ");
	    	  String firstPart = splitWord[0];
	    	  String remainingPart = "";
	    	  for (int i = 1; i < splitWord.length; i++) {
	    		  remainingPart += splitWord[i];
	    		  if (i != splitWord.length - 1) {
	    			  remainingPart += " ";
	    		  }
	    	  }
	    	  SharedPreferences prefs = getSharedPreferences(MainActivity.sharedPreferencesStore, 0);
	    	  SharedPreferences.Editor mEditor = prefs.edit();
	    	  mEditor.putString(MainActivity.word1Store, firstPart).commit();
	    	  mEditor.putString(MainActivity.word2Store, remainingPart).commit();
	    	  showFirstResult();
	      }
    }

    @Override
    protected void onPause() {
    	facebookManager.onPause();
	      if (hasAccelerometer) {
	          mSensorManager.unregisterListener(mSensorListener);
	      }
	      super.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        facebookManager.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        facebookManager.onSaveInstanceState(outState);
    }
    private void createAlarmManager() {
    	MyReceiver broadcastReceiver = new MyReceiver();
    	broadcastReceiver.runNotificationService(this.getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
        case R.id.action_bar_favourites :
        	this.openFavourites();
        	break;
        case R.id.action_bar_settings :
        	this.openSettings();
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void openSettings() {
    	Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
    	this.startActivity(settings);
    }
    
    private void openFavourites() {
    	Intent favourites = new Intent(MainActivity.this, MyFavourites.class);
    	this.startActivityForResult(favourites, FAVOURITES_ACTIVITY_RETURN);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FAVOURITES_ACTIVITY_RETURN) {
        	this.checkIfFavourite(this.getFullWord());
        }
        facebookManager.handleActivityResult(requestCode, resultCode, data);
    }
    

    private void instantiateUI() {
    	setUpGoButton();
    	setUpAddToFavouritesButton();
    	setUpRemoveFromFavouriesButton();
    	setUpPreviousButton();
    	setUpNextButton();
    	setUpShareToFacebookButton();
    }
    
	private void setUpGoButton() {
//		Button goButton = getGoButton();
//		/*Go button only appears on large screen else its invisible.*/
//		if ((getResources().getConfiguration().screenLayout & 
//			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
//			        Configuration.SCREENLAYOUT_SIZE_LARGE) {
//			    // on a large screen device ...
//			goButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					mainButtonAction();
//				}
//				
//			});
//		} else {
//			goButton.setVisibility(View.GONE);
//		}
	}
    
    private void setUpAddToFavouritesButton() {
    	ImageView addToFavsButton = getAddToFavouritesButton();
    	addToFavsButton.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			addToFavourites();
    		}
    	});
    	setMainTextViewToAddFavourites();
    }
    
    private void setMainTextViewToAddFavourites() {
    	TextView mainTextView = getWordResultsView();
    	mainTextView.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			addToFavourites();
    		}
    	});
    }
    
    private void setMainTextViewToRemoveFavourites() {
    	TextView mainTextView = getWordResultsView();
    	mainTextView.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			removeFromFavourites();
    		}
    	});
    }
    
    private void setUpRemoveFromFavouriesButton() {
    	ImageView addToFavsButton = this.getAddToFavourites_selectedButton();
    	addToFavsButton.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			removeFromFavourites();
    		}
    	});
    }
    
    private void setUpPreviousButton() {
    	Button button = this.getPreviousButton();
    	button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			previousWord();
    		}
    	});
    }
    
    private void setUpNextButton() {
    	Button button = this.getNextButton();
    	button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			nextWord();
    		}
    	});
    }
    
    private void setUpShareToFacebookButton() {
    	View button = this.getShareToFacebookButton();
    	button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			shareToFacebook();
    		}
    	});
    }
    
    private void shareToFacebook() {
    	String fullWord = this.getFullWord().getFullWord();
    	this.facebookManager.shareToFacebook(fullWord);
    }
    
    private void previousWord() {
    	if (!actionLock) {
    		actionLock = true;
        	FullWord word = previousAndNextManager.getPreviousWord();
        	if (word != null) {
        		this.word1 = word.getFirstPart();
        		this.word2 = word.getLastPart();
            	setTextAndShowPictures(word);
        	} else {
        		String message = previousStringsAccess.getString();
        		toaster.CreateToast(message);
        	}
    		actionLock = false;
    	}
    }
    
    private void nextWord() {
    	if (!actionLock) {
    		actionLock = true;
        	FullWord word = previousAndNextManager.getNextWord();
        	if (word != null) {
        		this.word1 = word.getFirstPart();
        		this.word2 = word.getLastPart();
            	setTextAndShowPictures(word);
        		actionLock = false;
        	} else {
        		actionLock = false;
        		this.mainButtonAction();
        	}
    	}
    }
    
    private void addToFavourites() {
    	FullWord fullWord = getFullWord();
    	favouritesManager.addToFavourites(fullWord);
//    	String toastText = fullWord.getFullWord() + " " + getString(R.string.favourites_added_to_favourites_toast);
    	String toastText = addToFavsStrings.replacePlaceholder(addToFavsStrings.getString(), fullWord.getFullWord());
    	toaster.CreateToast(toastText);
    	setActionToRemoveFromFavourites();
    }
    
    private void removeFromFavourites() {
    	FullWord fullWord = getFullWord();
    	favouritesManager.removeFavourite(fullWord);
    	String toastText = removeFromFavsStrings.replacePlaceholder(removeFromFavsStrings.getString(), fullWord.getFullWord());
//    	String toastText = fullWord.getFullWord() + " " + getString(R.string.favourites_remove_from_favourites_toast);
    	toaster.CreateToast(toastText);
    	setActionToAddToFavourites();
    }
    
    private void setActionToRemoveFromFavourites() {
    	setMainTextViewToRemoveFavourites();
    	DownloadImageTask.setVisible(this.getAddToFavourites_selectedButton());
    	DownloadImageTask.setInvisible(this.getAddToFavouritesButton());
    }
    
    private void setActionToAddToFavourites() {
    	setMainTextViewToAddFavourites();
    	DownloadImageTask.setVisible(this.getAddToFavouritesButton());
    	DownloadImageTask.setInvisible(this.getAddToFavourites_selectedButton());
    }
    
    private FullWord getFullWord() {
    	FullWord fullWord = new FullWord();
    	fullWord.setFirstPart(word1);
    	fullWord.setSecondPart(word2);
    	return fullWord;
    }
    
    
    private void showFirstResult() {
		SharedPreferences prefs = getSharedPreferences(MainActivity.sharedPreferencesStore, 0);
		word1 = prefs.getString(MainActivity.word1Store, null);
		word2 = prefs.getString(MainActivity.word2Store, null);
    	if (word1 == null || word2 == null)
    	{
    		mainButtonAction();
    	}
    	else
    	{
	    	setTextAndShowPictures(this.getFullWord());
	        		
	        SharedPreferences.Editor mEditor = prefs.edit();
	        mEditor.putString(MainActivity.word1Store, null).commit();
	        mEditor.putString(MainActivity.word2Store, null).commit();
    	}
    }
    
    private void mainButtonAction() {
    	if (!actionLock) {
    		actionLock = true;
        	AnimalAdjectiveGenerator gen = new AnimalAdjectiveGenerator(this);
        	word1 = gen.GetWord1();
        	word2 = gen.GetWord2();
        	
        	FullWord fullWord = this.getFullWord();
        	this.previousAndNextManager.addWord(fullWord);
        	setTextAndShowPictures(fullWord);
        	actionLock = false;
    	}
    }
    
    private void checkIfFavourite(FullWord query) {
    	if (this.favouritesManager.isFavourite(query)) {
    		this.setActionToRemoveFromFavourites();
    	} else {
    		this.setActionToAddToFavourites();
    	}
    }
    
    private void setTextAndShowPictures(FullWord fullWord) {
    	String result = fullWord.getFullWord();
    	checkIfFavourite(fullWord);
    	SetResultTextView(result);
    	
    	ConnectionTest connectionTest = new ConnectionTest();
    	if (connectionTest.isNetworkAvailable(this)) { //Get a picture if device is on the internet.
    		SetMainImageView(word2);
    	}
    	else
    	{
    		HTMLUtils.emptyLastImageURL();
    		View mainImage = getMainImageView();
    		View progress = getProgressView();
    		DownloadImageTask.setInvisible(mainImage);
    		DownloadImageTask.setInvisible(progress);
    	}
    	
    }
    
    private DownloadImageTask downloadImageTask;
    private void SetMainImageView(String wordToSearchFor) {
    	ImageView mainImage = getMainImageView();
    	View progress = getProgressView();
    	if (downloadImageTask != null && !downloadImageTask.isCancelled()) {
    		downloadImageTask.cancel(true);
    	}
    	downloadImageTask = new DownloadImageTask(mainImage, progress);
    	downloadImageTask.execute(wordToSearchFor);
    }
    
    private ImageView getMainImageView() {
    	return (ImageView) findViewById(R.id.mainImageView);
    }
    
    private View getProgressView() {
    	return (View) findViewById(R.id.imageLoading);
    }
    
    private ImageView getAddToFavouritesButton() {
    	return (ImageView)findViewById(R.id.main_addToFavourites);
    }
    
    private ImageView getAddToFavourites_selectedButton() {
    	return (ImageView)findViewById(R.id.main_addToFavourites_selected);
    }
    
    private Button getGoButton() {
    	return (Button)findViewById(R.id.goButton);
    }
    
    private TextView getWordResultsView() {
    	return (TextView) findViewById(R.id.wordResultView);
    }
    
    private Button getPreviousButton() {
    	return (Button)findViewById(R.id.main_previousButton);
    }
    
    private Button getNextButton() {
    	return (Button)findViewById(R.id.main_nextButton);
    }
    
    private void SetResultTextView(String text) {
    	TextView resultTextView = getWordResultsView();
    	SetTextViewText(resultTextView, text);
    }
    
    private void SetTextViewText(TextView view, String text) {
    	view.setText(text);
    }
    
    private View getShareToFacebookButton() {
    	return findViewById(R.id.main_shareToFacebook);
    }

}
