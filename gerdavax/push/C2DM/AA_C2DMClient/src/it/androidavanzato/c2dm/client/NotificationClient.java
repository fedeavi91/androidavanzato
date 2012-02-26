package it.androidavanzato.c2dm.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotificationClient extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {

		if (intent.getBooleanExtra("C2DM", false)) {
			showMessage();
		}
	}

	public void registerDevice(View view) {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", "androidavanzato@gmail.com");
		startService(registrationIntent);
		finish();
	}

	private void showMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("L'applicazione ha ricevuto una notifica via C2DM!").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}