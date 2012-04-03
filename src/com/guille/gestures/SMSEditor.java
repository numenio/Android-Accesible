package com.guille.gestures;  

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.guille.gestures.Enums.Tecla;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
//import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.guille.gestures.Enums.Gesture;


public class SMSEditor extends Activity implements ITecladoListener //IGestureListener//
{  

	private TextView txtTexto;
	private TextView txtNumero;
	LayoutInflater linflater;
    LinearLayout l;
    static int pos = 0; //para la posición de las vistas
	private String cadenaSMS = "";
	private String numeroSMS = "";
//	private int flagEstadoEscritura;
//	private int flagUbicaciónFoco;
	private EscribirEn flagEstadoEscritura;
	private Foco flagUbicaciónFoco;

	private ListaOpciones opcionesEditor;
	private ListaContactos misContactos;
	private View tecladoEyesFree = null;
	private View tecladoGestures = null;


//	private Boolean swMovimiento = false;
	private List<String> misOpciones = new ArrayList<String>();

	private enum EscribirEn {NÚMERO_SMS, TEXTO_SMS}
	private enum Foco {OPCIONES, ESCRITURA, CONTACTOS}
//	private static final int ESCRIBIR_NÚMERO = 0;
//	private static final int ESCRIBIR_TEXTO_SMS = 1;
//	private static final int FOCO_OPCIONES = 2;
//	private static final int FOCO_ESCRITURA = 3;
//	private static final int FOCO_CONTACTOS = 4;
//	private static final int TIEMPO_ESPERA_PRESIÓN_PARA_OPCIONES = 500; //son milisegundos

