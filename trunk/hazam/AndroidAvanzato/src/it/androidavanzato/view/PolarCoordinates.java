package it.androidavanzato.view;

import android.graphics.PointF;

public class PolarCoordinates {
	private float mRadius;
	private float mAngle;
	private final PointF mCenter = new PointF();
	private PointF mCartesian = null;
	
	public PolarCoordinates(float centerX, float centerY, float radius, float angle) {
		this.mCenter.x = centerX;
		this.mCenter.y = centerY;
		this.mRadius = radius;
		this.mAngle = angle;
	}
	
	private void calcCartesian() {
		if (mCartesian == null) {
			double rad = Math.toRadians(mAngle);
			mCartesian.x = mCenter.x + (float)Math.cos(rad) * mRadius;
			mCartesian.y = mCenter.y + (float)Math.sin(rad) * mRadius; 
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
}
