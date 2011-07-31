package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MessengerPipeTest extends ActivityInstrumentationTestCase2<MessengerPipe> {

	public MessengerPipeTest() {
		super(MessengerPipe.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	public void testPreconditions() throws InterruptedException {
		MessengerPipe out = getActivity();
		assertNotNull(out);
	}

	public void testFactorial() throws InterruptedException {
		MessengerPipe out = getActivity();
		TextView argument = (TextView) out.findViewById(R.id.argument);
		TextView result = (TextView) out.findViewById(R.id.result);
		Button btn = (Button) out.findViewById(R.id.btn_calc);
		TouchUtils.clickView(this, argument);
		Thread.sleep(200);
		getInstrumentation().sendStringSync("3");
		Thread.sleep(200);
		TouchUtils.clickView(this, btn);
		Thread.sleep(500);
		assertEquals("6", result.getText().toString());
	}

	public void testLongFactorial() throws InterruptedException {
		MessengerPipe out = getActivity();
		TextView argument = (TextView) out.findViewById(R.id.argument);
		TextView result = (TextView) out.findViewById(R.id.result);
		Button btn = (Button) out.findViewById(R.id.btn_calc);
		TouchUtils.clickView(this, argument);
		Thread.sleep(200);
		getInstrumentation().sendStringSync("7000");
		Thread.sleep(200);
		TouchUtils.clickView(this, btn);
		Thread.sleep(200);
		TouchUtils.clickView(this, result);
		Thread.sleep(200);
		assertTrue(result.hasFocus());
	}
	
}
