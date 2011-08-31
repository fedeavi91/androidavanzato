package it.androidavanzato.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwipeDetector implements View.OnTouchListener {

	static final String logTag = "SwipeDetector";
	private float downX, downY;

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

	public SwipeDetector(Listener listener) {
		this.mListener = listener;
	}

	public boolean onTouch(View v, MotionEvent event) {
		final float xThreshold = v.getWidth() * 0.4f;
		final float whoKnowsThreshold = Math.min(v.getWidth(), v.getHeight()) * 0.2f;
		final float yThreshold = v.getHeight() * 0.4f;
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
			if (Math.abs(deltaX) > xThreshold
					&& Math.abs(deltaY) < whoKnowsThreshold) {
				// left or right
				if (deltaX < 0) {
					mListener.onSwipeRight();
				} else {
					mListener.onSwipeLeft();
				}
				return true;
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaX)
						+ " long, need at least " + xThreshold);
			}

			// swipe vertical?
			if (Math.abs(deltaY) > yThreshold
					&& Math.abs(deltaX) < whoKnowsThreshold) {
				// top or down
				if (deltaY < 0) {
					mListener.onSwipeDown();
				} else {
					mListener.onSwipeUp();
				}
				return true;
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaY)
						+ " long, need at least " + yThreshold);
			}
			return true;
		}
		return false;
	}
}
