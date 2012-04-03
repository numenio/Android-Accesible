package com.guille.gestures;

import java.util.ArrayList;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;

public class TecladoGestures extends View {//implements OnGesturePerformedListener, OnTouchListener{
//	private GestureDetector gestureDetector; 
//	private GestureOverlayView gestureOverlayView;
//	private GestureLibrary gestureLib;
//	int cantDedosMaxima;
//	private ITecladoListener cb = null;
//
//	private static final int SWIPE_MIN_DISTANCE = 120;  
//	private static final int SWIPE_MAX_OFF_PATH = 250;  
//	private static final int SWIPE_THRESHOLD_VELOCITY = 200; 
//
//
//
	public TecladoGestures(Context context) {
		super(context);
		//cargarTecladoGestures();
	}
//	
//	public TecladoGestures(Context context, ITecladoListener callback) {
//		super(context);
//		cargarTecladoGestures();
//		cb = callback; //este sería el AdaptadorTeclado
//	}
//
//	private void cargarTecladoGestures() {
//		gestureOverlayView = new GestureOverlayView(this.getContext()); 
//		gestureOverlayView.setOrientation(gestureOverlayView.ORIENTATION_HORIZONTAL);  
//		gestureOverlayView.addOnGesturePerformedListener(this);  
//		gestureOverlayView.setOnTouchListener(this);
//		cargarGestosLetras();
//		gestureDetector = new GestureDetector(new MyGestureDetector(this.getContext()));
//	}
//
//	private void cargarGestosLetras() {
//		gestureLib = GestureLibraries.fromRawResource(this.getContext(), R.raw.gestures_letras);
//		gestureLib.load();
//	}
//
//	private void cargarGestosNumeros() {
//		gestureLib = GestureLibraries.fromRawResource(this.getContext(), R.raw.gestures_numeros);
//		gestureLib.load();
//	}  
//
//	//@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		//public boolean onTouchEvent(MotionEvent event) {
//		//en las opciones dejar navegar, en escritura por supus escribir
//		int cantDedosActual = 0;
//
//		switch (event.getPointerCount()) {
//		case 1:
//			if (cantDedosMaxima < 1) 
//				cantDedosMaxima = 1;
//
//			cantDedosActual = 1;
//
//			if (event.getAction() == event.ACTION_MOVE){
//				swMovimiento = true;
//			}
//
//			if (event.getAction() == event.ACTION_UP){
//				if (cantDedosMaxima == 1) {
//					if (!swMovimiento) { //hay presión pero no hay movimiento
//						//si se está en el texto del mensaje
//						if (getEstadoEscritura() == ESCRIBIR_TEXTO_SMS) { 
//							if (event.getEventTime() - event.getDownTime() > TIEMPO_ESPERA_PRESIÓN_PARA_OPCIONES) {
//								//enviarSMS();
//								if (getFoco() != FOCO_OPCIONES) {
//									setFoco(FOCO_OPCIONES);
//									opcionesEditor.seleccionarEn(0);
//									hablar ("Abriendo las opciones, para seleccionarlas deslizá a " +
//											"izquierda o derecha y aceptá con una pulsación corta. Estás " +
//											"en la primer opción " + opcionesEditor.getElementoSeleccionado());
//								}
//							} else { //presión breve, si está en opciones aceptar la selección
//								if (getFoco() == FOCO_OPCIONES)
//									ejecutarOpciónMenú(opcionesEditor.getLugarSelecciónActual());
//							}
//							//si se está en los números, la presión en lugar de abrir opciones
//							//abre los contactos para no escribir número por números que pueda tener un contacto
//						} 
//
//						if (getEstadoEscritura() == ESCRIBIR_NÚMERO) { //TODO que el usuario pueda elegir entre varios números
//							if (getFoco() == FOCO_OPCIONES || getFoco() == FOCO_CONTACTOS){
//								if (misOpciones != null){ //si hay algún contacto en la lista
//									if (event.getEventTime() - event.getDownTime() > TIEMPO_ESPERA_PRESIÓN_PARA_OPCIONES) {
//										if (getFoco() != FOCO_CONTACTOS) {
//											setFoco(FOCO_CONTACTOS);
//											misContactos.seleccionarContactoEn(0);
//											hablar ("Abriendo los Contactos, para moverte por ellos deslizá a " +
//													"izquierda o derecha y aceptá con una pulsación corta. Estás " +
//													"en " + misContactos.getContactoSeleccionado().getNombre());
//
//
//										}
//									} else { //presión breve, si está en opciones aceptar la selección
//										if (getFoco() == FOCO_CONTACTOS){
//											cargarContacto(misContactos.getLugarContactoActual());
//										}
//									}
//								} else { //si la lista de contactos es nula
//									hablar ("No hay contactos en su agenda. Por favor escribí manualmente el número al que querés enviar el mensaje");
//									setFoco(FOCO_ESCRITURA);
//								}
//							}
//
//
//						}
//					}
//				}
//				cantDedosMaxima = 0;
//				swMovimiento = false;
//			}
//
//			break;
//		case 2:
//			if (cantDedosMaxima < 2) cantDedosMaxima = 2;
//			cantDedosActual = 2;
//			break;
//		case 3:
//			cantDedosMaxima = 3;
//			cantDedosActual = 3;
//			break;
//		default:
//			break;
//		}
//
//		if (cantDedosMaxima == 2 && cantDedosActual == 2) {
//			if (event.getAction() == event.ACTION_POINTER_2_UP){//si mantiene apretado el dedo uno y suelta el dos
//				borrarCaracter();
//			}
//		}
//
//
//		if (cantDedosMaxima == 3 && cantDedosActual == 3) {
//			if (event.getAction() == event.ACTION_POINTER_3_UP)
//				enviarSMS();
//		}
//
//		return gestureDetector.onTouchEvent(event); //se le pasa el evento a gestureDetector
//	}
//
//	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {  
//		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
//		String predicción = "";
//		if (predictions.size() > 0) { //se busca la predicción
//			if (predictions.get(0).score > 1.0){
//				predicción = predictions.get(0).name;				
//			}
//		}
//
//		escribir(predicción);
//	}
//
//
//
//	class MyGestureDetector extends SimpleOnGestureListener {  
//		Context contexto;
//
//		public MyGestureDetector (Context miContexto){
//			contexto = miContexto;
//		}
//
//		@Override  
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
//				float velocityY) {
//			Boolean swDeslizaHaciaIzquierda=false;
//			Boolean swDeslizaHaciaDerecha=false;
//			Boolean swEmpiezaIzquierdaAfuera=false;
//			Boolean swDeslizaHaciaAbajo=false;
//			Boolean swDeslizaHaciaArriba=false;
//
//			try {  
//
//				//-------se evalúa dónde empieza el delizamiento y en qué dirección va --------
//				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)  
//					return false; 
//
//
//				if (e1.getX() < 10.0)
//					swEmpiezaIzquierdaAfuera=true;
//				else
//					swEmpiezaIzquierdaAfuera=false;
//
//				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE //deslizar el dedo horizontal a la izquierda 
//						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
//					swDeslizaHaciaIzquierda = true;
//					swDeslizaHaciaDerecha = false;
//
//				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE //deslizar el dedo horizontal a la derecha  
//						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//					swDeslizaHaciaIzquierda = false;
//					swDeslizaHaciaDerecha = true;
//				}
//
//
//				if (cb != null) {
//					cb.onTeclaLiberada(teclaActual);
//				}
//
//			} catch (Exception e) {  
//				Log.e("GestureTest-MyGestureDetector", "Error en onFling mensaje: " + e.getMessage());  
//			}  
//			return false;  
//		}	
//	}  


}
