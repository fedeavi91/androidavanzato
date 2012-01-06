package it.androidavanzato.easters;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

public class ICSEasterSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private Context mCtx;
	private final static String EASTERS_CALENDAR_NAME = "easters_cal";
	
	public ICSEasterSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mCtx = context;
	}
	
	public ICSEasterSyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, false);
		mCtx = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.i(C.TAG, "perform sync on account "+account);
		//CalendarQueryHelper.dumpCalendars(mCtx);
		//CalendarQueryHelper.createLocalCalendarWithName(mCtx, "Easters");
		Log.i(C.TAG, "--> AFTER");
		//CalendarQueryHelper.dumpCalendars(mCtx);
		//Log.i(C.TAG, "deleted: "+CalendarQueryHelper.deleteCalendarWithId(mCtx,2));
		//Log.i(C.TAG, "deleted: "+CalendarQueryHelper.deleteCalendarWithId(mCtx,3));
		CalendarQueryHelper.dumpCalendars(mCtx);
	}
}
