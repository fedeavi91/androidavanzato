package it.androidavanzato.view;


import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class Animator {

	private final Handler handler;
	private Interpolator interpolator = new DecelerateInterpolator();
	public static final int DEFAULTDURATION = 2000;
	private long duration = DEFAULTDURATION;
	private long startTime = -1;
	private long FRAMERATE = 25; // 40 fps

	public Animator() {
		this(new Handler());
	}

	public Animator(Handler h) {
		handler = h;
	}

	public void setInterpolator(Interpolator mInterpolator) {
		this.interpolator = mInterpolator;
	}

	public void setDuration(long d) {
		duration = d;
	}

	public final void start() {
		startTime = AnimationUtils.currentAnimationTimeMillis();
		animStart();
		handler.postDelayed(r, FRAMERATE);
		doUpdate(0.0f);
	}

	public final void abort() {
		handler.removeCallbacks(r);
	}

	public final void forceEnd() {
		abort();
		finalizeAnim();
	}

	protected void animStart() {
	}

	protected void animOver() {
	}

	protected abstract void doUpdate(float factor);

	private final Runnable r = new Runnable() {

		@Override
		public void run() {
			long delta = AnimationUtils.currentAnimationTimeMillis() - startTime;
			if (delta > duration) {
				finalizeAnim();
			} else {
				float factor = (float) delta / duration;
				factor = interpolator.getInterpolation(factor);
				doUpdate(factor);
				handler.postDelayed(r, FRAMERATE);
			}
		}
	};

	private void finalizeAnim() {
		doUpdate(1.0f);
		animOver();
	}
}