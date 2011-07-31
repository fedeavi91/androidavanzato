package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.TextView;


public class ColorFilters extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.colorfilters);

		tintBackgroundWith(R.id.textview1, 0xFF660099);
		tintBackgroundWith(R.id.textview2, 0xFFFF0000);
		tintBackgroundWith(R.id.textview3, 0xFF00FF00);
		tintBackgroundWith(R.id.textview4, 0xFF0000FF);
		tintBackgroundWith(R.id.textview5, 0xFFCC9933);
		tintBackgroundWith(R.id.textview6, 0xFF99FFFF);

	}

	private void tintBackgroundWith(int id, int color) {
		TextView tv = (TextView) findViewById(id);
		LayerDrawable d1 = (LayerDrawable) tv.getBackground();
		d1.getDrawable(0).setColorFilter(color, PorterDuff.Mode.SRC_IN);
	}
}