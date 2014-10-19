package chrisb.animaladjectives.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HTMLUtils {
	
	private static String lastImageURL = null;
	
	public static String getLastImageURL() {
		return lastImageURL;
	}
	
	public static void emptyLastImageURL() {
		lastImageURL = null;
	}

	public static String GetImageSrc(String url)
    {
		try 
		{
			Document doc = Jsoup.connect(url).get();
			String imageURL = GetFirstImageFromHtml(doc);
			lastImageURL = imageURL;
			return imageURL;
		}
		catch (IOException e) 
		{
			return null;
		}
    }

    public static String GetFirstImageFromHtml(Document doc)
    {
    	Elements imgs = doc.getElementsByTag("img");
    	if (imgs != null && imgs.size() > 0)
    	{
    		String src = imgs.get(0).attr("src");
    		return src;
    	}
    	return null;
    }
    
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
