package it.androidavanzato.romaski;

import it.androidavanzato.R;
import it.androidavanzato.romaski.model.Resort;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentBreadCrumbs;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RomaSkiHoneycombActivity extends Activity implements
		ActionBar.TabListener, OnBackStackChangedListener {

	private boolean mFromMachine = true;
	private final HashMap<String, Tab> mTagTagMap = new HashMap<String, Tab>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.home);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("HC Romaski");
		actionBar.setSubtitle("Applicazione d'esempio");

		mFromMachine = true;
		for (Resort r : Resort.ALL.values()) {
			Tab toadd = actionBar.newTab().setTag(r.getId())
					.setIcon(r.getDrawableId()).setTabListener(this);
			actionBar.addTab(toadd);
			mTagTagMap.put(r.getId(), toadd);
		}
		mFromMachine = false;
		
		switchToTab(Resort.CAMPO_FELICE.getId(), getFragmentManager()
				.beginTransaction(), true);

		FragmentBreadCrumbs fbc = (FragmentBreadCrumbs) findViewById(R.id.bread_crumbs);
		fbc.setActivity(this);

		getFragmentManager().addOnBackStackChangedListener(this);
	}

	private void switchToTab(String tag, FragmentTransaction ft, boolean back) {
		WebcamsFragment newWebFragment = new WebcamsFragment(tag);
		ft = getFragmentManager().beginTransaction();
		ft.setBreadCrumbTitle(tag);
		ft.replace(R.id.container, newWebFragment, tag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if (back) {
			ft.addToBackStack(null);
		}
		ft.commit();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (!mFromMachine) {
			String tag = (String) tab.getTag();
			switchToTab(tag, ft, true);
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int chosenItemId = item.getItemId();
		if (chosenItemId == R.id.reload) {
			FragmentManager fm = getFragmentManager();
			PinchableImageFragment detailFrag = (PinchableImageFragment) fm
					.findFragmentById(R.id.detail);
			detailFrag.forceReload();
			return true;
		} else if (chosenItemId == R.id.about) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("RomaSki per Honeycomb")
					.setMessage("Esempio a corredo del libro AndroidAvanzato")
					.setPositiveButton("OK", null).show();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackStackChanged() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			int lastPosition = getFragmentManager().getBackStackEntryCount() - 1;
			String tag = ""
					+ getFragmentManager().getBackStackEntryAt(lastPosition)
							.getBreadCrumbTitle();
			mFromMachine = true;
			getActionBar().selectTab(mTagTagMap.get(tag));
			mFromMachine = false;
		} else {
			finish();
		}
	}
}
