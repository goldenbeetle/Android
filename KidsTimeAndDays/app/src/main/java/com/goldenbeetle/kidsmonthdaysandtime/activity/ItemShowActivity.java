package com.goldenbeetle.kidsmonthdaysandtime.activity;

import Utility.AnimationListenerAdapter;
import Utility.TranslateAnimation;
import Utility.Utils;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.goldenbeetle.kidsmonthdaysandtime.*;

public abstract class ItemShowActivity extends TaskActivity
{
    private SharedPreferences mPreferences;
    private View mClouds;
    private int mCurrentItem;
    protected List<TextView> mNumViews;
    private View vAnimals;
    private boolean isAnimalsShown;
    private LinearLayout mItemCollection;
    private ImageView mNext;
    private ImageView mPrevious;
    private String mItemPrefix = getItemPrefix();
	private int itemdisplaysetting;
	private boolean quizmode=false;
	private boolean randomquizmode=false;
	private boolean itemtextcaps=false;
	
    protected abstract int getItemCount();
    protected abstract String[] getItemArray();
    protected abstract String getItemPrefix();
    protected abstract void initialize();
    protected abstract String getDefaultItemDisplay();
    protected abstract String getAudioHintFileNameFromBase();
    
    public ItemShowActivity()
    {
    }

    private void animateFlyup()
    {
        Iterator<TextView> iterator = mNumViews.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                mHandler.postDelayed(new Runnable() {
                    public void run()
                    {
                        reloadScreen();
                    }
                }, 1000L);
                return;
            }
            try {
                TextView textview = (TextView)iterator.next();
                TranslateAnimation translateanimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, ((TranslateAnimation)textview.getAnimation()).getDy() / (float)textview.getHeight(), 1, -2.5F);
                translateanimation.setInterpolator(new AccelerateInterpolator());
                translateanimation.setFillAfter(true);
                translateanimation.setDuration(500L);
                textview.startAnimation(translateanimation);            	
            }catch(Exception e) {}
        } while (true);
    }

    private void animateStaystill(TextView textview)
    {
    	try {
            TranslateAnimation translateanimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, ((TranslateAnimation)textview.getAnimation()).getDy() / (float)textview.getHeight(), 1, -0.3F);
            translateanimation.setFillAfter(true);
            translateanimation.setDuration(500L);
            translateanimation.setInterpolator(new DecelerateInterpolator());
            textview.startAnimation(translateanimation);    		
    	}catch(Exception e) {}
    }
    
    
    private class AnimationListener extends AnimationListenerAdapter
    {
        public void onAnimationEnd(Animation animation)
        {
            mSleeping = false;
        }

        private AnimationListener()
        {
            super();
        }

        AnimationListener(AnimationListener animationlistener)
        {
            this();
        }
    }


    private TextView getTouchedView(MotionEvent motionevent)
    {
        Iterator<TextView> iterator = mNumViews.iterator();
        TextView textview;
        Rect rect;
        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }
            textview = (TextView)iterator.next();
            rect = new Rect();
            textview.getHitRect(rect);
            View view = (View)textview.getParent();
            rect.offset(view.getLeft(), view.getTop());
            TranslateAnimation translateanimation = (TranslateAnimation)textview.getAnimation();
            rect.offset((int)translateanimation.getDx(), (int)translateanimation.getDy());
        } while (!rect.contains((int)motionevent.getX(), (int)motionevent.getY()));
        return textview;
    }

    private void hideAnimals()
    {
        isAnimalsShown = false;
        AlphaAnimation alphaanimation = new AlphaAnimation(1.0F, 0.0F);
        alphaanimation.setAnimationListener(new AnimationListener(null));
        alphaanimation.setFillAfter(true);
        alphaanimation.setDuration(2000L);
        vAnimals.startAnimation(alphaanimation);
        showClouds();
    }

    private void initDisplayText()
    {
    	mItemCollection.removeAllViews();
    	String itemArray[] =  getItemArray();
    	String itemText = itemArray[mCurrentItem]; 
    	if (itemtextcaps) {
    		itemText = itemText.toUpperCase(Locale.ENGLISH);
    	}
        int lentext = itemText.length();
        mNumViews = new ArrayList<TextView>();
        
        boolean displaysplit=true;
        if(itemdisplaysetting==0) {
        	displaysplit = false;
        } else if((itemdisplaysetting==2) && (mCurrentItem%2)==0){
        	displaysplit = false;
        }
        if (displaysplit) {
	        for(int i=0;i<lentext;i++ ) {
		        LinearLayout nlLayout = new LinearLayout(this);
		        nlLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
		        nlLayout.setGravity(Gravity.CENTER);
		        TextView ntView = new TextView(this);
		        switch(i%4) {
			        case 0:
			        	ntView.setBackgroundResource(R.drawable.balloon_1);
			        	ntView.setTextColor(getResources().getColor(R.color.blue));
			        	break;
			        case 1:
			        	ntView.setBackgroundResource(R.drawable.balloon_2);
			        	ntView.setTextColor(getResources().getColor(R.color.blue));
			        	break;
			        case 2:
			        	ntView.setBackgroundResource(R.drawable.balloon_3);
			        	ntView.setTextColor(getResources().getColor(R.color.blue));
			        	break;
			        case 3:
			        	ntView.setBackgroundResource(R.drawable.balloon_4);
			        	ntView.setTextColor(getResources().getColor(R.color.blue));
			        	break;
		        }
		        
		        ntView.setGravity(Gravity.CENTER_HORIZONTAL);
		        ntView.setText((new StringBuilder()).append(itemText.charAt(i)).toString());
		        ntView.setTypeface(null, Typeface.BOLD);
		        
		        ntView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		        nlLayout.addView(ntView);
		        mItemCollection.addView(nlLayout);
		        mNumViews.add(ntView);
	            startAnimation(ntView);
	        }
        } else {
	        LinearLayout nlLayout = new LinearLayout(this);
	        nlLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
	        nlLayout.setGravity(Gravity.CENTER);
	        TextView ntView = new TextView(this);
	        switch(mCurrentItem%4) {
		        case 0:
		        	ntView.setTextColor(getResources().getColor(R.color.blue));
		        	break;
		        case 1:
		        	ntView.setTextColor(getResources().getColor(R.color.red));
		        	break;
		        case 2:
		        	ntView.setTextColor(getResources().getColor(R.color.darkgreen));
		        	break;
		        case 3:
		        	ntView.setTextColor(getResources().getColor(R.color.drakbrown));
		        	break;
	        }
	        
	        ntView.setGravity(Gravity.CENTER_HORIZONTAL);
	        ntView.setText(itemText);
	        ntView.setTypeface(null, Typeface.BOLD);
	        
	        ntView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	        nlLayout.addView(ntView);
	        mItemCollection.addView(nlLayout);
	        mNumViews.add(ntView);
            startAnimation(ntView);       	
        }
        setupDimensions(displaysplit);		
    }
    
    protected String getAudioHintFileName() {
    	if(quizmode) {
    		return getAudioHintFileNameFromBase() + "_qz";
    	} else {
    		return getAudioHintFileNameFromBase();
    	}
    }
    
    private void setupButtonDimensions(View view)
    {
        android.widget.FrameLayout.LayoutParams layoutparams = (android.widget.FrameLayout.LayoutParams)view.getLayoutParams();
        int i = (2 * getDisplayMetrics().heightPixels) / 5;
        layoutparams.height = i;
        layoutparams.width = (i * 408) / 480;
    }

	private void removeClouds()
    {
        mSoundManager.playSound("wind");
        int i;
        TranslateAnimation translateanimation;
        if (mRandom.nextBoolean())
        {
            i = 1;
        } else
        {
            i = -1;
        }
        translateanimation = new TranslateAnimation(1, 0.0F, 1, i, 1, 0.0F, 1, 0.0F);
        translateanimation.setAnimationListener(new AnimationListener(null));
        translateanimation.setFillAfter(true);
        translateanimation.setDuration(3000L);
        mClouds.startAnimation(translateanimation);
    }

    private void setupDimensions(boolean displaysplit)  {
        int i = getDisplayMetrics().heightPixels / 3;
        int j = (i * 114) / 200;
        int k = (i * 3) / 8;
        Iterator<TextView> iterator = mNumViews.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                setupButtonDimensions(mNext);
            	if(!(quizmode && randomquizmode)) {
                	setupButtonDimensions(mPrevious);        
            	} else {
            		mPrevious.setVisibility(View.INVISIBLE);
            	}
                return;
            }
            TextView textview = (TextView)iterator.next();
            if(displaysplit) {
                textview.setLayoutParams(new android.widget.LinearLayout.LayoutParams(j, i));
                textview.setTextSize(0, k);
            } else {
            	textview.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, i));
                textview.setTextSize(0, k*1.5f);
            }
            textview.setPadding(0, i / 12, 0, 0);            	
        } while (true);
    }

	private void showClouds()
    {
        int i;
        TranslateAnimation translateanimation;
        if (mRandom.nextBoolean())
        {
            i = 1;
        } else
        {
            i = -1;
        }
        translateanimation = new TranslateAnimation(1, i, 1, 0.0F, 1, 0.0F, 1, 0.0F);
        translateanimation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation)
            {
                super.onAnimationEnd(animation);
                mClouds.clearAnimation();
            }
        });
        translateanimation.setFillAfter(true);
        translateanimation.setDuration(3000L);
        mClouds.startAnimation(translateanimation);
    }

    private void showAnimals()
    {
        isAnimalsShown = true;
        mSoundManager.playSound("success");
        AlphaAnimation alphaanimation = new AlphaAnimation(0.0F, 1.0F);
        alphaanimation.setAnimationListener(new AnimationListener(null));
        alphaanimation.setFillAfter(true);
        alphaanimation.setDuration(2000L);
        vAnimals.startAnimation(alphaanimation);
        removeClouds();
    }

    private void startAnimation(final TextView view)
    {
        final int duration = (int)(2500D + 2000D * mRandom.nextDouble());
        TranslateAnimation translateanimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, -2.5F, 1, 0.0F);
        translateanimation.setDuration(duration);
        translateanimation.setInterpolator(new DecelerateInterpolator());
        translateanimation.setAnimationListener(new AnimationListenerAdapter() {
            public void onAnimationEnd(Animation animation)
            {
                TranslateAnimation translateanimation1 = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, -1F);
                translateanimation1.setDuration(duration);
                translateanimation1.setRepeatCount(-1);
                translateanimation1.setRepeatMode(2);
                view.startAnimation(translateanimation1);
            }
        });
        view.startAnimation(translateanimation);
    }


    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        quizmode = mPreferences.getBoolean("quiz_mode_enabled", false);
        randomquizmode = mPreferences.getBoolean("random_quiz_mode_enabled", false);
        itemtextcaps = mPreferences.getBoolean("itemtext_caps", false);
        setContentView(R.layout.itemshow);
        setBackgroundByResolution("bg_common");
        setBackgroundByResolution(R.id.grass, "grass");
        mClouds = findViewById(R.id.clouds);
        vAnimals = findViewById(R.id.animals);
        mItemCollection = (LinearLayout)findViewById(R.id.itemcollection);
        mPrevious = (ImageView)findViewById(R.id.prev);
        mNext = (ImageView)findViewById(R.id.next);
        mPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPrevious();
			}
        });
        mNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showNext();
			}
        });        
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
        initialize();
        itemdisplaysetting = Integer.parseInt(mPreferences.getString("itemdisplaymode_"+getItemPrefix(), getDefaultItemDisplay()));
        hideAnimals();
        reloadScreen();
    }

    public void onShake()
    {
        if (mSleeping)
        {
            return;
        }
        mSleeping = true;
        if (!isAnimalsShown) {
            showAnimals();
        } else {
            hideAnimals();
        }
    }

    public final boolean onTouchEvent(MotionEvent motionevent)
    {
        if (motionevent.getAction() != 0 || mSleeping)
        {
            return false;
        }
        TextView textview = getTouchedView(motionevent);
        if (textview != null)
        {
            sleepForAWhile();
            animateStaystill(textview);
        }
        return super.onTouchEvent(motionevent);
    }

    protected void showNextTask() {
    	showNext();
    }
    
	private void showNext() {
		if(randomquizmode && quizmode) {
			mCurrentItem = mRandom.nextInt(getItemCount());
		} else {
			mCurrentItem = (mCurrentItem + 1) % getItemCount();
		}
        animateFlyup();
	}
	
	private void showPrevious() {
		if (mCurrentItem<=0) {
			mCurrentItem = getItemCount() - 1;
		} else {
			mCurrentItem = (mCurrentItem - 1) % getItemCount();
		}
		animateFlyup();
	}
    
	private void reloadScreen() {
        initDisplayText();
        mHandler.postDelayed(new Runnable() {
            public void run()
            {
                Iterator<TextView> iterator = mNumViews.iterator();
                while(iterator.hasNext()) {
                	TextView textview = (TextView)iterator.next();
                	animateStaystill(textview);
                }           	
                if (!quizmode) {
                	mSoundManager.playSound((new StringBuilder(String.valueOf(getLanguage()))).append("_").append(mItemPrefix).append("_").append(mCurrentItem).toString());
                }
            }
        }, 3000L);
	}
	
    public String getLanguage() {
        return "en";
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
