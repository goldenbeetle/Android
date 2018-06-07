package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;

public class Utils {

	public static void showAlertMessage(Context context, int icon,
			String title, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(icon);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {

					}
				});
		final AlertDialog alert = builder.create();
		alert.setTitle(title);
		alert.show();
	}

	public static long getSDCardFreeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getBlockCount();
		long megAvailable = bytesAvailable / 1048576;
		return megAvailable;
	}

	public static void vibratePhone(Context context) {
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(300);

	}

	public static boolean isMounted() {
		boolean mounted = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		return mounted;
	}

	public static void showAlertMessage(Context context, String title,
			String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {

					}
				});
		final AlertDialog alert = builder.create();
		alert.setTitle(title);
		alert.show();
	}

	private static MediaPlayer mp = null;

	/** Stop old song and start new one */
	public static void play(Context context, int resource) {
		stop(context);
		mp = MediaPlayer.create(context, resource);
		mp.setLooping(false);
		mp.start();
	}

	/** Stop the music */
	public static void stop(Context context) {
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}

	public static synchronized void playFile(File file, Context context,
			OnCompletionListener ocl) {
		try {
			stopPlaying();
			MediaPlayer mPlayer = new MediaPlayer();
			if (ocl != null) {
				mPlayer.setOnCompletionListener(ocl);
			}
			mPlayer.setDataSource(file.getAbsolutePath());
			mPlayer.prepare();
			mPlayer.start();
			mediaPlayerRec = mPlayer;
		} catch (Exception e) {
			if (ocl != null) {
				ocl.onCompletion(null);
			}
			e.printStackTrace();
		}
	}

	private static MediaPlayer mediaPlayerRec = null;

	public static synchronized void stopPlaying() {
		try {
			if (mediaPlayerRec != null) {
				try {
					mediaPlayerRec.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
				mediaPlayerRec.release();
				mediaPlayerRec = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] getFileData(Context context, String fileName) {
		byte[] ret = null;
		try {
			InputStream is = context.getAssets().open(fileName);
			byte[] data = new byte[2048];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read;
			while ((read = is.read(data)) != -1) {
				baos.write(data, 0, read);
			}
			is.close();
			ret = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;

	}

	public static boolean isDeviceOnline(Context context) {
		boolean ret = true;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = cm.getActiveNetworkInfo();
		if (i == null || (!i.isConnected()) || (!i.isAvailable())) {
			ret = false;
		}
		return ret;
	}

	public static final ProgressDialog getProgressDialog(Context context,
			String title, String message) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setMax(13);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}

	public static String getAndroidFormattedUrl(String url) {
		if (url != null) {
			url = url.replace(" ", "%20");
		}
		return url;
	}

	public static String[] getUniqueArray(String[] inputParent) {
		Set<String> tempArray = new HashSet<String>(Arrays.asList(inputParent));
		String[] uniqueArray = tempArray.toArray(new String[tempArray.size()]);
		return uniqueArray;
	}

	public static float getWidthDpiFactor(Activity activity) {
		int orientation = activity.getResources().getConfiguration().orientation;
		float widthFactor = 0;
		int width = 0;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			width = activity.getWindowManager().getDefaultDisplay().getWidth();
		} else {
			width = activity.getWindowManager().getDefaultDisplay().getHeight();
		}

		widthFactor = ((float) width) / 480;
		return widthFactor;
	}

	public static final ProgressDialog getDialog(Context context, String message) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}
	
}
