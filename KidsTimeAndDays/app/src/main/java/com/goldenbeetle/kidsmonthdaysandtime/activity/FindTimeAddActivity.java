package com.goldenbeetle.kidsmonthdaysandtime.activity;

import Utility.Utils;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.goldenbeetle.kidsmonthdaysandtime.*;
import com.ui.FancyClock;

public class FindTimeAddActivity extends TaskActivity
    implements android.view.View.OnClickListener
{
    private TextView mNum1;
    private TextView mNum2;
    private TextView mNum3;
    private TextView mNum4;
    private String mAnswer;
    private TextView mTaskView;
    private FancyClock mFancyClock;
    private TextView mTimediff;
    private SharedPreferences mPreferences;
    private int selecttimesetting;

    public FindTimeAddActivity()
    {
    }

    private void setupDimensions()
    {
        mTaskView.setTextSize(0, getDisplayMetrics().heightPixels / 12);
        mTimediff.setTextSize(0, getDisplayMetrics().heightPixels*2 / 12);
        int i = getDisplayMetrics().heightPixels*8 / 12;
        int j = (i * 600) / 400;
        findViewById(R.id.clock_container).setLayoutParams(new android.widget.LinearLayout.LayoutParams(j, (int)i - 50));
        setupAnswersDimensions();
    }

    private void showAnswers()
    {
    	int mHoursOne = mRandom.nextInt(11)+1;
        int mMinutesOne = mRandom.nextInt(59);
        if (selecttimesetting!=0) {
        	mMinutesOne = 0;
        } else {
        	int rem = mMinutesOne % selecttimesetting;
        	if (rem>0) {
        		mMinutesOne = mMinutesOne - rem;
        		if (mMinutesOne<0) {
        			mMinutesOne=0;
        		}
        	}
        }
    	mFancyClock.setTime(mHoursOne, mMinutesOne);

    	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.UK);
    	GregorianCalendar gc = new GregorianCalendar();

    	int mHoursDiff = mRandom.nextInt(5)+1;
        int mMinutesDiff = mRandom.nextInt(59);
        if (selecttimesetting==0) {
        	mMinutesDiff = 0;
        } else {
        	int rem = mMinutesDiff % selecttimesetting;
        	if (rem>0) {
        		mMinutesDiff = mMinutesDiff - rem;
        		if (mMinutesDiff<0) {
        			mMinutesDiff=0;
        		}
        	}
        }
        gc.set(Calendar.HOUR, mHoursDiff);
    	gc.set(Calendar.MINUTE, mMinutesDiff);
    	
    	mTimediff.setText(" + " + sdf.format(gc.getTime()));

        gc.set(Calendar.HOUR, mHoursOne);
    	gc.set(Calendar.MINUTE, mMinutesOne);
    	gc.add(Calendar.HOUR, mHoursDiff);
    	gc.add(Calendar.MINUTE, mMinutesDiff);

    	mAnswer = sdf.format(gc.getTime());
    	gc.add(Calendar.HOUR, 1);
    	gc.add(Calendar.MINUTE, 5);
        mNum1.setText(sdf.format(gc.getTime()));
    	gc.add(Calendar.HOUR, -2);
    	gc.add(Calendar.MINUTE, -15);
        mNum2.setText(sdf.format(gc.getTime()));
    	gc.add(Calendar.HOUR, -1);
    	gc.add(Calendar.MINUTE, -5);
        mNum3.setText(sdf.format(gc.getTime()));
    	gc.add(Calendar.HOUR, 5);
    	gc.add(Calendar.MINUTE, 25);
        mNum4.setText(sdf.format(gc.getTime()));
        switch(mRandom.nextInt(100)%4) {
        case 0:
        	mNum1.setText(mAnswer);
        	break;
        case 1:
        	mNum2.setText(mAnswer);
        	break;
        case 2:
        	mNum3.setText(mAnswer);
        	break;
        case 3:
        	mNum4.setText(mAnswer);
        	break;
        default:
        	mNum3.setText(mAnswer);
        	break;
        }
    }

    protected String getAudioHintFileName()
    {
        return "timeadd";
    }

    public void onClick(View view)
    {
        if (mSleeping)
        {
            return;
        }
        sleepForAWhile();
        if (getStringValue((TextView)view).equalsIgnoreCase(mAnswer))
        {
            onCorrectAnswer(true);
        } else
        {
            onIncorrectAnswer();
        }
        animateAnswer(view);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        setContentView(R.layout.findtimeadd);
        setBackgroundByResolution("bg_common");
        mTaskView = (TextView)findViewById(R.id.task);
        mFancyClock = (FancyClock) findViewById(R.id.fancyclock);
        int dialType = Integer.parseInt(mPreferences.getString("clk_clockdial", "0"));
        switch(dialType) {
        	case 1:
        		mFancyClock.setDial(R.drawable.clock_dialroman);
        		break;
        	case 2:
        		mFancyClock.setDial(R.drawable.clock_dialnonum);
        		break;
        }        
        mTimediff = (TextView) findViewById(R.id.timediff);
        mNum1 = (TextView)findViewById(R.id.num_1);
        mNum2 = (TextView)findViewById(R.id.num_2);
        mNum3 = (TextView)findViewById(R.id.num_3);
        mNum4 = (TextView)findViewById(R.id.num_4);
        mNum1.setOnClickListener(this);
        mNum2.setOnClickListener(this);
        mNum3.setOnClickListener(this);
        mNum4.setOnClickListener(this);
        setupDimensions();
		try {
			AdView adView = (AdView)this.findViewById(R.id.adView);
			if (Utils.ShowBannerAds) {
			    AdRequest adRequest = new AdRequest.Builder().build();
			    adView.loadAd(adRequest);	    
			} 
		} catch (Exception e) {}
	}

    protected void onResume()
    {
        super.onResume();
        selecttimesetting = Integer.parseInt(mPreferences.getString("clk_min_timeadd", "15"));
        nextTask();
    }

    public void onShake()
    {
    }

    protected void showNextTask()
    {
        showAnswers();
    }

	@Override
	void onLeftFlingCalled() {
		onShake();
	}

	@Override
	void onRightFlingCalled() {
		onShake();
	}
}