	//TODO hacer un método que convierta las cadenas en cadenas aceptables para ser escuchadas, ej sos lo lee "ese o ese"
	//TODO evitar que el tel entre a inactividad, que los botones se pulsen por error
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);

		irAlTextoMensaje();

		misOpciones.add("Enviar mensaje");
		misOpciones.add("Escribir símbolos");
		misOpciones.add("Escribir números");
		misOpciones.add("Cambiar teclado");
		opcionesEditor = new ListaOpciones(misOpciones);

		misContactos = new ListaContactos();

		setContentView(R.layout.main);

		txtTexto = (TextView) findViewById(R.id.txtTextoSMS);
		txtTexto.setText("hola"); //TODO borrar los hola

		txtNumero = (TextView) findViewById(R.id.txtNumero);
		txtNumero.setText("hola");
		
		l = (LinearLayout) findViewById(R.id.layoutFondo);
	    linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    cargarTecladoEyesFree();

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void cargarTecladoEyesFree() {
		removerTeclado();
		tecladoEyesFree = new TecladoEyesFree(this, new AdaptadorTeclado(this, this));
		tecladoEyesFree.setId(pos);
		pos++;
		l.addView(tecladoEyesFree);
	}
	
	private void cargarTecladoGestures() {
		removerTeclado();
		//TODO completar con el teclado Gestures
	}
	
	private void removerTeclado() {
		if (pos != 0) {
          pos--;
          View myView = l.findViewById(pos);
          l.removeView(myView);
      }
	}

	private void setEstadoEscritura(EscribirEn estado){//int estado){
		flagEstadoEscritura = estado;
	}

	private EscribirEn getEstadoEscritura() {
		return flagEstadoEscritura;
	}

	private void setFoco(Foco lugar){
		flagUbicaciónFoco = lugar;
	}

	private Foco getFoco() {
		return flagUbicaciónFoco;
	}

	private void escribirEnNúmeroSMS(String texto) {
		if (texto.equalsIgnoreCase("espacio")) {
			hablar("No se pueden escribir espacios en el número del destinatario");
		}else{
			numeroSMS += texto;
			txtNumero.setText(numeroSMS);
			hablar(texto);// + ". El número escrito hasta ahora es " + numeroSMS);
		}
	}
	
	private void escribirEnTextoSMS(String texto) {
		if (texto.equalsIgnoreCase("borrar"))
			borrarCaracterEnTexto();
		else if (texto.equalsIgnoreCase("espacio")) {
			cadenaSMS += " ";
			txtTexto.setText(cadenaSMS);
			hablar("Espacio");// + ". Llevás escrito: " + cadenaSMS);
		} else {
			cadenaSMS += texto;
			txtTexto.setText(cadenaSMS);
			hablar("Letra " + texto);// + ". Llevás escrito: " + cadenaSMS);
		}
	}

	private void escribirEnContactos(String texto) {
		if (misContactos.saltarALetra(texto) >= 0){ //si la letra tiene contactos
			misContactos.saltarALetra(texto);
			hablar("Saltando a la letra " + texto + ". Estás en el contacto " + misContactos.getContactoSeleccionado().getNombre());
		} else { //si la letra no tiene contactos
			hablar("No hay ningún contacto en la agenda que empiece con la letra " + texto + ". Imposible saltar");
		}
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

			irAlTextoMensaje();
		} else {
			hablar("El contacto "  + cont.getNombre() + " no tiene número de teléfono");
		}
	}

	private void irAlTextoMensaje() {
		setEstadoEscritura(EscribirEn.TEXTO_SMS);
		setFoco(Foco.ESCRITURA);
	}

	private void borrarCaracterEnTexto() {
		String caracterABorrar; 

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
	}
	
	private void borrarCaracterEnNúmero() {
		String caracterABorrar; 

		if (txtNumero.getText().length() != 0) {
			//String cadenaTexto = txtNumero.getText().toString();
			caracterABorrar = String.valueOf(numeroSMS.charAt(numeroSMS.length()-1));
			numeroSMS = numeroSMS.subSequence(0, numeroSMS.length()-1).toString();
			txtNumero.setText(numeroSMS);
			hablar("Borrando el número " + caracterABorrar + ". Queda escrito " + numeroSMS);
		} else {
			hablar("ya está todo el número borrado");
		}
	}

	private void hablar (String cadena){
		((MiApp)super.getApplication()).hablar(cadena);
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


	
	
	
	
	public void onTeclaApretada(Tecla t) {
		// TODO Auto-generated method stub

	}




	public void onTeclaMantienePresionada(Tecla t) {
		// TODO Auto-generated method stub

	}




	public void onTeclaLiberada(Tecla t) {
		// TODO Que en getFoco evalue la tecla que se envíe, puede ser letra, num, simbolo, o comando (ej, flecha)

		switch (getFoco()){
		case CONTACTOS:
			analizarTeclaPulsadaEnContactos(t);
			break;
		case OPCIONES:
			analizarTeclaPulsadaEnOpciones(t);
			break;
		case ESCRITURA:
			if (getEstadoEscritura() == EscribirEn.TEXTO_SMS)
				analizarTeclaPulsadaEnEscrituraEnTextoSMS(t);
			
			if (getEstadoEscritura() == EscribirEn.NÚMERO_SMS)
				analizarTeclaPulsadaEnEscrituraEnNúmeroSMS(t);
			
			break;
		}
	}

	private void analizarTeclaPulsadaEnEscrituraEnNúmeroSMS(Tecla t) {
		if (swSePuedeEscribir(t)) {
			escribirEnNúmeroSMS(t.name());
			return;
		}
				
		if (t == Tecla.BORRAR){
			borrarCaracterEnNúmero();
			return;
		}

		if (t == Tecla.FLECHA_IZQ){
			hablar("Ya estás en el número del destinatario, para cambiar al texto del " +
					"mensaje arrastrá el dedo hacia la derecha. Hasta ahora el número que " +
					"escribiste es " + numeroSMS);
			return;
		}

		if (t == Tecla.FLECHA_DER){
			flagEstadoEscritura = EscribirEn.TEXTO_SMS;
			String cadena = "Cambiando al texto del mensaje. "; 
			if (cadenaSMS.length() > 0)
				cadena += "Llevás escrito " + cadenaSMS;
			else
				cadena += "No hay escrito nada en el mensaje";

			hablar(cadena);
			return;
		}
		
		if (t == Tecla.OPCIONES) {
			if (misContactos != null){ //si hay algún contacto en la lista
				if (getFoco() != Foco.CONTACTOS) {
					setFoco(Foco.CONTACTOS);
					misContactos.seleccionarContactoEn(0);
					hablar ("Abriendo los Contactos, para moverte por ellos deslizá a " +
							"izquierda o derecha y aceptá con una pulsación corta. Estás " +
							"en " + misContactos.getContactoSeleccionado().getNombre());
				}
			} else { //si la lista de contactos es nula
				hablar ("No hay contactos en su agenda. Por favor escribí manualmente el número al que querés enviar el mensaje");
				setFoco(Foco.ESCRITURA);
			}
		}
	}

	private void analizarTeclaPulsadaEnEscrituraEnTextoSMS(Tecla t) {
		if (swSePuedeEscribir(t)){
			escribirEnTextoSMS(t.name());
			return;
		}
		
		
		if (t == Tecla.BORRAR){
			borrarCaracterEnTexto();
			return;
		}

		if (t == Tecla.FLECHA_IZQ){
			flagEstadoEscritura = EscribirEn.NÚMERO_SMS;
			String cadena = "Cambiando al número del destinatario. "; 
			if (numeroSMS.length() > 0)
				cadena += "Ya está escrito " + numeroSMS;
			else
				cadena += "No hay escrito ningún número";

			hablar(cadena);
			return;
		}

		if (t == Tecla.FLECHA_DER){
			hablar("Ya estás en el texto del mensaje, para cambiar al número del " +
					"destinatario arrastrá el dedo hacia la izquierda. Hasta ahora el " +
					"mensaje que llevás escrito es " + cadenaSMS);
			return;
		}
		
		if (t == Tecla.OPCIONES) {
			setFoco(Foco.OPCIONES);
			opcionesEditor.seleccionarEn(0);
			hablar ("Abriendo las opciones, para seleccionarlas deslizá a " +
					"izquierda o derecha y aceptá con una pulsación corta. Estás " +
					"en la primer opción " + opcionesEditor.getElementoSeleccionado());
		}
	}

	private void analizarTeclaPulsadaEnOpciones(Tecla t) {
		if (t == Tecla.FLECHA_IZQ){
			opcionesEditor.seleccionarAnterior();
			hablar(opcionesEditor.leerSelección());
		}

		if (t == Tecla.FLECHA_DER){
			opcionesEditor.seleccionarSiguiente();
			hablar(opcionesEditor.leerSelección());
		}
		
		if (t == Tecla.ENTER) { 
			ejecutarOpciónMenú(opcionesEditor.getLugarSelecciónActual());
		}
		
		if (t == Tecla.ESCAPE){
			irAlTextoMensaje();
		}
	}

	private void analizarTeclaPulsadaEnContactos(Tecla t) {
		if (swSePuedeEscribir(t)){
			escribirEnContactos(t.name());
			return;
		}
		
		if (t == Tecla.FLECHA_IZQ){
			misContactos.seleccionarAnterior();
			hablar(misContactos.getContactoSeleccionado().getNombre());
		}

		if (t == Tecla.FLECHA_DER){
			misContactos.seleccionarSiguiente();
			hablar(misContactos.getContactoSeleccionado().getNombre());
		}
		
		if (t == Tecla.ENTER){
			cargarContacto(misContactos.getLugarContactoActual());
		}
		
		if (t == Tecla.ESCAPE){
			irAlTextoMensaje();
		}
	}
	
	private Boolean swSePuedeEscribir (Tecla t){
		switch (t){
		case FLECHA_AB:
		case FLECHA_ARR:
		case FLECHA_DER:
		case FLECHA_IZQ:
		case ENTER:
		case OPCIONES:
		case BORRAR:
			return false;
		default:
			return true;
		}
	}
}  







//
//
//@Override
//public void onCreate(Bundle savedInstanceState) {
// 
//    setContentView(R.layout.main);
//    btn = (Button) findViewById(R.id.Button01);
//    btn1 = (Button) findViewById(R.id.Button02);
//    
//    btn.setOnClickListener(new myListener());
//    btn1.setOnClickListener(new myListener1());
//}
//
//class myListener implements View.OnClickListener {
//    @Override
//    public void onClick(View v) {
//        View myView = linflater.inflate(R.layout.dynamicoption, null);
//        myView.setId(pos);
//        pos++;
//        l.addView(myView);
//    }
//}
//
//class myListener1 implements View.OnClickListener {
//    @Override
//    public void onClick(View v) {
//        
//    }
//}