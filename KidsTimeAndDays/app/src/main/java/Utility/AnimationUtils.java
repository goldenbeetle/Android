package Utility;

import android.view.View;
import android.view.animation.Animation;

public final class AnimationUtils
{

    private AnimationUtils()
    {
    }

    public static void startAnimationChain(final View view, Animation aanimation[])
    {
        Animation animation = null;
        int i = aanimation.length;
        int j = 0;
        do
        {
            if (j >= i)
            {
                view.startAnimation(aanimation[0]);
                return;
            }
            final Animation a = aanimation[j];
            a.setFillAfter(true);
            if (animation != null)
            {
                animation.setAnimationListener(new AnimationListenerAdapter() {
                    public void onAnimationEnd(Animation animation1)
                    {
                        view.startAnimation(a);
                    }
                });
            }
            animation = a;
            j++;
        } while (true);
    }
}
