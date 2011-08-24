package it.androidavanzato.romaski.widget;

import it.androidavanzato.romaski.gesture.ScaleGestureDetector;
import it.androidavanzato.romaski.gesture.ScaleGestureDetector.OnScaleGestureListener;
import it.androidavanzato.romaski.util.L;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class Pinch {
	private static L log = new L("Pinch", Log.DEBUG);

	public static void makePinchable(final ImageView iv) {
		iv.setClickable(true);
		iv.setScaleType(ScaleType.MATRIX);
		final PinchByGesture list = new PinchByGesture(iv);
		iv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			public void onGlobalLayout() {
				float imvH = iv.getMeasuredHeight();
				float imvW = iv.getMeasuredWidth();
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
				log.d("ImageView: " + iv.getMeasuredWidth() + ", " + iv.getMeasuredHeight());
				log.d("Bitmap: " + iv.getDrawable().getIntrinsicWidth() + ", " + iv.getDrawable().getIntrinsicHeight());				
			}
		});
		// list.matrix.setScale(2.0f, 2.0f);
		// list.matrix.preTranslate(90.0f, 0.0f);
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
			log.d("onTouch");
			boolean consumed = scrollgd.onTouchEvent(event);
			consumed |= scalegd.onTouchEvent(event);
			return consumed;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			System.out.println("onScale");
			matrix.postScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(),
					detector.getFocusY());
			target.setImageMatrix(matrix);
			target.invalidate();
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			System.out.println("onScaleBegin");
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			System.out.println("dis " + distanceX);
			matrix.postTranslate(-distanceX, -distanceY);
			target.setImageMatrix(matrix);
			target.invalidate();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
