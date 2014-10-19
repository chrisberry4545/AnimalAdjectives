package chrisb.animaladjectives.utils;

import android.graphics.Bitmap;

public class PictureGenerator {
	
	public static String GoogleSearchStart = "https://www.google.co.uk/search?q=";
    public static String GoogleSearchEnd = "&safe=off&espv=210&es_sm=93&source=lnms&tbm=isch&sa=X&ei=8qJBU7eBCc2RhQeSmoDgCA&ved=0CAgQ_AUoAQ";
    public static String AdditionalSearchString = "%20animal";
    
    public static Bitmap GetImageSrc(String imageName) {
    	String searchTerm = imageName + AdditionalSearchString;
        String fullUrl = GoogleSearchStart + searchTerm + GoogleSearchEnd;
        String src = HTMLUtils.GetImageSrc(fullUrl);
        return HTMLUtils.getBitmapFromURL(src);
    }

}
