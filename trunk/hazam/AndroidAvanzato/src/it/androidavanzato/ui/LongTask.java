package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;


public class LongTask extends Activity implements OnCancelListener {
	private static String TAG = "LongTaskActivity";

	static final int MSG_TASK_DONE = 0x73770;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_TASK_DONE) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				Log.i(TAG, "TASK IS DONE, insert proper handling here");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.longrunning);
		Object last = getLastNonConfigurationInstance();
		if (last != null) {
			fakeThread = (CountToTenThread) last;
			fakeThread.setHandler(handler);
			if (fakeThread.isAlive()) {
				dialog = new ProgressDialog(this);
				dialog.show();
			} else {
				fakeThread = null;
				handler.sendEmptyMessage(MSG_TASK_DONE);
			}
		}
	}

	public void startLongRunningTask(View v) {
		dialog = new ProgressDialog(this);
		dialog.show();
		fakeThread = new CountToTenThread(handler);
		fakeThread.start();
	}

	private ProgressDialog dialog;

	public Object onRetainNonConfigurationInstance() {
		if (fakeThread != null && fakeThread.isAlive()) {
			Thread cpy = fakeThread;
			fakeThread.setHandler(null);
			fakeThread = null;
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			dialog = null;
			return cpy;
		}
		return null;
	}

	private CountToTenThread fakeThread;

	@Override
	public void onCancel(DialogInterface dialog) {
		fakeThread.interrupt();
		handler.removeMessages(MSG_TASK_DONE);
	}
}

class CountToTenThread extends Thread {
	private static final String TAG = CountToTenThread.class.getSimpleName();

	private Handler handler;
	private long delay = 10000;

	CountToTenThread(Handler h) {
		handler = h;
	}

	void setHandler(Handler h) {
		handler = h;
	}

	@Override
	public void run() {
		try {
			long timer = 0;
			while (timer < delay) {
				Thread.sleep(200);
				timer += 200;
				Log.i(TAG, "Tick " + timer);
			}
			if (handler != null) {
				handler.sendEmptyMessage(LongTask.MSG_TASK_DONE);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
