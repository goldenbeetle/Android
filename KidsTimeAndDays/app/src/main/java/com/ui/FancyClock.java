package com.ui;

import com.goldenbeetle.kidsmonthdaysandtime.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class FancyClock extends View {
	
    public FancyClock(Context context) {
		super(context);
	}

    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;

    private int mDialWidth;
    private int mDialHeight;
    private float mHour=1;
    private float mMinutes=0;
    private float mTargetHour=3;
    private float mTargetMinutes=11;
    private boolean mChanged;


Context mContext;
    public FancyClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FancyClock(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        mContext=context;
        mDial = r.getDrawable(R.drawable.clock_dial);
        mHourHand = r.getDrawable(R.drawable.clock_hour);
        mMinuteHand = r.getDrawable(R.drawable.clock_minute);
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        hScale = (float) widthSize / (float) mDialWidth;
        vScale = (float )heightSize / (float) mDialHeight;

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        float scale = Math.min((float) availableWidth / (float) w,
                               (float) availableHeight / (float) h);
        canvas.save();
        canvas.scale(scale, scale, x, y);

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        canvas.save();
        
		if (handNeedsToMove()) {
			moveHand();
		}
    }
    
	private void moveHand() {
		if (!handNeedsToMove()) {
			return;
		}
		
		if(mHour!=mTargetHour) {
			mHour = (mHour % 12) + 1;
			mChanged = true;
		}
		
		if(mMinutes!=mTargetMinutes) {
			mMinutes = (mMinutes+1) % 60;
			mChanged = true;
		}		
		
		if (mChanged) {
			invalidate();
		}
	}
    
    private boolean handNeedsToMove() {
		return Math.abs(mHour - mTargetHour) > 0.01f | Math.abs(mMinutes - mTargetMinutes) > 0.01f;
	}
    
	public void setTime(int Hours, int Minutes) {
		if (Hours==0) {Hours=12;}
		mTargetHour = Hours;
		mTargetMinutes = Minutes;
		mChanged = true;
		invalidate();
	}
	
	public void setDial(int dial) {
		Resources r = this.getResources();
		mDial = r.getDrawable(dial);
		mChanged = true;
		invalidate();
	}


}