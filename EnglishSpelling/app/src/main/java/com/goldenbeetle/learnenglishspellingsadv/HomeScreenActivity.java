package com.goldenbeetle.learnenglishspellingsadv;

import com.db.DataBaseHelper;
import com.db.DatabaseAccessor;

import Utility.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.utils.Constants;

public class HomeScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home_screen);
		//Tested for git hub
		try {

			DatabaseAccessor.initDB(HomeScreenActivity.this);
			DataBaseHelper.manageDatabase(HomeScreenActivity.this);
			DatabaseAccessor.createScoreTable();

		} catch (Exception e) {
			e.printStackTrace();
		}

		findViewById(R.id.settingButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(HomeScreenActivity.this,
								SettingPrefs.class);
						startActivity(intent);

					}
				});

		findViewById(R.id.newGameButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// Toast.makeText(HomeScreenActivity.this, "Clicked",
						// Toast.LENGTH_LONG).show();
						Intent intent = null;
						String gameLevel = SettingPrefs
								.getGameLevel(HomeScreenActivity.this);
						// Log.i("DREG", "Game level =" + gameLevel);
						boolean isEasyGame = gameLevel
								.equalsIgnoreCase(SettingPrefs.OPTION_LEVEL_DEFAULT);
						if (isEasyGame) {
							// Log.i("DREG", "Easy Game Here");
							intent = new Intent(HomeScreenActivity.this,
									EasyGameActivity.class);
							intent.putExtra(Constants.GAME_LEVEL,
									Constants.EASY_GAME);
						} else {
							// Log.i("DREG", "Hard Game Here");
							intent = new Intent(HomeScreenActivity.this,
									DifficultGameActivity.class);
							intent.putExtra(Constants.GAME_LEVEL,
									Constants.HARD_GAME);
						}

						startActivity(intent);

					}
				});

		findViewById(R.id.scoreButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(HomeScreenActivity.this,
								ScoreActivity.class);
						startActivity(intent);

					}
				});

		findViewById(R.id.helpButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(HomeScreenActivity.this,
								HelpActivity.class);
						startActivity(intent);

					}
				});
		findViewById(R.id.leaderBoardButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Utility.Utils.moreAppsFromDeveloper(getBaseContext(),Utils.DEVELOPER_NAME);
					}
				});
		try {
			final AdView adView = (AdView) findViewById(R.id.adView);
			adView.loadAd(new AdRequest.Builder().build());
			adView.setAdListener(new AdListener() {
				public void onAdLoaded() {
					adView.setVisibility(View.VISIBLE);
		        }
				public void onAdFailedToLoad(int errorCode) {
					if (!Utility.Utils.isNetworkAvailable(HomeScreenActivity.this)) {
						adView.setVisibility(View.GONE);
					}
		        }
		    });
		} catch (Exception e) {}
		
	    Bundle extras = getIntent().getExtras();
		if (extras!=null && !extras.isEmpty()) {
			Utils.processBundleExtras(getApplicationContext(),extras);
		}	
        if (EMSLog.LOG) { EMSLog.LogD("MainActivity:onCreate", "Ended"); }		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAlertMessage(HomeScreenActivity.this, R.drawable.icon_question,
					"Exit app?", "Do you want to exit from the application ?");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showAlertMessage(Context context, int icon, String title,
			String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(icon);
		builder.setMessage(message)
				.setCancelable(false)
				.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								HomeScreenActivity.this.finish();
								System.runFinalizersOnExit(true);
								System.exit(0);
								android.os.Process.killProcess(android.os.Process.myPid());
							}
						})
				.setPositiveButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.setTitle(title);
		alert.show();
	}

}
