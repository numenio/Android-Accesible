package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;

import com.guille.gestures.Enums.Tecla;
import com.guille.gestures.Enums.TipoTecla;
//import com.guille.gestures.Enums.TeclaComando;
//import com.guille.gestures.Enums.TeclaLetra;
//import com.guille.gestures.Enums.TeclaN�mero;
import com.guille.gestures.IGestureListener;
import com.guille.gestures.Enums.Gesture;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * A transparent overlay which catches all touch events and uses a call back to
 * return the gesture that the user performed.
 * 
 * @author clchen@google.com (Charles L. Chen)
 * @author  Nolan Darilek
 * 
 */

public class TecladoEyesFree extends View implements IGestureListener, ITeclado{
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

	//private IGestureListener cb = null;
	private ITecladoListener cbTeclado = null;
	private double downX;
	private double downY;
	private Gesture currentGesture;
	private TecladoEnC�rculos teclado = null;
	private Context contexto = null;
	private Vibrator vibe;
	private boolean swUs�AtajoTeclado;
//	private EvaluadorMovimientosEnPantalla miEvaluadorDeMovimientos;
	private static final long[] VIBE_PATTERN_START = {0, 70};
	private static final long[] VIBE_PATTERN_CHANGE = {0, 30};
	private static final long[] VIBE_PATTERN_FINISH = {0, 100};
	//	private boolean swDobleTeclado = true;

	//	public TecladoEyesFree(Context context, IGestureListener callback) {
	//		super(context);
	//		cb = callback;
	//		mEdgeTolerance = (int) (EDGE_TOLERANCE_INCHES
	//				* getResources().getDisplayMetrics().densityDpi);
	//		mRadiusTolerance = (int) (RADIUS_TOLERANCE_INCHES
	//				* getResources().getDisplayMetrics().densityDpi);
	//	}

	public TecladoEyesFree(Context context, ITecladoListener callback, boolean swTecladoDoble) {
		super(context);
		cbTeclado = callback;
		contexto = context;
		teclado = new TecladoEnC�rculos(swTecladoDoble);
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		setKeepScreenOn(true); //se evita que la pantalla se bloquee
//		miEvaluadorDeMovimientos = new EvaluadorMovimientosEnPantalla();
		mEdgeTolerance = (int) (EDGE_TOLERANCE_INCHES
				* getResources().getDisplayMetrics().densityDpi);
		mRadiusTolerance = (int) (RADIUS_TOLERANCE_INCHES
				* getResources().getDisplayMetrics().densityDpi);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (evalAtajoTeclado(event, true)){ //si hay un atajo, como enviar o borrar, se lanza el atajo y se deja de evaluar el evento
			swUs�AtajoTeclado = true;
			return true;
		}
		
//		miEvaluadorDeMovimientos.ingresarMovimiento(event);
//		if (miEvaluadorDeMovimientos.isEmpezandoIzquierdaAfuera()){
//			hablar("empez� desde la izquierda afuera");
//			return true;
//		}
//		
//		if (miEvaluadorDeMovimientos.isMovimientoChiquito()) return true; //si el mov es muy peque�o, no se hace nada

		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		Gesture prevGesture = null;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			downX = x;
			downY = y;
			currentGesture = Gesture.CENTRO;
			//			if (cb != null) {
			//				cb.onGestureStart(currentGesture);
			//			}
			//			pasarGestoATecla(currentGesture);
			onGestureStart(currentGesture);
			break;
		case MotionEvent.ACTION_UP:
			if (!swUs�AtajoTeclado){
				prevGesture = currentGesture;
				currentGesture = evalMotion(x, y);
				// Do some correction if the user lifts up on deadspace
				if (currentGesture == null) {
					currentGesture = prevGesture;
				}
				//			if (cb != null) {
				//				cb.onGestureFinish(currentGesture);
				//			}
				//			pasarGestoATecla(currentGesture);
				onGestureFinish(currentGesture);
			}else{
				swUs�AtajoTeclado=false;
			}
			break;
		default:
			if (!swUs�AtajoTeclado){
				prevGesture = currentGesture;
				currentGesture = evalMotion(x, y);
				// Do some correction if the user lifts up on deadspace
				if (currentGesture == null) {
					currentGesture = prevGesture;
					break;
				}
				if (prevGesture != currentGesture) {
					//if (cb != null) {
					//cb.onGestureChange(currentGesture);
					//}

					//pasarGestoATecla(currentGesture);
					onGestureChange(currentGesture);
				}
			}else{
				swUs�AtajoTeclado=false;
			}
			break;
		}

