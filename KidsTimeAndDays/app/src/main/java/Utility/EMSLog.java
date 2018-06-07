package Utility;

import android.util.Log;

public class EMSLog {
	public static final boolean LOG = false;
	
	public static void LogI(String method, String msg) {
		Log.i("EMS", method + " " + msg);
	}
	public static void LogD(String method, String msg) {
		Log.d("EMS", method + " " + msg);
	}
	public static void LogE(String method, String msg) {
		Log.e("EMS", method + " " + msg);
	}	
}
