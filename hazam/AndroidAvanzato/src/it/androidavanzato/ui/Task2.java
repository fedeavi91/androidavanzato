package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Task2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task2);
	}
	
	public void next(View v) {
		Intent i = new Intent(this, Task3.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
	
	public void jump(View v) {
		Intent i = new Intent(this, Task4.class);
		startActivity(i);
	}
}
