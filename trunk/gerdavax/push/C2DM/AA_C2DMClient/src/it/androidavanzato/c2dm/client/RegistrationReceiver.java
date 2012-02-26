package it.androidavanzato.c2dm.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RegistrationReceiver extends BroadcastReceiver {
	
	public void onReceive(final Context context, final Intent intent) {
	    if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
	        handleRegistration(context, intent);
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	       
	    	Intent startActivityIntent = new Intent(context, NotificationClient.class);
	    	startActivityIntent.putExtra("C2DM", true);
	    	startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(startActivityIntent);
	    	
	     }
	 }

	private void handleRegistration(Context context, Intent intent) {
	    if (intent.getStringExtra("registration_id") != null) {
	    	String registration_id = intent.getStringExtra("registration_id");
	    	sendRegistrationToServer(registration_id);
	    } else if (intent.getStringExtra("error") != null) {
	        // si e' verificato un errore...
	    } else if (intent.getStringExtra("unregistered") != null) {
	        // non saranno ricevute più notifiche.
	    } 
	}
	
	private void sendRegistrationToServer(final String reg) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost("http://INDIRIZZO_SERVER:8099/register");
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("registration_id", reg));
					params.add(new BasicNameValuePair("device", Build.MODEL));
					
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
					request.setEntity(entity);
					
					HttpResponse response = client.execute(request);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
