package Utility;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerListener
    implements SensorEventListener
{
    public static interface ShakeListener
    {
        public abstract void onShake();
    }

    private static final int FORCE_THRESHOLD = 900;
    private float currenForce;
    private float current_x;
    private float current_y;
    private float current_z;
    private float last_x;
    private float last_y;
    private float last_z;
    private long mCurrentTime;
    private long mLastUpdate;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private ShakeListener mSubscriber;

    public AccelerometerListener(Context context, ShakeListener shakelistener)
    {
        mLastUpdate = -1L;
        mCurrentTime = -1L;
        mSensorManager = (SensorManager)context.getSystemService("sensor");
        mSensor = mSensorManager.getDefaultSensor(1);
        mSubscriber = shakelistener;
    }

    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }

    public void onSensorChanged(SensorEvent sensorevent)
    {
        if (sensorevent.sensor.getType() == 1 && sensorevent.values.length >= 3)
        {
            mCurrentTime = System.currentTimeMillis();
            if (mCurrentTime - mLastUpdate > 100L)
            {
                long l = mCurrentTime - mLastUpdate;
                mLastUpdate = mCurrentTime;
                current_x = sensorevent.values[0];
                current_y = sensorevent.values[1];
                current_z = sensorevent.values[2];
                currenForce = 10000F * (Math.abs((current_x + current_y + current_z) - last_x - last_y - last_z) / (float)l);
                if (currenForce > FORCE_THRESHOLD)
                {
                    mSubscriber.onShake();
                }
                last_x = current_x;
                last_y = current_y;
                last_z = current_z;
                return;
            }
        }
    }

    public void start()
    {
        if (mSensor != null)
        {
            mSensorManager.registerListener(this, mSensor, 1);
        }
    }

    public void stop()
    {
        mSensorManager.unregisterListener(this);
    }
}
