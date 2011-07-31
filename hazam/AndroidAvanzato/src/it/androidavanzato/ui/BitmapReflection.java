package it.androidavanzato.ui;

import it.androidavanzato.R;
import it.androidavanzato.util.ImageUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.ImageView;


public class BitmapReflection extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bitmapreflection);
		ImageView imageView = (ImageView) findViewById(R.id.image);
		Bitmap originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.images);
		int spacing = (int) (4 * getResources().getDisplayMetrics().density);
		imageView.setImageBitmap( ImageUtils.buildReflectedBitmap(originalImage, spacing, 128, 0.5f));
		originalImage.recycle();
		getWindow().setFormat(PixelFormat.RGBA_8888);
	}
}