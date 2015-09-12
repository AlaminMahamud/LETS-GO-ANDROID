package com.example.letsgogooglemap.Maps.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.Map;
import com.example.letsgogooglemap.Maps.newMap;
import com.example.letsgogooglemap.Maps.loginRegister.Login;
import com.example.letsgogooglemap.Maps.main.Main;
import com.example.letsgogooglemap.Maps.main.Search;

public class SplashScreen extends Activity {
	
	// 1. Splash -> Login
	
	
	TextView t;
	ImageView i;
	ProgressBar bar;
	
	private static final long SPLASH_TIME_OUT = 3000;
	public static final String ROBOTO_BOLD_CONDENSED_PATH = "fonts/RobotoCondensed-Bold.ttf";

	
	//************************** ON CREATE **************************//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		t = (TextView) findViewById(R.id.textView);
		i = (ImageView) findViewById(R.id.imageView);
		bar = (ProgressBar) findViewById(R.id.progressBar1);

		// Custom TypeFace
		Typeface tf = Typeface.createFromAsset(getAssets(),
				ROBOTO_BOLD_CONDENSED_PATH);

		// Applying TypeFace
		t.setTypeface(tf);

		// Splash Screen Code
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				bar.setIndeterminate(true);
//				for (int i = 0; i <= 100; i = i + 5) {
//					final int value = i;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					bar.setProgress(value);
//				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Intent i = new Intent(SplashScreen.this, Login.class);
				startActivity(i);
				finish();
			}

		}, SPLASH_TIME_OUT);		
	}
}
