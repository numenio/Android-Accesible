package com.guille.gestures;  

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class SMSEditor extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, OnGesturePerformedListener//, OnTouchListener
{  
	private GestureLibrary gestureLib;
	private TextView txtTexto;
	private TextView txtNumero;
	private String cadenaSMS = "";
	private String numeroSMS = "";
	private int flagEstadoEscritura;
	private int flagUbicaciónFoco;
	private int flagTeclado;
	private GestureDetector gestureDetector;  
	private ListaOpciones opcionesEditor;
	private ListaContactos misContactos;
	//private List<Contacto> misContactos = new ArrayList<Contacto>();
	private GestureOverlayView gestureOverlayView;
	//private GestureOverlayView gestureOverlayViewCarrusel;
	//private SurfaceView surfaceView;
	int cantDedosMaxima;
	TecladoCarrusel tecladoCarrusel = new TecladoCarrusel();
	Boolean swMovimiento = false;
	List<String> misOpciones = new ArrayList<String>();

	private static final int SWIPE_MIN_DISTANCE = 120;  
	private static final int SWIPE_MAX_OFF_PATH = 250;  
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;  
	private static final int ESCRIBIR_NÚMERO = 0;
	private static final int ESCRIBIR_TEXTO_SMS = 1;
	private static final int FOCO_OPCIONES = 2;
	private static final int FOCO_ESCRITURA = 3;
	private static final int FOCO_CONTACTOS = 4;
	private static final int TIEMPO_ESPERA_PRESIÓN_PARA_OPCIONES = 500; //son milisegundos
	private static final int TECLADO_GESTURES = 300;
	private static final int TECLADO_PANELES = 301;
	private static final int TECLADO_CARRUSEL = 302;

	
	//TODO hacer un método que convierta las cadenas en cadenas aceptables para ser escuchadas, ej sos lo lee "ese o ese"
	//TODO evitar que el tel entre a inactividad, que los botones se pulsen por error
	/** Called when the activity is first created. */  
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);

		cargarTecladoGestures();

		txtTexto = (TextView) findViewById(R.id.txtTextoSMS);
		txtTexto.setText("");

		txtNumero = (TextView) findViewById(R.id.txtNumero);
		txtNumero.setText("");

		volverAlTextoMensaje();

		misOpciones.add("Enviar mensaje");
		misOpciones.add("Escribir símbolos");
		misOpciones.add("Escribir números");
		misOpciones.add("Cambiar teclado");
		opcionesEditor = new ListaOpciones(misOpciones);

		misContactos = new ListaContactos();
		
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void setTipoTeclado (int tipoTeclado){
		flagTeclado=tipoTeclado;
	}
	
	private int getTipoTeclado(){
		return flagTeclado;
	}

	private void quitarTecladoGestures() {
		ViewGroup vg = (ViewGroup) (gestureOverlayView.getParent());
		vg.removeView(gestureOverlayView);
	}
	
	private void cargarTecladoCarrusel() {
//		gestureOverlayView = new GestureOverlayView(this); 
		gestureOverlayView.setOrientation(gestureOverlayView.ORIENTATION_VERTICAL);
		//gestureOverlayView.setGestureStrokeType(gestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
//		View inflate = getLayoutInflater().inflate(R.layout.main, null);  
//		gestureOverlayView.addView(inflate);  
//		gestureOverlayView.addOnGesturePerformedListener(this);  
//		gestureOverlayView.setOnTouchListener(this);
		cargarGestosCarrusel();
		setTipoTeclado(TECLADO_CARRUSEL);
//		setContentView(gestureOverlayView);
//		gestureDetector = new GestureDetector(new MyGestureDetectorTecladoGestures(this));
		hablar("Cambiando al teclado carrusel. Para escribir, movete arriba o abajo buscando la letra que quieras escribir y aceptala con un toque en la pantalla");
	}

	private void cargarTecladoGestures() {
		gestureOverlayView = new GestureOverlayView(this); 
		gestureOverlayView.setOrientation(gestureOverlayView.ORIENTATION_HORIZONTAL);
		//gestureOverlayView.setGestureStrokeType(gestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
		View inflate = getLayoutInflater().inflate(R.layout.main, null);  
		gestureOverlayView.addView(inflate);  
		gestureOverlayView.addOnGesturePerformedListener(this);  
		//gestureOverlayView.setOnTouchListener(this);
		cargarGestosLetras();
		setContentView(gestureOverlayView);
		//gestureDetector = new GestureDetector(new MyGestureDetectorTecladoGestures(this));
		setTipoTeclado(TECLADO_GESTURES);
		hablar("Cambiando al teclado de gestos. Para escribir, dibujá letras en imprenta en la pantalla");
	}

	private void setEstadoEscritura(int estado){
		flagEstadoEscritura = estado;
	}

	private int getEstadoEscritura() {
		return flagEstadoEscritura;
	}

	private void setFoco(int lugar){
		flagUbicaciónFoco = lugar;
	}

	private int getFoco() {
		return flagUbicaciónFoco;
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {  
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		String predicción = "";
		if (predictions.size() > 0) { //se busca la predicción
			if (predictions.get(0).score > 1.0){
				predicción = predictions.get(0).name;				
			}
		}
		
//		if (getTipoTeclado() == TECLADO_CARRUSEL){ //TODO que no se escriba la letra hasta que se pulse
//			if (!predicción.equals("")){
//				if (predicción.equals("arriba"))
//					tecladoCarrusel.seleccionarAnterior();
//				
//				if (predicción.equals("abajo"))
//					tecladoCarrusel.seleccionarSiguiente();
//				
//				hablar("letra " + tecladoCarrusel.getTeclaSeleccionada());
//			}
//		}

		if (getTipoTeclado() == TECLADO_GESTURES) //somamente se escribe si es por gestos, si es carrusel, el gesto sólo mueve por las teclas
			escribir(predicción);
	}

	private void escribir(String predicción) {
		if (getFoco() == FOCO_CONTACTOS){

			if (predicción.equalsIgnoreCase("borrar"))
				volverAlTextoMensaje(); //TODO arreglar que en lugar de pasar al texto del mensaje, como cancela poner contacto, se pase a los números
			else {
				if (misContactos.saltarALetra(predicción) >= 0){ //si la letra tiene contactos
					misContactos.saltarALetra(predicción);
					hablar("Saltando a la letra " + predicción + ". Estás en el contacto " + misContactos.getContactoSeleccionado().getNombre());
				} else { //si la letra no tiene contactos
					hablar("No hay ningún contacto en la agenda que empiece con la letra " + predicción + ". Imposible saltar");
				}
			}

		}


		if (getFoco() == FOCO_ESCRITURA){
			if (predicción.equalsIgnoreCase("borrar"))
				borrarCaracter();
			else if (predicción.equalsIgnoreCase("espacio"))
				escribirEspacio();
			else
				escribirTexto(predicción);
		}

		if (getFoco() == FOCO_OPCIONES) {
			if (predicción.equalsIgnoreCase("borrar")){
				setFoco(FOCO_ESCRITURA);
				//se arma la cadena para cambiar el foco
				String cadena = "Cerrando las opciones. Estás otra vez en ";
				if (getEstadoEscritura() == ESCRIBIR_TEXTO_SMS){
					cadena += "el texto de tu mensaje";
					if (cadenaSMS.length() > 0)
						cadena += ". Llevás escrito " + cadenaSMS;
					else
						cadena += ". Todavía no escribiste nada";
				}

				if (getEstadoEscritura() == ESCRIBIR_NÚMERO){
					cadena += "el número del destinatario";
					if (numeroSMS.length() > 0)
						cadena += ". Ya está escrito " + numeroSMS;
					else
						cadena += ". No hay ningún número escrito";
				}

				hablar(cadena);
				
			}
		}
	}

	private void escribirTexto(String cadena) {
		switch (getEstadoEscritura()){
		case ESCRIBIR_NÚMERO:
			numeroSMS += cadena;
			txtNumero.setText(numeroSMS);
			hablar(cadena);// + ". El número escrito hasta ahora es " + numeroSMS);
			break;
		case ESCRIBIR_TEXTO_SMS:
			cadenaSMS += cadena;
			txtTexto.setText(cadenaSMS);
			hablar("Letra " + cadena);// + ". Llevás escrito: " + cadenaSMS);
			break;
		}
	}

	private void escribirEspacio() {
		switch (getEstadoEscritura()){
		case ESCRIBIR_NÚMERO:
			hablar("No se pueden escribir espacios en el número del destinatario");
			break;
		case ESCRIBIR_TEXTO_SMS:
			cadenaSMS += " ";
			txtTexto.setText(cadenaSMS);
			hablar("Espacio");// + ". Llevás escrito: " + cadenaSMS);
			break;
		}
	}

	@Override
	//public boolean onTouch(View v, MotionEvent event) {
	public boolean onTouchEvent(MotionEvent event) {
		//en las opciones dejar navegar, en escritura por supus escribir
		int cantDedosActual = 0;

		switch (event.getPointerCount()) {
		case 1:
			if (cantDedosMaxima < 1) 
				cantDedosMaxima = 1;

			cantDedosActual = 1;

			if (event.getAction() == event.ACTION_MOVE){
				swMovimiento = true;
			}

			if (event.getAction() == event.ACTION_UP){
				if (cantDedosMaxima == 1) {
					if (!swMovimiento) { //hay presión pero no hay movimiento
						//si se está en el texto del mensaje
						if (getEstadoEscritura() == ESCRIBIR_TEXTO_SMS) { 
							if (event.getEventTime() - event.getDownTime() > TIEMPO_ESPERA_PRESIÓN_PARA_OPCIONES) {
								//enviarSMS();
								if (getFoco() != FOCO_OPCIONES) {
									setFoco(FOCO_OPCIONES);
									opcionesEditor.seleccionarEn(0);
									hablar ("Abriendo las opciones, para seleccionarlas deslizá a " +
											"izquierda o derecha y aceptá con una pulsación corta. Estás " +
											"en la primer opción " + opcionesEditor.getElementoSeleccionado());
								}
							} else { //presión breve, si está en opciones aceptar la selección
								if (getFoco() == FOCO_OPCIONES)
									ejecutarOpciónMenú(opcionesEditor.getLugarSelecciónActual());
								else {//si no está en opciones, ver si el teclado es carrusel, y ahí escribir con el toque
									if (getTipoTeclado() == TECLADO_CARRUSEL){
										escribir(tecladoCarrusel.getTeclaSeleccionada());
									}
								}
							}
							//si se está en los números, la presión en lugar de abrir opciones
							//abre los contactos para no escribir número por números que pueda tener un contacto
						} 
						
						if (getEstadoEscritura() == ESCRIBIR_NÚMERO) { //TODO que el usuario pueda elegir entre varios números
							if (getFoco() == FOCO_OPCIONES || getFoco() == FOCO_CONTACTOS){
								if (misOpciones != null){ //si hay algún contacto en la lista
									if (event.getEventTime() - event.getDownTime() > TIEMPO_ESPERA_PRESIÓN_PARA_OPCIONES) {
										if (getFoco() != FOCO_CONTACTOS) {
											setFoco(FOCO_CONTACTOS);
											misContactos.seleccionarContactoEn(0);
											hablar ("Abriendo los Contactos, para moverte por ellos deslizá a " +
													"izquierda o derecha y aceptá con una pulsación corta. Estás " +
													"en " + misContactos.getContactoSeleccionado().getNombre());


										}
									} else { //presión breve, si está en opciones aceptar la selección
										if (getFoco() == FOCO_CONTACTOS){
											cargarContacto(misContactos.getLugarContactoActual());
										}
									}
								} else { //si la lista de contactos es nula
									hablar ("No hay contactos en su agenda. Por favor escribí manualmente el número al que querés enviar el mensaje");
									setFoco(FOCO_ESCRITURA);
								}
							}
							
							if (getFoco() == FOCO_ESCRITURA){
								if (getTipoTeclado() == TECLADO_CARRUSEL){
									escribir(tecladoCarrusel.getTeclaSeleccionada());
								}
							}
						}
					}
				}
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
			if (event.getAction() == event.ACTION_POINTER_2_UP){//si mantiene apretado el dedo uno y suelta el dos
				borrarCaracter();
				//quitarTecladoGestures();
				cargarTecladoCarrusel();
			}
		}


		if (cantDedosMaxima == 3 && cantDedosActual == 3) {
			if (event.getAction() == event.ACTION_POINTER_3_UP)
				enviarSMS();
		}
//TODO 
//		if (gestureDetector.onTouchEvent(event))  //se pasa el evento a onTouchEvent del gestureDetector
//			return true;  
//		else  
//			return false; 
		
		return false;
	}

	private void cargarContacto(int indiceContacto){
		Contacto cont = misContactos.getContactoEn(indiceContacto);

		if (cont.getTeléfonos() != null) {
			String numeroYaEscrito = numeroSMS;
			numeroSMS = cont.getTeléfonos().get(0);
			txtNumero.setText(numeroSMS);
			String cadena="";
			if (numeroYaEscrito.length() > 0){
				cadena = "Borrando el número " + numeroYaEscrito + " que habías escrito y ";
			}
			cadena += "Cargando el teléfono de " + cont.getNombre() + " con número " + numeroSMS + 
					". Ahora estás de nuevo en el texto de tu mensaje";
			if (cadenaSMS.length() > 0)
					cadena += ". Llevás escrito " + cadenaSMS;
			else
				cadena += "Todavía no escribiste aquí nada";
			
			hablar(cadena);
			
			volverAlTextoMensaje();
		} else {
			hablar("El contacto "  + cont.getNombre() + " no tiene número de teléfono");
		}
	}

	private void volverAlTextoMensaje() {
		setEstadoEscritura(ESCRIBIR_TEXTO_SMS);
		setFoco(FOCO_ESCRITURA);
		cargarGestosLetras();
	}

	private void borrarCaracter() {
		String caracterABorrar; 
		switch (getEstadoEscritura()) {
		case ESCRIBIR_NÚMERO:
			if (txtNumero.getText().length() != 0) {
				//String cadenaTexto = txtNumero.getText().toString();
				caracterABorrar = String.valueOf(numeroSMS.charAt(numeroSMS.length()-1));
				numeroSMS = numeroSMS.subSequence(0, numeroSMS.length()-1).toString();
				txtNumero.setText(numeroSMS);
				hablar("Borrando el número " + caracterABorrar + ". Queda escrito " + numeroSMS);
			} else {
				hablar("ya está todo el número borrado");
			}
			break;
		case ESCRIBIR_TEXTO_SMS:
			if (txtTexto.getText().length() != 0) {
				//String cadenaTexto = txtTexto.getText().toString();
				caracterABorrar = String.valueOf(cadenaSMS.charAt(cadenaSMS.length()-1));
				cadenaSMS = cadenaSMS.subSequence(0, cadenaSMS.length()-1).toString();
				txtTexto.setText(cadenaSMS);
				if (caracterABorrar != " ")
					hablar("Borrando la letra " + caracterABorrar + ". Queda escrito " + cadenaSMS);
				else
					hablar("Borrando el espacio. Queda escrito " + cadenaSMS);
			} else {
				hablar("Ya está todo el texto del mensaje borrado");
			}
			break;
		}
	}

	private void enviarSMS() {
		if (numeroSMS.length()>0 && cadenaSMS.length()>0) {             
			sendSMS(numeroSMS, cadenaSMS); //se envía el mensaje
		}
		else
			if (numeroSMS.length()<=0) {
				hablar("No se puede enviar el mensaje porque no escribiste el número del " +
						"destinatario. Por favor deslizá tu dedo horizontalmente " +
						"hacia la izquierda y escribí el número al cual querés enviar tu mensaje");
			} else if (cadenaSMS.length()<=0){
				hablar("No se puede enviar el mensaje porque no escribiste el texto del " +
						"mensaje. Por favor deslizá tu dedo horizontalmente " +
						"hacia la derecha y escribí el texto que querés que tenga tu mensaje");
				//				} else if (cadenaSMS.length()<=0 && numeroSMS.length()<=0){
				//					hablar("No se puede enviar el mensaje porque no escribió ni el texto del mensaje " +
				//							"ni el número del destinatario. Por favor escriba ambos y luego " +
				//							"deje dos dedos en la pantalla y luego presione con un tercero para enviar el mensaje");
			}
	}

	private void hablar (String cadena){
		((MiApp)super.getApplication()).hablar(cadena);
	}

//	class MyGestureDetectorTecladoGestures extends SimpleOnGestureListener {  
//		Context contexto;
//
//		public MyGestureDetectorTecladoGestures (Context miContexto){
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
//				if (getTipoTeclado() == TECLADO_CARRUSEL){
//					if (e1.getY() > e2.getY()) { //hacia arriba 
////						swDeslizaHaciaAbajo = true;
////						swDeslizaHaciaArriba = false;
//						tecladoCarrusel.seleccionarAnterior();
//						hablar(tecladoCarrusel.getTeclaSeleccionada());
//
//					} else if (e1.getY() < e2.getY()) { //hacia abajo  
////						swDeslizaHaciaAbajo = false;
////						swDeslizaHaciaArriba = true;
//						tecladoCarrusel.seleccionarSiguiente();
//						hablar(tecladoCarrusel.getTeclaSeleccionada());
//					}
//				}
//				//----------------fin evaluar deslizamiento-----------------
//
//				switch (getFoco()){
//				case FOCO_CONTACTOS:
//					//						if (swEmpiezaIzquierdaAfuera){
//					//							
//					//						}
//
//					if (swDeslizaHaciaIzquierda){
//						misContactos.seleccionarAnterior();
//						hablar(misContactos.getContactoSeleccionado().getNombre());
//					}
//
//					if (swDeslizaHaciaDerecha){
//						misContactos.seleccionarSiguiente();
//						hablar(misContactos.getContactoSeleccionado().getNombre());
//					}
//
//					break;
//				case FOCO_OPCIONES:
//					//						if (swEmpiezaIzquierdaAfuera){
//					//							
//					//						}
//
//					if (swDeslizaHaciaIzquierda){
//						opcionesEditor.seleccionarAnterior();
//						hablar(opcionesEditor.leerSelección());
//					}
//
//					if (swDeslizaHaciaDerecha){
//						opcionesEditor.seleccionarSiguiente();
//						hablar(opcionesEditor.leerSelección());
//					}
//
//					break;
//
//				case FOCO_ESCRITURA:
//					//el deslizamiento horizontal viene desde fuera por la izquierda: borrar
//					if (swEmpiezaIzquierdaAfuera){
//						borrarCaracter();
//						return true;
//					}
//
//					//hacia la izquierda
//					if (swDeslizaHaciaIzquierda){
//						if (flagEstadoEscritura != ESCRIBIR_NÚMERO){ //si no está en escribir número
//							cambiarANúmeros();
//							
//							flagEstadoEscritura = ESCRIBIR_NÚMERO;
//							String cadena = "Cambiando al número del destinatario. "; 
//							if (numeroSMS.length() > 0)
//								cadena += "Ya está escrito " + numeroSMS;
//							else
//								cadena += "No hay escrito ningún número";
//
//							hablar(cadena);
//						} else {
//							hablar("Ya estás en el número del destinatario, para cambiar al texto del " +
//									"mensaje arrastrá el dedo hacia la derecha. Hasta ahora el número que " +
//									"escribiste es " + numeroSMS);
//						}
//						return true;
//					}
//
//					//hacia la derecha
//					if (swDeslizaHaciaDerecha){
//						if (flagEstadoEscritura != ESCRIBIR_TEXTO_SMS){
//							cambiarALetras();
//								
//							flagEstadoEscritura = ESCRIBIR_TEXTO_SMS;
//							String cadena = "Cambiando al texto del mensaje. "; 
//							if (cadenaSMS.length() > 0)
//								cadena += "Llevás escrito " + cadenaSMS;
//							else
//								cadena += "No hay escrito nada en el mensaje";
//
//							hablar(cadena);
//						} else {
//							hablar("Ya estás en el texto del mensaje, para cambiar al número del " +
//									"destinatario arrastrá el dedo hacia la izquierda. Hasta ahora el " +
//									"mensaje que llevás escrito es " + cadenaSMS);
//						}
//						return true;
//					}
//					break;
//				}
//
//
//
//			} catch (Exception e) {  
//				Log.e("GestureTest-MyGestureDetector", "Error en onFling mensaje: " + e.getMessage());  
//			}  
//			return false;  
//		}	
//	}  
//	
	
	private void cambiarANúmeros() {
		if (getTipoTeclado() == TECLADO_GESTURES)
			cargarGestosNumeros();
		
		if (getTipoTeclado() == TECLADO_CARRUSEL)
			tecladoCarrusel.cambiarATecladoNumérico();
	}

	private void cambiarALetras() {
		if (getTipoTeclado() == TECLADO_GESTURES)
			cargarGestosLetras();
		
		if (getTipoTeclado() == TECLADO_CARRUSEL)
			tecladoCarrusel.cambiarATecladoAlfabético();
	}

	private void cargarGestosLetras() {
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures_letras);
		gestureLib.load();
	}

	private void cargarGestosNumeros() {
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures_numeros);
		gestureLib.load();
	}  
	
	private void cargarGestosCarrusel() {
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures_carrusel);
		gestureLib.load();
	}

	private void sendSMS(String phoneNumber, String message)
	{        
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		//---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					hablar ("El mensaje ha sido enviado correctamente");
					cadenaSMS = ""; //se resetean los campos de texto y número
					numeroSMS = "";
					txtNumero.setText(numeroSMS);
					txtTexto.setText(cadenaSMS);
					Toast.makeText(getBaseContext(), "SMS enviado", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					hablar ("Hubo un fallo genérico en el envío del mensaje. Quizás usted escribió " +
							"mal el número del destinatario");
					Toast.makeText(getBaseContext(), "Fallo Genérico", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					hablar ("El mensaje no se pudo enviar porque no hay servicio del operador");
					Toast.makeText(getBaseContext(), "No hay servicio", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					hablar ("Error enviando. PeDeU nulo");
					Toast.makeText(getBaseContext(), "PDU nulo", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					hablar("No se pudo enviar el mensaje porque se ha apagado la antena, quizás " +
							"está activo el modo avión");
					Toast.makeText(getBaseContext(), "Radio off", 
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		//---cuando el SMS fue entregado al otro aparato---
		registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					hablar("El mensaje ha llegado al teléfono del destinatario");
					Toast.makeText(getBaseContext(), "SMS entregado", 
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					hablar ("No se pudo entregar el mensaje al destinatario");
					Toast.makeText(getBaseContext(), "SMS no entregado", 
							Toast.LENGTH_SHORT).show();
					break;                        
				}
			}
		}, new IntentFilter(DELIVERED));        

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}

	private void ejecutarOpciónMenú (int opciónMenú){
		String opciónElegida = misOpciones.get(opciónMenú);

		if (opciónElegida == "Enviar mensaje")
			enviarSMS();
		if (opciónElegida =="Escribir símbolos")
			hablar("Esa función todavía no está implementada");
		if (opciónElegida =="Escribir números")
			hablar("Esa función todavía no está implementada");
		if (opciónElegida =="Cambiar teclado")
			hablar("Esa función todavía no está implementada");

	}

	class ListaContactos {
		List<Contacto> misContactos = new ArrayList<Contacto>();
		int índiceActual = 0;
		public static final int PICK_CONTACT = 1;

		public ListaContactos() {
			misContactos = obtenerInfoContactos(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI));
			misContactos = listarSóloContactosConTel(misContactos);
			ordenarContactosPorNombre();
		}
		
		protected List<Contacto> listarSóloContactosConTel(List<Contacto> misCont){
			List<Contacto> misContConTel = new ArrayList<Contacto>();
			if (misCont.size() > 0){
				for (int i=0; i < misCont.size(); i++){
					if (misCont.get(i).getTeléfonos() != null){
						misContConTel.add(misCont.get(i));
					}
				}
			}
			
			return misContConTel;
		}

		protected List<Contacto> obtenerInfoContactos(Intent intent)
		{
			List<Contacto> misContactos = new ArrayList<Contacto>();

			Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);      
			while (cursor.moveToNext()) 
			{
				Contacto contacto = new Contacto();
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				contacto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(
						ContactsContract.Contacts.DISPLAY_NAME))); 

				String hasPhone = cursor.getString(cursor.getColumnIndex(
						ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (hasPhone.equalsIgnoreCase("1")) //si tiene teléfono
				{
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
					List<String> teléfonos = new ArrayList<String>();
					while (phones.moveToNext()) 
					{
						teléfonos.add(phones.getString(phones.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.NUMBER)));
					}
					phones.close();
					contacto.setTeléfonos(teléfonos);
				}

				// Find Email Addresses
				Cursor emails = getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
						ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
				List<String> listaEmails = new ArrayList<String>();
				while (emails.moveToNext()) 
				{
					listaEmails.add(emails.getString(emails.getColumnIndex(
							ContactsContract.CommonDataKinds.Email.DATA)));
				}
				emails.close();
				contacto.setEmails(listaEmails);

				//			Cursor address = getContentResolver().query(
				//					ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
				//					null,
				//					ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
				//					null, null);
				//	    while (address.moveToNext()) 
				//	    { 
				//	      // These are all private class variables, don't forget to create them.
				//	      poBox      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
				//	      street     = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
				//	      city       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
				//	      state      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
				//	      postalCode = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
				//	      country    = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
				//	      type       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
				//	    }  //address.moveToNext()
				misContactos.add(contacto);
			}  //while (cursor.moveToNext())        
			cursor.close();
			return misContactos;
		}//obtenerInfoContactos
		
