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


public class PinchableImageFragment  extends Fragment {
	
	private RemoteImageView riw;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View toret = inflater.inflate(R.layout.pinchable_fragment, container);
		riw = (RemoteImageView) toret.findViewById(R.id.img);
		return toret;
	}
	
	public void setTarget(Uri object) {
		riw.setImageURI(object);
		riw.netReload();
		Pinch.makePinchable(riw);
	}
}
