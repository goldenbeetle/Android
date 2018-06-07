package com.goldenbeetle.learnenglishspellingsadv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.speech.tts.TextToSpeech;
import org.json.*;
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

public class DifficultGameActivity extends Activity implements OnClickListener,
		TextToSpeech.OnInitListener {

	private JSONArray inputWordListArr;

	private JSONArray ja;
	private List<ListItem> items;
	private NoRepeatRandom nrr;
	private NoRepeatRandom firstNrr;
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
	private ImplementQuize iquize;
	private int quizeIndex = -1;
	private SharedPreferences prefs;
	private int numOfQuestion;
	private int score = 0;
	private String GAME_LEVEL;
	private int missedCharCount;
	private String compareText = "";
	private boolean isExit;
	private boolean isSoundOn;
	private Bitmap bm;

	private Typeface font;
	private int screenwidth;

	private String ttsWord;
	private TextToSpeech mTts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screenwidth = getWindowManager().getDefaultDisplay().getWidth();
		if (screenwidth > 250) {
			setContentView(R.layout.layout_game_screen);
		} else {
			setContentView(R.layout.layout_game_ldpi_screen);
		}
		mTts = new TextToSpeech(this, this);

		containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
		bm = BitmapFactory.decodeResource(getResources(), R.drawable.button_1);

		font = Typeface.createFromAsset(getAssets(), "Epifania.ttf");
		isSoundOn = SettingPrefs.getSettingValue(this, SettingPrefs.SOUND_ON);
		GAME_LEVEL = getIntent().getStringExtra(Constants.GAME_LEVEL);
		Log.i("DREG", GAME_LEVEL);

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
		manageAllInput(DifficultGameActivity.this);

		// setAllButtonText(GameScreenActivity.this, items.get(0).inputString);
		iquize = new ImplementQuize(quizeIndex, Constants.HARD_GAME_MILLISECONDS);
		iquize.execute();

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
					if (!Utility.Utils.isNetworkAvailable(DifficultGameActivity.this)) {
						adView.setVisibility(View.GONE);
					}
		        }
		    });
		} catch (Exception e) {}
	}

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}

		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// isExit = true;
			try {
				iquize.cancel(true);
				timer.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class ImplementQuize extends AsyncTask<String, String, String> {
		private int currentIndex;
		private long waitingTime;

		public ImplementQuize(int currentIndex, long waitingTime) {
			super();
			this.currentIndex = currentIndex;
			this.waitingTime = waitingTime;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onProgressUpdate(String... arg0) {
			try {

				Log.i("DREG", "Here ");
				currentIndex++;
				quizeIndex = currentIndex;

				firstButton.setEnabled(true);
				secondButton.setEnabled(true);
				thirdButton.setEnabled(true);
				fourthButton.setEnabled(true);

				missedCharCount = 0;
				showTimer(waitingTime);
				// Log.i("DREG", "Missed String ="
				// + items.get(currentIndex).missedString);
				ttsWord = items.get(currentIndex).targetString;
				fillGapWithQuestionMark(DifficultGameActivity.this,
						items.get(currentIndex).inputString,
						items.get(currentIndex).missedString, randomValue);
				wordsVeiw.setText("Words left: "
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
			wordsVeiw.setText("Words left: " + 0);
			if (!isExit) {
				if (score > 0) {
					Utils.play(DifficultGameActivity.this, R.raw.applause);
				}
				if (DatabaseAccessor.isScoreInTop(GAME_LEVEL, numOfQuestion,score)) {
					showScorePostPopUp(DifficultGameActivity.this);
				} else {
					showAlertMessage(DifficultGameActivity.this,android.R.drawable.ic_dialog_alert, "Game Completed!","The game is completed !");
				}
			}
		}
	}

	private void showScorePostPopUp(final Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View dialog = factory
				.inflate(R.layout.layout_alert_message, null);
		final EditText nameEdit = (EditText) dialog.findViewById(R.id.nameEdit);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Congratulations for your highest score !");
		builder.setCancelable(true).setPositiveButton("Save score",
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

			ja = new JSONArray(new String(Utils.getFileData(context,Constants.JSON_FILE)));
			int count = 0;
			inputWordListArr = new JSONArray();

			int maxLength;
			if (screenwidth > 250) {
				maxLength = 6;
			} else {
				maxLength = 5;
			}

			for (int i=0;i<ja.length();i++) {
				Iterator<?> keys = ja.getJSONObject(i).keys();
				while (keys.hasNext()) {
					String arrayName=String.valueOf(keys.next());
					if (prefs.getBoolean(SettingPrefs.ITEM_SELECTED + arrayName, true)) {
						JSONArray setArray = ja.getJSONObject(i).getJSONArray(arrayName);
						for (int j = 0; j < setArray.length(); j++) {
							int length = setArray.getJSONObject(j).getString("word").length();
							if ((length >= 2) && (length <= maxLength)) {
								inputWordListArr.put(count++,setArray.getJSONObject(j));
							}
						}							
					}				
				}
			}
			int targePosition = 0;
			String targetString = "";
			int firstMissedPosition;
			int secondMissedPosition;
			String inputString = "";

			nrr = new NoRepeatRandom(0, inputWordListArr.length() - 1);
			int numOfGap;
			for (int i = 0; i <= (numOfQuestion + 1); i++) {

				String missedString = "";
				targePosition = nrr.GetRandom();

				firstMissedPosition = -1;
				secondMissedPosition = -1;

				targetString = inputWordListArr.getJSONObject(targePosition)
						.getString("word");

				int randomGap = (new Random()).nextInt(2);

				if (randomGap == 0) {
					numOfGap = 1;
					firstMissedPosition = (new Random()).nextInt(targetString
							.length() - 1);
					missedString += targetString.charAt(firstMissedPosition);
					// Log.i("DREG", "Missed String =" + missedString);

				} else {
					numOfGap = 2;
					firstNrr = new NoRepeatRandom(0, targetString.length() - 1);
					int first = firstNrr.GetRandom();
					int second = firstNrr.GetRandom();
					firstMissedPosition = Math.min(first, second);// firstNrr.GetRandom();
					secondMissedPosition = Math.max(first, second);// firstNrr.GetRandom();

					missedString += Character.toString(targetString
							.charAt(firstMissedPosition))
							+ Character.toString(targetString
									.charAt(secondMissedPosition));

					// Log.i("DREG", "Missed String =" + missedString);
				}

				StringBuilder targetBuilder = new StringBuilder(targetString);
				if (firstMissedPosition != -1) {
					targetBuilder.setCharAt(firstMissedPosition, '?');

				}
				if (secondMissedPosition != -1) {
					targetBuilder.setCharAt(secondMissedPosition, '?');
				}

				inputString = new String(targetBuilder);

				missedString = missedString.toUpperCase(Locale.ENGLISH);

				items.add(new ListItem(numOfGap, missedString, targetString,
						inputString));

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public class ListItem {
		String targetString;
		String inputString;
		String missedString;
		int numOfGap;

		public ListItem(int numOfGap, String missedString, String targetString,
				String inputString) {
			super();
			this.numOfGap = numOfGap;
			this.missedString = missedString;
			this.targetString = targetString;
			this.inputString = inputString;
		}

	}

	private void fillGapWithQuestionMark(Context context, String inputString, String missedString, int randomValue) {
		if (isSoundOn) {
			try {
				speakTTS(ttsWord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		inputString = inputString.toUpperCase(Locale.ENGLISH);
		missedString = missedString.toUpperCase(Locale.ENGLISH);
		// Log.i("DREG", inputString);
		containerLayout.removeAllViews();

		char inputChars[] = new char[4];

		char firstMissedChar;
		char secondMissedChar;
		if (missedString.length() == 1) {
			firstMissedChar = missedString.charAt(0);
			inputChars[0] = firstMissedChar;

			inputChars[0] = firstMissedChar;
			int count = 0;
			NoRepeatRandom chNrr = new NoRepeatRandom(0,
					Constants.letter.length - 1);
			for (int i = 0; i < Constants.letter.length; i++) {
				int position = chNrr.GetRandom();
				// Log.i("DREG", "Position ="+position);
				char ch = Constants.letter[position];
				if ((ch != firstMissedChar) && (count <= 2)) {
					inputChars[++count] = ch;
					// Log.i("DREG", "Count ="+count);
				}
			}

		} else {
			firstMissedChar = missedString.charAt(0);
			secondMissedChar = missedString.charAt(1);
			if (firstMissedChar == secondMissedChar) {
				inputChars[0] = firstMissedChar;
				int count = 0;
				NoRepeatRandom chNrr = new NoRepeatRandom(0,
						Constants.letter.length - 1);
				for (int i = 0; i < Constants.letter.length; i++) {
					int position = chNrr.GetRandom();
					char ch = Constants.letter[position];
					if ((ch != firstMissedChar) && (count <= 2)) {
						inputChars[++count] = ch;
					}
				}
			} else {
				inputChars[0] = firstMissedChar;
				inputChars[1] = secondMissedChar;

				int count = 1;
				NoRepeatRandom nrr = new NoRepeatRandom(0,
						Constants.letter.length - 1);
				for (int i = 0; i < Constants.letter.length; i++) {
					int position = nrr.GetRandom();
					char ch = Constants.letter[position];
					if ((ch != firstMissedChar) && (ch != secondMissedChar)
							&& (count <= 2)) {
						inputChars[++count] = ch;
					}
				}
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
				lp.leftMargin = 0;
				textSize = bm.getWidth();
			} else {
				lp.leftMargin = 10;
				textSize = bm.getWidth();
			}

			textSize = 60;
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

			// button.setHeight(hw);
			// button.setWidth(hw);
			button.setTextSize(textSize);
			containerLayout.addView(button);

			String option0 = "" + inputChars[0];
			String option1 = "" + inputChars[1];
			String option2 = "" + inputChars[2];
			String option3 = "" + inputChars[3];

			switch (randomValue) {
			case 0:
				firstButton.setText(option0);
				secondButton.setText(option1);
				thirdButton.setText(option2);
				fourthButton.setText(option3);
			case 1:
				firstButton.setText(option1);
				secondButton.setText(option0);
				thirdButton.setText(option2);
				fourthButton.setText(option3);
				break;
			case 2:
				firstButton.setText(option2);
				secondButton.setText(option1);
				thirdButton.setText(option0);
				fourthButton.setText(option3);
				break;
			case 3:
				firstButton.setText(option3);
				secondButton.setText(option1);
				thirdButton.setText(option2);
				fourthButton.setText(option0);

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
		switch (id) {
		case R.id.firstButton:
			compareText += firstButton.getText().toString();
			buttonClicked(0);
			break;
		case R.id.secondButton:
			compareText += secondButton.getText().toString();
			buttonClicked(1);
			break;
		case R.id.thirdButton:
			compareText += thirdButton.getText().toString();
			buttonClicked(2);
			break;
		case R.id.fourthButton:
			compareText += fourthButton.getText().toString();
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
										DifficultGameActivity.this,
										HomeScreenActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						});
		final AlertDialog alert = builder.create();
		alert.setTitle(title);
		alert.show();
	}

	private void buttonClicked(int position) {
		missedCharCount++;

		Log.i("DREG", "Missed String =" + items.get(quizeIndex).missedString);

		if (missedCharCount == items.get(quizeIndex).numOfGap) {

			firstButton.setEnabled(false);
			secondButton.setEnabled(false);
			thirdButton.setEnabled(false);
			fourthButton.setEnabled(false);

			compareText = compareText.toUpperCase(Locale.ENGLISH);
			missedCharCount = 0;
			Log.i("DREG", "CompareText =" + compareText);

			boolean isRight = compareText.equalsIgnoreCase(items
					.get(quizeIndex).missedString);
			if (isRight) {
				correctionView.setText("Right ! !");
				correctionView.setTextColor(Color.WHITE);
				score += 10 + bonusScore;
				scoreView.setText("" + score);
				if (isSoundOn) {
					Utils.play(DifficultGameActivity.this, R.raw.right);
				}
			} else {
				correctionView.setText("Wrong ! - " + ttsWord.toUpperCase(Locale.getDefault()));
				correctionView.setTextColor(Color.RED);
				scoreView.setText("" + score);
			}
			compareText = "";
			Log.i("DREG", "Quiz index =" + quizeIndex);
			if (quizeIndex < (numOfQuestion - 1)) {
				try {
					timer.cancel();
					iquize.cancel(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					new AsyncTask<String, String, String>() {
						@Override
						protected void onPreExecute() {

						}

						@Override
						protected String doInBackground(String... arg0) {
							try {
								// Log.i("DREG", "Quize Index =" + quizeIndex);
								if (quizeIndex < numOfQuestion - 1) {
									Thread.sleep(500);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(String unused) {
							if (quizeIndex < (numOfQuestion - 1)) {
								iquize = new ImplementQuize(quizeIndex++, Constants.HARD_GAME_MILLISECONDS);
							}
							iquize.execute();

						}
					}.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void speakTTS(String text) {
		mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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

			Log.i("DREG", "Could not initialize TextToSpeech.");
		}

	}

}
