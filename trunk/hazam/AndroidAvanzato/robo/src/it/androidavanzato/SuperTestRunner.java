package it.androidavanzato;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class SuperTestRunner extends RobolectricTestRunner {
	public SuperTestRunner(Class testClass) throws InitializationError {
		super(testClass, "../");
	}
	
	@Override public void beforeTest(Method method) {
        Robolectric.bindShadowClass(ShadowListActivity.class);
    }
}
