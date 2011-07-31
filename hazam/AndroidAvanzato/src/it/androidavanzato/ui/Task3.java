package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Task3 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task3);
	}
	
	public void next(View v) {
		Intent i = new Intent(this, Task4.class);
		startActivity(i);
	}
}
