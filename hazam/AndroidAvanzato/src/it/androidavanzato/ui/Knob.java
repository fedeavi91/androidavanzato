package it.androidavanzato.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * This class implements a rotating ImageView. The rotation is done as
 * a rotating drag, in order to simulate a phisical round knob.
 * 
 * @author Emanuele Di Saverio (emanuele.disaverio at gmail.com)
 *
 */
public class Knob extends ImageView {

	private static final String ATTRS_NS = "http://hazam.com";
	private static final float DEFAULT_MIN_ROTATION = 0.0f;
	private static final float DEFAULT_MAX_ROTATION = 360.0f;
	private static final float INVALID_ROTATION = Float.MIN_VALUE;
	private static final String TAG = "Knob";

	private float mMaxRotation = DEFAULT_MAX_ROTATION;
	private float mMinRotation = DEFAULT_MIN_ROTATION;
	private float mRotation = DEFAULT_MIN_ROTATION;

	private final PointF mCenter = new PointF(0.0f, 0.0f);
	private float sensibilityThreshold = 0.0f;
	private double startAngle = 0.0d;

	protected OnKnobPositionChangeListener listener = null;

	public Knob(Context context) {
		super(context);
	}

	public Knob(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public Knob(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attr) {
		mMaxRotation = attr.getAttributeFloatValue(ATTRS_NS, "maxRotation", DEFAULT_MAX_ROTATION);
		mMinRotation = attr.getAttributeFloatValue(ATTRS_NS, "minRotation", DEFAULT_MIN_ROTATION);
		setRotation(mMinRotation);
		checkForMinMax(mMaxRotation, mMinRotation);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Log.d(TAG, "onFinishInflate(): " + this);
	}

	private void checkForMinMax(float max, float min) {
		if (mMaxRotation < mMinRotation) {
			throw new RuntimeException("Invalid max, min rotation!");
		}
	}

	/** ROTATION HANDLING **/
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(TAG, "onMeasure(widthMeasureSpec: "+widthMeasureSpec+", heightMeasureSpec: "+heightMeasureSpec+")");
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			mCenter.x = getWidth() >> 1;
			mCenter.y = getHeight() >> 1;
			sensibilityThreshold = Math.min(getWidth(), getHeight()) >> 3;
		}
		Log.d(TAG, "onLayout, changed: "+changed+", center: " + mCenter + ", sens: " + sensibilityThreshold);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(mRotation, mCenter.x, mCenter.y);
		super.onDraw(canvas);
		canvas.restore();
	}

	protected double getAngle(float x, float y) {
		double rad = Math.atan2(y, x);
		return Math.toDegrees(rad);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean consumed = false;
		final float ex = event.getX() - mCenter.x;
		final float ey = event.getY() - mCenter.y;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float b = PointF.length(ex, ey);
			if (b > sensibilityThreshold) {
				final double theAngle = getAngle(ex, ey);
				startAngle = theAngle;
			} else {
				startAngle = INVALID_ROTATION;
			}
			consumed = true;
		} else if (!consumed && event.getAction() == MotionEvent.ACTION_UP) {
			startAngle = INVALID_ROTATION;
			consumed = true;
		} else if (!consumed && event.getAction() == MotionEvent.ACTION_MOVE) {
			if (startAngle != INVALID_ROTATION) {
				final double theAngle = getAngle(ex, ey);

				double endAngle = theAngle;

				double angle = endAngle - startAngle;
				double turned = angle < 0 ? angle + 360.0 : angle - 360.0;

				mRotation += Math.abs(angle) < Math.abs(turned) ? angle : turned;

				invalidate();
				startAngle = endAngle;
				consumed = true;
				trimRotation();
				if (listener != null) {
					listener.onKnobPositionChange(this, mRotation, true);
				}
			}
		}

		return consumed;
	}

	private void trimRotation() {
		if (mRotation < mMinRotation) {
			mRotation = mMinRotation;
		} else if (mRotation > mMaxRotation) {
			mRotation = mMaxRotation;
		}
	}

	public float getRotation() {
		return mRotation;
	}

	public void setRotation(float rot) {
		if (this.mRotation != rot) {
			this.mRotation = rot;
			if (listener != null) {
				listener.onKnobPositionChange(this, rot, true);
			}
		}
	}

	public float getMaxRotation() {
		return mMaxRotation;
	}

	public float getMinRotation() {
		return mMinRotation;
	}

	/** END ROTATION HANDLING **/

	/** ANDROID LISTENER **/
	/**
	 * Listener for changes in the Knob position. As Android usually does, the View that caused the change is passed as
	 * first argument to allow multiple Views to be handled by the same listener.
	 */
	public static interface OnKnobPositionChangeListener {
		public void onKnobPositionChange(Knob knob, float rot, boolean fromUser);
	}

	public void setOnKnobPositionChangeListener(OnKnobPositionChangeListener l) {
		listener = l;
		if (listener != null) {
			listener.onKnobPositionChange(this, mRotation, false);
		}
	}

	/** END ANDROID LISTENER **/

	/** SAVING VIEW STATE **/
	public static class SavedState extends BaseSavedState {
		private float savedRotation;

		public SavedState(Parcelable p) {
			super(p);
		}

		/**
		 * here I have to write the relevant field that I want to save
		 * about my custom View component
		 */
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeFloat(savedRotation);
		}

		/**
		 * We have to provide a static final Parcelable.Creator<?> named EXACTLY "CREATOR"
		 * to enable Android to recover the Parcelable object from Interprocess Stream Communication
		 */
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

		private SavedState(Parcel in) {
			super(in);
			savedRotation = in.readFloat();
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		SavedState toret = new SavedState(super.onSaveInstanceState());
		toret.savedRotation = mRotation;
		return toret;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setRotation(ss.savedRotation);
	}

	/** END SAVING VIEW STATE **/

	@Override
	public String toString() {
		String str = "Knob.SavedState{\n";
		str += Integer.toHexString(System.identityHashCode(this)) + "\n";
		str += " mRotation=" + Float.toString(mRotation) + "\n";
		str += " mMaxRotation=" + Float.toString(mMaxRotation) + "\n";
		str += " mMinRotation=" + Float.toString(mMinRotation);
		return str + "}\n";
	}
}
