package com.guille.gestures;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class EvaluadorMovimientosEnPantalla extends SimpleOnGestureListener {
//	Context contexto;
	private static final int SWIPE_MIN_DISTANCE = 120;  
	private static final int SWIPE_MINIMO_MOVIMIENTO = 1000;  
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	private GestureDetector miDetectorDeGestos; 
	
	private boolean swDeslizaHaciaIzquierda=false;
	private boolean swDeslizaHaciaDerecha=false;
	private boolean swEmpiezaIzquierdaAfuera=false;
	private boolean swDeslizaHaciaAbajo=false;
	private boolean swDeslizaHaciaArriba=false;
	private boolean swMovimientoChiquito=false;
	
	public EvaluadorMovimientosEnPantalla(){
		miDetectorDeGestos = new GestureDetector(this);
	}

	public boolean isMovimientoChiquito() {
		return swMovimientoChiquito;
	}

	private void setSwMovimientoChiquito(boolean swMovimientoChiquito) {
		this.swMovimientoChiquito = swMovimientoChiquito;
	}

	public boolean isDeslizandoHaciaIzquierda() {
		return swDeslizaHaciaIzquierda;
	}

	private void setSwDeslizaHaciaIzquierda(boolean swDeslizaHaciaIzquierda) {
		this.swDeslizaHaciaIzquierda = swDeslizaHaciaIzquierda;
	}

	public boolean isDeslizandoHaciaDerecha() {
		return swDeslizaHaciaDerecha;
	}

	private void setSwDeslizaHaciaDerecha(boolean swDeslizaHaciaDerecha) {
		this.swDeslizaHaciaDerecha = swDeslizaHaciaDerecha;
	}

	public boolean isEmpezandoIzquierdaAfuera() {
		return swEmpiezaIzquierdaAfuera;
	}

	private void setSwEmpiezaIzquierdaAfuera(boolean swEmpiezaIzquierdaAfuera) {
		this.swEmpiezaIzquierdaAfuera = swEmpiezaIzquierdaAfuera;
	}

	public boolean isDeslizandoHaciaAbajo() {
		return swDeslizaHaciaAbajo;
	}

	private void setSwDeslizaHaciaAbajo(boolean swDeslizaHaciaAbajo) {
		this.swDeslizaHaciaAbajo = swDeslizaHaciaAbajo;
	}

	public boolean isDeslizandoHaciaArriba() {
		return swDeslizaHaciaArriba;
	}

	private void setSwDeslizaHaciaArriba(boolean swDeslizaHaciaArriba) {
		this.swDeslizaHaciaArriba = swDeslizaHaciaArriba;
	}
	
	public boolean ingresarMovimiento(MotionEvent evento){
		return miDetectorDeGestos.onTouchEvent(evento);
	}

	@Override  
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		try {  

			//-------se evalúa dónde empieza el delizamiento y en qué dirección va --------
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MINIMO_MOVIMIENTO || e1.getX() - e2.getX() > SWIPE_MINIMO_MOVIMIENTO) {
				setSwMovimientoChiquito(true);
				return false; 
			}

			if (e1.getX() < 10.0)
				setSwEmpiezaIzquierdaAfuera(true);
			else
				setSwEmpiezaIzquierdaAfuera(false);

			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE //deslizar el dedo horizontal a la izquierda 
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				setSwDeslizaHaciaIzquierda(true);
				setSwDeslizaHaciaDerecha(false);
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE //deslizar el dedo horizontal a la derecha  
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				setSwDeslizaHaciaIzquierda(false);
				setSwDeslizaHaciaDerecha(true);
			}
			
			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE //deslizar el dedo vertical hacia arriba 
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				setSwDeslizaHaciaArriba(true);
				setSwDeslizaHaciaAbajo(false);
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE //deslizar el dedo vertical hacia abajo  
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				setSwDeslizaHaciaArriba(false);
				setSwDeslizaHaciaAbajo(true);
			}

		} catch (Exception e) {  
			Log.e("EvaluadorMovimientosEnPantalla", "Error en onFling mensaje: " + e.getMessage());  
		}  
		return false;
	}
}

