package it.androidavanzato.ui;

import it.androidavanzato.ui.FactorialService.LocalBinder;

import java.math.BigInteger;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.test.ServiceTestCase;

public class FactorialServiceTest extends ServiceTestCase<FactorialService> {
	IBinder mBinder;

	public FactorialServiceTest() {
		super(FactorialService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getContext(), FactorialService.class);
		intent.putExtra("test", true);
		mBinder = bindService(intent);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		FactorialService serv = getService();
		assertNotNull(serv);
	}

	public void testDirectSimpleFactorials() {
		FactorialService serv = getService();
		BigInteger result = serv.factorial(new BigInteger("1"));
		assertEquals("1", result.toString());
		result = serv.factorial(new BigInteger("2"));
		assertEquals("2", result.toString());
		result = serv.factorial(new BigInteger("3"));
		assertEquals("6", result.toString());
	}

	public void testChanneled() throws InterruptedException {
		FactorialService serv = ((LocalBinder)mBinder).getService();
		BigInteger result = serv.factorial(new BigInteger("1"));
		assertEquals("1", result.toString());
	}
}
