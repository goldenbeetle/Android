package com.goldenbeetle.learnenglishspellingsadv;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HelpActivity extends Activity  {

	private WebView webView; 
	private Typeface font;
	private TextView titleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_help_screen);
		webView = (WebView)findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/help.html");
		webView.getSettings().setBuiltInZoomControls(true);
		
		titleView=(TextView)findViewById(R.id.titleView);
		
		font=Typeface.createFromAsset(getAssets(), "Comfortaa_Bold.ttf");
		titleView.setTypeface(font);
		
		webView.setBackgroundColor(0);
		webView.setBackgroundResource(R.drawable.background);
		try {
			final AdView adView = (AdView) findViewById(R.id.adView);
			adView.loadAd(new AdRequest.Builder().build());
			adView.setAdListener(new AdListener() {
				public void onAdLoaded() {
					adView.setVisibility(View.VISIBLE);
		        }
				public void onAdFailedToLoad(int errorCode) {
					if (!Utility.Utils.isNetworkAvailable(HelpActivity.this)) {
						adView.setVisibility(View.GONE);
					}
		        }
		    });
		} catch (Exception e) {}		
	}

}
