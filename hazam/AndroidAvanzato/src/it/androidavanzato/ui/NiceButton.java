package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;


public class NiceButton extends Activity {
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nicebutton);
        final Button tv= (Button) findViewById(R.id.button); 
        Typeface face=Typeface.createFromAsset(getAssets(), "fonts/HandmadeTypewriter.ttf");          
        tv.setTypeface(face); 
        
        
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LayerDrawable ld = (LayerDrawable)tv.getBackground();
                ld.setLayerInset(1, 0, 0, 0, tv.getHeight() / 2);
            }
        });
    }
}