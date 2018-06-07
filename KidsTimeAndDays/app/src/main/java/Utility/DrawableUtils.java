package Utility;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class DrawableUtils
{

    private static final int WVGA800_WIDTH = 800;
    private static final int WVGA854_WIDTH = 854;

    private DrawableUtils()
    {
    }

    private static String getDrawableResolutionSuffix(Resources resources)
    {
        DisplayMetrics displaymetrics = resources.getDisplayMetrics();
        int i = Math.max(displaymetrics.heightPixels, displaymetrics.widthPixels);
        if (i > WVGA854_WIDTH)
        {
            return "_1280x800";
        }
        if (i == WVGA854_WIDTH)
        {
            return "_854x480";
        }
        if (i >= WVGA800_WIDTH)
        {
            return "_800x480";
        } else
        {
            return "_480x320";
        }
    }

    public static int getDrawableResource(String s)
    {
        int i;
        try
        {
            i = com.goldenbeetle.kidsmonthdaysandtime.R.drawable.class.getField(s).getInt(null);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
        return i;
    }

    public static int getDrawableResourceByResolution(Resources resources, String s)
    {
        return getDrawableResource((new StringBuilder(String.valueOf(s))).append(getDrawableResolutionSuffix(resources)).toString());
    }
}
