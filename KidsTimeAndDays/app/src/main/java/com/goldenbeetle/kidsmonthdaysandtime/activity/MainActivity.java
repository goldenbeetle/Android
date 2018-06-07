package com.goldenbeetle.kidsmonthdaysandtime.activity;

import java.util.Random;

import Utility.Utils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.goldenbeetle.kidsmonthdaysandtime.*;

public class MainActivity extends FullScreenActivity
{
    private void init()
    {
        findViewById(R.id.play_button).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                start(com.goldenbeetle.kidsmonthdaysandtime.activity.GameMenuActivity.class);
            }
        });
        findViewById(R.id.preferences_button).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                start(com.goldenbeetle.kidsmonthdaysandtime.activity.PreferencesActivity.class);
            }
        });       
        
        findViewById(R.id.more_games_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		try {
        			Random rand = new Random() ; 
        			switch(rand.nextInt(100)%5) {
	        			case 0:
	        			case 2:
	        			case 4:
	                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:Golden+Beetle")));
	                        break;
	        			case 1:
	        			case 3:
	                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:Golden+Beetle")));
	                        break;
	                    default:
	                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:Golden+Beetle")));
        			}
        		} catch (android.content.ActivityNotFoundException anfe) {
        		} 
			}
		});
    }

    private void start(Class<?> class1)
    {
        startActivity(new Intent(this, class1));
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        if (i == 0)
        {
            finish();
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        setBackgroundByResolution("bg_common");
        init();
	    Bundle extras = getIntent().getExtras();
		if (extras!=null && !extras.isEmpty())
		{
			Utils.processBundleExtras(getApplicationContext(),extras);
		}	        
    }
   
	@Override
	public void onBackPressed() {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	        alertDialog.setTitle(R.string.msg_exit_title);
	        alertDialog.setMessage(R.string.msg_exit_content);
	        alertDialog.setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	try {finish();System.exit(0);}catch(Exception e) {}
	            }
	        });
	        alertDialog.setPositiveButton(R.string.btn_no, null);
	        alertDialog.show();
	}

    protected void onResume()
    {
        super.onResume();
    }

}
