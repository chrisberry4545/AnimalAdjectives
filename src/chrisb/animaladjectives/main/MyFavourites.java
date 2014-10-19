package chrisb.animaladjectives.main;

import chrisb.animaladjectives.R;
import chrisb.animaladjectives.content.FullWord;
import chrisb.animaladjectives.socialmedia.FacebookShare;
import chrisb.animaladjectives.storage.FavouritesManager;
import chrisb.animaladjectives.utils.ConnectionTest;
import chrisb.animaladjectives.utils.DownloadImageTask;
import chrisb.animaladjectives.utils.OrientationManager;
import chrisb.animaladjectives.utils.ShakeDetector;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFavourites extends ActionBarActivity {
	private FavouritesManager favsManager = new FavouritesManager(this);
	private int favouriteNumber = 0;
	private FullWord word = new FullWord();
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
        
        setContentView(R.layout.activity_favourites);
        instantiateUI();
        
        PackageManager manager = getPackageManager();
        hasAccelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        if (hasAccelerometer) {
        	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensorListener = new ShakeDetector();   
            mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {

              public void onShake() {
            	  nextFavourite();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    private void instantiateUI() {
    	showFavourites();
    	setPreviousFavouriteButton();
    	setUpNextFavouriteButton();
    	setUpRemoveFavouriteButton();
    	setUpShareToFacebookButton();
    }
    
    private void showFavourites() {
    	if (favouriteNumber > -1)
    	{
    		FullWord favourite = favsManager.getFavourite(favouriteNumber);
        	if (favourite != null && favourite.getFullWord() != "") {
        		setResultsText(favourite.getFullWord());
        		setPicture(favourite.getLastPart());
        		word = favourite;
        	} else {
        		setResultsText(this.getString(R.string.favourites_none_selected));
        		getMainImageView().setImageBitmap(null);
        		DownloadImageTask.setInvisible(this.getProgressView());
        		DownloadImageTask.setInvisible(this.getRemoveFavouriteButton());
        		DownloadImageTask.setInvisible(this.getPreviousFavouriteButton());
        		DownloadImageTask.setInvisible(this.getNextFavouriteButton());
        	}
    	}
    }
    
    private void setPreviousFavouriteButton() {
    	Button button = getPreviousFavouriteButton();
    	button.setOnClickListener(new OnClickListener() {
		    		
		    		@Override
		    		public void onClick(View v) {
		    			previousFavourite();
		    		}
			});
    }
    
    private void setUpNextFavouriteButton() {
    	Button button = getNextFavouriteButton();
    	button.setOnClickListener(new OnClickListener() {
		    		
		    		@Override
		    		public void onClick(View v) {
		    			nextFavourite();
		    		}
			});
    }
    
    private void setUpRemoveFavouriteButton() {
    	View button = getRemoveFavouriteButton();
    	button.setOnClickListener(new OnClickListener() {
		    		
		    		@Override
		    		public void onClick(View v) {
		    			removeFavourite();
		    		}
			});
    	TextView mainTextView = getMainTextView();
    	mainTextView.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			removeFavourite();
    		}
    	});
    }
    
    private void setUpShareToFacebookButton() {
    	View button = getShareToFacebookButton();
    	button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			shareToFacebook();
    		}
    	});
    }
    
    private void shareToFacebook() {
    	FullWord favourite = favsManager.getFavourite(favouriteNumber);
    	this.facebookManager.shareToFacebook(favourite.getFullWord());
    }
    
    private void nextFavourite() {
    	if (!actionLock) {
    		actionLock = true;
        	favouriteNumber++;
        	int lastFavNumber = favsManager.getLastFavouriteNumber();
        	if (favouriteNumber > lastFavNumber) {
        		favouriteNumber = 0;
        	}
        	showFavourites();
        	actionLock = false;
    	}
    }
    
    private void previousFavourite() {
    	if (!actionLock) {
    		actionLock = true;
    		favouriteNumber--;
        	if (favouriteNumber < 0) {
        		favouriteNumber = favsManager.getLastFavouriteNumber();
        	}
        	showFavourites();
        	actionLock = false;
    	}
    }
    
    private void processRemoveFavourite() {
    	this.favsManager.removeFavourite(word);
		nextFavourite();
    }
    
    private void removeFavourite() {
    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	processRemoveFavourite();
    	            break;

    	        case DialogInterface.BUTTON_NEGATIVE:
    	            break;
    	        }
    	    }
    	};
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(
    			getString(R.string.favourites_remove_from_part1) 
    			+ " "
    			+ word.getFullWord() 
    			+ " "
    			+ getString(R.string.favourites_remove_from_part2))
    			.setPositiveButton(getString(R.string.yes), dialogClickListener)
    	    .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }
    
    private void setResultsText(String text) {
    	TextView resultsText = getMainTextView();
    	resultsText.setText(text);
    }
    
    private void setPicture(String text) {
    	ConnectionTest connectionTest = new ConnectionTest();
    	if (connectionTest.isNetworkAvailable(this)) { //Get a picture if device is on the internet.
    		SetMainImageView(text);
    	}
    	else
    	{
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
    	return (ImageView) findViewById(R.id.favourites_mainImageView);
    }
    
    private View getProgressView() {
    	return (View) findViewById(R.id.favourites_imageLoading);
    }
    
    private View getRemoveFavouriteButton() {
    	return findViewById(R.id.favourites_removeFavouriteButton);
    }
    
    private Button getNextFavouriteButton() {
    	return (Button) findViewById(R.id.favourites_nextFavouriteButton);
    }
    
    private Button getPreviousFavouriteButton() {
    	return (Button) findViewById(R.id.favourites_previousFavouriteButton);
    }
    
    private TextView getMainTextView() {
    	return (TextView)findViewById(R.id.favourites_wordResultView);
    }
    
    private View getShareToFacebookButton() {
    	return findViewById(R.id.favourites_shareToFacebookButton);
    }
}
