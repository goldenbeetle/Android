package Utility;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class TranslateAnimation extends Animation
{

    private float mDx;
    private float mDy;
    private float mFromXDelta;
    private int mFromXType;
    private float mFromXValue;
    private float mFromYDelta;
    private int mFromYType;
    private float mFromYValue;
    private float mToXDelta;
    private int mToXType;
    private float mToXValue;
    private float mToYDelta;
    private int mToYType;
    private float mToYValue;

    public TranslateAnimation(int i, float f, int j, float f1, int k, float f2, int l, 
            float f3)
    {
        mFromXType = 0;
        mToXType = 0;
        mFromYType = 0;
        mToYType = 0;
        mFromXValue = 0.0F;
        mToXValue = 0.0F;
        mFromYValue = 0.0F;
        mToYValue = 0.0F;
        mFromXValue = f;
        mToXValue = f1;
        mFromYValue = f2;
        mToYValue = f3;
        mFromXType = i;
        mToXType = j;
        mFromYType = k;
        mToYType = l;
    }

    protected void applyTransformation(float f, Transformation transformation)
    {
        mDx = mFromXDelta;
        mDy = mFromYDelta;
        if (mFromXDelta != mToXDelta)
        {
            mDx = mFromXDelta + f * (mToXDelta - mFromXDelta);
        }
        if (mFromYDelta != mToYDelta)
        {
            mDy = mFromYDelta + f * (mToYDelta - mFromYDelta);
        }
        transformation.getMatrix().setTranslate(mDx, mDy);
    }

    public float getDx()
    {
        return mDx;
    }

    public float getDy()
    {
        return mDy;
    }

    public float getToXValue()
    {
        return mToXValue;
    }

    public float getToYValue()
    {
        return mToYValue;
    }

    public void initialize(int i, int j, int k, int l)
    {
        super.initialize(i, j, k, l);
        mFromXDelta = resolveSize(mFromXType, mFromXValue, i, k);
        mToXDelta = resolveSize(mToXType, mToXValue, i, k);
        mFromYDelta = resolveSize(mFromYType, mFromYValue, j, l);
        mToYDelta = resolveSize(mToYType, mToYValue, j, l);
    }
}