		return true;
	}
	
	private int cantDedosMaxima;
	private boolean swMovimiento;
	private boolean evalAtajoTeclado(MotionEvent event, boolean swEjecutarAtajo){
		int cantDedosActual = 0;
		
		switch (event.getPointerCount()) {
		case 1:
			if (cantDedosMaxima < 1) 
				cantDedosMaxima = 1;

			cantDedosActual = 1;

			if (event.getAction() == event.ACTION_MOVE){
				swMovimiento = true;
			}

			if (event.getAction() == event.ACTION_UP){ //cuando se levanta el �ltimo dedo, cantDedosM�xima tiene el n�mero de cu�ntos dedos se pulsaron
				if (cantDedosMaxima == 1) {}
				cantDedosMaxima = 0;
				swMovimiento = false;
			}

			break;
		case 2:
			if (cantDedosMaxima < 2) cantDedosMaxima = 2;
			cantDedosActual = 2;
			break;
		case 3:
			cantDedosMaxima = 3;
			cantDedosActual = 3;
			break;
		default:
			break;
		}

		if (cantDedosMaxima == 2 && cantDedosActual == 2) {
			if (event.getAction() == event.ACTION_POINTER_2_UP){//si mantiene apretado el dedo uno y suelta el dos: BORRAR
				if (swEjecutarAtajo)
					cbTeclado.onTeclaLiberada(Tecla.BORRAR);
				return true;
			}
		}


		if (cantDedosMaxima == 3 && cantDedosActual == 3) {
			if (event.getAction() == event.ACTION_POINTER_3_UP){//si mantiene apretados dos dedos y suelta el tres: ENVIAR SMS
				if (swEjecutarAtajo)
					cbTeclado.onTeclaLiberada(Tecla.ENVIAR);
				return true;
			}
		}
		
		return false;
	}
	
	public void mostrarS�loN�meros(){
		teclado.mostrarS�loC�rculosN�meros();
	}
	
	public void mostrarLetrasN�meros(){
		teclado.mostrarLetrasN�merosAcentos();
	}
	
	public void mostrarTodasLasTeclas(){
		teclado.mostrarTodosLosTiposDeLetra();
	}

	private Gesture evalMotion(double x, double y) {
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
			nearEdge = true; //TODO chequear cu�ndo si esto registra si un movimiento se va de la pantalla o si viene desde afuera, as� se pueden poner m�s gestures personalizados
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

	public void onGestureStart(Gesture g) {
		if (teclado.isSwTecladoDoble())
			hablar("Desliz� tu dedo en los c�rculos interno y externo para elegir qu� tecla quer�s pulsar. Para cancelar, volv� a este centro");
		else
			hablar("Desliz� tu dedo en c�rculo para elegir qu� tecla quer�s pulsar. Para cancelar, volv� a este centro");
		
		teclado.getC�rculoActual().seleccionarTeclaCentro();
		//vibe.vibrate(VIBE_PATTERN_START, -1);
	}

	public void onGestureChange(Gesture g) { 
		switch (g){
		case ARRIBA:
			seleccionarTeclaEn(0);
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case ARRIBA_DERECHA:
			seleccionarTeclaEn(1);
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case ABAJO_DERECHA:
			seleccionarTeclaEn(4);
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case ABAJO:
			seleccionarTeclaEn(3);
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;	
		case ABAJO_IZQUIERDA:
			seleccionarTeclaEn(2);
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case ARRIBA_IZQUIERDA:
			seleccionarTeclaEn(5);
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;	
		case ARRIBA_LEJOS: //desde aqu� se eval�a si es un teclado doble o simple
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(6);
			} else {
//				onGestureChange(Gesture.ARRIBA);
//				teclado.getC�rculoActual().seleccionarTeclaCentro();
				hablar("Pasar a los n�meros");
			}
			break;	
		case ARRIBA_DERECHA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(7);
			} else {
//				onGestureChange(Gesture.ARRIBA_DERECHA);
//				teclado.getC�rculoActual().seleccionarTeclaCentro();
				hablar("Pasar a las letras");
			}
			break;
		case ABAJO_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(9);
			} else {
				//onGestureChange(Gesture.ABAJO);
//				teclado.getC�rculoActual().seleccionarTeclaCentro(); //TODO para que cambie la selec
				hablar("espacio");
			}
			break;
		case ABAJO_DERECHA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(10);
			} else {
//				onGestureChange(Gesture.ABAJO_DERECHA);
//				teclado.getC�rculoActual().seleccionarTeclaCentro();
				hablar("borrar");
			}
			break;
		case ABAJO_IZQUIERDA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(8);
			} else {
//				onGestureChange(Gesture.ABAJO_IZQUIERDA);
//				teclado.getC�rculoActual().seleccionarTeclaCentro();
				hablar("enviar el mensaje");
			}
			break;
		case ARRIBA_IZQUIERDA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(11);
			} else {
//				onGestureChange(Gesture.ARRIBA_IZQUIERDA);
//				teclado.getC�rculoActual().seleccionarTeclaCentro();
				hablar("Pasar a los s�mbolos");
			}
			break;
		case CENTRO:
			hablar("No hacer nada");
			teclado.getC�rculoActual().seleccionarTeclaCentro();
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case DERECHA:
			hablar("Pasar al c�rculo de la derecha");
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case DERECHA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case IZQUIERDA:
			hablar("Pasar al c�rculo de la izquierda");
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case IZQUIERDA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		}
	}

	private void seleccionarTeclaEn(int lugar) {
		if (teclado.getC�rculoActual().getTeclaActual() != teclado.getC�rculoActual().getTeclaEn(lugar)){//s�lo selecciona si no es la que ya est� seleccionada
			Tecla miTecla = teclado.getC�rculoActual().getTeclaEn(lugar);
			if (miTecla != Tecla.NINGUNA) //si es distinto de ninguna
				hablar(UtilesCadena.traducirTeclaParaLeer(miTecla));
			else {
				String cadena = "Este c�rculo s�lo tiene " + teclado.getC�rculoActual().getCantidadLugaresUsados();
				switch(teclado.getC�rculoActual().getTipoC�rculo()){
				case LETRAS:
					cadena += " letras disponibles. Aqu� no hay letras";
					break;
				case COMANDOS:
					cadena += " comandos disponibles. Aqu� no hay teclas de comando";
					break;
				case LETRAS_ACENTUADAS:
					cadena += " vocales acentuadas disponibles. Aqu� no hay vocal acentuada";
					break;
				case N�MEROS:
					cadena += " n�meros disponibles. Aqu� no hay n�mero";
					break;
				case S�MBOLOS:
					cadena += " s�mbolos disponibles. Aqu� no hay s�mbolo";
					break;
				} 
				hablar(cadena);
			}
		}
	}
	
	public void onGestureFinish(Gesture g) {
		vibe.vibrate(VIBE_PATTERN_FINISH, -1);
		switch (g) {
		case DERECHA:
			teclado.pasarAlSiguienteC�rculo();
			hablar(armarCadenaPasarAlC�rculoSiguiente());
			break;
		case DERECHA_LEJOS:
			cbTeclado.onTeclaLiberada(Tecla.FLECHA_DER);
			break;
		case ABAJO_DERECHA_LEJOS:
			cbTeclado.onTeclaLiberada(Tecla.BORRAR);
			break;
		case ABAJO_LEJOS:
			cbTeclado.onTeclaLiberada(Tecla.ESPACIO);
			break;
		case ABAJO_IZQUIERDA_LEJOS:
			cbTeclado.onTeclaLiberada(Tecla.ENVIAR);
			break;
		case ARRIBA_DERECHA_LEJOS:
			teclado.pasarAlC�rculoDeLetras();
			hablar("Pasando al primer c�rculo de letras que va desde " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getC�rculoActual().getNombre�ltimaTecla());
			break;
		case ARRIBA_LEJOS:
			teclado.pasarAlC�rculoDeN�meros();
			hablar("Pasando al primer c�rculo de n�meros que va desde " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getC�rculoActual().getNombre�ltimaTecla());
			break;
		case ARRIBA_IZQUIERDA_LEJOS:
			teclado.pasarAlC�rculoDeS�mbolos();
			hablar("Pasando al primer c�rculo de s�mbolos que va desde " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getC�rculoActual().getNombre�ltimaTecla());
			break;
		case IZQUIERDA:
			teclado.pasarAlC�rculoAnterior();
			hablar(armarCadenaPasarAlC�rculoAnterior());
			break;
		case IZQUIERDA_LEJOS:
			cbTeclado.onTeclaLiberada(Tecla.FLECHA_IZQ);
			break;
		case CENTRO:
			//hablar("Cancelado");
			break;
		default:	
			Tecla miTecla = teclado.getC�rculoActual().getTeclaActual();
			cbTeclado.onTeclaLiberada(miTecla);
			break;
		}
	}
	
	private String armarCadenaPasarAlC�rculoAnterior(){
		String cadena = "";
		if (!teclado.getC�rculoActual().isPrimerC�rculo()){
			cadena = "Pasando al c�rculo " + armarCadenaSeg�nTipoC�rculoActual() + " que va desde " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getC�rculoActual().getNombre�ltimaTecla();
		} else{
			cadena = "Est�s en el primer c�rculo. El c�rculo empieza en " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			" y termina en " + teclado.getC�rculoActual().getNombre�ltimaTecla();
		}
		
		return cadena;
	}
	
	private String armarCadenaPasarAlC�rculoSiguiente(){
		String cadena = "";
		if (!teclado.getC�rculoActual().is�ltimoC�rculo()){
			cadena = "Pasando al c�rculo " + armarCadenaSeg�nTipoC�rculoActual() + " que va desde " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getC�rculoActual().getNombre�ltimaTecla();
		}else{
			cadena = "Est�s en el �ltimo c�rculo. El c�rculo empieza en " +  teclado.getC�rculoActual().getNombrePrimeraTecla() + 
			" y termina en " + teclado.getC�rculoActual().getNombre�ltimaTecla();
		}
		
		return cadena;
	}
	
	private String armarCadenaSeg�nTipoC�rculoActual(){
		String cadena="";
		switch(teclado.getC�rculoActual().getTipoC�rculo()){
		case LETRAS:
			cadena = " de letras ";
			break;
		case COMANDOS:
			cadena = " de comandos ";
			break;
		case LETRAS_ACENTUADAS:
			cadena = " de vocales acentuadas y di�resis ";
			break;
		case N�MEROS:
			cadena = " de n�meros ";
			break;
		case S�MBOLOS:
			cadena = " de s�mbolos ";
			break;
		}
		return cadena;
	}


