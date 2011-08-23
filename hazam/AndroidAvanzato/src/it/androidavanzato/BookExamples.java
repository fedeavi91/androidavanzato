package it.androidavanzato;


import it.androidavanzato.romaski.RomaSkiHoneycombActivity;
import it.androidavanzato.ui.BitmapReflection;
import it.androidavanzato.ui.BlinkingText;
import it.androidavanzato.ui.ColorFilters;
import it.androidavanzato.ui.KnobCustomViewActivity;
import it.androidavanzato.ui.LongTask;
import it.androidavanzato.ui.MessengerPipe;
import it.androidavanzato.ui.NiceButton;
import it.androidavanzato.ui.Rotate;
import it.androidavanzato.ui.Task1;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Semplice Activity che realizza una lista che funga da menu principale
 * per accedere ad una serei di Activity.
 * In questo caso, ad ognuna corrisponde un esempio relativo a qualche
 * capitolo del libro AndroidAvanzato 
 * 
 * @author Emanuele Di Saverio - emanuele.disaverio at gmail.com
 *
 */
public class BookExamples extends ListActivity {
	private ClassListAdapter mAdapter;
	
	private final Class<?>[] activities = new Class[] { 
			NiceButton.class,
			BitmapReflection.class,
			ColorFilters.class,
			Rotate.class,
			Task1.class,
			LongTask.class,
			KnobCustomViewActivity.class,
			MessengerPipe.class,
			BlinkingText.class,
			RomaSkiHoneycombActivity.class
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ClassListAdapter(activities);
		setListAdapter(mAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Class<?> tolaunch = (Class<?>) getListAdapter().getItem(position);
		Intent i = new Intent(this, tolaunch);
		startActivity(i);
	}
}

class ClassListAdapter extends BaseAdapter {
	private Class<?>[] activities;
	
	public ClassListAdapter(Class<?>[] _activities) {
		activities = _activities;
	}
	
	@Override
	public int getCount() {
		return activities.length;
	}

	@Override
	public Object getItem(int position) {
		return activities[position];
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	private TextView createTextView(Context ctx) {
		int padding = (int) (10 * ctx.getResources().getDisplayMetrics().density);
		TextView toret = new TextView(ctx);
		toret.setPadding(padding, padding, padding, padding);
		toret.setTextSize(16);
		LayoutParams lp = new ListView.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		toret.setLayoutParams(lp);
		return toret;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView toret;
		if (convertView == null) {
			toret = createTextView(parent.getContext());
		} else {
			toret = (TextView) convertView;
		}
		Class<?> item = (Class<?>) getItem(position);
		toret.setText(item.getSimpleName());
		return toret;
	}
}