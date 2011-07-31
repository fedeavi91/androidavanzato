package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class Task4 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task4);
	}
	
	public void next(View v) {
		moveTaskToBack(true);
	}
}
