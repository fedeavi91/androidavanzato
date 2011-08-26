package it.androidavanzato.romaski;

import it.androidavanzato.R;
import it.androidavanzato.romaski.model.Resort;
import it.androidavanzato.romaski.model.WebcamImagesAdapter;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class WebcamsFragment extends ListFragment {

	private WebcamImagesAdapter mAdapter;

	private String mResortId = Resort.CAMPO_FELICE.getId();

	public WebcamsFragment() {
	}

	public WebcamsFragment(String resortId) {
		mResortId = resortId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//molto importante avere l'attach to root a false!
		View toret = inflater.inflate(R.layout.webcams_list, container, false);
		mAdapter = new WebcamImagesAdapter(getActivity(), mResortId);
		setListAdapter(mAdapter);
		return toret;
	}

	@Override
	public void onListItemClick(ListView adapterView, View parent, int pos,
			long id) {
		FragmentManager fm = getActivity().getFragmentManager();
		
		FragmentTransaction ft = fm.beginTransaction();
		PinchableImageFragment newWebFragment = new PinchableImageFragment();
		Uri targetUri =(Uri) mAdapter.getItem(pos);
		Bundle arg = new Bundle();
		arg.putParcelable("uri", targetUri);
		newWebFragment.setArguments(arg);
		ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.detail_container, newWebFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
}
