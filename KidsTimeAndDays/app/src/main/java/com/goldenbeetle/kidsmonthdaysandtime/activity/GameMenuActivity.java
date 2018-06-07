package com.goldenbeetle.kidsmonthdaysandtime.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.HashMap;
import java.util.Iterator;
import com.goldenbeetle.kidsmonthdaysandtime.*;

public class GameMenuActivity extends FullScreenActivity
{

    public GameMenuActivity()
    {
    }

    @SuppressLint("UseSparseArrays")
	private void init()
    {
        final HashMap<Integer, Class<?>> activityMap = new HashMap<Integer, Class<?>>();
        activityMap.put(Integer.valueOf(R.id.btn_day), com.goldenbeetle.kidsmonthdaysandtime.activity.WeekdayItemShowActivity.class);
        activityMap.put(Integer.valueOf(R.id.btn_month), com.goldenbeetle.kidsmonthdaysandtime.activity.MonthItemShowActivity.class);
        activityMap.put(Integer.valueOf(R.id.btn_num), com.goldenbeetle.kidsmonthdaysandtime.activity.NumberItemShowActivity.class);
        activityMap.put(Integer.valueOf(R.id.btn_seltime), com.goldenbeetle.kidsmonthdaysandtime.activity.FindTheTimeActivity.class);
        activityMap.put(Integer.valueOf(R.id.btn_timediff), com.goldenbeetle.kidsmonthdaysandtime.activity.FindTimeDiffActivity.class);
        activityMap.put(Integer.valueOf(R.id.btn_timeadd), com.goldenbeetle.kidsmonthdaysandtime.activity.FindTimeAddActivity.class);
        Iterator<Integer> iterator = activityMap.keySet().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                return;
            }
            final Integer key = (Integer)iterator.next();
            findViewById(key.intValue()).setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view)
                {
                    start((Class<?>)activityMap.get(key));
                }
            });
        } while (true);
    }

    private void start(Class<?> class1)
    {
        startActivity(new Intent(this, class1));
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.game_menu);
        setBackgroundByResolution("bg_common");
        init();
    }

}
