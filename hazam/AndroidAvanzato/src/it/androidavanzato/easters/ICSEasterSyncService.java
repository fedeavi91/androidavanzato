package it.androidavanzato.easters;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ICSEasterSyncService extends Service {

	private static final Object sSyncAdapterLock = new Object();

	private static ICSEasterSyncAdapter sSyncAdapter = null;

	@Override
	public void onCreate() {
		synchronized (sSyncAdapterLock) {
			if (sSyncAdapter == null) {
				sSyncAdapter = new ICSEasterSyncAdapter(
						getApplicationContext(), true);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}
}
