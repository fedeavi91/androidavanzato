package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Task1 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task1);
	}
	
	public void next(View v) {
		Intent i = new Intent(this, Task2.class);
		startActivity(i);
	}
}
