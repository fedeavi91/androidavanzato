package it.androidavanzato.view;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CustomView extends View implements View.OnClickListener {

	private Paint mPaint;
	private RectF mRect;
	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mRect = new RectF();
		setOnClickListener(this);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
				MeasureSpec.getSize(widthMeasureSpec), 
				MeasureSpec.getSize(heightMeasureSpec));
	}

	private float delta = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final Paint paint = mPaint;
		paint.setStyle(Style.FILL); paint.setColor(Color.RED);
		final RectF oval = mRect;
		oval.left = 0; oval.right = getWidth();
		oval.top = 0; oval.bottom = getHeight();
		canvas.drawArc(mRect, 0 + delta, 90, true, paint);
	}

	@Override
	public void onClick(View v) {
		ValueAnimator va = ValueAnimator.ofFloat(0, 360).setDuration(2000);
		delta = 0;
		va.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				delta = (Float) animation.getAnimatedValue();
				invalidate();
			}
		});
		va.setInterpolator(new DecelerateInterpolator());
		va.start();
		invalidate();
	}
}
