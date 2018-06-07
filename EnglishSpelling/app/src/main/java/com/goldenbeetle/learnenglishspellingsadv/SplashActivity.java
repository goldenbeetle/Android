package com.goldenbeetle.learnenglishspellingsadv;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layout_splash);
			new AsyncTask<String, String, String>() {

				@Override
				protected void onPreExecute() {
				}

				@Override
				protected String doInBackground(String... arg0) {
					try {
						Thread.sleep(1500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(String unused) {
					Intent i = new Intent(SplashActivity.this, HomeScreenActivity.class);
					startActivity(i);
				}
			}.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showDatabaseErrorAlertMessage() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				SplashActivity.this);
		builder.setMessage("Your device has not much memory to run the app.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						finish();
					}
				});
		final AlertDialog alert = builder.create();
		alert.setTitle("Sorry");
		alert.show();
	}

}