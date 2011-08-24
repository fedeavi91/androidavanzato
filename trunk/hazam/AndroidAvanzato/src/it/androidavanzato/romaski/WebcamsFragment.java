package it.androidavanzato.romaski;

import it.androidavanzato.R;
import it.androidavanzato.romaski.model.Resort;
import it.androidavanzato.romaski.model.WebcamImagesAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class WebcamsFragment extends Fragment implements OnItemClickListener {
	private AdapterView<BaseAdapter> mAdapterView;
	private WebcamImagesAdapter mAdapter;

	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toret = inflater.inflate(R.layout.webcams_list, container, true);
		mAdapterView = (AdapterView<BaseAdapter>) toret
				.findViewById(R.id.webcams);
		mAdapter = new WebcamImagesAdapter(getActivity(),
				Resort.CAMPO_FELICE.getId());
		mAdapterView.setAdapter(mAdapter);
		mAdapterView.setOnItemClickListener(this);
		return toret;
	}

	public void setAdapter(WebcamImagesAdapter adapter) {
		mAdapter = adapter;
		mAdapterView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View parent, int pos,
			long id) {
		FragmentManager fm = getActivity().getFragmentManager();
		PinchableImageFragment detailFrag = (PinchableImageFragment) fm
				.findFragmentById(R.id.detail);
		detailFrag.setTarget((Uri) mAdapter.getItem(pos));
	}
}
