package com.goldenbeetle.learnenglishspellingsadv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.speech.tts.TextToSpeech;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.db.DatabaseAccessor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.utils.Constants;
import com.utils.NoRepeatRandom;
import com.utils.Utils;

public class EasyGameActivity extends Activity implements OnClickListener,
		TextToSpeech.OnInitListener {

	private JSONArray inputWordListArr;
	private JSONArray ja;
	private List<ListItem> items;
	private NoRepeatRandom nrr;

	private LinearLayout containerLayout;
	private Button firstButton;
	private Button secondButton;
	private Button thirdButton;
	private Button fourthButton;
	private ImageButton replayButton;
	private TextView timerView;
	private TextView wordsVeiw;
	private TextView correctionView;
	private TextView scoreView;

	private int randomValue;
	private int bonusScore;
	private CountDownTimer timer;
	private ImplementQuiz iquiz;
	private int quizeIndex = -1;
	private int untargetPosition[] = new int[3];
	private SharedPreferences prefs;
	private int numOfQuestion;
	private int score = 0;
	private String GAME_LEVEL;
	private boolean isExit;
	private boolean isSoundOn;
	private Typeface font;
	private Bitmap bm;
	private TextToSpeech mTts;
	private String ttsWord = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int screenwidth = getWindowManager().getDefaultDisplay().getWidth();
		if (screenwidth > 250) {
			setContentView(R.layout.layout_game_screen);

		} else {
			setContentView(R.layout.layout_game_ldpi_screen);
		}

		mTts = new TextToSpeech(this, this);

		containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
		bm = BitmapFactory.decodeResource(getResources(), R.drawable.button_1);

		font = Typeface.createFromAsset(getAssets(), "Comfortaa_Regular.ttf");
		GAME_LEVEL = getIntent().getStringExtra(Constants.GAME_LEVEL);
		isSoundOn = SettingPrefs.getSettingValue(this, SettingPrefs.SOUND_ON);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		firstButton = (Button) findViewById(R.id.firstButton);
		secondButton = (Button) findViewById(R.id.secondButton);
		thirdButton = (Button) findViewById(R.id.thirdButton);
		fourthButton = (Button) findViewById(R.id.fourthButton);
		replayButton = (ImageButton) findViewById(R.id.btn_play_icon);
		timerView = (TextView) findViewById(R.id.timerView);
		wordsVeiw = (TextView) findViewById(R.id.wordsView);
		correctionView = (TextView) findViewById(R.id.correctionView);
		scoreView = (TextView) findViewById(R.id.scoreView);

		timerView.setTypeface(font);
		wordsVeiw.setTypeface(font);
		correctionView.setTypeface(font);
		scoreView.setTypeface(font);

		items = new ArrayList<ListItem>();
		manageAllInput(EasyGameActivity.this);

		iquiz = new ImplementQuiz(quizeIndex, Constants.EASY_GAME_MILLISECONDS);
		iquiz.execute();

		firstButton.setOnClickListener(this);
		secondButton.setOnClickListener(this);
		thirdButton.setOnClickListener(this);
		fourthButton.setOnClickListener(this);
		replayButton.setOnClickListener(this);
		try {
			final AdView adView = (AdView) findViewById(R.id.adView);
			adView.loadAd(new AdRequest.Builder().build());
			adView.setAdListener(new AdListener() {
				public void onAdLoaded() {
					adView.setVisibility(View.VISIBLE);
		        }
				public void onAdFailedToLoad(int errorCode) {
					if (!Utility.Utils.isNetworkAvailable(EasyGameActivity.this)) {
						adView.setVisibility(View.GONE);
					}
		        }
		    });
		} catch (Exception e) {}		
	}

	@Override
	public void onDestroy() {
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}

		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				iquiz.cancel(true);
				timer.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class ImplementQuiz extends AsyncTask<String, String, String> {
		private int currentIndex;
		private long waitingTime;

		public ImplementQuiz(int currentIndex, long waitingTime) {
			super();
			this.currentIndex = currentIndex;
			this.waitingTime = waitingTime;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onProgressUpdate(String... arg0) {
			try {
				quizeIndex = currentIndex;
				currentIndex++;
				firstButton.setEnabled(true);
				secondButton.setEnabled(true);
				thirdButton.setEnabled(true);
				fourthButton.setEnabled(true);
				showTimer(waitingTime);
				ttsWord = items.get(currentIndex).targetString;
				playQuiz(EasyGameActivity.this,
						items.get(currentIndex).inputString,
						items.get(currentIndex).missedChar, randomValue);

				wordsVeiw.setText("Words remaining: "
						+ (numOfQuestion - currentIndex - 1));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				while ((currentIndex < (numOfQuestion - 1)) && (!isFinishing())) {
					Random random = new Random();
					randomValue = random.nextInt(4);
					publishProgress("");
					Thread.sleep(waitingTime);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String unused) {
			wordsVeiw.setText("Words remaining: " + 0);
			if (!isExit) {
				if (score > 0) {
					Utils.play(EasyGameActivity.this, R.raw.applause);
				}
				if (DatabaseAccessor.isScoreInTop(GAME_LEVEL, numOfQuestion,score)) {
					showScorePostPopUp(EasyGameActivity.this);
				} else {
					showAlertMessage(EasyGameActivity.this,android.R.drawable.ic_dialog_alert, "Game Completed!","The game is completed !");
				}
			}
		}
	}

	private void speakTTS(String text) {
		mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	private void showAlertMessage(Context context, int icon, String title,
			String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(icon);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								Intent intent = new Intent(
										EasyGameActivity.this,
										HomeScreenActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						});
		final AlertDialog alert = builder.create();
		alert.setTitle(title);
		alert.show();
	}

	private void showScorePostPopUp(final Context context) {

		LayoutInflater factory = LayoutInflater.from(context);
		final View dialog = factory
				.inflate(R.layout.layout_alert_message, null);
		final EditText nameEdit = (EditText) dialog.findViewById(R.id.nameEdit);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Congratulations for your highest score !");
		builder.setCancelable(false).setPositiveButton("Save Score",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						String name = nameEdit.getText().toString();
						if (name.length() == 0) {
							Toast.makeText(context, "Name can not be empty",
									Toast.LENGTH_SHORT).show();
						} else {
							boolean isSuccess = DatabaseAccessor.addScore(name,
									score, GAME_LEVEL, numOfQuestion);
							if (isSuccess) {
								Toast.makeText(context,
										"Score save successfully ",
										Toast.LENGTH_SHORT).show();
								dialog.cancel();
								finish();
							}

						}

					}
				});
		final AlertDialog alert = builder.create();
		alert.setView(dialog);
		alert.setTitle("Congratulations !!");
		alert.show();

	}

	private void showTimer(long remainingCountTime) {
		bonusScore = 0;
		timer = new CountDownTimer(remainingCountTime, 100) {

			@Override
			public void onTick(long millisUntilFinished) {
				String time = "";
				bonusScore = (int) (millisUntilFinished / 1000);
				long second = millisUntilFinished / 1000;
				long tenth = (millisUntilFinished % 1000) / 100;
				time = second + "." + tenth;
				timerView.setText(time);

			}

			@Override
			public void onFinish() {
				timerView.setText(0 + "." + 0);

			}
		}.start();

	}

	private void manageAllInput(Context context) {

		try {
			numOfQuestion = Integer.parseInt(prefs.getString(SettingPrefs.QUIZ_SIZE, "20"));
			ja = new JSONArray(new String(Utils.getFileData(context, Constants.JSON_FILE)));
			int count = 0;
			inputWordListArr = new JSONArray();
			
			for (int i=0;i<ja.length();i++) {
				Iterator<?> keys = ja.getJSONObject(i).keys();
				while (keys.hasNext()) {
					String arrayName=String.valueOf(keys.next());
					if (prefs.getBoolean(SettingPrefs.ITEM_SELECTED + arrayName, true)) {
						JSONArray setArray = ja.getJSONObject(i).getJSONArray(arrayName);
						for (int j = 0; j < setArray.length(); j++) {
							if (setArray.getJSONObject(j).getString("word").length() <= 5) {
								inputWordListArr.put(count++,setArray.getJSONObject(j));
							}
						}	
					}
				}
			}

			int targePosition = 0;
			String targetString = "";
			int missedPosition;
			String inputString = "";
			nrr = new NoRepeatRandom(0, inputWordListArr.length() - 1);
			for (int i = 0; i < numOfQuestion; i++) {
				targePosition = nrr.GetRandom();

				targetString = inputWordListArr.getJSONObject(targePosition)
						.getString("word");
				missedPosition = (new Random()).nextInt(targetString.length());

				StringBuilder targetBuilder = new StringBuilder(targetString);
				targetBuilder.setCharAt(missedPosition, '?');

				inputString = new String(targetBuilder);

				items.add(new ListItem(missedPosition, targetString
						.charAt(missedPosition), targetString, inputString));

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public class ListItem {
		int missedIndex;
		String targetString;
		String inputString;
		char missedChar;

		public ListItem(int missedPosition, char missedChar,
				String targetString, String inputString) {
			super();
			this.missedIndex = missedPosition;
			this.missedChar = missedChar;
			this.targetString = targetString;
			this.inputString = inputString;
		}

	}

	private void playQuiz(Context context, String inputString, char missedChar,
			int randomValue) {
		if (isSoundOn) {
			try {
				speakTTS(ttsWord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Toast.makeText(EasyGameActivity.this, inputString,
		// Toast.LENGTH_SHORT).show();
		Log.i("DREG", inputString);
		containerLayout.removeAllViews();

		if (missedChar < 'm') {
			nrr = new NoRepeatRandom(14, 25);
			for (int i = 0; i < 3; i++) {
				untargetPosition[i] = nrr.GetRandom();
			}
		} else {
			nrr = new NoRepeatRandom(0, 12);
			for (int i = 0; i < 3; i++) {
				untargetPosition[i] = nrr.GetRandom();
			}
		}

		for (int i = 0; i < inputString.length(); i++) {

			Button button = new Button(context);
			button.setText(Character.toString(inputString.charAt(i)).toUpperCase(Locale.ENGLISH));

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			float textSize = 0;

			int displayWidth = getWindowManager().getDefaultDisplay().getWidth();

			if (displayWidth < 600) {
				lp.leftMargin = 5;

			} else {
				lp.leftMargin = 15;

			}

			//textSize = bm.getWidth();
			textSize = 75;
			button.setLayoutParams(lp);

			switch (i % 4) {
			case 0:
				button.setBackgroundResource(R.drawable.button_1);
				break;
			case 1:
				button.setBackgroundResource(R.drawable.button_2);
				break;
			case 2:
				button.setBackgroundResource(R.drawable.button_3);
				break;
			case 3:
				button.setBackgroundResource(R.drawable.button_4);
				break;

			default:
				break;
			}

			button.setTextSize(textSize);
			button.setTextColor(Color.WHITE);
			containerLayout.addView(button);

			switch (randomValue) {
			case 0:
				firstButton.setText(("" + missedChar).toUpperCase(Locale.ENGLISH));
				secondButton
						.setText("" + Constants.letter[untargetPosition[0]]);
				thirdButton.setText("" + Constants.letter[untargetPosition[1]]);
				fourthButton
						.setText("" + Constants.letter[untargetPosition[2]]);
				break;
			case 1:
				firstButton.setText("" + Constants.letter[untargetPosition[0]]);
				secondButton.setText(("" + missedChar).toUpperCase(Locale.ENGLISH));
				thirdButton.setText("" + Constants.letter[untargetPosition[1]]);
				fourthButton
						.setText("" + Constants.letter[untargetPosition[2]]);
				break;
			case 2:
				firstButton.setText("" + Constants.letter[untargetPosition[0]]);
				secondButton
						.setText("" + Constants.letter[untargetPosition[1]]);
				thirdButton.setText(("" + missedChar).toUpperCase(Locale.ENGLISH));
				fourthButton
						.setText("" + Constants.letter[untargetPosition[2]]);

				break;
			case 3:
				firstButton.setText("" + Constants.letter[untargetPosition[0]]);
				secondButton
						.setText("" + Constants.letter[untargetPosition[1]]);
				thirdButton.setText("" + Constants.letter[untargetPosition[2]]);
				fourthButton.setText(("" + missedChar).toUpperCase(Locale.ENGLISH));

				break;
			//
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if ((id == R.id.firstButton) || (id == R.id.secondButton)
				|| (id == R.id.thirdButton) || (id == R.id.fourthButton)) {
			firstButton.setEnabled(false);
			secondButton.setEnabled(false);
			thirdButton.setEnabled(false);
			fourthButton.setEnabled(false);
			timer.cancel();
		}
		switch (id) {
		case R.id.firstButton:
			buttonClicked(0);
			break;
		case R.id.secondButton:
			buttonClicked(1);
			break;
		case R.id.thirdButton:
			buttonClicked(2);
			break;
		case R.id.fourthButton:
			buttonClicked(3);
			break;
		case R.id.btn_play_icon:
			if (isSoundOn) {
				try {
					speakTTS(ttsWord);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	private void buttonClicked(int position) {

		if (randomValue == position) {
			correctionView.setText("Right ! !");
			correctionView.setTextColor(Color.WHITE);
			score += 10 + bonusScore;
			scoreView.setText("" + score);

			if (isSoundOn) {
				Utils.play(EasyGameActivity.this, R.raw.right);
			}
		}

		else {
			correctionView.setText("Wrong ! - " + ttsWord.toUpperCase(Locale.getDefault()));
			correctionView.setTextColor(Color.RED);
			scoreView.setText("" + score);
		}

		if (quizeIndex < (numOfQuestion - 1)) {
			try {
				new AsyncTask<String, String, String>() {
					@Override
					protected void onPreExecute() {
						try {
							iquiz.cancel(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					protected String doInBackground(String... arg0) {
						try {
							Log.i("DREG", "Quize Index =" + quizeIndex);
							if (quizeIndex < (numOfQuestion - 1)) {
								Thread.sleep(500);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(String unused) {
						if (quizeIndex < (numOfQuestion - 2)) {
							iquiz = new ImplementQuiz(++quizeIndex, Constants.EASY_GAME_MILLISECONDS);
						} else {
							iquiz = new ImplementQuiz(++quizeIndex, 0);
						}
						iquiz.execute();

					}
				}.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			Locale applyLocale=Locale.UK;
			if (mTts.isLanguageAvailable(Locale.UK)==TextToSpeech.LANG_AVAILABLE) {
				applyLocale=Locale.UK;
			} else if (mTts.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE) {
				applyLocale=Locale.US;
			} else {
				applyLocale=Locale.ENGLISH;
			}
			int result=mTts.setLanguage(applyLocale);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.i("DREG", "Language is not available.");
			} else {
				 speakTTS(ttsWord);
			}
		} else {
			Log.e("DREG", "Could not initialize TextToSpeech.");
		}

	}

}
