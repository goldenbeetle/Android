package Utility;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

public class Utils {
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final boolean ShowBannerAds = true;
	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return  (xlarge || large);
	}
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	public static String Left(String s, int len){
	    if (s == null)
	        return "";
	    else if (len == 0 || s.length() == 0)
	        return "";
	    else if (s.length() <= len)
	        return s;
	    else
	        return s.substring(0, len-3) + "...";
	}
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
	public static void processBundleExtras(Context con, Bundle extras) {
		try {
	    	String urltype=extras.getString("urltype");
	    	String urlpath=extras.getString("urlpath");
	    	String packagename=extras.getString("package");
	    	if (packagename==null || packagename.equals("") || packagename.equalsIgnoreCase(con.getPackageName())) {
		    	if (urltype!=null && urlpath!=null && !urltype.equalsIgnoreCase("none") && !urlpath.equalsIgnoreCase("")) {
		    		if (urltype.equalsIgnoreCase("gplay"))
		        	{
		        		try {
		        		    con.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + urlpath)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		        		} catch (android.content.ActivityNotFoundException anfe) {
		        		    try {con.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + urlpath)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));} catch(Exception e){}
		        		}        		
		        	} else {
		        		try { con.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlpath)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); }catch(Exception e) { }
		        	}
		    	} 
	    	}
		}catch(Exception e) {}
	}

}
