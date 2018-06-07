package com.goldenbeetle.kidsmonthdaysandtime.activity;

public class WeekdayItemShowActivity extends ItemShowActivity
{  
	public static final String itemArray[] = {
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
	
    public WeekdayItemShowActivity()
    {
    }

    protected String getAudioHintFileNameFromBase()
    {
        return "weekdaynames";
    }

	@Override
	protected String[] getItemArray() {
		return itemArray;
	}

	@Override
	protected int getItemCount() {
		return itemArray.length;
	}

	@Override
	protected String getItemPrefix() {
		return "day";
	}

	@Override
	protected void initialize() {
        mSoundManager.loadWeekdaysSounds(getLanguage());
	}

	@Override
	protected String getDefaultItemDisplay() {
		return "1";
	}
    
}
