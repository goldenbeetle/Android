package Utility;

import android.content.Context;
import android.media.SoundPool;
import java.util.HashMap;
import java.util.Random;

public class SoundManager
{
    private static final int NUMBER_OF_CORRECT_PHRASES = 5;
    private static final int NUMBER_OF_INCORRECT_PHRASES = 3;
    private static final Random RANDOM = new Random();
    private static SoundManager mInstance;
    private Context mContext;
    private String mCurrentLanguage;
    private SoundPool mSoundPool;
    private HashMap<String, Integer> mSoundPoolMap;

    private SoundManager(Context context)
    {
        super();
        this.mSoundPoolMap = new HashMap<String, Integer>();
        this.mCurrentLanguage = "";
        this.mContext = context;
        this.mSoundPool = new SoundPool(4, 3, 0);

        for(int var2 = 1; var2 <= NUMBER_OF_CORRECT_PHRASES; ++var2) {
           this.addSound("correct" + var2);
        }

        for(int var3 = 1; var3 <= NUMBER_OF_INCORRECT_PHRASES; ++var3) {
           this.addSound("incorrect" + var3);
        }

        this.addSound("falling");
        this.addSound("success");
        this.addSound("selecttime");
        this.addSound("timeadd");
        this.addSound("timediff");
    }

    private void addSound(String s)
    {
        try
        {
            addSound(s, com.goldenbeetle.kidsmonthdaysandtime.R.raw.class.getField(s).getInt(null));
            return;
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    private void addSound(String s, int i)
    {
        mSoundPoolMap.put(s, Integer.valueOf(mSoundPool.load(mContext, i, 1)));
    }

    public static SoundManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new SoundManager(context);
        }
        return mInstance;
    }

    public String getCurrentLanguage()
    {
        return mCurrentLanguage;
    }

    public void loadWeekdaysSounds(String s)
    {
        for(int i=0;i<7;i++) {
        	addSound(new StringBuilder(s).append("_day_").append(i).toString());
        }
        this.addSound("weekdaynames");
        this.addSound("weekdaynames_qz");
        mCurrentLanguage = s;
    }
    
    public void loadMonthsSounds(String s)
    {
        for(int i=0;i<12;i++) {
        	addSound(new StringBuilder(s).append("_mon_").append(i).toString());
        }
        this.addSound("monthnames");
        this.addSound("monthnames_qz");        
        mCurrentLanguage = s;
    }
    
    public void loadNumbersSounds(String s)
    {
        for(int i=0;i<=20;i++) {
        	addSound(new StringBuilder(s).append("_num_").append(i).toString());
        }
        this.addSound("learnnumbers");
        this.addSound("learnnumbers_qz");        
        mCurrentLanguage = s;
    }    

    public void playCorrectAnswer()
    {
        playSound((new StringBuilder("correct")).append(1 + RANDOM.nextInt(NUMBER_OF_CORRECT_PHRASES-1)).toString());
    }

    public void playIncorrectAnswer()
    {
        playSound((new StringBuilder("incorrect")).append(1 + RANDOM.nextInt(NUMBER_OF_INCORRECT_PHRASES-1)).toString());
    }

    public void playSound(String s)
    {
        try
        {
            mSoundPool.play(((Integer)mSoundPoolMap.get(s)).intValue(), 0.99F, 0.99F, 1, 0, 1.0F);
            return;
        }
        catch (NullPointerException nullpointerexception)
        {
            return;
        }
    }

}
