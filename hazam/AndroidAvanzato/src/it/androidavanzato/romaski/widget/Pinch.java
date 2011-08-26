package it.androidavanzato.romaski.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class Pinch {
	private static String TAG = "Pinch";

	public static void makePinchable(final ImageView iv) {
		iv.setClickable(true);
		iv.setScaleType(ScaleType.MATRIX);
		final PinchByGesture list = new PinchByGesture(iv);
		iv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			public void onGlobalLayout() {
				float imvH = iv.getMeasuredHeight() - iv.getPaddingTop() - iv.getPaddingBottom();
				float imvW = iv.getMeasuredWidth() - iv.getPaddingLeft() - iv.getPaddingRight();
				Drawable bitmap = iv.getDrawable();
				float bmpH = bitmap.getIntrinsicHeight();
				float bmpW = bitmap.getIntrinsicWidth();

				float heightRatio = imvH / bmpH;
				float widthRatio = imvW / bmpW;
				float ratioClosestToOne;
				if (heightRatio < widthRatio) {
					ratioClosestToOne = heightRatio;
				} else {
					ratioClosestToOne = widthRatio;
				}
				list.matrix.setScale(ratioClosestToOne, ratioClosestToOne);
				float newHeight = ratioClosestToOne * bmpH;
				float newWidth = ratioClosestToOne * bmpW;
				list.matrix.postTranslate((imvW - newWidth) / 2, (imvH - newHeight) / 2);
				iv.setImageMatrix(list.matrix);
				Log.d(TAG, "ImageView: " + iv.getMeasuredWidth() + ", " + iv.getMeasuredHeight());
				Log.d(TAG, "Bitmap: " + iv.getDrawable().getIntrinsicWidth() + ", " + iv.getDrawable().getIntrinsicHeight());				
			}
		});
		iv.setImageMatrix(list.matrix);

		iv.setOnTouchListener(list);
	}

	private static class PinchByGesture implements OnTouchListener, OnScaleGestureListener, OnGestureListener {

		private ImageView target;
		private Matrix matrix = new Matrix();
		{
			matrix.setTranslate(1f, 1f);
		}
		private GestureDetector scrollgd;
		private ScaleGestureDetector scalegd;

		private PinchByGesture(ImageView iv) {
			target = iv;
			final Context ctx = iv.getContext();
			scrollgd = new GestureDetector(ctx, this);
			scalegd = new ScaleGestureDetector(ctx, this);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			boolean consumed = scrollgd.onTouchEvent(event);
			consumed |= scalegd.onTouchEvent(event);
			return consumed;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			matrix.postScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(),
					detector.getFocusY());
			target.setImageMatrix(matrix);
			target.invalidate();
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			matrix.postTranslate(-distanceX, -distanceY);
			target.setImageMatrix(matrix);
			target.invalidate();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
	}
}
