package it.androidavanzato.easters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;

public class CalendarQueryHelper {
	public static Uri createCalendarWithName(Context ctx, String name) {
		Account[] accounts = AccountManager.get(ctx).getAccountsByType("com.google");
		if (accounts == null || accounts.length < 1) {
			throw new RuntimeException("No google account configured on this device!");
		}
		//prendiamo soltanto il primo degli account configurati per semplicita
		String accountName = accounts[0].name;
		
		Uri target = Uri.parse(CalendarContract.Calendars.CONTENT_URI.toString());
		target = target.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
		.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
		.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google").build();
		
		ContentValues values = new ContentValues();
		values.put(Calendars.ACCOUNT_NAME, accountName);
		values.put(Calendars.ACCOUNT_TYPE, "com.google");
		//inseriamo lo stesso valore nome e display name per semplicita
		values.put(Calendars.NAME, name);
		values.put(Calendars.CALENDAR_DISPLAY_NAME, name);
		
		values.put(Calendars.CALENDAR_COLOR, 0xFF0000);
		values.put(Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
		values.put(Calendars.OWNER_ACCOUNT, accountName);
		values.put(Calendars.VISIBLE, 1);
		values.put(Calendars.SYNC_EVENTS, 1);
		values.put(Calendars.CALENDAR_TIME_ZONE, "Europe/Rome");
		values.put(Calendars.CAN_PARTIALLY_UPDATE, 1);
		values.put(Calendars.CAL_SYNC1, "https://www.google.com/calendar/feeds/emanuele.disaverio%40gmail.com/private/full");
		values.put(Calendars.CAL_SYNC2, "https://www.google.com/calendar/feeds/default/allcalendars/full/emanuele.disaverio%40gmail.com");
		values.put(Calendars.CAL_SYNC3, "https://www.google.com/calendar/feeds/default/allcalendars/full/emanuele.disaverio%40gmail.com");
		values.put(Calendars.CAL_SYNC4, 1);
		values.put(Calendars.CAL_SYNC5, 0);
		values.put(Calendars.CAL_SYNC8, System.currentTimeMillis());
		
		Uri newCalendar = ctx.getContentResolver().insert(target, values);
		return newCalendar;
	}
	
	public static Uri ensureLocalCalendarWithName(Context ctx, String name) {
		Uri query = CalendarContract.Calendars.CONTENT_URI;
		
		Cursor c = ctx.getContentResolver().query(query, null, Calendars.NAME+"=?", new String[] { name }, null);
		long id = -1;
		if (c.moveToNext()) {
			id = c.getLong(c.getColumnIndex(Calendars._ID));
		}
		c.close();
		if (id > 0) {
			return query.buildUpon().appendPath(Long.toString(id)).build();
		}
		
		
		Uri target = CalendarContract.Calendars.CONTENT_URI.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, Boolean.TRUE.toString())
		.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "dummyAccount")
		.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL).build();
		
		ContentValues values = new ContentValues();
		
		values.put(Calendars.ACCOUNT_NAME, "dummyAccount");
		values.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		
		//inseriamo lo stesso valore nome e display name per semplicita
		values.put(Calendars.NAME, name);
		values.put(Calendars.CALENDAR_DISPLAY_NAME, name);
		
		values.put(Calendars.CALENDAR_COLOR, 0xFF0000);
		
		values.put(Calendars.VISIBLE, 1);
		values.put(Calendars.SYNC_EVENTS, 1);
		values.put(Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().toString());
		
		Uri newCalendar = ctx.getContentResolver().insert(target, values);
		return newCalendar;
	}
	
	public static int deleteCalendarWithName(Context ctx, String name) {
		
		Uri target = Uri.parse(CalendarContract.Calendars.CONTENT_URI.toString());
		target = target.buildUpon()
		.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL).build();
		
		return ctx.getContentResolver().delete(target, Calendars.NAME+"=?", new String[] { name });
	}
	
	public static Uri createEventWithName(Context ctx, Uri calendar, String name, Date at) {
		long id = Long.parseLong(calendar.getLastPathSegment());
		ContentValues cv = new ContentValues();
		cv.put(Events.TITLE, name);
		cv.put(Events.DTSTART, at.getTime());
		cv.put(Events.DTEND, at.getTime() + 1000000);
		cv.put(Events.CALENDAR_ID, id);
		cv.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
		//cv.put(Events.RRULE, "FREQ=DAILY;INTERVAL=2");
		
		Uri newEvent = ctx.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, cv);
		return newEvent;
	}
	
	public static Uri addAttendeeToEvent(Context ctx, Uri calendar, String name, String email) {
		long id = Long.parseLong(calendar.getLastPathSegment());
		ContentValues cv = new ContentValues();
		cv.put(Attendees.ATTENDEE_NAME, name);
		cv.put(Attendees.ATTENDEE_EMAIL, email);
		cv.put(Attendees.EVENT_ID, id);
		
		Uri newEvent = ctx.getContentResolver().insert(CalendarContract.Attendees.CONTENT_URI, cv);
		return newEvent;
	}
	
 	public static void dumpCalendars(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		Cursor c = cr.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		Map<String, String> list = new HashMap<String,String>();
		while (c.moveToNext()) {
			list.put(Calendars._ID, ""+c.getLong(c.getColumnIndex(Calendars._ID)));
			list.put(Calendars.NAME, c.getString(c.getColumnIndex(Calendars.NAME)));
			/*list.put(Calendars.ACCOUNT_NAME, c.getString(c.getColumnIndex(Calendars.ACCOUNT_NAME)));
			list.put(Calendars.ACCOUNT_TYPE, c.getString(c.getColumnIndex(Calendars.ACCOUNT_TYPE)));
			
			list.put(Calendars.NAME, c.getString(c.getColumnIndex(Calendars.NAME)));
			list.put(Calendars.CALENDAR_DISPLAY_NAME, c.getString(c.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
			
			list.put(Calendars.CALENDAR_COLOR, ""+c.getInt(c.getColumnIndex(Calendars.CALENDAR_COLOR)));
			list.put(Calendars.CALENDAR_ACCESS_LEVEL, ""+c.getInt(c.getColumnIndex(Calendars.CALENDAR_ACCESS_LEVEL)));
			list.put(Calendars.OWNER_ACCOUNT, c.getString(c.getColumnIndex(Calendars.OWNER_ACCOUNT)));
			list.put(Calendars.VISIBLE, ""+c.getInt(c.getColumnIndex(Calendars.VISIBLE)));
			list.put(Calendars.SYNC_EVENTS, ""+c.getInt(c.getColumnIndex(Calendars.SYNC_EVENTS)));
			list.put(Calendars.CALENDAR_TIME_ZONE, c.getString(c.getColumnIndex(Calendars.CALENDAR_TIME_ZONE)));
			
			list.put(Calendars._SYNC_ID, c.getString(c.getColumnIndex(Calendars._SYNC_ID)));

			list.put(Calendars.DIRTY, ""+c.getLong(c.getColumnIndex(Calendars.DIRTY)));
			list.put(Calendars.DELETED, ""+c.getInt(c.getColumnIndex(Calendars.DELETED)));
			list.put(Calendars.MAX_REMINDERS, ""+c.getInt(c.getColumnIndex(Calendars.MAX_REMINDERS)));
			
			list.put(Calendars.ALLOWED_REMINDERS, c.getString(c.getColumnIndex(Calendars.ALLOWED_REMINDERS)));
			list.put(Calendars.ALLOWED_AVAILABILITY, c.getString(c.getColumnIndex(Calendars.ALLOWED_AVAILABILITY)));
			list.put(Calendars.ALLOWED_ATTENDEE_TYPES, c.getString(c.getColumnIndex(Calendars.ALLOWED_ATTENDEE_TYPES)));
			
			list.put(Calendars.CAN_MODIFY_TIME_ZONE, ""+c.getInt(c.getColumnIndex(Calendars.CAN_MODIFY_TIME_ZONE)));
			list.put(Calendars.CAN_ORGANIZER_RESPOND, ""+c.getInt(c.getColumnIndex(Calendars.CAN_ORGANIZER_RESPOND)));
			list.put(Calendars.CAN_PARTIALLY_UPDATE, ""+c.getInt(c.getColumnIndex(Calendars.CAN_PARTIALLY_UPDATE)));
			
			list.put(Calendars.CALENDAR_LOCATION, c.getString(c.getColumnIndex(Calendars.CALENDAR_LOCATION)));

			list.put(Calendars.CAL_SYNC1, c.getString(c.getColumnIndex(Calendars.CAL_SYNC1)));
			list.put(Calendars.CAL_SYNC2, c.getString(c.getColumnIndex(Calendars.CAL_SYNC2)));
			list.put(Calendars.CAL_SYNC3,c.getString(c.getColumnIndex(Calendars.CAL_SYNC3)));
			list.put(Calendars.CAL_SYNC4,c.getString(c.getColumnIndex(Calendars.CAL_SYNC4)));
			list.put(Calendars.CAL_SYNC5,c.getString(c.getColumnIndex(Calendars.CAL_SYNC5)));
			list.put(Calendars.CAL_SYNC6,c.getString(c.getColumnIndex(Calendars.CAL_SYNC6)));
			list.put(Calendars.CAL_SYNC7,c.getString(c.getColumnIndex(Calendars.CAL_SYNC7)));
			list.put(Calendars.CAL_SYNC8,c.getString(c.getColumnIndex(Calendars.CAL_SYNC8)));
			list.put(Calendars.CAL_SYNC9,c.getString(c.getColumnIndex(Calendars.CAL_SYNC9)));
			list.put(Calendars.CAL_SYNC10,c.getString(c.getColumnIndex(Calendars.CAL_SYNC10)));*/

			StringBuilder sb = new StringBuilder("--CALENDAR DUMP--");
			for (Entry<String, String> s: list.entrySet()) {
				sb.append("("+s.getKey()+"|"+s.getValue()+")");
			}
			sb.append("----------");
			Log.i(C.TAG, sb.toString());
			list.clear();
		}
		c.close();
	}
}
