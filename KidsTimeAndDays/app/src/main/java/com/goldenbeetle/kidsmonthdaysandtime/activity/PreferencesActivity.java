package com.goldenbeetle.kidsmonthdaysandtime.activity;

import com.goldenbeetle.kidsmonthdaysandtime.*;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity
{

    public PreferencesActivity()
    {
    }

    @SuppressWarnings("deprecation")
	protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
    }

}
