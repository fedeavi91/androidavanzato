package it.androidavanzato.androboxe;

import it.androidavanzato.R;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

public class BoxeActivity extends Activity {
	private BoxeView mBoxeView;
	private Camera mCamera;
	private int mCameraId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.androboxe);

		mBoxeView = (BoxeView) findViewById(R.id.boxe_view);

		mCameraId = Util.getBackFacingCameraID();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(Util.TAG, "OnResume iwth camera id: " + mCameraId);

		if (mCameraId >= 0) {
			mCamera = Camera.open(mCameraId);
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(mCameraId, info);
			int rotation = getWindowManager().getDefaultDisplay().getRotation();
			int degrees = 0;
			switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
			}

			int result;
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				result = (info.orientation + degrees) % 360;
				result = (360 - result) % 360;
			} else {
				result = (info.orientation - degrees + 360) % 360;
			}
			mCamera.setDisplayOrientation(result);
			mBoxeView.setCamera(mCamera);
			//new SoftwareFaceDetector(mCamera, (ImageView)findViewById(R.id.out));
		}
	}

	@Override
	protected void onPause() {
		Log.i(Util.TAG, "OnPause iwth camera id: " + mCamera);
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mBoxeView.release();
			mCamera = null;
		}
		super.onPause();
	}
}