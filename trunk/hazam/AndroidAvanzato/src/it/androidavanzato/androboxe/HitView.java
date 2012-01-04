package it.androidavanzato.androboxe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;


public class HitView extends View implements FaceDetectionListener, OnClickListener {
	private Paint mPaint;
	private PunchListener mListener = null; 
	
	public static interface PunchListener {
		public void punchHitAt(int x, int y); 
	}
	
	public HitView(Context context) {
		super(context);
		init();
	}

	public HitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HitView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private final void init() {
		mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.STROKE);
		setOnClickListener(this);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		Log.d(Util.TAG, "measure "+width+":"+height);
		setMeasuredDimension(width, height);
	}
	
	private Rect mLastPosition;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mLastPosition != null) {
			canvas.drawRect(mLastPosition, mPaint);
		}
	}
	
	private int transformX(int x) {
		return getWidth() - (int) (((float)x + 1000) / 2000 * getWidth());
	}
	
	private int transformY(int y) {
		return (int) (((float)y + 1000) / 2000 * getHeight());
	}

	@Override
	public void onFaceDetection(Face[] faces, Camera camera) {
		if (faces.length > 0) {
			Rect absolute = faces[0].rect;
			if (mLastPosition == null) {
				mLastPosition = new Rect();
			}
			mLastPosition.top = transformY(absolute.left);
			mLastPosition.bottom = transformY(absolute.right);
			mLastPosition.left = transformX(absolute.bottom);
			mLastPosition.right = transformX(absolute.top);
			invalidate();
		} else {
			mLastPosition = null;
			invalidate();
		}
	}
	private final Point mLastTouch = new Point();
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mLastTouch.set((int)event.getX(), (int)event.getY());
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		if (mLastPosition != null && mLastPosition.contains(mLastTouch.x, mLastTouch.y)) {
			if (mListener != null) {
				mListener.punchHitAt(mLastTouch.x, mLastTouch.y);
			}
		}
	}
	
	public void setPunchListener(PunchListener mListener) {
		this.mListener = mListener;
	}
}
