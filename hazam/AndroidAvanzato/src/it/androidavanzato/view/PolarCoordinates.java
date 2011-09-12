package it.androidavanzato.view;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.PointF;
import android.view.View;

/**
 * Semplice classe per gestire coordinate polari. Mantiene le coordinate di
 * un punto nello spazio 2D tramite un valore di rotazione.
 * Gli angoli sono espressi in gradi.
 * 
 */
public class PolarCoordinates {
	private float mRadius;
	private float mAngle;
	private final PointF mCenter = new PointF();
	private PointF mCartesian = null;

	public PolarCoordinates(float centerX, float centerY, float radius,
			float angle) {
		this.mCenter.x = centerX;
		this.mCenter.y = centerY;
		this.mRadius = radius;
		this.mAngle = angle;
	}

	private void calcCartesian() {
		if (mCartesian == null) {
			mCartesian = new PointF();
			//tra la definizione matematica e quella
			//"comune" dell'angolo polare c'e' una differenza di 90 gradi
			double rad = Math.toRadians(mAngle - 90);
			mCartesian.x = mCenter.x + (float) Math.cos(rad) * mRadius;
			mCartesian.y = mCenter.y + (float) Math.sin(rad) * mRadius;
		}
	}

	public float getCartesianX() {
		calcCartesian();
		return mCartesian.x;
	}

	public float getCartesianY() {
		calcCartesian();
		return mCartesian.y;
	}

	public void setAngle(float mAngle) {
		this.mAngle = mAngle;
		mCartesian = null;
	}

	public void setRadius(float mRadius) {
		this.mRadius = mRadius;
		mCartesian = null;
	}

	public float getAngle() {
		return mAngle;
	}

	public float getRadius() {
		return mRadius;
	}

	/**
	 * Evaluator personalizzato che permette di interpolare tra due oggetti coordinate polari.
	 * Responsaiblita' di questo oggetto e' cmputare correttamente uno stato intermedio
	 * di una transizione
	 * 
	 */
	public static class Evaluator implements TypeEvaluator {

		private final static FloatEvaluator floatEval = new FloatEvaluator();

		@Override
		public Object evaluate(float fraction, Object startValue,
				Object endValue) {
			
			PolarCoordinates from = (PolarCoordinates) startValue;
			PolarCoordinates to = (PolarCoordinates) endValue;

			float evalCenterX = (Float) floatEval.evaluate(fraction, from.mCenter.x, to.mCenter.x);
			float evalCenterY = (Float) floatEval.evaluate(fraction, from.mCenter.y, to.mCenter.y);

			float evalRadius = (Float) floatEval.evaluate(fraction, from.getRadius(), to.getRadius());

			float evalAngle = (Float) floatEval.evaluate(fraction, from.getAngle(), to.getAngle());

			return new PolarCoordinates(evalCenterX, evalCenterY, evalRadius, evalAngle);
		}
	}

	/**
	 * un update listener che si incarica di trasformare una posizione in coordinate polari animata nelle
	 * rispettive coordinate cartesiane e le applica ad una View bersaglio. 
	 */
	public static class PolarToCartesian implements AnimatorUpdateListener {

		private View mTarget;

		public PolarToCartesian(View v) {
			mTarget = v;
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			PolarCoordinates animated = (PolarCoordinates)animation.getAnimatedValue();
			mTarget.setX(animated.getCartesianX());
			mTarget.setY(animated.getCartesianY());
		}
	}
}
