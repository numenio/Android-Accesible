package com.guille.gestures;

import com.guille.gestures.Enums.Gesture;


/**
 * The callback interface to be used when a gesture is detected.
 */
public interface IGestureListener {
	public void onGestureStart(Gesture g);

	public void onGestureChange(Gesture g);

	public void onGestureFinish(Gesture g);
}