package com.goldenbeetle.kidsmonthdaysandtime.activity;

import Utility.AccelerometerListener;
import Utility.AppOnGestureListener;
import Utility.SoundManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import java.util.Random;
import com.goldenbeetle.kidsmonthdaysandtime.*;

public abstract class BaseActivity extends FullScreenActivity
    implements Utility.AccelerometerListener.ShakeListener
{

    private static final int EXIT_MENU = 2;
    private static final int PREFERENCES_MENU = 1;
    static final Random mRandom = new Random();
    private AccelerometerListener mAccelerometerListener;
    protected Handler mHandler;
    protected boolean mSleeping;
    protected SoundManager mSoundManager;
    private GestureDetector mGestureDetector;

    private boolean isShakeEnabled()
    {
        return getPreferences().getBoolean("shake_enabled", true);
    }

    public boolean dispatchTouchEvent(MotionEvent motionevent)
    {
    	mGestureDetector.onTouchEvent(motionevent);
        return super.dispatchTouchEvent(motionevent);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setVolumeControlStream(3);
        mHandler = new Handler();
        mSoundManager = SoundManager.getInstance(this);
        mAccelerometerListener = new AccelerometerListener(this, this);
        mGestureDetector = new GestureDetector(this, new AppOnGestureListener() {
            protected void onLeftFling()
            {
            	onLeftFlingCalled();
            }
            protected void onRightFling()
            {
            	onRightFlingCalled();
            }
        });     
       
    }
    
    abstract void onLeftFlingCalled();
    
    abstract void onRightFlingCalled();

    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, PREFERENCES_MENU, 3, getString(R.string.preferences));
        menu.add(0, EXIT_MENU, 4, getString(R.string.main_menu));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch (menuitem.getItemId())
        {
        default:
           return false;

        case PREFERENCES_MENU: // '\001'
            startActivity(new Intent(this, com.goldenbeetle.kidsmonthdaysandtime.activity.PreferencesActivity.class));
            return true;

        case EXIT_MENU: // '\002'
            finish();
            break;
        }
        return true;
    }

    protected void onPause()
    {
        if (isShakeEnabled())
        {
            mAccelerometerListener.stop();
        }
        super.onPause();
    }

    protected void onResume()
    {
        super.onResume();
        if (isShakeEnabled()) {
            mAccelerometerListener.start();
        }
    }

    protected void sleep(long l)
    {
        mSleeping = true;
        mHandler.postDelayed(new Runnable() {
            public void run()
            {
                mSleeping = false;
            }
        }, l);
    }

    protected void sleepForAWhile()
    {
        sleep(500L);
    }








}
