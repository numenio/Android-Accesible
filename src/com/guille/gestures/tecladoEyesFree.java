package com.guille.gestures;

import com.guille.eyesFree.GestureListener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * A transparent overlay which catches all touch events and uses a call back to
 * return the gesture that the user performed.
 * 
 * @author clchen@google.com (Charles L. Chen)
 * @author  Nolan Darilek
 */

public class tecladoEyesFree extends View {

	/**
	 * An enumeration of the possible gestures.
	 */
	public enum Gesture {
		ARRIBA_IZQUIERDA, ARRIBA, ARRIBA_DERECHA, IZQUIERDA, CENTRO, DERECHA, ABAJO_DERECHA, ABAJO, ABAJO_IZQUIERDA, ARRIBA_LEJOS, ABAJO_LEJOS, IZQUIERDA_LEJOS, 
		DERECHA_LEJOS, ARRIBA_DERECHA_LEJOS, ARRIBA_IZQUIERDA_LEJOS, ABAJO_DERECHA_LEJOS, ABAJO_IZQUIERDA_LEJOS
	}

	private final double left = 0;
	private final double upleft = Math.PI * .25;
	private final double up = Math.PI * .5;
	private final double upright = Math.PI * .75;
	private final double downright = -Math.PI * .75;
	private final double down = -Math.PI * .5;
	private final double downleft = -Math.PI * .25;
	private final double right = Math.PI;
	private final double rightWrap = -Math.PI;
	//		/** Scaled edge tolerance in pixels. Used for edge commands like delete. */
	private final int mEdgeTolerance;
	//
	//  /** Scaled radius tolerance in pixels. Used for outer commands like star. */
	private final int mRadiusTolerance;

	/**
	 * Edge touch tolerance in inches. Used for edge-based commands like delete.
	 */
	private static final double EDGE_TOLERANCE_INCHES = 0.25;

	/**
	 * Radius tolerance in inches. Used for calculating distance from center.
	 */
	private static final double RADIUS_TOLERANCE_INCHES = 0.15;

	private GestureListener cb = null;
	private double downX;
	private double downY;
	private Gesture currentGesture;
	
	

	public tecladoEyesFree(Context context, GestureListener callback) {
		super(context);
		cb = callback;
		mEdgeTolerance = (int) (EDGE_TOLERANCE_INCHES
				* getResources().getDisplayMetrics().densityDpi);
		mRadiusTolerance = (int) (RADIUS_TOLERANCE_INCHES
				* getResources().getDisplayMetrics().densityDpi);
	}

	public tecladoEyesFree(Context context) {
		super(context);
		mEdgeTolerance = (int) (EDGE_TOLERANCE_INCHES
				* getResources().getDisplayMetrics().densityDpi);
		mRadiusTolerance = (int) (RADIUS_TOLERANCE_INCHES
				* getResources().getDisplayMetrics().densityDpi);
	}

	public void setGestureListener(GestureListener callback) {
		cb = callback;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		Gesture prevGesture = null;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			downX = x;
			downY = y;
			currentGesture = Gesture.CENTRO;
			if (cb != null) {
				cb.onGestureStart(currentGesture);
			}
			break;
		case MotionEvent.ACTION_UP:
			prevGesture = currentGesture;
			currentGesture = evalMotion(x, y);
			// Do some correction if the user lifts up on deadspace
			if (currentGesture == null) {
				currentGesture = prevGesture;
			}
			if (cb != null) {
				cb.onGestureFinish(currentGesture);
			}
			break;
		default:
			prevGesture = currentGesture;
			currentGesture = evalMotion(x, y);
			// Do some correction if the user lifts up on deadspace
			if (currentGesture == null) {
				currentGesture = prevGesture;
				break;
			}
			if (prevGesture != currentGesture) {
				if (cb != null) {
					cb.onGestureChange(currentGesture);
				}
			}
			break;
		}
		return true;
	}

	public Gesture evalMotion(double x, double y) {
		boolean movedFar = false;
		boolean nearEdge = false;

		float rTolerance = mRadiusTolerance;//25;
		double thetaTolerance = (Math.PI / 12);

		double r = Math.sqrt(((downX - x) * (downX - x)) + ((downY - y) * (downY - y)));


		if (r < rTolerance) {
			return Gesture.CENTRO;
		}

		if (r > 6 * rTolerance) {
			movedFar = true;
		}

		if (x < mEdgeTolerance || x > (getWidth() - mEdgeTolerance) || y < mEdgeTolerance
				|| y > (getHeight() - mEdgeTolerance)) {
			nearEdge = true;
		}

		double theta = Math.atan2(downY - y, downX - x);
		if (Math.abs(theta - left) < thetaTolerance) {
			return movedFar ? Gesture.IZQUIERDA_LEJOS : Gesture.IZQUIERDA;
		} else if (Math.abs(theta - upleft) < thetaTolerance) {
			return movedFar ? Gesture.ARRIBA_IZQUIERDA_LEJOS : Gesture.ARRIBA_IZQUIERDA;
		} else if (Math.abs(theta - up) < thetaTolerance) {
			return movedFar ? Gesture.ARRIBA_LEJOS : Gesture.ARRIBA;
		} else if (Math.abs(theta - upright) < thetaTolerance) {
			return movedFar ? Gesture.ARRIBA_DERECHA_LEJOS : Gesture.ARRIBA_DERECHA;
		} else if (Math.abs(theta - downright) < thetaTolerance) {
			return movedFar ? Gesture.ABAJO_IZQUIERDA_LEJOS : Gesture.ABAJO_IZQUIERDA;
		} else if (Math.abs(theta - down) < thetaTolerance) {
			return movedFar ? Gesture.ABAJO_LEJOS : Gesture.ABAJO;
		} else if (Math.abs(theta - downleft) < thetaTolerance) {
			return movedFar ? Gesture.ABAJO_DERECHA_LEJOS : Gesture.ABAJO_DERECHA;
		} else if ((theta > right - thetaTolerance) || (theta < rightWrap + thetaTolerance)) {
			return movedFar ? Gesture.DERECHA_LEJOS : Gesture.DERECHA;
		}

		// Off by more than the threshold, so it doesn't count
		return null;
	}

}
