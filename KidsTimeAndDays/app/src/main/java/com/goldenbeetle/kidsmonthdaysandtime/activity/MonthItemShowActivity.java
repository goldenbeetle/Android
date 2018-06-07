package com.goldenbeetle.kidsmonthdaysandtime.activity;

public class MonthItemShowActivity extends ItemShowActivity
{
	public static final String itemArray[] = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };
	
    public MonthItemShowActivity()
    {
    }

    protected String getAudioHintFileNameFromBase()
    {
        return "monthnames";
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
		return "mon";
	}

	@Override
	protected void initialize() {
        mSoundManager.loadMonthsSounds(getLanguage());
	}

	@Override
	protected String getDefaultItemDisplay() {
		return "1";
	}
    
}
