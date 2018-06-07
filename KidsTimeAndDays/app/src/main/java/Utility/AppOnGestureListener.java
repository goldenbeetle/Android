package Utility;

import android.view.MotionEvent;

public abstract class AppOnGestureListener extends android.view.GestureDetector.SimpleOnGestureListener
{

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public AppOnGestureListener()
    {
    }

    public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
        if (motionevent != null && motionevent1 != null && Math.abs(f) > SWIPE_THRESHOLD_VELOCITY)
        {
            if (motionevent.getX() - motionevent1.getX() > SWIPE_MIN_DISTANCE)
            {
                onLeftFling();
                return true;
            }
            if (motionevent1.getX() - motionevent.getX() > SWIPE_MIN_DISTANCE)
            {
                onRightFling();
                return true;
            }
        }
        return false;
    }

    protected abstract void onLeftFling();

    protected abstract void onRightFling();
}
