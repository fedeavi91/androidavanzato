package it.androidavanzato.romaski.util;

import it.androidavanzato.romaski.fs.FilesystemCache;
import it.androidavanzato.romaski.net.Base64;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class ImageDownloader extends AsyncTaskLoader<Uri> {
	private static final HttpClient httpClient = AndroidHttpClient.newInstance("androidavanzato");

	private Uri mTargetUri;
	private FilesystemCache mCache;
	private Throwable error = null;
	private Context mCtx;
	private ConnectivityManager mConnectivityManager;
	private long clength;
	private static final String TAG = "RemoteImageView";

	public ImageDownloader(Context _ctx, Uri targetUri, FilesystemCache cache) {
		super(_ctx);
		this.mCtx = _ctx.getApplicationContext();
		this.mTargetUri = targetUri;
		this.mCache = cache;
		this.mConnectivityManager = (ConnectivityManager) mCtx
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
	}


	private static void trace(String msg) {
		Log.v(TAG, msg);
	}

	@Override
	protected void onStartLoading() {
		trace("onStartLoading");
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		trace("onStopLoading");
		cancelLoad();
	}
	
	@Override
	public Uri loadInBackground() {
		trace("loadInbackground");
		error = null;
		if (weAreOnline()) {
			final HttpGet getFile = new HttpGet(mTargetUri.toString());
			try {
				final HttpResponse resp = httpClient.execute(getFile);

				final HttpEntity ent = resp.getEntity();
				final int statusCode = resp.getStatusLine().getStatusCode();
				switch (statusCode) {
				case HttpStatus.SC_OK:
					handleEntity(ent);
					break;
				case HttpStatus.SC_NOT_MODIFIED:
					break;
				default:
					throw new RuntimeException("Http Status Code: "
							+ statusCode);
				}

				if (ent != null) {
					ent.consumeContent();
				}
				return mTargetUri;
			} catch (Throwable e) {
				error = new RuntimeException(TAG + ": error", e);
				e.printStackTrace();
				return null;
			} finally {
			}
		} else {
			return null;
		}
	}

	private void handleEntity(final HttpEntity ent)
			throws IllegalStateException, IOException {
		clength = ent.getContentLength();
		InputStream in = ent.getContent();
		if (mCache != null) {
			String tUr = mTargetUri.toString();

			mCache.save(Base64.encodeToString(tUr.getBytes(), Base64.DEFAULT),
					in);
		}
	}

	private final boolean weAreOnline() {
		final NetworkInfo ni = mConnectivityManager.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}
	
	public Throwable getError() {
		return error;
	}
	
	public long getContentLength() {
		return clength;
	}
}
