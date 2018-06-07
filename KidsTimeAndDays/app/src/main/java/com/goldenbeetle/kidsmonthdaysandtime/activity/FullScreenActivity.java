package com.goldenbeetle.kidsmonthdaysandtime.activity;


import Utility.DrawableUtils;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import com.goldenbeetle.kidsmonthdaysandtime.*;

public class FullScreenActivity extends Activity
{

    private SharedPreferences mPreferences;

    public FullScreenActivity()
    {
    }

    protected DisplayMetrics getDisplayMetrics()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
    }

    protected SharedPreferences getPreferences()
    {
        return mPreferences;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyevent)
    {
        while (keyCode == KeyEvent.KEYCODE_BACK && mPreferences.getBoolean("back_button_disabled", false) || keyCode == KeyEvent.KEYCODE_SEARCH && mPreferences.getBoolean("search_button_disabled", false)) 
        {
            return true;
        }
        return super.onKeyDown(keyCode, keyevent);
    }

    protected void onStart()
    {
        super.onStart();
    }

    public void onStop()
    {
        super.onStop();
    }

    protected void setBackgroundByResolution(int i, String s)
    {
        findViewById(i).setBackgroundResource(DrawableUtils.getDrawableResourceByResolution(getResources(), s));
    }

    protected void setBackgroundByResolution(String s)
    {
        setBackgroundByResolution(R.id.root, s);
    }
}
