package it.androidavanzato.romaski;

import it.androidavanzato.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

public class RomaSkiHoneycombActivity extends Activity {
	private final boolean isHoneycomb = android.os.Build.VERSION.SDK_INT > 10;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (!isHoneycomb) {
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		}
		int scrSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (scrSize) {
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		default:
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		}
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.home);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (!isHoneycomb) {
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		}
	}
}
