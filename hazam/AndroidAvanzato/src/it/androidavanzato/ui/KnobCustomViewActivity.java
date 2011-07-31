package it.androidavanzato.ui;

import it.androidavanzato.R;
import it.androidavanzato.ui.Knob.OnKnobPositionChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;


public class KnobCustomViewActivity extends Activity implements OnKnobPositionChangeListener {
	protected final String TAG = "Knob";
	private ProgressBar progressBar = null;
	private TextView text = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		text = (TextView) findViewById(R.id.progressText);
		
		Knob k = (Knob) findViewById(R.id.the_knob);
		k.setOnKnobPositionChangeListener(this);
	}

	@Override
	public void onKnobPositionChange(Knob target, float rot, boolean fromUser) {
		Log.v(TAG, "ACTIVITY - Rot changed " + rot);
		if (progressBar != null) {
			float max = target.getMaxRotation();
			float min = target.getMinRotation();
			float range = max - min;

			float percentage = (rot - min) / range;
			progressBar.setProgress((int) (percentage * ((float) progressBar.getMax())));
		}
		if (text != null) {
			text.setText(Float.toString(rot));
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}