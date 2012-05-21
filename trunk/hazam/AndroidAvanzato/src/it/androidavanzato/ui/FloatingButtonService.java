package it.androidavanzato.ui;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class FloatingButtonService extends Service {
	private Button mButton;
	private WindowManager mWm;

	@Override
	public void onCreate() {
		super.onCreate();
		mButton = new Button(this);
		mButton.setText("Forever Here");
		mButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Toast.makeText(FloatingButtonService.this,
						"Overlay button event", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				LayoutParams.FLAG_NOT_FOCUSABLE
						| LayoutParams.FLAG_NOT_TOUCH_MODAL,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.CENTER;
		mWm = (WindowManager) getSystemService(WINDOW_SERVICE);
		mWm.addView(mButton, params);
	}

	@Override
	public void onDestroy() {
		mWm.removeView(mButton);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
