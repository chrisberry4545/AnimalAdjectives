package chrisb.animaladjectives.socialmedia;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import chrisb.animaladjectives.R;
import chrisb.animaladjectives.utils.HTMLUtils;

import com.facebook.AppLinkData;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;

public class FacebookShare {

	private UiLifecycleHelper uiHelper;
	private Activity activity;
	
	private static final String animalAdjectivesQuery = "cbanimaladjectives:fullword";
	
	public FacebookShare(Bundle savedInstanceState, Activity activity) {
		this.activity = activity;
        uiHelper = new UiLifecycleHelper(activity, null);
        uiHelper.onCreate(savedInstanceState);
	}
	
    public void onResume() {
    	uiHelper.onResume();
    }

    public void onPause() {
        uiHelper.onPause();
    }
    
    public void onDestroy() {
        uiHelper.onDestroy();
    }
    
    public void onSaveInstanceState(Bundle outState) {
        uiHelper.onSaveInstanceState(outState);
    }
    
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
    	uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }
    
    public void shareToFacebook(String name) {
    	String fullLink = "https://play.google.com/store/apps/details?id=chrisb.animaladjectives&" + animalAdjectivesQuery + "=" + name;
    	
    	OpenGraphObject animalAdjective = OpenGraphObject.Factory.createForPost("cbanimaladjectives:animal_adjective");
    	animalAdjective.setProperty("title", name);
    	animalAdjective.setProperty("url", fullLink);
    	animalAdjective.setProperty("description", activity.getResources().getString(R.string.share_description));
    	animalAdjective.setProperty(animalAdjectivesQuery, name);
    	
    	String lastURL = HTMLUtils.getLastImageURL();
    	if (lastURL != null && lastURL != "") {
        	animalAdjective.setProperty("image", lastURL);
    	}
    	
    	OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
    	action.setProperty("animal_adjective", animalAdjective);
    	action.setProperty(animalAdjectivesQuery, name);
    	action.setProperty("type", "cbanimaladjectives:create");
    	
    	FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(activity, action, "animal_adjective")
    	        .build();
    	uiHelper.trackPendingDialogCall(shareDialog.present());
    }
    
    public String getFacebookData() {
        AppLinkData appLinkData = AppLinkData.createFromActivity(activity);
        if (appLinkData != null) {
            Bundle arguments = appLinkData.getArgumentBundle();
            if (arguments != null) {
                String targetUrl = arguments.getString("target_url");
                if (targetUrl != null) {
                	Uri uri = Uri.parse(targetUrl);
                	String fullWord = uri.getQueryParameter("cbanimaladjectives:fullword");
                	return fullWord;
                }
            }
        }
        return null;
    }
    
    public void printHashKey() {
    	try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "chrisb.animaladjectives", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Toast.makeText(activity.getApplicationContext(), "KeyHash L I l i :" + Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();;
                }
        } catch (NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }
}
