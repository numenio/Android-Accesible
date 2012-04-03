package com.guille.gestures;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.guille.gestures.Enums.Gesture;
import com.guille.gestures.Enums.Tecla;

public class AdaptadorTeclado implements IGestureListener, ITecladoListener {
	//private IGestureListener gestures = null;
	private ITecladoListener teclado = null; //para recibirlo de SMSEditor
	private Vibrator vibe;
	private static final long[] VIBE_PATTERN = {0, 70};
	
	public AdaptadorTeclado (Context context, ITecladoListener teclas) {
		teclado = teclas;
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	//------------- métodos de la interfaz onTecladoListener ----------------
	public void onTeclaApretada(Tecla t) {
		teclado.onTeclaApretada(t);
	}

	public void onTeclaMantienePresionada(Tecla t) {
		teclado.onTeclaMantienePresionada(t);
	}

	public void onTeclaLiberada(Tecla t) {
		teclado.onTeclaLiberada(t);
	}
	//--------------- fin métodos onTecladoListener --------------------------
	
	
	//-------------- métodos interfaz onGestureListener -----------------------
	public void onGestureStart(Gesture g) {
		vibe.vibrate(VIBE_PATTERN, -1);
	}

	public void onGestureChange(Gesture g) {
		vibe.vibrate(VIBE_PATTERN, -1);
	}

	public void onGestureFinish(Gesture g) {
		Tecla miTecla;
		
		switch (g)  {
		case ARRIBA_IZQUIERDA: 
			arrIzq();
			break;
		case ARRIBA:
			arriba();
			break;
		case ARRIBA_DERECHA:
			arrDer();
			break;
		case IZQUIERDA:
			izquierda();
			break;
		case CENTRO:
			centro();
			break;
		case DERECHA:
			derecha();
			break;
		case ABAJO_DERECHA:
			abDer();
			break;
		case ABAJO:
			abajo();
			break;
		case ABAJO_IZQUIERDA:
			abIzq();
			break;
		case ARRIBA_IZQUIERDA_LEJOS: 
			arrIzqLejos();
			break;
		case ARRIBA_LEJOS:
			arrLejos();
			break;
		case ARRIBA_DERECHA_LEJOS:
			arrDerLejos();
			break;
		case IZQUIERDA_LEJOS:
			izqLejos();
			break;
		case DERECHA_LEJOS:
			derLejos();
			break;
		case ABAJO_DERECHA_LEJOS:
			abDerLejos();
			break;
		case ABAJO_LEJOS:
			abLejos();
			break;
		case ABAJO_IZQUIERDA_LEJOS:
			abIzqLejos();
			break;
		}	
	}
	//-------------- fin métodos interfaz onGestureListener ------------------
	

	private void abIzqLejos() {
		// TODO Auto-generated method stub
		
	}

	private void abLejos() {
		// TODO Auto-generated method stub
		
	}

	private void abDerLejos() {
		// TODO Auto-generated method stub
		
	}

	private void derLejos() {
		// TODO Auto-generated method stub
		
	}

	private void izqLejos() {
		// TODO Auto-generated method stub
		
	}

	private void arrDerLejos() {
		// TODO Auto-generated method stub
		
	}

	private void arrLejos() {
		// TODO Auto-generated method stub
		
	}

	private void arrIzqLejos() {
		// TODO Auto-generated method stub
		
	}

	private void abIzq() {
		// TODO Auto-generated method stub
		
	}

	private void abajo() {
		// TODO Auto-generated method stub
		
	}

	private void abDer() {
		// TODO Auto-generated method stub
		
	}

	private void derecha() {
		// TODO Auto-generated method stub
		
	}

	private void centro() {
		// TODO Auto-generated method stub
		
	}

	private void izquierda() {
		// TODO Auto-generated method stub
		
	}

	private void arrDer() {
		// TODO Auto-generated method stub
		
	}

	private void arriba() {
		// TODO Auto-generated method stub
		
	}

	private void arrIzq() {
		// TODO Auto-generated method stub
		
	}
}
