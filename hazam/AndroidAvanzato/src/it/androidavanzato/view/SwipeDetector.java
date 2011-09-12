package it.androidavanzato.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwipeDetector implements View.OnTouchListener {

	static final String logTag = "SwipeDetector";
	private float downX, downY;
	private final float THRESHOLD_MDPI = 150;
	private float threshold = 0; 
	
	
	public static interface Listener {
		public void onSwipeLeft();

		public void onSwipeRight();

		public void onSwipeDown();

		public void onSwipeUp();
	}

	public static abstract class ListenerAdapter implements Listener {
		public void onSwipeLeft() {
		}

		public void onSwipeRight() {
		}

		public void onSwipeDown() {
		}

		public void onSwipeUp() {
		}
	}

	private Listener mListener = null;

	public SwipeDetector(Context ctx, Listener listener) {
		this.mListener = listener;
		this.threshold = THRESHOLD_MDPI * ctx.getResources().getDisplayMetrics().density;
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			return true;

		case MotionEvent.ACTION_UP:
			if (mListener == null)
				return false;

			final float deltaX = downX - event.getX();
			final float deltaY = downY - event.getY();

			// swipe horizontal?
			if (Math.abs(deltaX) > threshold) {
				// left or right
				if (deltaX < 0) {
					mListener.onSwipeRight();
				} else {
					mListener.onSwipeLeft();
				}
				return true;
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaX)
						+ " long, need at least " + threshold);
			}

			// swipe vertical?
			if (Math.abs(deltaY) > threshold) {
				// top or down
				if (deltaY < 0) {
					mListener.onSwipeDown();
				} else {
					mListener.onSwipeUp();
				}
				return true;
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaY)
						+ " long, need at least " + threshold);
			}
			return true;
		}
		return false;
	}
}
