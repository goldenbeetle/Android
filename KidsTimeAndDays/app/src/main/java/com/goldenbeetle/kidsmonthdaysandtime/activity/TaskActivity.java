package com.goldenbeetle.kidsmonthdaysandtime.activity;

import Utility.AnimationUtils;
import android.content.Intent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import com.goldenbeetle.kidsmonthdaysandtime.*;

public abstract class TaskActivity extends BaseActivity
{
    private static final int CORRECT_ANSWER_THRESHOLD = 5;
    private int countCorrectAns;
    private boolean mAudioHintPlayed;

    public TaskActivity()
    {
    }

    protected void animateAnswer(View view)
    {
        TranslateAnimation translateanimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, -20);
        translateanimation.setDuration(50L);
        translateanimation.setInterpolator(new DecelerateInterpolator(1.0F));
        TranslateAnimation translateanimation1 = new TranslateAnimation(0.0F, 0.0F, -20, 0.0F);
        translateanimation1.setDuration(50L);
        translateanimation1.setInterpolator(new AccelerateInterpolator(1.0F));
        AnimationUtils.startAnimationChain(view, new Animation[] {
            translateanimation, translateanimation1
        });
    }
    
    protected void animateQuestion(View view)
    {
        TranslateAnimation translateanimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, -100);
        translateanimation.setDuration(300L);
        translateanimation.setInterpolator(new DecelerateInterpolator(1.0F));
        TranslateAnimation translateanimation1 = new TranslateAnimation(0.0F, 0.0F, -100, 0.0F);
        translateanimation1.setDuration(300L);
        translateanimation1.setInterpolator(new AccelerateInterpolator(1.0F));
        AnimationUtils.startAnimationChain(view, new Animation[] {
            translateanimation, translateanimation1
        });
    }

    protected abstract String getAudioHintFileName();

    protected int getIntValue(TextView textview)
    {
        return Integer.parseInt((new StringBuilder()).append(textview.getText()).toString());
    }
    
    protected String getStringValue(TextView textview)
    {
        return new StringBuilder().append(textview.getText()).toString();
    }

    protected void nextTask()
    {
        showNextTask();
    }

    protected void onCorrectAnswer(boolean flag)
    {
        countCorrectAns++;
        int mTrophyCount = countCorrectAns/CORRECT_ANSWER_THRESHOLD;
        if ((countCorrectAns % CORRECT_ANSWER_THRESHOLD)==0)
        {
            SuccessActivity.TrophyCount = mTrophyCount;
            startActivity(new Intent(this, com.goldenbeetle.kidsmonthdaysandtime.activity.SuccessActivity.class));
            mSoundManager.playSound("success");
            return;
        }
        playCorrectAnswer();
        if (flag)
        {
            mHandler.postDelayed(new Runnable() {
                public void run()
                {
                    nextTask();
                }
            }, 200L);
            return;
        } else
        {
            nextTask();
            return;
        }
    }

    protected void onIncorrectAnswer()
    {
        playIncorrectAnswer();
    }

    protected void onResume()
    {
        super.onResume();
    }

    public final void onWindowFocusChanged(boolean flag)
    {
        if (flag && !mAudioHintPlayed)
        {
            mAudioHintPlayed = true;
            mHandler.postDelayed(new Runnable() {
                public void run()
                {
                    mSoundManager.playSound(getAudioHintFileName());
                }
            }, 500L);
        }
    }

    protected void playCorrectAnswer()
    {
        mSoundManager.playCorrectAnswer();
    }

    protected void playIncorrectAnswer()
    {
        mSoundManager.playIncorrectAnswer();
    }

    protected void setupAnswersDimensions()
    {
        Integer ainteger[] = new Integer[4];
        ainteger[0] = Integer.valueOf(R.id.num_1);
        ainteger[1] = Integer.valueOf(R.id.num_2);
        ainteger[2] = Integer.valueOf(R.id.num_3);
        ainteger[3] = Integer.valueOf(R.id.num_4);
        List<Integer> list = Arrays.asList(ainteger);
        int i = getDisplayMetrics().heightPixels / 8;
        Iterator<Integer> iterator = list.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                return;
            }
            ((TextView)findViewById(((Integer)iterator.next()).intValue())).setTextSize(0, i);
        } while (true);
    }

    protected abstract void showNextTask();
}
