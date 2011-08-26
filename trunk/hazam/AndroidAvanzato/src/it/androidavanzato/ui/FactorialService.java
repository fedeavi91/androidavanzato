package it.androidavanzato.ui;

import java.math.BigInteger;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class FactorialService extends Service {
	public static final String TAG = "messengerPipe";
	public static final int MSG_FACTORIAL = 1;
	public static final int MSG_FACTORIAL_WRONG = 23;
	static final int MSG_RESULT = 111;

	private class FactorialProcessor implements Runnable {
		private final Message copy;
		
		public FactorialProcessor(Message msg) {
			copy = Message.obtain(msg);
		}
		
		@Override
		public void run() {
			BigInteger result = factorial(new BigInteger(copy.obj.toString()));
			Message answer = Message.obtain();
			answer.what = MSG_RESULT;
			answer.obj = result.toString();
			try {
				copy.replyTo.send(answer);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	};
	
	class FactorialHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.v(TAG, "Ricevuto messaggio! "+msg.what);
			switch (msg.what) {
			case MSG_FACTORIAL:
				Thread t = new Thread(new FactorialProcessor(msg));
				t.start();
				break;
			case MSG_FACTORIAL_WRONG:
				new FactorialProcessor(msg).run();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	final Messenger mMessenger = new Messenger(new FactorialHandler());

	@Override
	public IBinder onBind(Intent intent) {
		loadValuesFromDisk();
		if (intent.getBooleanExtra("test", false)) {
			return new LocalBinder();
		} else {
			return mMessenger.getBinder();
		}
	}
	
    public class LocalBinder extends Binder {
        FactorialService getService() {
            return FactorialService.this;
        }
    }

	@Override
	public boolean onUnbind(Intent intent) {
		saveValuesToDisk();
		return false;
	}

	private void loadValuesFromDisk() {
		// load the computed values from disk
	}

	private void saveValuesToDisk() {
		// we should save the compute values on disk since we may be shut down
	}

	//private ConcurrentHashMap<BigInteger, BigInteger> values = new ConcurrentHashMap<BigInteger, BigInteger>();

	public BigInteger factorial(BigInteger arg) {
		if (arg.equals(BigInteger.ONE)) {
			return arg;
		}
		/*if (values.containsKey(arg)) {
			Log.i(TAG, "found precomputed value!");
			return values.get(arg);
		}*/

		BigInteger counter = BigInteger.ONE;
		BigInteger accu = BigInteger.ONE;
		for (; counter.compareTo(arg) <= 0; counter = counter.add(BigInteger.ONE)) {
			accu = accu.multiply(counter);
			/*if (!values.containsKey(counter)) {
				values.put(counter, accu);
			}*/
		}
		return accu;
	}
}
