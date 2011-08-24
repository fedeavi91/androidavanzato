package it.androidavanzato.ui;

import it.androidavanzato.R;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AnimatorHoneycombActivity extends Activity {
	private TextView mTextContrast;
	private View decor;
	private int bkgColor = Color.BLUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		decor = getWindow().getDecorView();	
		decor.setBackgroundColor(bkgColor);
		setContentView(R.layout.freeform_animations);
		
		mTextContrast = (TextView) findViewById(R.id.text_contrast);
		mTextContrast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTextContrast.animate().rotationBy(90).xBy(30f);
			}
		});
	}
	
	private final ArrayList<ValueAnimator> anims = new ArrayList<ValueAnimator>();
	
	@Override
	protected void onResume() {
		super.onResume();
		anims.clear();
		ValueAnimator va = ValueAnimator.ofFloat(0f, 10f);
		va.setRepeatCount(ValueAnimator.INFINITE);
		va.setRepeatMode(ValueAnimator.REVERSE);
		va.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (Float)animation.getAnimatedValue();
				mTextContrast.setShadowLayer(5.0f, value,value, Color.BLUE);
			}
		});
		va.start();
		anims.add(va);
		
		
		ObjectAnimator objAnim = ObjectAnimator.ofInt(this, "redComp", 20 , 255);
		objAnim.setRepeatMode(ObjectAnimator.REVERSE);
		objAnim.setRepeatCount(ObjectAnimator.INFINITE);
		objAnim.start();
		anims.add(objAnim);
	}
	
	public void setRedComp(int value) {
		int color = value << 16;
		bkgColor = bkgColor & 0xFF00FFFF;
		bkgColor = bkgColor | color;
		System.out.println("bkgColor "+Integer.toHexString(bkgColor));
		decor.setBackgroundColor(bkgColor);
	}
	
	@Override
	protected void onPause() {
		for(ValueAnimator va : anims) {
			if (va.isRunning()) {
				va.end();
			}
		}
		anims.clear();
		super.onPause();
	}
	
	public void relayout(View v) {
		decor.requestLayout();
		mTextContrast.setTranslationX(0.0f);
	}
}
