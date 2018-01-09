package com.qin.gao.followme;

import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class MyOnGestureListener
		implements GestureDetector.OnGestureListener{
	@Override
	public boolean onSingleTapUp(MotionEvent me) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent me) {
	}

	@Override
	public boolean onDown(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent) {
	}

	@Override
	public boolean onScroll(MotionEvent mE, MotionEvent mE1, float v, float v1) {
		return false;
	}
}
