package com.goldenbeetle.kidsmonthdaysandtime.activity;

import Utility.SoundManager;
import Utility.Utils;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import com.goldenbeetle.kidsmonthdaysandtime.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SuccessActivity extends FullScreenActivity
{
	private static final int MAX_ROW_ITEMS = 10;
	static int TrophyCount = 0;
    private LinearLayout mRow1;
    private LinearLayout mRow2;
    private TextView mTaskView;

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.success);
        mRow1 = (LinearLayout)findViewById(R.id.trophy_row1);
        mRow2 = (LinearLayout)findViewById(R.id.trophy_row2);
        mTaskView = (TextView)findViewById(R.id.winnermessage);
        setBackgroundByResolution("bg_common");
        setupDimensions();
        createTrophyViews();
		try {
			AdView adView = (AdView)this.findViewById(R.id.adView);
			if (Utils.ShowBannerAds) {
			    AdRequest adRequest = new AdRequest.Builder().build();
			    adView.loadAd(adRequest);
			} 
		} catch (Exception e) {}	
    }
    
    private void createTrophyViews()
    {
    	if (TrophyCount==1) {
            mTaskView.setText("You are the winner, Get more trophies.");
    	}else{
            mTaskView.setText((new StringBuilder()).append("You have won ").append(TrophyCount).append(" trophies.").toString());    		
    	}
    	if(TrophyCount<=MAX_ROW_ITEMS) {
            fillRow(mRow1, TrophyCount);    		
    	} else if(TrophyCount<=MAX_ROW_ITEMS*2) {
    		fillRow(mRow1, MAX_ROW_ITEMS); 
    		fillRow(mRow2, TrophyCount-MAX_ROW_ITEMS);
    	} else {
    		fillRow(mRow1, MAX_ROW_ITEMS); 
    		fillRow(mRow2, MAX_ROW_ITEMS);
    	}
    }
    
    private void fillRow(ViewGroup viewgroup, int count)
    {
        int i = 0;
        do
        {
            if (i >= count)
            {
                return;
            }
            FrameLayout framelayout = new FrameLayout(this);
            LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F);    
            framelayout.setLayoutParams(lp);
            ImageView imageview = new ImageView(this);
            imageview.setImageResource(R.drawable.trophy);
            framelayout.addView(imageview, new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewgroup.addView(framelayout);
            i++;
        } while (true);
    }
    
    private void setupDimensions()
    {
        mTaskView.setTextSize(0, getDisplayMetrics().heightPixels / 10);
        int i = getDisplayMetrics().heightPixels / 40;
        mRow1.setPadding(i, i, i, i);
        LinearLayout.LayoutParams lp = (LayoutParams) mRow1.getLayoutParams();
        lp.setMargins(i, 0, i, 0);
        mRow1.setLayoutParams(lp);
        mRow2.setPadding(i, i, i, i);
        lp = (LayoutParams) mRow1.getLayoutParams();
        lp.setMargins(i, 0, i, 0);
        mRow2.setLayoutParams(lp);
    }

    protected void onResume()
    {
        super.onResume();
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if (motionevent.getAction() != 0)
        {
            return false;
        } else
        {
            SoundManager.getInstance(this).playSound("success");
            finish();
            return true;
        }
    }
}
