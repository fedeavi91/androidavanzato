package it.androidavanzato.push.sms.sender;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

public class SMSSender extends Activity {
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
	
	public void sendMessage(View view) {
		String recipient = getValue(R.id.recipient);
		int port = Integer.parseInt(getValue(R.id.port));
		String message = getValue(R.id.message);
		
		SmsManager.getDefault().sendDataMessage(recipient, null, (short) port, message.getBytes(), null, null);
	}
	
	private String getValue(int editText) {
		return ((EditText) findViewById(editText)).getText().toString();
	}
}