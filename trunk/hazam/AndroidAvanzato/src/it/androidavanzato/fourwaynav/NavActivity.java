/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.androidavanzato.fourwaynav;

import it.androidavanzato.R;
import it.androidavanzato.fourwaynav.FourWayNavView.Roll;
import it.androidavanzato.view.SwipeDetector;
import it.androidavanzato.view.SwipeDetector.Listener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NavActivity extends Activity {

	private FourWayNavView mView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.fourway);
		mView = (FourWayNavView) findViewById(R.id.fourway);
		mView.setOnTouchListener(new SwipeDetector(new Listener() {

			@Override
			public void onSwipeUp() {
				mView.roll(Roll.DOWN);
				Toast.makeText(NavActivity.this, "onSwipeUp",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSwipeRight() {
				mView.roll(Roll.LEFT);
				Toast.makeText(NavActivity.this, "onSwipeRight",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSwipeLeft() {
				mView.roll(Roll.RIGHT);
				Toast.makeText(NavActivity.this, "onSwipeLeft",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSwipeDown() {
				mView.roll(Roll.UP);
				Toast.makeText(NavActivity.this, "onSwipeDown",
						Toast.LENGTH_SHORT).show();
			}
		}));
	}

	// Ideally an app should implement onResume() and onPause()
	// to take appropriate action when the activity loses focus
	@Override
	protected void onResume() {
		super.onResume();
		mView.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mView.pause();
	}
}
