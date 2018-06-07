package com.goldenbeetle.learnenglishspellingsadv;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;

import com.utils.Constants;
import com.utils.Utils;
import android.content.Context;
import android.graphics.Color;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.os.Bundle;

public class SettingPrefs extends PreferenceActivity {

	public static final String SOUND_ON = "key_sound_setting";
	public static final String ITEM_SELECTED = "key_category_";
	public static final String QUIZ_SIZE = "key_quiz_size";
	private static final String OPTION_LEVEL = "game_level";
	//public static final String OPTION_LEVEL_DEFAULT = "hard_game";
	public static final String OPTION_LEVEL_DEFAULT = "easy_game";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.preferences);
		getListView().setBackgroundColor(Color.TRANSPARENT);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setBackgroundResource(R.drawable.background);
		initializeCheckBoxPreferences();
	}


	private void initializeCheckBoxPreferences() {
		CheckBoxPreference soundCheck = (CheckBoxPreference) findPreference(SOUND_ON);
		if (getSettingValue(getBaseContext(),SOUND_ON)) {
			soundCheck.setLayoutResource(R.layout.custom_preference_layout_checked);
		} else {
			soundCheck.setLayoutResource(R.layout.custom_preference_layout_unchecked);
		}
				
		try {
			JSONArray ja=new JSONArray(new String(Utils.getFileData(getBaseContext(), Constants.JSON_FILE)));
			PreferenceCategory targetCategory = (PreferenceCategory)findPreference("category_title");
			for (int i=0;i<ja.length();i++) {
				Iterator<?> keys = ja.getJSONObject(i).keys();
				while (keys.hasNext()) {
					String arrayName=String.valueOf(keys.next());
					CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);
					checkBoxPreference.setTitle(arrayName);
			        checkBoxPreference.setKey(ITEM_SELECTED + arrayName);
			        boolean categorySettingValue = getSettingValue(getBaseContext(),ITEM_SELECTED + arrayName);
			        checkBoxPreference.setChecked(categorySettingValue);
					if (categorySettingValue) {
						checkBoxPreference.setLayoutResource(R.layout.custom_preference_layout_checked);
					} else {
						checkBoxPreference.setLayoutResource(R.layout.custom_preference_layout_unchecked);
					}			        
			        targetCategory.addPreference(checkBoxPreference);
				}
			}			
		} catch (JSONException e) {

		}
	}

	public static boolean getSettingValue(Context context, String settingKey) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(settingKey, true);
	}

	public static String getGameLevel(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPTION_LEVEL, OPTION_LEVEL_DEFAULT);
	}
}
