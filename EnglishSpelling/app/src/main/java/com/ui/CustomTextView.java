package com.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView{
    public CustomTextView(Context context) {
        super(context);
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "Epifania.ttf");
        setTypeface(tfs);

    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "Epifania.ttf");
        setTypeface(tfs);
       
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "Epifania.ttf");
        setTypeface(tfs);

    }

}