//		private void cargarNombresDesdeLista(){
//			if (misContactos != null){
//				for (int i=0; i < misContactos.size(); i++){
//					nombresContactos.add(misContactos.get(i).getNombre());
//				}
//			}
//		}
		
		private void ordenarContactosPorNombre(){
			Collections.sort(misContactos);
			//cargarNombresDesdeLista();
		}
		
		public int saltarALetra(String letra){
			if (misContactos != null){
				for (int i=0; i < misContactos.size(); i++){
					if (misContactos.get(i).getNombre().toUpperCase().startsWith(letra.toUpperCase())){
					índiceActual = i;
					return i;
					}
				}
				return -1;//si no hay contactos con esa letra
			}
			return -2; //si la lista de contactos está vacía
		}


		public List<Contacto> getTodosLosContactos() {
			return misContactos;
		}

		public int getCantContactos(){
			return misContactos.size();
		}

		public void seleccionarContactoEn(int nuevoLugarIndice){
			if (nuevoLugarIndice <= getCantContactos())
				índiceActual = nuevoLugarIndice;
			else
				índiceActual = getCantContactos();
		}

		public int getLugarContactoActual(){
			return índiceActual;
		}

		public Contacto getContactoSeleccionado(){
			return misContactos.get(índiceActual);
		}

		public Contacto getContactoEn(int índiceContacto){
			if (índiceContacto > getCantContactos()-1) índiceContacto = getCantContactos()-1;
			if (índiceContacto < 0) índiceContacto = 0;
			return misContactos.get(índiceContacto);
		}

		public Contacto getElementoSeleccionadoEn(int lugarDelMenú){
			if (lugarDelMenú < getCantContactos())
				return misContactos.get(lugarDelMenú);
			else
				return misContactos.get(getCantContactos()-1);
		}

		public void seleccionarSiguiente(){
			índiceActual++;
			if (índiceActual > getCantContactos()-1)
				índiceActual = getCantContactos()-1;
		}

		public void seleccionarAnterior(){
			índiceActual--;

			if (índiceActual < 0)
				índiceActual = 0;
		}
	} //CLASS listaContactos
	
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		hablar("entré a on double tap");
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		hablar("entré a on double tap event");
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		hablar("entré a on single tap confirmed");
		// TODO Auto-generated method stub
		return false;
	}

	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		hablar("entré a on gesture");
	}

	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		hablar("entré a on gesture cancelled");
	}

	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		hablar("entré a on gesture ended");
	}

	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		hablar("entré a on gesture started");
	}

	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		hablar("entré a on long press");
	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		hablar("entré a on scroll");
		return false;
	}

	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		hablar("entré a on show press");
	}

	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		hablar("entré a on single tap up");
		return false;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		hablar("entré a on down");
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		hablar("entré a on fling");
		return false;
	}

}  
