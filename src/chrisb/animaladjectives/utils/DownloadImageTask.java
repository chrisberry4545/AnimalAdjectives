package chrisb.animaladjectives.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	  private ImageView bmImage;
	  private View progress;
	  

	  public DownloadImageTask(ImageView bmImage, View spinner) {
	      this.bmImage = bmImage;
	      this.progress = spinner;
	      setInvisible(this.bmImage);
	      setVisible(this.progress);
	  }

	  protected Bitmap doInBackground(String... imgNames) {
	      String imgName = imgNames[0];
	      Bitmap mIcon11 = null;
	      try {
	    	  mIcon11 = PictureGenerator.GetImageSrc(imgName);
	    	  mIcon11 = ImageHelper.getRoundedCornerBitmap(mIcon11, 50);
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	      return mIcon11;
	  }

	  protected void onPostExecute(Bitmap result) {
		  if (!this.isCancelled()) {
		      setInvisible(this.progress);
		      setVisible(this.bmImage);
		      bmImage.setImageBitmap(result);
		  }
	  }
	  
	  public static void setInvisible(View view) {
		  view.setVisibility(View.GONE);
	  }
	  
	  public static void setVisible(View view) {
		  view.setVisibility(View.VISIBLE);
	  }
}