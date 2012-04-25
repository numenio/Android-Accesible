package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;

import com.guille.gestures.Enums.Tecla;
import com.guille.gestures.Enums.TipoTecla;
//import com.guille.gestures.Enums.TeclaComando;
//import com.guille.gestures.Enums.TeclaLetra;
//import com.guille.gestures.Enums.TeclaNúmero;
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
	private TecladoEnCírculos teclado = null;
	private Context contexto = null;
	private Vibrator vibe;
	private boolean swUsóAtajoTeclado;
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
		teclado = new TecladoEnCírculos(swTecladoDoble);
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
			swUsóAtajoTeclado = true;
			return true;
		}
		
//		miEvaluadorDeMovimientos.ingresarMovimiento(event);
//		if (miEvaluadorDeMovimientos.isEmpezandoIzquierdaAfuera()){
//			hablar("empezó desde la izquierda afuera");
//			return true;
//		}
//		
//		if (miEvaluadorDeMovimientos.isMovimientoChiquito()) return true; //si el mov es muy pequeño, no se hace nada

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
			if (!swUsóAtajoTeclado){
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
				swUsóAtajoTeclado=false;
			}
			break;
		default:
			if (!swUsóAtajoTeclado){
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
				swUsóAtajoTeclado=false;
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

			if (event.getAction() == event.ACTION_UP){ //cuando se levanta el último dedo, cantDedosMáxima tiene el número de cuántos dedos se pulsaron
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
	
	public void mostrarSóloNúmeros(){
		teclado.mostrarSóloCírculosNúmeros();
	}
	
	public void mostrarLetrasNúmeros(){
		teclado.mostrarLetrasNúmerosAcentos();
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
			nearEdge = true; //TODO chequear cuándo si esto registra si un movimiento se va de la pantalla o si viene desde afuera, así se pueden poner más gestures personalizados
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
			hablar("Deslizá tu dedo en los círculos interno y externo para elegir qué tecla querés pulsar. Para cancelar, volvé a este centro");
		else
			hablar("Deslizá tu dedo en círculo para elegir qué tecla querés pulsar. Para cancelar, volvé a este centro");
		
		teclado.getCírculoActual().seleccionarTeclaCentro();
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
		case ARRIBA_LEJOS: //desde aquí se evalúa si es un teclado doble o simple
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(6);
			} else {
//				onGestureChange(Gesture.ARRIBA);
//				teclado.getCírculoActual().seleccionarTeclaCentro();
				hablar("Pasar a los números");
			}
			break;	
		case ARRIBA_DERECHA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(7);
			} else {
//				onGestureChange(Gesture.ARRIBA_DERECHA);
//				teclado.getCírculoActual().seleccionarTeclaCentro();
				hablar("Pasar a las letras");
			}
			break;
		case ABAJO_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(9);
			} else {
				//onGestureChange(Gesture.ABAJO);
//				teclado.getCírculoActual().seleccionarTeclaCentro(); //TODO para que cambie la selec
				hablar("espacio");
			}
			break;
		case ABAJO_DERECHA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(10);
			} else {
//				onGestureChange(Gesture.ABAJO_DERECHA);
//				teclado.getCírculoActual().seleccionarTeclaCentro();
				hablar("borrar");
			}
			break;
		case ABAJO_IZQUIERDA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(8);
			} else {
//				onGestureChange(Gesture.ABAJO_IZQUIERDA);
//				teclado.getCírculoActual().seleccionarTeclaCentro();
				hablar("enviar el mensaje");
			}
			break;
		case ARRIBA_IZQUIERDA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			if (teclado.isSwTecladoDoble()){
				seleccionarTeclaEn(11);
			} else {
//				onGestureChange(Gesture.ARRIBA_IZQUIERDA);
//				teclado.getCírculoActual().seleccionarTeclaCentro();
				hablar("Pasar a los símbolos");
			}
			break;
		case CENTRO:
			hablar("No hacer nada");
			teclado.getCírculoActual().seleccionarTeclaCentro();
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case DERECHA:
			hablar("Pasar al círculo de la derecha");
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case DERECHA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case IZQUIERDA:
			hablar("Pasar al círculo de la izquierda");
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		case IZQUIERDA_LEJOS:
			vibe.vibrate(VIBE_PATTERN_CHANGE, -1);
			break;
		}
	}

	private void seleccionarTeclaEn(int lugar) {
		if (teclado.getCírculoActual().getTeclaActual() != teclado.getCírculoActual().getTeclaEn(lugar)){//sólo selecciona si no es la que ya está seleccionada
			Tecla miTecla = teclado.getCírculoActual().getTeclaEn(lugar);
			if (miTecla != Tecla.NINGUNA) //si es distinto de ninguna
				hablar(UtilesCadena.traducirTeclaParaLeer(miTecla));
			else {
				String cadena = "Este círculo sólo tiene " + teclado.getCírculoActual().getCantidadLugaresUsados();
				switch(teclado.getCírculoActual().getTipoCírculo()){
				case LETRAS:
					cadena += " letras disponibles. Aquí no hay letras";
					break;
				case COMANDOS:
					cadena += " comandos disponibles. Aquí no hay teclas de comando";
					break;
				case LETRAS_ACENTUADAS:
					cadena += " vocales acentuadas disponibles. Aquí no hay vocal acentuada";
					break;
				case NÚMEROS:
					cadena += " números disponibles. Aquí no hay número";
					break;
				case SÍMBOLOS:
					cadena += " símbolos disponibles. Aquí no hay símbolo";
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
			teclado.pasarAlSiguienteCírculo();
			hablar(armarCadenaPasarAlCírculoSiguiente());
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
			teclado.pasarAlCírculoDeLetras();
			hablar("Pasando al primer círculo de letras que va desde " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getCírculoActual().getNombreÚltimaTecla());
			break;
		case ARRIBA_LEJOS:
			teclado.pasarAlCírculoDeNúmeros();
			hablar("Pasando al primer círculo de números que va desde " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getCírculoActual().getNombreÚltimaTecla());
			break;
		case ARRIBA_IZQUIERDA_LEJOS:
			teclado.pasarAlCírculoDeSímbolos();
			hablar("Pasando al primer círculo de símbolos que va desde " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getCírculoActual().getNombreÚltimaTecla());
			break;
		case IZQUIERDA:
			teclado.pasarAlCírculoAnterior();
			hablar(armarCadenaPasarAlCírculoAnterior());
			break;
		case IZQUIERDA_LEJOS:
			cbTeclado.onTeclaLiberada(Tecla.FLECHA_IZQ);
			break;
		case CENTRO:
			//hablar("Cancelado");
			break;
		default:	
			Tecla miTecla = teclado.getCírculoActual().getTeclaActual();
			cbTeclado.onTeclaLiberada(miTecla);
			break;
		}
	}
	
	private String armarCadenaPasarAlCírculoAnterior(){
		String cadena = "";
		if (!teclado.getCírculoActual().isPrimerCírculo()){
			cadena = "Pasando al círculo " + armarCadenaSegúnTipoCírculoActual() + " que va desde " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getCírculoActual().getNombreÚltimaTecla();
		} else{
			cadena = "Estás en el primer círculo. El círculo empieza en " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			" y termina en " + teclado.getCírculoActual().getNombreÚltimaTecla();
		}
		
		return cadena;
	}
	
	private String armarCadenaPasarAlCírculoSiguiente(){
		String cadena = "";
		if (!teclado.getCírculoActual().isÚltimoCírculo()){
			cadena = "Pasando al círculo " + armarCadenaSegúnTipoCírculoActual() + " que va desde " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			"hasta " + teclado.getCírculoActual().getNombreÚltimaTecla();
		}else{
			cadena = "Estás en el último círculo. El círculo empieza en " +  teclado.getCírculoActual().getNombrePrimeraTecla() + 
			" y termina en " + teclado.getCírculoActual().getNombreÚltimaTecla();
		}
		
		return cadena;
	}
	
	private String armarCadenaSegúnTipoCírculoActual(){
		String cadena="";
		switch(teclado.getCírculoActual().getTipoCírculo()){
		case LETRAS:
			cadena = " de letras ";
			break;
		case COMANDOS:
			cadena = " de comandos ";
			break;
		case LETRAS_ACENTUADAS:
			cadena = " de vocales acentuadas y diéresis ";
			break;
		case NÚMEROS:
			cadena = " de números ";
			break;
		case SÍMBOLOS:
			cadena = " de símbolos ";
			break;
		}
		return cadena;
	}


//	private Tecla pasarGestoATecla (Gesture gesto){//TODO hacer el método
//		Tecla miTecla = Tecla.NINGUNA;
//		return miTecla; 
//	}
	
	private void hablar (String cadena){
		((MiApp)contexto.getApplicationContext()).hablar(cadena);
	}
	
	class TecladoEnCírculos {
		class PanelEnCírculo {
			private int cantidadLugares;
			private int cantidadLugaresUsados;
			private int cantidadLugaresDisponibles;
			private int últimoLugarUsado;
			private int primerLugarDisponible;
			private TipoTecla tipoCírculo;
			private boolean swTieneCírculoExterno;
			private boolean swPrimerCírculo=false;
			private boolean swÚltimoCírculo=false;
			private List<Tecla> teclas = new ArrayList<Tecla>();
			
			public boolean isPrimerCírculo() {
				return swPrimerCírculo;
			}
			private void setSwPrimerCírculo(boolean swPrimerCírculo) {
				this.swPrimerCírculo = swPrimerCírculo;
			}
			public boolean isÚltimoCírculo() {
				return swÚltimoCírculo;
			}
			private void setSwÚltimoCírculo(boolean swÚltimoCírculo) {
				this.swÚltimoCírculo = swÚltimoCírculo;
			}
			
			public boolean swTieneCírculoExterno() {
				return swTieneCírculoExterno;
			}
			private void setSwTieneCírculoExterno(boolean swTieneCírculoExterno) {
				this.swTieneCírculoExterno = swTieneCírculoExterno;
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
			private int getÚltimoLugarUsado() {
				return últimoLugarUsado;
			}
			private void setÚltimoLugarUsado(int últimoLugarUsado) {
				this.últimoLugarUsado = últimoLugarUsado;
			}
			private int getPrimerLugarDisponible() {
				return primerLugarDisponible;
			}
			private void setPrimerLugarDisponible(int primerLugarDisponible) {
				this.primerLugarDisponible = primerLugarDisponible;
			}
			public TipoTecla getTipoCírculo() {
				return tipoCírculo;
			}
			private void setTipoCírculo(TipoTecla tipo) {
				this.tipoCírculo = tipo;
			}
			
			public PanelEnCírculo(List<Tecla> t, boolean swTecladoDoble){
				if (swTecladoDoble)
					setCantidadLugares(12);
				else
					setCantidadLugares(6);
				
				if (t.size() <= getCantidadLugares()) //si las teclas que se pasan son menos o igual a las que caben en un panel, se las carga directo, sinó, se
					this.teclas = t;				  //cargan sólo las que quepan
				else
					cargarSóloTeclasSegúnLugaresMáximos(t, teclas);
				
				int cantLugaresUsados=0;
				for(int i=0; i<teclas.size(); i++){
					if (teclas.get(i)!=Tecla.NINGUNA)
						cantLugaresUsados++;
				}
				setCantidadLugaresDisponibles(getCantidadLugares() - cantLugaresUsados);
				setCantidadLugaresUsados(cantLugaresUsados);
				setPrimerLugarDisponible(cantLugaresUsados + 1);
				setÚltimoLugarUsado(cantLugaresUsados);
				setCantidadLugares(getCantidadLugares()+1);//sumamos un lugar para que sea la tecla centro
				teclas.add(Tecla.NINGUNA); //tecla centro, ninguna
			}
			
			private void cargarSóloTeclasSegúnLugaresMáximos(List<Tecla> listaOrigen, List<Tecla> listaDestino){
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
			
			public Tecla getÚltimaTecla(){
				if (teclas.size() > 0){
					for (int i=1; i<teclas.size(); i++){
						if (teclas.get(i) == Tecla.NINGUNA)
							return teclas.get(i-1); //se pasa la tecla anterior a la que está vacía
					}
					return teclas.get(teclas.size()-2);//es menos 2 y no menos 1 porque la última es la tecla centro 
				}else
					return Tecla.NINGUNA;
			}
			
			public String getNombreÚltimaTecla(){
					return UtilesCadena.traducirTeclaParaLeer(getÚltimaTecla());
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
				lugarSelecciónActual = lugar;
				return teclas.get(lugar);
			}
			
			public Tecla getTeclaActual(){
				return teclas.get(lugarSelecciónActual);
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
		private List<Tecla> misTeclasNúmeros = misListas.getTeclasNúmero();
		private List<Tecla> misTeclasSímbolos = misListas.getTeclasSímbolo();
		
		private int letrasPorCírculo; //círculo interno y externo, si fuera un sólo círculo habría q dividir por 6. 
								   //Son 6 posiciones por círculo porque a las 8 posiciones de dividir un círculo vertical, horizontal y diagonalmente,
		   						   //se les restan dos posiciones que corresponden a flechas der e izq
		private int cantTeclasLetra;
		private int cantCírculosLetra;
		private int cantTeclasComando;
		private int cantCírculosComando;
		private int cantTeclasSímbolo;
		private int cantCírculosSímbolo;
		private int cantTeclasNúmero;
		private int cantCírculosNúmero;
		private int cantTeclasLetrasAcentuadas;
		private int cantCírculosLetrasAcentuadas;
		private int lugarInicioCírculoLetras;
		private int lugarInicioCírculoComando;
		private int lugarInicioCírculoSímbolo;
		private int lugarInicioCírculoNúmero;
		private int lugarInicioCírculoLetrasAcentuadas;
		//Tecla[] círculosLetra = null;
		private int círculoActual;
		private int cantCírculos;
		private int lugarSelecciónActual;
		private List<PanelEnCírculo> miTeclado = new ArrayList<TecladoEnCírculos.PanelEnCírculo>();
		private boolean swTecladoDoble;
		private boolean swMostrarCículosLetras=true;
		private boolean swMostrarCículosNúmeros=true;
		private boolean swMostrarCículosLetrasAcentuadas=true;
		private boolean swMostrarCículosSímbolos=true;
		private boolean swMostrarCículosComandos=true;
		
		
		public TecladoEnCírculos(boolean swtecladoDoble){
			if (swtecladoDoble)
				setLetrasPorCírculo(12);
			else
				setLetrasPorCírculo(6);

			//sólo se cargan si tienen alguna tecla
			cantTeclasLetra = misTeclasLetras.size(); //letras
			if (cantTeclasLetra != 0){
				cantCírculosLetra = cantTeclasLetra / letrasPorCírculo;
				if (cantTeclasLetra % letrasPorCírculo != 0) //se ve si queda resto para sumarle un círculo más
					cantCírculosLetra+=1;

				cargarTeclasEnCírculos(misTeclasLetras, cantCírculosLetra, TipoTecla.LETRAS);
			}
			lugarInicioCírculoLetras=0;

			cantTeclasLetrasAcentuadas = misTeclasLetrasAcentuadas.size(); //letras acentuadas
			if (cantTeclasLetrasAcentuadas != 0){
				cantCírculosLetrasAcentuadas = cantTeclasLetrasAcentuadas / letrasPorCírculo;
				if (cantTeclasLetrasAcentuadas % letrasPorCírculo != 0)
					cantCírculosLetrasAcentuadas+=1;
				
				cargarTeclasEnCírculos(misTeclasLetrasAcentuadas, cantCírculosLetrasAcentuadas, TipoTecla.LETRAS_ACENTUADAS);
			}
			lugarInicioCírculoLetrasAcentuadas = lugarInicioCírculoLetras + cantCírculosLetra; //porque el círculo de letras empieza en 0

			cantTeclasNúmero = misTeclasNúmeros.size(); //números
			if (cantTeclasNúmero != 0){
				cantCírculosNúmero = cantTeclasNúmero / letrasPorCírculo;
				if (cantTeclasNúmero % letrasPorCírculo != 0)
					cantCírculosNúmero+=1;
				
				cargarTeclasEnCírculos(misTeclasNúmeros, cantCírculosNúmero, TipoTecla.NÚMEROS);
			}
			lugarInicioCírculoNúmero = lugarInicioCírculoLetrasAcentuadas + cantCírculosLetrasAcentuadas; //porque el círculo de letras empieza en 0

			cantTeclasSímbolo = misTeclasSímbolos.size(); //símbolos
			if (cantTeclasSímbolo != 0){
				cantCírculosSímbolo = cantTeclasSímbolo / letrasPorCírculo;
				if (cantTeclasSímbolo % letrasPorCírculo != 0)
					cantCírculosSímbolo+=1;
				
				cargarTeclasEnCírculos(misTeclasSímbolos, cantCírculosSímbolo, TipoTecla.SÍMBOLOS);
			}
			lugarInicioCírculoSímbolo = lugarInicioCírculoNúmero + cantCírculosNúmero; //porque el círculo de letras empieza en 0

			cantTeclasComando = misTeclasComando.size(); //comandos
			if (cantTeclasComando != 0){
				cantCírculosComando = cantTeclasComando / letrasPorCírculo;
				if (cantTeclasComando % letrasPorCírculo != 0)
					cantCírculosComando+=1;
				
				cargarTeclasEnCírculos(misTeclasComando, cantCírculosComando, TipoTecla.COMANDOS);
			}
			lugarInicioCírculoComando = lugarInicioCírculoSímbolo + cantCírculosComando; //porque el círculo de letras empieza en 0
			
			//cantCírculos = cantCírculosComando + cantCírculosLetra + cantCírculosNúmero + cantCírculosSímbolo;
			cantCírculos = miTeclado.size()-1;
			
			setCírculoActual(0);
				
			setSwTecladoDoble(swtecladoDoble);
			cargarBanderaPrimerCírculo();
			cargarBanderaÚltimoCírculo();
		}
		
		private void cargarBanderaÚltimoCírculo(){
			miTeclado.get(cantCírculos).setSwÚltimoCírculo(true);
		}
		
		private void cargarBanderaPrimerCírculo(){
			miTeclado.get(0).setSwPrimerCírculo(true);
		}
		
		private boolean isMostrarCículosLetras() {
			return swMostrarCículosLetras;
		}

		public void setMostrarCículosLetras(boolean swMostrarCículosLetras) {
			this.swMostrarCículosLetras = swMostrarCículosLetras;
		}

		private boolean isMostrarCículosNúmeros() {
			return swMostrarCículosNúmeros;
		}

		public void setMostrarCículosNúmeros(boolean swMostrarCículosNúmeros) {
			this.swMostrarCículosNúmeros = swMostrarCículosNúmeros;
		}

		private boolean isMostrarCículosLetrasAcentuadas() {
			return swMostrarCículosLetrasAcentuadas;
		}

		public void setMostrarCículosLetrasAcentuadas(
				boolean swMostrarCículosLetrasAcentuadas) {
			this.swMostrarCículosLetrasAcentuadas = swMostrarCículosLetrasAcentuadas;
		}

		private boolean isMostrarCículosSímbolos() {
			return swMostrarCículosSímbolos;
		}

		public void setMostrarCículosSímbolos(boolean swMostrarCículosSímbolos) {
			this.swMostrarCículosSímbolos = swMostrarCículosSímbolos;
		}

		private boolean isMostrarCículosComandos() {
			return swMostrarCículosComandos;
		}

		public void setMostrarCículosComandos(boolean swMostrarCículosComandos) {
			this.swMostrarCículosComandos = swMostrarCículosComandos;
		}
		
		private boolean isSwTecladoDoble() {
			return swTecladoDoble;
		}

		private void setSwTecladoDoble(boolean swTecladoDoble) {
			this.swTecladoDoble = swTecladoDoble;
		}
		
		private int getLetrasPorCírculo() {
			return letrasPorCírculo;
		}

		private void setLetrasPorCírculo(int letrasPorCírculo) {
			this.letrasPorCírculo = letrasPorCírculo;
		}
		
		public int getLugarCírculoActual() {
			return círculoActual;
		}

		public void setCírculoActual(int círculoActual) {
			this.círculoActual = círculoActual;
		}
		
		private void cargarCírculoEnTeclado(PanelEnCírculo círculoACargar){
			miTeclado.add(círculoACargar);
		}
		
		private boolean cargarTeclasEnCírculos(List<Tecla> teclas, int cantidadCírculos, TipoTecla tipo){
			int teclasYaInsertadas = 0;
			int contador;
			List<Tecla> misTeclas;

			for (int i=0; i < cantidadCírculos; i++){
				misTeclas = new ArrayList<Enums.Tecla>();
				contador = 0;
				for (int j=0; j<letrasPorCírculo; j++){
					if (teclas.size() > teclasYaInsertadas + j) { //por si la cantidad de teclas no llega a completar exactamente el último círculo
						misTeclas.add(teclas.get(teclasYaInsertadas + j));
					} else {
						misTeclas.add(Tecla.NINGUNA);
					}
					contador++;
				}

				PanelEnCírculo miCírculo = new PanelEnCírculo(misTeclas, isSwTecladoDoble());
				miCírculo.setTipoCírculo(tipo);
				if (misTeclas.size() > 6)
					miCírculo.setSwTieneCírculoExterno(true);
				else
					miCírculo.setSwTieneCírculoExterno(false);
				
				cargarCírculoEnTeclado(miCírculo);
				teclasYaInsertadas += contador;
			}
			
			return true;
		}
		
		public void pasarAlSiguienteCírculo(){
			if (círculoActual < cantCírculos)
				círculoActual++;
			
			//-----------se comprueba que se pueda mostrar el círculo seleccionado----------- 
			if (!isTipoCírculoVisible(getCírculoActual().getTipoCírculo())){
				if (getCírculoActual().isÚltimoCírculo()){ //se comprueba que no se haya llegdo al fin sin encontrar círculo
					setCírculoActual(0);
					if (isTipoCírculoVisible(getCírculoActual().getTipoCírculo())){ //si el primero es del tipo seleccionado
						pasarAlSiguienteCírculo();
						return;
					}
				}
				pasarAlSiguienteCírculo();
				return;
			}
		}
		
		private boolean isTipoCírculoVisible(TipoTecla t){
			switch (t){
			case LETRAS:
				if (isMostrarCículosLetras() == false)
					return false;
				break;
			case COMANDOS:
				if (isMostrarCículosComandos() == false)
					return false;
				break;
			case NÚMEROS:
				if (isMostrarCículosNúmeros() == false)
					return false;
				break;
			case LETRAS_ACENTUADAS:
				if (isMostrarCículosLetrasAcentuadas() == false)
					return false;
				break;
			case SÍMBOLOS:
				if (isMostrarCículosSímbolos() == false)
					return false;
				break;
			}
			return true;
		}
		
		
		public void pasarAlCírculoAnterior(){
			if (círculoActual > 0)
				círculoActual--;
			
			//-----------se comprueba que se pueda mostrar el círculo seleccionado----------- 
			if (!isTipoCírculoVisible(getCírculoActual().getTipoCírculo())){
				if (getCírculoActual().isPrimerCírculo()){ //se comprueba que no se haya llegdo al inicio sin encontrar círculo
					setCírculoActual(cantCírculos);
					if (isTipoCírculoVisible(getCírculoActual().getTipoCírculo())){ //si el primero es del tipo seleccionado
						pasarAlCírculoAnterior();
						return;
					}
				}
				pasarAlCírculoAnterior();
				return;
			}
		}
		
		public void pasarAlCírculoEn(int lugar){ //TODO ver si círculoActual puede tener como límites 0 y cantCírculo o tiene q ser cantCírculos-1
			círculoActual = lugar;

			if (círculoActual > cantCírculos) 
				círculoActual = cantCírculos;
			
			if (círculoActual < 0)
				círculoActual = 0;
		}
		
		public void pasarAlCírculoDeLetras(){
			pasarAlCírculoEn(lugarInicioCírculoLetras);
		}
		
		public void pasarAlCírculoDeLetrasAcentuadas(){
			pasarAlCírculoEn(lugarInicioCírculoLetrasAcentuadas);
		}

		public void pasarAlCírculoDeNúmeros(){
			pasarAlCírculoEn(lugarInicioCírculoNúmero);
		}

		public void pasarAlCírculoDeSímbolos(){
			pasarAlCírculoEn(lugarInicioCírculoSímbolo);
		}

		public void pasarAlCírculoDeComandos(){
			pasarAlCírculoEn(lugarInicioCírculoComando);
		}
		
		public PanelEnCírculo getCírculoEn (int lugar){
			if (lugar > cantCírculos)
				return miTeclado.get(cantCírculos);
			
			if (lugar < 0)
				return miTeclado.get(0);
			
			return miTeclado.get(lugar);
		}
		
		public PanelEnCírculo getCírculoActual(){
			if (círculoActual > cantCírculos)
				return miTeclado.get(cantCírculos);
			
			if (círculoActual < 0)
				return miTeclado.get(0);
			
			return miTeclado.get(círculoActual);
		}
		
		public void mostrarSóloCírculosNúmeros(){
			setMostrarCículosComandos(false);
			setMostrarCículosLetras(false);
			setMostrarCículosLetrasAcentuadas(false);
			setMostrarCículosSímbolos(false);
			setMostrarCículosNúmeros(true);
			pasarAlCírculoDeNúmeros();
		}
		
		public void mostrarLetrasNúmerosAcentos(){
			setMostrarCículosComandos(false);
			setMostrarCículosLetras(true);
			setMostrarCículosLetrasAcentuadas(false);
			setMostrarCículosSímbolos(false);
			setMostrarCículosNúmeros(true);
			pasarAlCírculoDeNúmeros();
		}
		
		public void mostrarTodosLosTiposDeLetra(){
			setMostrarCículosComandos(true);
			setMostrarCículosLetras(true);
			setMostrarCículosLetrasAcentuadas(true);
			setMostrarCículosSímbolos(true);
			setMostrarCículosNúmeros(true);
			pasarAlCírculoDeLetras();
		}
	}
}

