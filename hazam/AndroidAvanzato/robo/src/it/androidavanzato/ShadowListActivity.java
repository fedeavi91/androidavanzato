package it.androidavanzato;

import android.app.ListActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

@Implements(ListActivity.class)
public class ShadowListActivity extends ShadowActivity {
	
    private ListAdapter mAdapter;
    @Implementation
    public void setListAdapter(ListAdapter a) {
    	mAdapter = a;
    }
    
    @Implementation
    public ListAdapter getListAdapter() {
    	return mAdapter;
    }
}
