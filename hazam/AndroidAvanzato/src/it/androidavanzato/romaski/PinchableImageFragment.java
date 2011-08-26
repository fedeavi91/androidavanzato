package it.androidavanzato.romaski;

import it.androidavanzato.R;
import it.androidavanzato.romaski.widget.Pinch;
import it.androidavanzato.romaski.widget.RemoteImageView;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PinchableImageFragment extends Fragment {

	private RemoteImageView mRemoteImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//riceviamo la URI come parametro del fragment
		Uri targetUri = (Uri) getArguments().get("uri");
		View toret = inflater.inflate(R.layout.pinchable_fragment, container,
				false);
		mRemoteImageView = (RemoteImageView) toret.findViewById(R.id.img);
		mRemoteImageView.setImageURI(targetUri);
		mRemoteImageView.netReload();
		Pinch.makePinchable(mRemoteImageView);
		return toret;
	}

	public void forceReload() {
		if (mRemoteImageView != null) {
			mRemoteImageView.netReload();
		}
	}
}
