package it.androidavanzato;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SuperTestRunner.class)
public class BookExamplesTest {
	private BookExamples activity;
	@Before
    public void setUp() throws Exception {
        activity = new BookExamples();
        activity.onCreate(null);
    }
	
	@Test 
	public void thereAreActivities() {
        assertThat(activity.getListAdapter().getCount(), greaterThan(3) );
	}
}
