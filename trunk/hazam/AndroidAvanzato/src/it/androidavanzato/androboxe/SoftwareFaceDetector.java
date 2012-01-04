package it.androidavanzato.androboxe;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.FaceDetector;
import android.util.Log;
import android.widget.ImageView;

public class SoftwareFaceDetector implements PreviewCallback {
	private ImageView mPreviewTarget;
	private Bitmap mBuf;
	private int[] mPixels, mPixels2;
	private FaceDetector mDetector;
	private final PointF point = new PointF();
	private final FaceDetector.Face[] mFaces = new FaceDetector.Face[MAX_FACES];
	public static final int MAX_FACES = 1;

	public SoftwareFaceDetector(Camera c) {
		init(c);
	}

	public SoftwareFaceDetector(Camera c, ImageView targ) {
		mPreviewTarget = targ;
		init(c);
	}

	byte[] buf = new byte[152080];

	private void init(Camera camera) {
		camera.setPreviewCallbackWithBuffer(this);
		adaptToSize(camera, camera.getParameters().getPreviewSize()); 
	}

	private void adaptToSize(Camera c, Size s) {
		boolean toadapt = mBuf == null
				|| (mBuf.getWidth() != s.height || mBuf.getHeight() != s.width);
		if (toadapt) {
			mPixels = new int[s.width * s.height];
			mPixels2 = new int[s.width * s.height];
			mBuf = Bitmap
					.createBitmap(s.height, s.width, Bitmap.Config.RGB_565);
			buf = new byte[s.width * s.height * ImageFormat.getBitsPerPixel(c.getParameters().getPreviewFormat()) / 8 + 16];
			mDetector = new FaceDetector(s.height, s.width, 1);
			c.addCallbackBuffer(buf);
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Log.d(Util.TAG, "bytes: " + data.length);
		Camera.Parameters pars = camera.getParameters();
		Size s = pars.getPreviewSize();
		adaptToSize(camera, s);
		// ipotizza un formato in entrata YV12 (Samsung Nexus S)
		Util.toRGB565(data, s.width, s.height, mPixels);
		Util.rotateClockWise(mPixels, s.width, s.height, mPixels2);
		mBuf.setPixels(mPixels2, 0, s.height, 0, 0, s.height, s.width);
		if (mPreviewTarget != null) {
			mPreviewTarget.setImageBitmap(mBuf);
		}
		int detected = 0; 
		detected = mDetector.findFaces(mBuf, mFaces);
		if (detected > 0) {
			mFaces[0].getMidPoint(point);
			Log.i(Util.TAG, "detected face at "+point.x+";"+point.y);
			
		}
		camera.addCallbackBuffer(buf);
	}
}
