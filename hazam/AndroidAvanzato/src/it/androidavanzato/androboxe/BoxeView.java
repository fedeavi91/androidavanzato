package it.androidavanzato.androboxe;

import it.androidavanzato.R;
import it.androidavanzato.androboxe.HitView.PunchListener;

import java.io.IOException;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class BoxeView extends FrameLayout implements SurfaceTextureListener, PunchListener {

	private TextureView mSurfaceView;
	private SurfaceTexture mSurface;
	private HitView mHitView;
	private ImageView mKapow;
	private Size mPreviewSize;
	private List<Size> mSupportedPreviewSizes;
	private Camera mCamera;

	public BoxeView(Context context) {
		super(context);
		init(context);
	}

	public BoxeView(Context context, AttributeSet set) {
		super(context, set);
		init(context);
	}

	private final void init(Context context) {
		mSurfaceView = new TextureView(context);
		mSurfaceView.setVisibility(View.VISIBLE);
		mSurfaceView.setSurfaceTextureListener(this);
		addView(mSurfaceView);
		
		mHitView = new HitView(context);
		mHitView.setPunchListener(this);
		addView(mHitView);
		
		mKapow = new ImageView(context);
		mKapow.setImageResource(R.drawable.kapow);
		mKapow.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		addView(mKapow, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	private void restorePreview() {
		if (mCamera != null) {
			try {
				if (mSurface != null) { 
					mCamera.setPreviewTexture(mSurface);
				}
				mCamera.startPreview();
				mCamera.setFaceDetectionListener(mHitView);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			requestLayout();
			restorePreview();
		}
	}
	
	public void release() {
		mCamera.stopPreview();
		mCamera.release();	
		mCamera = null;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = Util.getOptimalPreviewSize(mSupportedPreviewSizes,
					width, height);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.i(Util.TAG, "BoxeView Changed " + changed);
		if (getChildCount() == 3) {
			final int width = r - l;
			final int height = b - t;

			int previewWidth = width;
			int previewHeight = height;
			int orientation = getContext().getResources().getConfiguration().orientation;
			if (mPreviewSize != null) {
				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					previewWidth = mPreviewSize.height;
					previewHeight = mPreviewSize.width;
				} else {
					previewWidth = mPreviewSize.width;
					previewHeight = mPreviewSize.height;
				}
			}

			int cl, ct, cr, cb;
			if (width * previewHeight > height * previewWidth) {
				final int scaledChildWidth = previewWidth * height
						/ previewHeight;
				cl = (width - scaledChildWidth) / 2;
				ct = 0;
				cr = (width + scaledChildWidth) / 2;
				cb = height;
			} else {
				final int scaledChildHeight = previewHeight * width
						/ previewWidth;
				cl = 0;
				ct = (height - scaledChildHeight) / 2;
				cr = width;
				cb = (height + scaledChildHeight) / 2;
			}

			mSurfaceView.layout(cl, ct, cr, cb);
			mHitView.layout(cl, ct, cr, cb);
			
			mKapow.layout(0, 0, mKapow.getMeasuredWidth(), mKapow.getMeasuredHeight());
			mKapow.setAlpha(0.0f);
		}
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		Log.i(Util.TAG, "onSurfaceTextureAvailable " + mCamera);
		mSurface = surface;		
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		requestLayout();
		restorePreview();
		mCamera.startFaceDetection();
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		if (mCamera != null) {
			mCamera.stopFaceDetection();
			mCamera.stopPreview();
			mCamera.release();
		}
		return true;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		Log.i(Util.TAG, "onSurfaceTextureSizeChanged " + mCamera);
		if (mCamera != null) {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			requestLayout();
			mCamera.setParameters(parameters);
			restorePreview();
		}
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		//nothing to do on update
	}

	@Override
	public void punchHitAt(int x, int y) {
		Log.i(Util.TAG, "PUNCH!");
		mKapow.setX(x - mKapow.getWidth() / 2 + mHitView.getLeft());
		mKapow.setY(y - mKapow.getHeight() / 2 + mHitView.getTop());
		AnimatorSet as = new AnimatorSet();
		as.play(ObjectAnimator.ofFloat(mKapow, "rotation", -45, 0)).
			with(ObjectAnimator.ofFloat(mKapow, "alpha", 0, 1)).
			before(ObjectAnimator.ofFloat(mKapow, "alpha", 1, 0));
		as.start();
	}

}
