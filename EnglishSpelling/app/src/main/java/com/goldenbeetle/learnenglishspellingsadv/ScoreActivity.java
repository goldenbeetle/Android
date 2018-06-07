package com.goldenbeetle.learnenglishspellingsadv;

import java.util.ArrayList;
import java.util.List;

import com.db.DatabaseAccessor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.utils.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ScoreActivity extends Activity implements OnCheckedChangeListener,
		OnItemSelectedListener {

	private List<String[]> items;
	private EfficientAdapter adapter;
	private ListView listView;

	private RadioGroup radioGroup;

	private String GAME_LEVEL;
	private int numOfQuestion;
	private SharedPreferences prefs;

	private Spinner sizeSpinner;
	private int selectionIndex = 1;
	private boolean isEasyGame;
	private RadioButton easyButton;
	private RadioButton hardButton;
	
	private Typeface font;
	private TextView nameView;
	private TextView scoreView;
	private TextView selectionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_score_screen);
		
		font=Typeface.createFromAsset(getAssets(), "Epifania.ttf");
		
		nameView=(TextView)findViewById(R.id.nameView);
		scoreView=(TextView)findViewById(R.id.scoreView);
		selectionView=(TextView)findViewById(R.id.selectionView);
		
		nameView.setTypeface(font);
		scoreView.setTypeface(font);
		selectionView.setTypeface(font);
		
		listView = (ListView) findViewById(R.id.listView);
		easyButton = (RadioButton) findViewById(R.id.easyButton);
		hardButton = (RadioButton) findViewById(R.id.hardButton);
		
		easyButton.setTypeface(font);
		hardButton.setTypeface(font);

		GAME_LEVEL = SettingPrefs.getGameLevel(ScoreActivity.this);
		isEasyGame = GAME_LEVEL.equalsIgnoreCase(Constants.EASY_GAME);
		if (isEasyGame) {
			easyButton.setChecked(true);
		} else {
			hardButton.setChecked(true);
		}

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		numOfQuestion = Integer.parseInt(prefs.getString(
				SettingPrefs.QUIZ_SIZE, "20"));

		sizeSpinnerOperation();

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);

		adapter = new EfficientAdapter(ScoreActivity.this);
		items = new ArrayList<String[]>();
		listView.setAdapter(adapter);

		setListItem(GAME_LEVEL, numOfQuestion);
		
		try {
			final AdView adView = (AdView) findViewById(R.id.adView);
			adView.loadAd(new AdRequest.Builder().build());
			adView.setAdListener(new AdListener() {
				public void onAdLoaded() {
					adView.setVisibility(View.VISIBLE);
		        }
				public void onAdFailedToLoad(int errorCode) {
					if (!Utility.Utils.isNetworkAvailable(ScoreActivity.this)) {
						adView.setVisibility(View.GONE);
					}
		        }
		    });
		} catch (Exception e) {}
		
	}

	private void sizeSpinnerOperation() {
		sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
		ArrayAdapter<CharSequence> adap = ArrayAdapter
				.createFromResource(this, R.array.pref_entries_size,
						android.R.layout.simple_spinner_item);
		adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sizeSpinner.setAdapter(adap);
		if (numOfQuestion == 10) {
			selectionIndex = 0;
		} else if (numOfQuestion == 20) {
			selectionIndex = 1;
		} else if (numOfQuestion == 35) {
			selectionIndex = 2;
		} else if (numOfQuestion == 50) {
			selectionIndex = 3;
		}
		sizeSpinner.setSelection(selectionIndex);
		sizeSpinner.setOnItemSelectedListener(this);
	}

	private void setListItem(String GAME_LEVEL, int numOfQuestion) {
		items = DatabaseAccessor.getScoreList(GAME_LEVEL, numOfQuestion);
		adapter.notifyDataSetChanged();
	}

	public class EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.layout_score_list_content, null);
				holder = new ViewHolder();
				holder.scoreView = (TextView) convertView
						.findViewById(R.id.scoreView);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.nameView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.nameView.setText(items.get(position)[0]);
			holder.scoreView.setText(items.get(position)[1]);
			holder.nameView.setTypeface(font);
			holder.scoreView.setTypeface(font);

			return convertView;
		}

		class ViewHolder {
			TextView nameView;
			TextView scoreView;

		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.easyButton:
			GAME_LEVEL = Constants.EASY_GAME;
			setListItem(GAME_LEVEL, numOfQuestion);
			break;

		case R.id.hardButton:
			GAME_LEVEL = Constants.HARD_GAME;
			setListItem(GAME_LEVEL, numOfQuestion);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		switch (parent.getId()) {
		case R.id.sizeSpinner:
			numOfQuestion = Integer.parseInt(sizeSpinner.getSelectedItem()
					.toString());
			setListItem(GAME_LEVEL, numOfQuestion);
			break;

		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
