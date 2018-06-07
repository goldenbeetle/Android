package com.goldenbeetle.kidsmonthdaysandtime.activity;


import com.goldenbeetle.kidsmonthdaysandtime.*;

import Utility.SoundManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashScreenActivity extends Activity
{

    private static final int TIMEOUT = 2000;

    public SplashScreenActivity()
    {
    }

    private void loadSounds()
    {
        (new Thread() {
            public void run()
            {
                SoundManager.getInstance(SplashScreenActivity.this);
            }
        }).start();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.splash_screen);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        loadSounds();
        (new Handler()).postDelayed(new Runnable() {
            public void run()
            {
                if (isFinishing())
                {
                    return;
                } else
                {
                    startActivity(new Intent(SplashScreenActivity.this, com.goldenbeetle.kidsmonthdaysandtime.activity.MainActivity.class));
                    finish();
                    return;
                }
            }
        }, TIMEOUT);
    }
}
