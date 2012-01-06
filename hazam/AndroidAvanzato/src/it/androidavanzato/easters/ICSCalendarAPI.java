package it.androidavanzato.easters;

import java.util.Calendar;
import java.util.Date;

import it.androidavanzato.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

public class ICSCalendarAPI extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_api);
		//insertViaContentResolver();
		//queryCalendars();
		//deleteCalendars();
		//queryCalendars();
		//AccountManager.get(this).addAccountExplicitly(new Account("dumb", "it.androidvanzato"), "pippo", new Bundle());
		Log.i("AndroidAvanzato", Easter.forYear(2040).toString());
		CalendarQueryHelper.createLocalCalendarWithName(this, "Easters");
		CalendarQueryHelper.dumpCalendars(this);
		//CalendarQueryHelper.deleteCalendarWithId(this, 2);
		CalendarQueryHelper.dumpCalendars(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

class Easter {
	
	static Date forYear(int yer) {
		int dayOfMonth = 0, month = 0;
		//algoritmo di Oudin-Tondering come riportato a
		//http://xoomer.virgilio.it/esongi/oudin.htm
		int N = yer;
		int G = N % 19;
		int C = N / 100;
		int H = ((C-C / 4-(8*C+13) / 25+19*G+15) % 30);
		int I = H-(H / 28)*(1-(29 / (H+1))*((21-G) / 11));
		int J = ((N+N / 4+I+2-C+C / 4) % 7);
		int L = I-J;
		month = (3+(L+40) / 44);
		dayOfMonth = L+28-31*(month / 4);
		
		//correggere i mesi che per java.util.Date sono 0-based
		month--;
		
		Calendar c = Calendar.getInstance();
		c.set(yer, month, dayOfMonth, 0 , 0 , 0);
		return c.getTime();
	}
}
