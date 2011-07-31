package it.androidavanzato.ui;

import it.androidavanzato.R;
import it.androidavanzato.view.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BlinkingText extends Activity {
	private TextView tv;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.blinking);
		tv = (TextView) findViewById(R.id.label);
	}
	
	public void blink(View v) {
		new BlinkAnimator(tv).start();
	}
	
	static class BlinkAnimator extends Animator {
		private TextView tv;
		
		public BlinkAnimator(TextView tv) {
			this.tv = tv;
		}
		
		@Override
		protected void doUpdate(float factor) {
			int alpha = (int) (factor * 0xFF);
			
			tv.setTextColor((alpha << 24) | 0xFFFFFF);
		}
		
	}
}
