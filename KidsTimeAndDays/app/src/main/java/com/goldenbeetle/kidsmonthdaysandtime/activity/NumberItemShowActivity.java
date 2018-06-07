package com.goldenbeetle.kidsmonthdaysandtime.activity;

public class NumberItemShowActivity extends ItemShowActivity
{
	public static final String itemArray[] = {
        "0  -  Zero", "1  -  One", "2  -  Two", "3  -  Three", "4  -  Four", "5  -  Five", "6  -  Six", "7  -  Seven", "8  -  Eight", "9  -  Nine", "10  -  Ten", 
        "11 - Eleven", "12 - Twelve", "13 - Thirteen", "14 - Fourteen", "15 - Fifteen", "16 - Sixteen", "17 - Seventeen", "18 - Eighteen", "19 - Nineteen", "20 - Twenty"
    };
	
    public NumberItemShowActivity()
    {
    }

    protected String getAudioHintFileNameFromBase()
    {
        return "learnnumbers";
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
		return "num";
	}

	@Override
	protected void initialize() {
		mSoundManager.loadNumbersSounds(getLanguage());
	}

	@Override
	protected String getDefaultItemDisplay() {
		return "0";
	}
    
}
