package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Looper;
import android.test.ActivityUnitTestCase;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MessengerPipeIsolatedTest extends ActivityUnitTestCase<MessengerPipe> {
	public MessengerPipeIsolatedTest() {
		super(MessengerPipe.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	public void testPreconditions() {
		Intent i = new Intent(getInstrumentation().getTargetContext(), MessengerPipe.class);
		startActivity(i, null, null);
		Activity out = getActivity();
		assertNotNull(out);
	}

	public void testBindServiceCalled() {
		Intent i = new Intent(getInstrumentation().getTargetContext(), MessengerPipe.class);
		class ResultHolder {
			boolean bindCalled = false;
		}
		final ResultHolder result = new ResultHolder();
		setActivityContext(new ContextWrapper(getInstrumentation().getTargetContext()) {
			@Override
			public boolean bindService(Intent service, ServiceConnection conn, int flags) {
				String compName = service.getComponent().getClassName();
				if (compName.equals("it.androidavanzato.ui.FactorialService")) {
					result.bindCalled = true;
				}
				return getInstrumentation().getTargetContext().bindService(service, conn, flags);
			}
		});
		startActivity(i, null, null);
		MessengerPipe out = getActivity();
		getInstrumentation().callActivityOnStart(out);
		assertTrue(result.bindCalled);
	}


	public void testInsertText() {
		Intent i = new Intent();
		startActivity(i, null, null);
		MessengerPipe out = getActivity();
		TextView argument = (TextView) out.findViewById(R.id.argument);
		argument.setText("10");
	}

	public void testLayoutLabel() throws InterruptedException {
		Intent i = new Intent();
		startActivity(i, null, null);
		MessengerPipe out = getActivity();
		TextView argument = (TextView) out.findViewById(R.id.label);
		assertTrue(argument.getWidth() < 20);
		argument.setText("Very very very " +
				"long String. Bit more.");
		Thread.sleep(500);
		assertTrue(argument.getWidth() > 50);
	}

	public void testWheelOnScreen() throws InterruptedException {
		Intent i = new Intent();
		startActivity(i, null, null);
		ProgressBar wheel = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
		assertTrue(wheel.isIndeterminate());
	}
}
