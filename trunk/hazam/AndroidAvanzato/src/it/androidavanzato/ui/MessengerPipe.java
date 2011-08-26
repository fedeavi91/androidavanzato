package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MessengerPipe extends Activity {
	private TextView argument = null;
	private TextView result = null;
	private Handler handler;
	private Messenger messenger;
	private Messenger service = null;
	
	private static final String TAG = "MessengerPipe";

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = new Messenger(binder);
			Log.v(TAG, "Service Connected!");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.factorial_service);
		argument = (TextView) findViewById(R.id.argument);
		result = (TextView) findViewById(R.id.result);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				result.setText(msg.obj.toString());
			};
		};
		messenger = new Messenger(handler);
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent(this, FactorialService.class), conn, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		unbindService(conn);
		super.onStop();
	}

	private void sendMessage(int msgid) {
		if (service != null) {
			result.setText("?");
			Message message = Message.obtain();
			message.what = msgid;
			message.obj = argument.getText().toString();
			message.replyTo = messenger;
			try {
				service.send(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void calculate(View v) {
		sendMessage(FactorialService.MSG_FACTORIAL);
	}

	public void calculateWrong(View v) {
		sendMessage(FactorialService.MSG_FACTORIAL_WRONG);
	}
}
