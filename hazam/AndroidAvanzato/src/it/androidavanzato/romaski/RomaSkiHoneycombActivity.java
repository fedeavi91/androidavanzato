package it.androidavanzato.romaski;

import it.androidavanzato.R;
import it.androidavanzato.romaski.model.Resort;
import it.androidavanzato.romaski.model.WebcamImagesAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RomaSkiHoneycombActivity extends Activity implements
		ActionBar.TabListener {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.home);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("HC Romaski");
		actionBar.setSubtitle("App esempio a scopi didattici");
		for (Resort r : Resort.ALL.values()) {
			actionBar.addTab(actionBar.newTab().setTag(r.getId())
					.setIcon(r.getDrawableId())
					.setTabListener(this));
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		System.out.println("Reselected " + tab);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		System.out.println("Selected " + tab.getTag());
		String tag = (String) tab.getTag();
		WebcamImagesAdapter adapter = new WebcamImagesAdapter(this, tag);
		FragmentManager fm = getFragmentManager();
		WebcamsFragment wf = (WebcamsFragment) fm
				.findFragmentById(R.id.webcams);
		wf.setAdapter(adapter);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		System.out.println("Unseselected " + tab);
	}

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
			detailFrag.forceRelead();
			return true;
		} else if (chosenItemId == R.id.about) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("RomaSki per Honeycomb")
					.setMessage("Esempio a corredo del libro AndroidAvanzato")
					.setPositiveButton("OK", null).show();
		}
		return super.onOptionsItemSelected(item);
	}
}