//	private Tecla pasarGestoATecla (Gesture gesto){//TODO hacer el m�todo
//		Tecla miTecla = Tecla.NINGUNA;
//		return miTecla; 
//	}
	
	private void hablar (String cadena){
		((MiApp)contexto.getApplicationContext()).hablar(cadena);
	}
	
	class TecladoEnC�rculos {
		class PanelEnC�rculo {
			private int cantidadLugares;
			private int cantidadLugaresUsados;
			private int cantidadLugaresDisponibles;
			private int �ltimoLugarUsado;
			private int primerLugarDisponible;
			private TipoTecla tipoC�rculo;
			private boolean swTieneC�rculoExterno;
			private boolean swPrimerC�rculo=false;
			private boolean sw�ltimoC�rculo=false;
			private List<Tecla> teclas = new ArrayList<Tecla>();
			
			public boolean isPrimerC�rculo() {
				return swPrimerC�rculo;
			}
			private void setSwPrimerC�rculo(boolean swPrimerC�rculo) {
				this.swPrimerC�rculo = swPrimerC�rculo;
			}
			public boolean is�ltimoC�rculo() {
				return sw�ltimoC�rculo;
			}
			private void setSw�ltimoC�rculo(boolean sw�ltimoC�rculo) {
				this.sw�ltimoC�rculo = sw�ltimoC�rculo;
			}
			
			public boolean swTieneC�rculoExterno() {
				return swTieneC�rculoExterno;
			}
			private void setSwTieneC�rculoExterno(boolean swTieneC�rculoExterno) {
				this.swTieneC�rculoExterno = swTieneC�rculoExterno;
			}
			public int getCantidadLugares() {
				return cantidadLugares;
			}
			private void setCantidadLugares(int cant) {
				cantidadLugares = cant;
			}
			private int getCantidadLugaresUsados() {
				return cantidadLugaresUsados;
			}
			private void setCantidadLugaresUsados(int cantidadLugaresUsados) {
				this.cantidadLugaresUsados = cantidadLugaresUsados;
			}
			private int getCantidadLugaresDisponibles() {
				return cantidadLugaresDisponibles;
			}
			private void setCantidadLugaresDisponibles(int cantidadLugaresDisponibles) {
				this.cantidadLugaresDisponibles = cantidadLugaresDisponibles;
			}
			private int get�ltimoLugarUsado() {
				return �ltimoLugarUsado;
			}
			private void set�ltimoLugarUsado(int �ltimoLugarUsado) {
				this.�ltimoLugarUsado = �ltimoLugarUsado;
			}
			private int getPrimerLugarDisponible() {
				return primerLugarDisponible;
			}
			private void setPrimerLugarDisponible(int primerLugarDisponible) {
				this.primerLugarDisponible = primerLugarDisponible;
			}
			public TipoTecla getTipoC�rculo() {
				return tipoC�rculo;
			}
			private void setTipoC�rculo(TipoTecla tipo) {
				this.tipoC�rculo = tipo;
			}
			
			public PanelEnC�rculo(List<Tecla> t, boolean swTecladoDoble){
				if (swTecladoDoble)
					setCantidadLugares(12);
				else
					setCantidadLugares(6);
				
				if (t.size() <= getCantidadLugares()) //si las teclas que se pasan son menos o igual a las que caben en un panel, se las carga directo, sin�, se
					this.teclas = t;				  //cargan s�lo las que quepan
				else
					cargarS�loTeclasSeg�nLugaresM�ximos(t, teclas);
				
				int cantLugaresUsados=0;
				for(int i=0; i<teclas.size(); i++){
					if (teclas.get(i)!=Tecla.NINGUNA)
						cantLugaresUsados++;
				}
				setCantidadLugaresDisponibles(getCantidadLugares() - cantLugaresUsados);
				setCantidadLugaresUsados(cantLugaresUsados);
				setPrimerLugarDisponible(cantLugaresUsados + 1);
				set�ltimoLugarUsado(cantLugaresUsados);
				setCantidadLugares(getCantidadLugares()+1);//sumamos un lugar para que sea la tecla centro
				teclas.add(Tecla.NINGUNA); //tecla centro, ninguna
			}
			
			private void cargarS�loTeclasSeg�nLugaresM�ximos(List<Tecla> listaOrigen, List<Tecla> listaDestino){
				for (int i = 0; i < getCantidadLugares(); i++){
					listaDestino.add(listaOrigen.get(i));
				}
			}
			
			public Tecla getPrimeraTecla(){
				if (teclas.size() > 0)
					return teclas.get(0);
				else
					return Tecla.NINGUNA;
			}
			
			public String getNombrePrimeraTecla(){
				return UtilesCadena.traducirTeclaParaLeer(getPrimeraTecla());
			}
			
			public Tecla get�ltimaTecla(){
				if (teclas.size() > 0){
					for (int i=1; i<teclas.size(); i++){
						if (teclas.get(i) == Tecla.NINGUNA)
							return teclas.get(i-1); //se pasa la tecla anterior a la que est� vac�a
					}
					return teclas.get(teclas.size()-2);//es menos 2 y no menos 1 porque la �ltima es la tecla centro 
				}else
					return Tecla.NINGUNA;
			}
			
			public String getNombre�ltimaTecla(){
					return UtilesCadena.traducirTeclaParaLeer(get�ltimaTecla());
			}
			
			public Tecla getTeclaCentro(){
				if (teclas.size() > 0){
					return teclas.get(teclas.size()-1); 
				}else
					return Tecla.NINGUNA;
			}
			
			private int getLugarTeclaCentro(){
				return teclas.size()-1;
			}
			
			public Tecla seleccionarTeclaCentro(){
				return this.getTeclaEn(getLugarTeclaCentro());
			}
			
			public boolean addTecla(Tecla t){
				if (getCantidadLugaresDisponibles() < 0){
					teclas.add(t);
					return true;
				} else {
					return false;
				}
			}
			
			public List<Tecla> getTodasLasTeclas(){
				return teclas;
			}
			
			public Tecla getTeclaEn(int lugar){
				lugarSelecci�nActual = lugar;
				return teclas.get(lugar);
			}
			
			public Tecla getTeclaActual(){
				return teclas.get(lugarSelecci�nActual);
			}
			
			public boolean setTeclaEn(Tecla t, int lugar){
				if (lugar < getCantidadLugares()){
					teclas.remove(lugar);
					teclas.add(lugar, t);
					return true;
				} else {
					return false;
				}
			}
			
		}

		private Enums misListas = new Enums();
		private List<Tecla> misTeclasComando = misListas.getTeclasComando();
		private List<Tecla> misTeclasLetras = misListas.getTeclasLetra();
		private List<Tecla> misTeclasLetrasAcentuadas = misListas.getTeclasLetraAcentuadas();
		private List<Tecla> misTeclasN�meros = misListas.getTeclasN�mero();
		private List<Tecla> misTeclasS�mbolos = misListas.getTeclasS�mbolo();
		
		private int letrasPorC�rculo; //c�rculo interno y externo, si fuera un s�lo c�rculo habr�a q dividir por 6. 
								   //Son 6 posiciones por c�rculo porque a las 8 posiciones de dividir un c�rculo vertical, horizontal y diagonalmente,
		   						   //se les restan dos posiciones que corresponden a flechas der e izq
		private int cantTeclasLetra;
		private int cantC�rculosLetra;
		private int cantTeclasComando;
		private int cantC�rculosComando;
		private int cantTeclasS�mbolo;
		private int cantC�rculosS�mbolo;
		private int cantTeclasN�mero;
		private int cantC�rculosN�mero;
		private int cantTeclasLetrasAcentuadas;
		private int cantC�rculosLetrasAcentuadas;
		private int lugarInicioC�rculoLetras;
		private int lugarInicioC�rculoComando;
		private int lugarInicioC�rculoS�mbolo;
		private int lugarInicioC�rculoN�mero;
		private int lugarInicioC�rculoLetrasAcentuadas;
		//Tecla[] c�rculosLetra = null;
		private int c�rculoActual;
		private int cantC�rculos;
		private int lugarSelecci�nActual;
		private List<PanelEnC�rculo> miTeclado = new ArrayList<TecladoEnC�rculos.PanelEnC�rculo>();
		private boolean swTecladoDoble;
		private boolean swMostrarC�culosLetras=true;
		private boolean swMostrarC�culosN�meros=true;
		private boolean swMostrarC�culosLetrasAcentuadas=true;
		private boolean swMostrarC�culosS�mbolos=true;
		private boolean swMostrarC�culosComandos=true;
		
		
		public TecladoEnC�rculos(boolean swtecladoDoble){
			if (swtecladoDoble)
				setLetrasPorC�rculo(12);
			else
				setLetrasPorC�rculo(6);

			//s�lo se cargan si tienen alguna tecla
			cantTeclasLetra = misTeclasLetras.size(); //letras
			if (cantTeclasLetra != 0){
				cantC�rculosLetra = cantTeclasLetra / letrasPorC�rculo;
				if (cantTeclasLetra % letrasPorC�rculo != 0) //se ve si queda resto para sumarle un c�rculo m�s
					cantC�rculosLetra+=1;

				cargarTeclasEnC�rculos(misTeclasLetras, cantC�rculosLetra, TipoTecla.LETRAS);
			}
			lugarInicioC�rculoLetras=0;

			cantTeclasLetrasAcentuadas = misTeclasLetrasAcentuadas.size(); //letras acentuadas
			if (cantTeclasLetrasAcentuadas != 0){
				cantC�rculosLetrasAcentuadas = cantTeclasLetrasAcentuadas / letrasPorC�rculo;
				if (cantTeclasLetrasAcentuadas % letrasPorC�rculo != 0)
					cantC�rculosLetrasAcentuadas+=1;
				
				cargarTeclasEnC�rculos(misTeclasLetrasAcentuadas, cantC�rculosLetrasAcentuadas, TipoTecla.LETRAS_ACENTUADAS);
			}
			lugarInicioC�rculoLetrasAcentuadas = lugarInicioC�rculoLetras + cantC�rculosLetra; //porque el c�rculo de letras empieza en 0

			cantTeclasN�mero = misTeclasN�meros.size(); //n�meros
			if (cantTeclasN�mero != 0){
				cantC�rculosN�mero = cantTeclasN�mero / letrasPorC�rculo;
				if (cantTeclasN�mero % letrasPorC�rculo != 0)
					cantC�rculosN�mero+=1;
				
				cargarTeclasEnC�rculos(misTeclasN�meros, cantC�rculosN�mero, TipoTecla.N�MEROS);
			}
			lugarInicioC�rculoN�mero = lugarInicioC�rculoLetrasAcentuadas + cantC�rculosLetrasAcentuadas; //porque el c�rculo de letras empieza en 0

			cantTeclasS�mbolo = misTeclasS�mbolos.size(); //s�mbolos
			if (cantTeclasS�mbolo != 0){
				cantC�rculosS�mbolo = cantTeclasS�mbolo / letrasPorC�rculo;
				if (cantTeclasS�mbolo % letrasPorC�rculo != 0)
					cantC�rculosS�mbolo+=1;
				
				cargarTeclasEnC�rculos(misTeclasS�mbolos, cantC�rculosS�mbolo, TipoTecla.S�MBOLOS);
			}
			lugarInicioC�rculoS�mbolo = lugarInicioC�rculoN�mero + cantC�rculosN�mero; //porque el c�rculo de letras empieza en 0

			cantTeclasComando = misTeclasComando.size(); //comandos
			if (cantTeclasComando != 0){
				cantC�rculosComando = cantTeclasComando / letrasPorC�rculo;
				if (cantTeclasComando % letrasPorC�rculo != 0)
					cantC�rculosComando+=1;
				
				cargarTeclasEnC�rculos(misTeclasComando, cantC�rculosComando, TipoTecla.COMANDOS);
			}
			lugarInicioC�rculoComando = lugarInicioC�rculoS�mbolo + cantC�rculosComando; //porque el c�rculo de letras empieza en 0
			
			//cantC�rculos = cantC�rculosComando + cantC�rculosLetra + cantC�rculosN�mero + cantC�rculosS�mbolo;
			cantC�rculos = miTeclado.size()-1;
			
			setC�rculoActual(0);
				
			setSwTecladoDoble(swtecladoDoble);
			cargarBanderaPrimerC�rculo();
			cargarBandera�ltimoC�rculo();
		}
		
		private void cargarBandera�ltimoC�rculo(){
			miTeclado.get(cantC�rculos).setSw�ltimoC�rculo(true);
		}
		
		private void cargarBanderaPrimerC�rculo(){
			miTeclado.get(0).setSwPrimerC�rculo(true);
		}
		
		private boolean isMostrarC�culosLetras() {
			return swMostrarC�culosLetras;
		}

		public void setMostrarC�culosLetras(boolean swMostrarC�culosLetras) {
			this.swMostrarC�culosLetras = swMostrarC�culosLetras;
		}

		private boolean isMostrarC�culosN�meros() {
			return swMostrarC�culosN�meros;
		}

		public void setMostrarC�culosN�meros(boolean swMostrarC�culosN�meros) {
			this.swMostrarC�culosN�meros = swMostrarC�culosN�meros;
		}

		private boolean isMostrarC�culosLetrasAcentuadas() {
			return swMostrarC�culosLetrasAcentuadas;
		}

		public void setMostrarC�culosLetrasAcentuadas(
				boolean swMostrarC�culosLetrasAcentuadas) {
			this.swMostrarC�culosLetrasAcentuadas = swMostrarC�culosLetrasAcentuadas;
		}

		private boolean isMostrarC�culosS�mbolos() {
			return swMostrarC�culosS�mbolos;
		}

		public void setMostrarC�culosS�mbolos(boolean swMostrarC�culosS�mbolos) {
			this.swMostrarC�culosS�mbolos = swMostrarC�culosS�mbolos;
		}

		private boolean isMostrarC�culosComandos() {
			return swMostrarC�culosComandos;
		}

		public void setMostrarC�culosComandos(boolean swMostrarC�culosComandos) {
			this.swMostrarC�culosComandos = swMostrarC�culosComandos;
		}
		
		private boolean isSwTecladoDoble() {
			return swTecladoDoble;
		}

		private void setSwTecladoDoble(boolean swTecladoDoble) {
			this.swTecladoDoble = swTecladoDoble;
		}
		
		private int getLetrasPorC�rculo() {
			return letrasPorC�rculo;
		}

		private void setLetrasPorC�rculo(int letrasPorC�rculo) {
			this.letrasPorC�rculo = letrasPorC�rculo;
		}
		
		public int getLugarC�rculoActual() {
			return c�rculoActual;
		}

		public void setC�rculoActual(int c�rculoActual) {
			this.c�rculoActual = c�rculoActual;
		}
		
		private void cargarC�rculoEnTeclado(PanelEnC�rculo c�rculoACargar){
			miTeclado.add(c�rculoACargar);
		}
		
		private boolean cargarTeclasEnC�rculos(List<Tecla> teclas, int cantidadC�rculos, TipoTecla tipo){
			int teclasYaInsertadas = 0;
			int contador;
			List<Tecla> misTeclas;

			for (int i=0; i < cantidadC�rculos; i++){
				misTeclas = new ArrayList<Enums.Tecla>();
				contador = 0;
				for (int j=0; j<letrasPorC�rculo; j++){
					if (teclas.size() > teclasYaInsertadas + j) { //por si la cantidad de teclas no llega a completar exactamente el �ltimo c�rculo
						misTeclas.add(teclas.get(teclasYaInsertadas + j));
					} else {
						misTeclas.add(Tecla.NINGUNA);
					}
					contador++;
				}

				PanelEnC�rculo miC�rculo = new PanelEnC�rculo(misTeclas, isSwTecladoDoble());
				miC�rculo.setTipoC�rculo(tipo);
				if (misTeclas.size() > 6)
					miC�rculo.setSwTieneC�rculoExterno(true);
				else
					miC�rculo.setSwTieneC�rculoExterno(false);
				
				cargarC�rculoEnTeclado(miC�rculo);
				teclasYaInsertadas += contador;
			}
			
			return true;
		}
		
		public void pasarAlSiguienteC�rculo(){
			if (c�rculoActual < cantC�rculos)
				c�rculoActual++;
			
			//-----------se comprueba que se pueda mostrar el c�rculo seleccionado----------- 
			if (!isTipoC�rculoVisible(getC�rculoActual().getTipoC�rculo())){
				if (getC�rculoActual().is�ltimoC�rculo()){ //se comprueba que no se haya llegdo al fin sin encontrar c�rculo
					setC�rculoActual(0);
					if (isTipoC�rculoVisible(getC�rculoActual().getTipoC�rculo())){ //si el primero es del tipo seleccionado
						pasarAlSiguienteC�rculo();
						return;
					}
				}
				pasarAlSiguienteC�rculo();
				return;
			}
		}
		
		private boolean isTipoC�rculoVisible(TipoTecla t){
			switch (t){
			case LETRAS:
				if (isMostrarC�culosLetras() == false)
					return false;
				break;
			case COMANDOS:
				if (isMostrarC�culosComandos() == false)
					return false;
				break;
			case N�MEROS:
				if (isMostrarC�culosN�meros() == false)
					return false;
				break;
			case LETRAS_ACENTUADAS:
				if (isMostrarC�culosLetrasAcentuadas() == false)
					return false;
				break;
			case S�MBOLOS:
				if (isMostrarC�culosS�mbolos() == false)
					return false;
				break;
			}
			return true;
		}
		
		
		public void pasarAlC�rculoAnterior(){
			if (c�rculoActual > 0)
				c�rculoActual--;
			
			//-----------se comprueba que se pueda mostrar el c�rculo seleccionado----------- 
			if (!isTipoC�rculoVisible(getC�rculoActual().getTipoC�rculo())){
				if (getC�rculoActual().isPrimerC�rculo()){ //se comprueba que no se haya llegdo al inicio sin encontrar c�rculo
					setC�rculoActual(cantC�rculos);
					if (isTipoC�rculoVisible(getC�rculoActual().getTipoC�rculo())){ //si el primero es del tipo seleccionado
						pasarAlC�rculoAnterior();
						return;
					}
				}
				pasarAlC�rculoAnterior();
				return;
			}
		}
		
		public void pasarAlC�rculoEn(int lugar){ //TODO ver si c�rculoActual puede tener como l�mites 0 y cantC�rculo o tiene q ser cantC�rculos-1
			c�rculoActual = lugar;

			if (c�rculoActual > cantC�rculos) 
				c�rculoActual = cantC�rculos;
			
			if (c�rculoActual < 0)
				c�rculoActual = 0;
		}
		
		public void pasarAlC�rculoDeLetras(){
			pasarAlC�rculoEn(lugarInicioC�rculoLetras);
		}
		
		public void pasarAlC�rculoDeLetrasAcentuadas(){
			pasarAlC�rculoEn(lugarInicioC�rculoLetrasAcentuadas);
		}

		public void pasarAlC�rculoDeN�meros(){
			pasarAlC�rculoEn(lugarInicioC�rculoN�mero);
		}

		public void pasarAlC�rculoDeS�mbolos(){
			pasarAlC�rculoEn(lugarInicioC�rculoS�mbolo);
		}

		public void pasarAlC�rculoDeComandos(){
			pasarAlC�rculoEn(lugarInicioC�rculoComando);
		}
		
		public PanelEnC�rculo getC�rculoEn (int lugar){
			if (lugar > cantC�rculos)
				return miTeclado.get(cantC�rculos);
			
			if (lugar < 0)
				return miTeclado.get(0);
			
			return miTeclado.get(lugar);
		}
		
		public PanelEnC�rculo getC�rculoActual(){
			if (c�rculoActual > cantC�rculos)
				return miTeclado.get(cantC�rculos);
			
			if (c�rculoActual < 0)
				return miTeclado.get(0);
			
			return miTeclado.get(c�rculoActual);
		}
		
		public void mostrarS�loC�rculosN�meros(){
			setMostrarC�culosComandos(false);
			setMostrarC�culosLetras(false);
			setMostrarC�culosLetrasAcentuadas(false);
			setMostrarC�culosS�mbolos(false);
			setMostrarC�culosN�meros(true);
			pasarAlC�rculoDeN�meros();
		}
		
		public void mostrarLetrasN�merosAcentos(){
			setMostrarC�culosComandos(false);
			setMostrarC�culosLetras(true);
			setMostrarC�culosLetrasAcentuadas(false);
			setMostrarC�culosS�mbolos(false);
			setMostrarC�culosN�meros(true);
			pasarAlC�rculoDeN�meros();
		}
		
		public void mostrarTodosLosTiposDeLetra(){
			setMostrarC�culosComandos(true);
			setMostrarC�culosLetras(true);
			setMostrarC�culosLetrasAcentuadas(true);
			setMostrarC�culosS�mbolos(true);
			setMostrarC�culosN�meros(true);
			pasarAlC�rculoDeLetras();
		}
	}
}

