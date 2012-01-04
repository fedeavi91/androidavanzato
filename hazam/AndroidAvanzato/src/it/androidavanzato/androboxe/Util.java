package it.androidavanzato.androboxe;

import java.util.List;

import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.util.Log;

public class Util {
	public static final String TAG = "AndroBoxe";
	
	public static int getBackFacingCameraID() {
		int numberOfCameras = Camera.getNumberOfCameras();
		int cameraId = -1;
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
			}
		}
		return cameraId;
	}
	
	public static void rotateClockWise(int[] pixels, int currWidth,
			int currHeight, int[] target) {
		for (int i = 0; i < currWidth; i++) {
			for (int j = currHeight - 1; j > 0; j--) {
				int val = pixels[j * currWidth + i];
				target[(currHeight - 1 - j) + i * currHeight] = val;
			}
		}
	}

	public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(ratio - targetRatio);
			}
		}
		Log.i(TAG, "optimalPreview: " + optimalSize.width + ";"
				+ optimalSize.height);
		return optimalSize;
	}

	public static void toRGB565(byte[] yuvs, int width, int height, int[] rgbs) {
		// the end of the luminance data
		final int lumEnd = width * height;
		// points to the next luminance value pair
		int lumPtr = 0;
		// points to the next chromiance value pair
		int chrPtr = lumEnd;
		int chr2Ptr = lumEnd + (lumEnd >> 2);
		// points to the next byte output pair of RGB565 value
		int outPtr = 0;
		// the end of the current luminance scanline
		int lineEnd = width;

		while (true) {

			// skip back to the start of the chromiance values when necessary
			if (lumPtr == lineEnd) {
				if (lumPtr == lumEnd)
					break; // we've reached the end
				// division here is a bit expensive, but's only done once per
				// scanline
				chrPtr = lumEnd + ((lumPtr >> 2) / width) * width;
				chr2Ptr = chrPtr + (lumEnd >> 2);
				lineEnd += width;
			}

			// read the luminance and chromiance values
			final int Y1 = yuvs[lumPtr++] & 0xff;
			final int Y2 = yuvs[lumPtr++] & 0xff;
			final int U = (yuvs[chrPtr++] & 0xff);
			final int V = (yuvs[chr2Ptr++] & 0xff);
			int C1 = Y1 - 16;
			int C2 = Y2 - 16;
			int D = U - 128;
			int E = V - 128;
			int R, G, B;

			// generate first RGB components
			B = ((298 * C1 + 516 * D + 128) >> 8);
			if (B < 0)
				B = 0;
			else if (B > 255)
				B = 255;
			G = ((298 * C1 - 100 * D - 208 * E + 128) >> 8);
			if (G < 0)
				G = 0;
			else if (G > 255)
				G = 255;
			R = ((298 * C1 + 409 * E + 128) >> 8);
			if (R < 0)
				R = 0;
			else if (R > 255)
				R = 255;
			// NOTE: this assume little-endian encoding
			rgbs[outPtr++] = Color.rgb(R, G, B);

			// generate second RGB components
			B = ((298 * C2 + 516 * D + 128) >> 8);
			if (B < 0)
				B = 0;
			else if (B > 255)
				B = 255;
			G = ((298 * C2 - 100 * D - 208 * E + 128) >> 8);
			if (G < 0)
				G = 0;
			else if (G > 255)
				G = 255;
			R = ((298 * C2 + 409 * E + 128) >> 8);
			if (R < 0)
				R = 0;
			else if (R > 255)
				R = 255;
			// NOTE: this assume little-endian encoding
			rgbs[outPtr++] = Color.rgb(R, G, B);
		}
	}
}
