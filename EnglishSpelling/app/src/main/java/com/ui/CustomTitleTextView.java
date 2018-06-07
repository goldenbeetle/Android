package com.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTitleTextView extends TextView{

	public CustomTitleTextView(Context context) {
        super(context);
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "Arista.ttf");
        setTypeface(tfs);

    }
    public CustomTitleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "Arista.ttf");
        setTypeface(tfs);
       
    }

    public CustomTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tfs = Typeface.createFromAsset(context.getAssets(), "Arista.ttf");
        setTypeface(tfs);

    }

}