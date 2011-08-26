package it.androidavanzato.romaski.widget;

import it.androidavanzato.AndroidAvanzatoApplication;
import it.androidavanzato.R;
import it.androidavanzato.romaski.fs.ImageCache;
import it.androidavanzato.romaski.net.Base64;
import it.androidavanzato.romaski.util.ImageDownloader;

import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


/**
 * Extended ImageView to handle http:// loading of the images. Includes caching on SDCARD (if available)
 * 
 */
public class RemoteImageView extends ImageView implements LoaderCallbacks<Uri> {

	private static final String TAG = "RemoteImageView";
	private Uri remoteUri = null;
	private ImageCache cache;
	private LoaderManager loaderMan = null;
	private ImageDownloader loader = null;

	public RemoteImageView(Context context) {
		super(context);
		init(context);
	}

	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context ctx) {
		if (!Activity.class.isAssignableFrom(ctx.getClass())) {
			throw new RuntimeException("We need an activity context!");
		}
		cache = (ImageCache) AndroidAvanzatoApplication.getAppService(AndroidAvanzatoApplication.IMAGE_CACHE_APPSERVICE);
		if (cache == null) {
			throw new RuntimeException("No ImageCache service registered!");
		}
		loaderMan = ((Activity) ctx).getLoaderManager();
	}

	private static void trace(String msg) {
		Log.v(TAG, msg);
	}

	@Override
	public void setImageURI(Uri uri) {
		if (uri.toString().startsWith("http")) {
			remoteUri = uri;
			super.setImageResource(R.drawable.hourglass);
			cacheReload();
		} else {
			super.setImageURI(uri);
		}
	}
	
	static AtomicInteger id = new AtomicInteger(0);

	public void netReload() {
		super.setImageResource(R.drawable.hourglass);
		if (loader != null) {
			if (loader.isStarted()) {
				loader.cancelLoad();
			}
			loader = null;
		}
		setImageFromCache();
		int loaderId = id.getAndIncrement();
		loader = (ImageDownloader) loaderMan.initLoader(loaderId, null, this);		
		trace("Start download id["+loaderId+"] for: " + remoteUri);
	}

	public void cacheReload() {
		if (remoteUri == null) {
			return;
		}
		setImageFromCache();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		trace("onDrawCalled "+this+" "+remoteUri);
		super.onDraw(canvas);
	}
	
	private void setImageFromCache() {
		if (remoteUri == null) return;
		String id = Base64.encodeToString(remoteUri.toString().getBytes(), Base64.DEFAULT);
		if (cache.hasEntryFor( id )) {
			trace("--Found in cache!" + remoteUri);
			Bitmap orig = cache.getBitmap(id);
			trace("--"+this);
			setImageBitmap(orig);
			invalidate();
		}
	}

	@Override
	public Loader<Uri> onCreateLoader(int id, Bundle args) {
		trace("->onCreateLoader");	
		ImageDownloader loader = new ImageDownloader(getContext(), remoteUri, cache);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Uri> arg0, Uri uri) {		
		trace("onLoadFinished: RESOURCE IS DOWNLOADED " + uri);
		setImageFromCache();
	}

	@Override
	public void onLoaderReset(Loader<Uri> uri) {
		trace("o->nLoaderReset: " + uri);		
	}
}
