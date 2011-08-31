package it.androidavanzato.fourwaynav;

import android.content.Context;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.util.AttributeSet;
import android.util.Log;

public class FourWayNavView extends RSSurfaceView {
	public static enum Roll {
		LEFT(0), UP(1), DOWN(2), RIGHT(3), NONE(-1);
		int value;
		
		private Roll(int value) {
			this.value = value;
		}
	}
	private RenderScriptGL mRS;
	private FourWayRS mRender;

	public FourWayNavView(Context context) {
		super(context);
	}
	public FourWayNavView(Context context, AttributeSet attribs) {
		super(context, attribs);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mRender.init(mRS, getResources());
	}

	private void ensureRenderScript() {
		try {
			if (mRS == null) {
				// Initialize renderscript with desired surface characteristics.
				// In this case, just use the defaults
				RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
				sc.setDepth(16, 24);
				mRS = createRenderScriptGL(sc);
				// Create an instance of the script that does the rendering
				mRender = new FourWayRS();
				mRender.init(mRS, getResources());
			}
		} catch (Throwable th) {
			Log.w("RenderScript", "Exception in init: ", th);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ensureRenderScript();
	}

	@Override
	protected void onDetachedFromWindow() {
		// Handle the system event and clean up
		mRender = null;
		if (mRS != null) {
			mRS = null;
			destroyRenderScriptGL();
		}
	}
	
	public void roll(Roll roll) {
		mRender.roll( roll );
		mRender.rollLeft();
	}
}
