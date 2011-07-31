package it.androidavanzato.ui;

import it.androidavanzato.R;
import it.androidavanzato.util.RotateAnimation;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class Rotate extends Activity {
	private RotateAnimation anim = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate);
        anim = new RotateAnimation(findViewById(R.id.elo), findViewById(R.id.back));
    }
    
	public void rotate(View v) {
		v.startAnimation(anim);
	}
}