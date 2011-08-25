package it.androidavanzato.ui;

import it.androidavanzato.R;
import it.androidavanzato.view.PolarCoordinates;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ComplexAnimatorHoneycombActivity extends Activity {

	private final float FIRST_RADIUS = 400;
	private final float SECOND_RADIUS = 200;

	private final static float FINAL_X = 900.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complexanimator);
	}

	public void animateComplex(final View v) {
		final float centerX = v.getLeft() + FIRST_RADIUS;
		final float centerY = v.getTop();

		//primo quarto di cerchio
		PolarCoordinates.Evaluator evaluator = new PolarCoordinates.Evaluator();
		PolarCoordinates from = new PolarCoordinates(centerX, centerY,
				FIRST_RADIUS, 270);
		PolarCoordinates to = new PolarCoordinates(centerX, centerY,
				FIRST_RADIUS, 360);
		ValueAnimator va = ValueAnimator.ofObject(evaluator, from, to);
		PolarCoordinates.PolarToCartesian actuator = new PolarCoordinates.PolarToCartesian(
				v);
		va.addUpdateListener(actuator);
		
		//traslazione su una linea spezzata tramite Keyframe
		PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe("x",
				Keyframe.ofFloat(0f, v.getLeft() + FIRST_RADIUS),
				Keyframe.ofFloat(0.3f, 0.9f * FINAL_X),
				Keyframe.ofFloat(0.6f, FINAL_X), Keyframe.ofFloat(1f, FINAL_X));
		ObjectAnimator animX = ObjectAnimator.ofPropertyValuesHolder(v, pvh);
		ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", centerY
				- SECOND_RADIUS);

		//ultimo quarto di cerchio
		from = new PolarCoordinates(FINAL_X, centerY, SECOND_RADIUS, 0);
		to = new PolarCoordinates(FINAL_X, centerY, SECOND_RADIUS, 90);

		ValueAnimator va2 = ValueAnimator.ofObject(evaluator, from, to);
		va2.addUpdateListener(actuator);

		AnimatorSet as = new AnimatorSet();
		as.play(animX).with(animY).before(va2).after(va);
		as.setDuration(2000);
		as.setInterpolator(new LinearInterpolator());
		as.start();
	}

	private static Path createPath(View v) {
		Path path = new Path();
		path.moveTo(v.getLeft(), v.getTop());
		float huntingX = v.getLeft();
		float huntingY = v.getTop() + 200;
		float endX = huntingX + 200;
		float endY = huntingY;
		path.quadTo(huntingX, huntingY, endX, endY);
		huntingX = endX + 200;
		huntingY = endY;
		endX = huntingX;
		endY = huntingY - 100;
		path.quadTo(huntingX, huntingY, endX, endY);
		huntingX = endX;
		huntingY = endY - 100;
		endX = huntingX + 200;
		endY = huntingY;
		path.quadTo(huntingX, huntingY, endX, endY);
		path.lineTo(FINAL_X, endY + 200);
		return path;
	}

	public void animatePath(final View v) {

		//vogliamo animare lungo il path in rapporto alla sua lunghezza lineare
		//abbiamo bisogno di un oggetto PathMeasure
		final PathMeasure pm = new PathMeasure(createPath(v), false);
		float pathlength = pm.getLength();
		ValueAnimator va = ValueAnimator.ofFloat(0.0f, pathlength);
		va.addUpdateListener(new AnimatorUpdateListener() {
			
			final float[] pos = new float[2];

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float currDistance = (Float) animation.getAnimatedValue();
				//PathMeasure ricava e coordinate di un punto nel path posto ad un certo livello di
				//distanza lineare
				pm.getPosTan(currDistance, pos, null);
				v.setX(pos[0]);
				v.setY(pos[1]);
			}
		});
		va.setDuration(4000);
		va.start();
	}
}
