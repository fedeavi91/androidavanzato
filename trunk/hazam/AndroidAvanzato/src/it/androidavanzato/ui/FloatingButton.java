package it.androidavanzato.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FloatingButton extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent serv = new Intent(this, FloatingButtonService.class);
		startService(serv);
	}
}
