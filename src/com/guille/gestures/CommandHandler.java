package com.guille.gestures;

import android.app.Activity;
//import android.gesture.Gesture;
import android.os.Bundle;
import com.guille.gestures.tecladoEyesFree.GestureListener;
import com.guille.gestures.tecladoEyesFree.Gesture;

class CommandHandler extends Activity implements GestureListener {

	@Override
	public void  onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(new tecladoEyesFree(this, this));

	}

	private void upLeft() {
	}

	private void up() {
	}

	private void upRight() {
	}

	private void left() {
	}

	private void center() {
	}

	private void right() {
	}

	private void downLeft() {
	}

	private void down() {
	}

	private void downRight() {
	}

	public void onGestureStart(com.guille.gestures.tecladoEyesFree.Gesture g) {
		= g match 
	case _ =>
	}

	public void onGestureChange(com.guille.gestures.tecladoEyesFree.Gesture g) {
		= g match 
	case _ =>

	}

	public void onGestureFinish(com.guille.gestures.tecladoEyesFree.Gesture g) {
		switch (g)  {
		case Gesture.UPLEFT: upLeft();
		case Gesture.UP => up()
		case Gesture.UPRIGHT => upRight()
		case Gesture.LEFT => left()
		case Gesture.CENTER => center()
		case Gesture.RIGHT => right()
		case Gesture.DOWNLEFT => downLeft()
		case Gesture.DOWN => down()
		case Gesture.DOWNRIGHT => downRight()
		}
	}

	//	  override def onKeyDown(keyCode:Int, event:KeyEvent) = {
	//	    keyCode match {
	//	      case KeyEvent.KEYCODE_DPAD_UP => up()
	//	      case KeyEvent.KEYCODE_DPAD_DOWN => down()
	//	      case KeyEvent.KEYCODE_DPAD_LEFT => left()
	//	      case KeyEvent.KEYCODE_DPAD_RIGHT => right()
	//	      case _ =>
	//	    }
	//	    super.onKeyDown(keyCode, event)
	//	  }


}