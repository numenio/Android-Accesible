package com.guille.gestures;  

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guille.gestures.Enums.Tecla;
import com.guille.gestures.Enums.TipoTecla;
import com.guille.gestures.UtilesCadena;


public class SMSEditor extends Activity implements ITecladoListener //IGestureListener//
{  

	private TextView txtTexto;
	private TextView txtNumero;
	//private LayoutInflater linflater;
	private LinearLayout l;
    static int pos = 0; //para la posici�n de las vistas
	private String cadenaSMS = "";
	private String numeroSMS = "";
//	private int flagEstadoEscritura;
//	private int flagUbicaci�nFoco;
	private EscribirEn flagEstadoEscritura;
	private Foco flagUbicaci�nFoco;
	private Teclado flagTecladoActual;

	private ListaOpciones opcionesEditor;
	private ListaContactos misContactos;
	private View tecladoEyesFree = null;
	private View tecladoGestures = null;


//	private Boolean swMovimiento = false;
	private List<String> misOpciones = new ArrayList<String>();

	private enum EscribirEn {N�MERO_SMS, TEXTO_SMS}
	private enum Foco {OPCIONES, ESCRITURA, CONTACTOS}
	private enum Teclado {EYES_FREE, GESTURES}
//	private static final int ESCRIBIR_N�MERO = 0;
//	private static final int ESCRIBIR_TEXTO_SMS = 1;
//	private static final int FOCO_OPCIONES = 2;
//	private static final int FOCO_ESCRITURA = 3;
//	private static final int FOCO_CONTACTOS = 4;
//	private static final int TIEMPO_ESPERA_PRESI�N_PARA_OPCIONES = 500; //son milisegundos

	//TODO hacer un m�todo que convierta las cadenas en cadenas aceptables para ser escuchadas, ej sos lo lee "ese o ese"
	//TODO evitar que el tel entre a inactividad, que los botones se pulsen por error
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);

		irAlTextoMensaje();

		misOpciones.add("Enviar mensaje");
		misOpciones.add("Escribir s�mbolos");
		misOpciones.add("Escribir n�meros");
		misOpciones.add("Cambiar teclado");
		opcionesEditor = new ListaOpciones(misOpciones);

		misContactos = new ListaContactos();

		setContentView(R.layout.main);

		txtTexto = (TextView) findViewById(R.id.txtTextoSMS);
		txtTexto.setText(""); 
		cadenaSMS = txtTexto.getText().toString();

		txtNumero = (TextView) findViewById(R.id.txtNumero);
		txtNumero.setText("");
		numeroSMS = txtNumero.getText().toString();
		
		l = (LinearLayout) findViewById(R.id.layoutFondo);
	    //linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    cargarTecladoEyesFree();

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void cargarTecladoEyesFree() {
		removerTeclado();
		setTecladoActual(Teclado.EYES_FREE);
		tecladoEyesFree = new TecladoEyesFree(this, this, false);//new AdaptadorTeclado(this, this));
		tecladoEyesFree.setId(pos);
		pos++;
		l.addView(tecladoEyesFree);
		hablar("abriendo la aplicaci�n de mensajes. En la pantalla hay dos c�rculos, uno interno en el que se elije qu� tecla uno quiere, y otro externo " +
		" en el que ten�s atajos de teclado");
	}
	
	private void cargarTecladoGestures() {
		removerTeclado();
		setTecladoActual(Teclado.GESTURES);
		hablar("abriendo el teclado de gestos");
		//TODO completar con el teclado Gestures
	}
	
	private void cambiarTeclado(Teclado t){ //cambio especificando el teclado
		switch(t){
		case EYES_FREE:
			cargarTecladoEyesFree();
			break;
		case GESTURES:
			cargarTecladoGestures();
			break;
		}
	}
	
	private void cambiarTeclado(){ //cambio autom�tico
		switch (getTecladoActual()){
		case EYES_FREE:
			cambiarTeclado(Teclado.GESTURES);
			break;
		case GESTURES:
			cambiarTeclado(Teclado.EYES_FREE);
			break;
		}
		
	}
	
	private void removerTeclado() {
		if (pos != 0) {
          pos--;
          View myView = l.findViewById(pos);
          l.removeView(myView);
      }
	}

	private void setD�ndeEscribir(EscribirEn estado){//int estado){
		flagEstadoEscritura = estado;
	}

	private EscribirEn getD�ndeEscribir() {
		return flagEstadoEscritura;
	}

	private void setFoco(Foco lugar){
		flagUbicaci�nFoco = lugar;
	}

	private Foco getFoco() {
		return flagUbicaci�nFoco;
	}
	
	private void setTecladoActual(Teclado t){
		flagTecladoActual = t;
	}

	private Teclado getTecladoActual() {
		return flagTecladoActual;
	}

	private void escribirEnN�meroSMS(String texto) {
		if (texto.equalsIgnoreCase("espacio")) {
			hablar("No se pueden escribir espacios en el n�mero del destinatario");
		}else{
			if (UtilesCadena.swEsN�mero(texto)){
				numeroSMS += texto;
				txtNumero.setText(numeroSMS);
				hablar(texto);// + ". El n�mero escrito hasta ahora es " + numeroSMS);
			} else {
				hablar("S�lo se pueden ingresar n�meros en el destinatario. Para buscar un contacto por nombre, abr� las opciones");
			}
		}
	}
	
	private void escribirEnTextoSMS(Tecla t) {
		if (t == Tecla.ESPACIO){//texto.equalsIgnoreCase("espacio")) {
			cadenaSMS += " ";
			txtTexto.setText(cadenaSMS);
			hablar("Espacio");// + ". Llev�s escrito: " + cadenaSMS);
		} else {
			cadenaSMS += UtilesCadena.traducirTeclarParaEscribir(t);//texto;
			txtTexto.setText(cadenaSMS);
			hablar(armarCadenaLeerTeclaEscrita(t));// + ". Llev�s escrito: " + cadenaSMS);
		}
	}

	private void escribirEnContactos(String texto) {
		if (misContactos.saltarALetra(texto) >= 0){ //si la letra tiene contactos
			misContactos.saltarALetra(texto);
			hablar("Saltando a la letra " + texto + ". Est�s en el contacto " + misContactos.getContactoSeleccionado().getNombre());
		} else { //si la letra no tiene contactos
			hablar("No hay ning�n contacto en la agenda que empiece con la letra " + texto + ". Imposible saltar");
		}
	}
	
	private String armarCadenaLeerTeclaEscrita(Tecla t){
		String cadena=""; 
		switch (UtilesCadena.reconocerTipoTecla(t)) {
		case LETRAS:
			cadena = "Letra " + UtilesCadena.traducirTeclaParaLeer(t);
			break;
		case S�MBOLOS:
			cadena = UtilesCadena.traducirTeclaParaLeer(t);
			break;
		case N�MEROS:
			cadena = "N�mero " + UtilesCadena.traducirTeclaParaLeer(t);
			break;
		default:
			cadena  = "Letra " + UtilesCadena.traducirTeclaParaLeer(t);
			break;
		}
		return cadena;
	}

	private void cargarContacto(int indiceContacto){
		Contacto cont = misContactos.getContactoEn(indiceContacto);

		if (cont.getTel�fonos() != null) {
			String numeroYaEscrito = numeroSMS;
			numeroSMS = cont.getTel�fonos().get(0);
			txtNumero.setText(numeroSMS);
			String cadena="";
			if (numeroYaEscrito.length() > 0){
				cadena = "Borrando el n�mero " + numeroYaEscrito + " que hab�as escrito y ";
			}
			cadena += "Cargando el tel�fono de " + cont.getNombre() + " con n�mero " + numeroSMS + 
					". Ahora est�s de nuevo en el texto de tu mensaje";
			if (cadenaSMS.length() > 0)
				cadena += ". Llev�s escrito " + UtilesCadena.traducirCadenaParaLeer(cadenaSMS);
			else
				cadena += "Todav�a no escribiste aqu� nada";

			hablar(cadena);

			irAlTextoMensaje();
		} else {
			hablar("El contacto "  + cont.getNombre() + " no tiene n�mero de tel�fono");
		}
	}

	private void irAlTextoMensaje() {
		setD�ndeEscribir(EscribirEn.TEXTO_SMS);
		setFoco(Foco.ESCRITURA);
	}

	
	
	private String armarCadenaPorCar�cter (String car�cter){
		String cadena="";
		String car�cterTraducido = UtilesCadena.traducirTeclaParaLeer(UtilesCadena.traducirCar�cterATecla(car�cter));
		car�cter = car�cter.toLowerCase();
		
		if (car�cter == " ") return " el espacio";
		
		if (UtilesCadena.swEsS�mbolo(car�cter))
			cadena = " el s�mbolo " + car�cterTraducido;
		
		if (UtilesCadena.swEsLetra(car�cter))
			cadena = " la letra " + car�cterTraducido;
		
		if (UtilesCadena.swEsN�mero(car�cter))
			cadena = " el n�mero " + car�cterTraducido;
		
		return cadena;
	}
	
	private void borrarCaracterEnTexto() {
		String caracterABorrar; 

		if (txtTexto.getText().length() != 0) {
			caracterABorrar = String.valueOf(cadenaSMS.charAt(cadenaSMS.length()-1));
			cadenaSMS = cadenaSMS.subSequence(0, cadenaSMS.length()-1).toString();
			txtTexto.setText(cadenaSMS);
			String cadena = "Borrando " + armarCadenaPorCar�cter(caracterABorrar);
			if (cadenaSMS.length() != 0)
				cadena += ". Queda escrito " + UtilesCadena.traducirCadenaParaLeer(cadenaSMS);
			else
				cadena += ". Ya est� todo borrado";
				
			hablar(cadena);
		} else {
			hablar("Ya est� todo el texto del mensaje borrado");
		}
	}
	
	private void borrarCaracterEnN�mero() {
		String caracterABorrar; 

		if (txtNumero.getText().length() != 0) {
			caracterABorrar = String.valueOf(numeroSMS.charAt(numeroSMS.length()-1));
			numeroSMS = numeroSMS.subSequence(0, numeroSMS.length()-1).toString();
			txtNumero.setText(numeroSMS);
			hablar("Borrando el n�mero " + caracterABorrar + ". Queda escrito " + numeroSMS);
		} else {
			hablar("ya est� todo el n�mero borrado");
		}
	}

	private void hablar (String cadena){
		((MiApp)super.getApplication()).hablar(cadena);
//		TTS.getInstance().hablar("Iniciando la aplicaci�n de mensajes");
	}

	private void enviarSMS() {
		numeroSMS = numeroSMS.trim();
		if (!UtilesCadena.swCadenaEsN�mero(numeroSMS)){
			hablar("No se puede enviar el mensaje porque en el n�mero del destinatario hay escrito algo que no es un n�mero. Por favor correj� y volv� a enviar");
			return;
		}
		
		if (numeroSMS.length()>0 && cadenaSMS.length()>0) {             
			sendSMS(numeroSMS, cadenaSMS); //se env�a el mensaje
		}
		else
			if (numeroSMS.length()<=0) {
				hablar("No se puede enviar el mensaje porque no escribiste el n�mero del " +
						"destinatario. Por favor desliz� tu dedo horizontalmente " +
						"hacia la izquierda y escrib� el n�mero al cual quer�s enviar tu mensaje");
			} else if (cadenaSMS.length()<=0){
				hablar("No se puede enviar el mensaje porque no escribiste el texto del " +
						"mensaje. Por favor desliz� tu dedo horizontalmente " +
						"hacia la derecha y escrib� el texto que quer�s que tenga tu mensaje");
				//				} else if (cadenaSMS.length()<=0 && numeroSMS.length()<=0){
				//					hablar("No se puede enviar el mensaje porque no escribi� ni el texto del mensaje " +
				//							"ni el n�mero del destinatario. Por favor escriba ambos y luego " +
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
					cadenaSMS = ""; //se resetean los campos de texto y n�mero
					numeroSMS = "";
					txtNumero.setText(numeroSMS);
					txtTexto.setText(cadenaSMS);
					Toast.makeText(getBaseContext(), "SMS enviado", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					hablar ("Hubo un fallo gen�rico en el env�o del mensaje. Quiz�s usted escribi� " +
							"mal el n�mero del destinatario");
					Toast.makeText(getBaseContext(), "Fallo Gen�rico", 
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
					hablar("No se pudo enviar el mensaje porque se ha apagado la antena, quiz�s " +
							"est� activo el modo avi�n");
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
					hablar("El mensaje ha llegado al tel�fono del destinatario");
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { //se evita que al pulsar back se salga de la aplicaci�n
		if (keyCode == KeyEvent.KEYCODE_BACK) return false;
		return super.onKeyDown(keyCode, event);
	}

	private void ejecutarOpci�nMen� (int opci�nMen�){
		String opci�nElegida = misOpciones.get(opci�nMen�);

		if (opci�nElegida == "Enviar mensaje")
			enviarSMS();
		if (opci�nElegida =="Escribir s�mbolos")
			hablar("Esa funci�n todav�a no est� implementada");
		if (opci�nElegida =="Escribir n�meros")
			hablar("Esa funci�n todav�a no est� implementada");
		if (opci�nElegida =="Cambiar teclado")
			cambiarTeclado();
	}

	class ListaContactos {
		List<Contacto> misContactos = new ArrayList<Contacto>();
		int �ndiceActual = 0;
		public static final int PICK_CONTACT = 1;

		public ListaContactos() {
			misContactos = obtenerInfoContactos(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI));
			misContactos = listarS�loContactosConTel(misContactos);
			ordenarContactosPorNombre();
		}

		protected List<Contacto> listarS�loContactosConTel(List<Contacto> misCont){
			List<Contacto> misContConTel = new ArrayList<Contacto>();
			if (misCont.size() > 0){
				for (int i=0; i < misCont.size(); i++){
					if (misCont.get(i).getTel�fonos() != null){
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

				if (hasPhone.equalsIgnoreCase("1")) //si tiene tel�fono
				{
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
					List<String> tel�fonos = new ArrayList<String>();
					while (phones.moveToNext()) 
					{
						tel�fonos.add(phones.getString(phones.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.NUMBER)));
					}
					phones.close();
					contacto.setTel�fonos(tel�fonos);
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
						�ndiceActual = i;
						return i;
					}
				}
				return -1;//si no hay contactos con esa letra
			}
			return -2; //si la lista de contactos est� vac�a
		}


		public List<Contacto> getTodosLosContactos() {
			return misContactos;
		}

		public int getCantContactos(){
			return misContactos.size();
		}

		public void seleccionarContactoEn(int nuevoLugarIndice){
			if (nuevoLugarIndice <= getCantContactos())
				�ndiceActual = nuevoLugarIndice;
			else
				�ndiceActual = getCantContactos();
		}

		public int getLugarContactoActual(){
			return �ndiceActual;
		}

		public Contacto getContactoSeleccionado(){
			return misContactos.get(�ndiceActual);
		}

		public Contacto getContactoEn(int �ndiceContacto){
			if (�ndiceContacto > getCantContactos()-1) �ndiceContacto = getCantContactos()-1;
			if (�ndiceContacto < 0) �ndiceContacto = 0;
			return misContactos.get(�ndiceContacto);
		}

		public Contacto getElementoSeleccionadoEn(int lugarDelMen�){
			if (lugarDelMen� < getCantContactos())
				return misContactos.get(lugarDelMen�);
			else
				return misContactos.get(getCantContactos()-1);
		}

		public void seleccionarSiguiente(){
			�ndiceActual++;
			if (�ndiceActual > getCantContactos()-1)
				�ndiceActual = getCantContactos()-1;
		}

		public void seleccionarAnterior(){
			�ndiceActual--;

			if (�ndiceActual < 0)
				�ndiceActual = 0;
		}
	} //CLASS listaContactos

	public void onTeclaApretada(Tecla t) {
		
	}

	public void onTeclaMantienePresionada(Tecla t) {
	
	}

	public void onTeclaLiberada(Tecla t) {
		if (t == Tecla.ENVIAR){
			enviarSMS();
			return;
		}
			
		switch (getFoco()){
		case CONTACTOS:
			analizarTeclaPulsadaEnContactos(t);
			break;
		case OPCIONES:
			analizarTeclaPulsadaEnOpciones(t);
			break;
		case ESCRITURA:
			if (getD�ndeEscribir() == EscribirEn.TEXTO_SMS) {
				analizarTeclaPulsadaEnEscrituraEnTextoSMS(t);
				return;
			}
			
			if (getD�ndeEscribir() == EscribirEn.N�MERO_SMS) {
				analizarTeclaPulsadaEnEscrituraEnN�meroSMS(t);
				return;
			}
			break;
		}
	}

	private void analizarTeclaPulsadaEnEscrituraEnN�meroSMS(Tecla t) {
		if (swSePuedeEscribir(t)) {
			escribirEnN�meroSMS(UtilesCadena.traducirTeclarParaEscribir(t));
			return;
		}

		if (t == Tecla.BORRAR){
			borrarCaracterEnN�mero();
			return;
		}

		if (t == Tecla.FLECHA_IZQ){
			hablar("Ya est�s en el n�mero del destinatario, para cambiar al texto del " +
					"mensaje arrastr� el dedo hacia la derecha. Hasta ahora el n�mero que " +
					"escribiste es " + numeroSMS);
			return;
		}

		if (t == Tecla.FLECHA_DER){
			flagEstadoEscritura = EscribirEn.TEXTO_SMS;
			TecladoEyesFree miTeclado = (TecladoEyesFree)this.findViewById(tecladoEyesFree.getId());
			miTeclado.mostrarTodasLasTeclas();
			String cadena = "Cambiando al texto del mensaje. "; 
			if (cadenaSMS.length() > 0)
				cadena += "Llev�s escrito " + UtilesCadena.traducirCadenaParaLeer(cadenaSMS);
			else
				cadena += "No hay escrito nada en el mensaje";

			hablar(cadena);
			return;
		}
		
		if (t == Tecla.OPCIONES) {
			if (misContactos != null){ //si hay alg�n contacto en la lista
				if (getFoco() != Foco.CONTACTOS) {
					setFoco(Foco.CONTACTOS);
					TecladoEyesFree miTeclado = (TecladoEyesFree)this.findViewById(tecladoEyesFree.getId());
					miTeclado.mostrarLetrasN�meros();
					misContactos.seleccionarContactoEn(0);
					hablar ("Abriendo los Contactos, para moverte por ellos desliz� a " +
							"izquierda o derecha y acept� con una pulsaci�n corta. Est�s " +
							"en " + misContactos.getContactoSeleccionado().getNombre());
				}
			} else { //si la lista de contactos es nula
				hablar ("No hay contactos en su agenda. Por favor escrib� manualmente el n�mero al que quer�s enviar el mensaje");
				setFoco(Foco.ESCRITURA);
			}
		}
	}

	private void analizarTeclaPulsadaEnEscrituraEnTextoSMS(Tecla t) {
		if (swSePuedeEscribir(t)){
			escribirEnTextoSMS(t);
			return;
		}
		
		
		if (t == Tecla.BORRAR){
			borrarCaracterEnTexto();
			return;
		}

		if (t == Tecla.FLECHA_IZQ){
			flagEstadoEscritura = EscribirEn.N�MERO_SMS;
			TecladoEyesFree miTeclado = (TecladoEyesFree)this.findViewById(tecladoEyesFree.getId());
			miTeclado.mostrarS�loN�meros();
			String cadena = "Cambiando al n�mero del destinatario. "; 
			if (numeroSMS.length() > 0)
				cadena += "Ya est� escrito " + numeroSMS;
			else
				cadena += "No hay escrito ning�n n�mero";

			hablar(cadena);
			return;
		}

		if (t == Tecla.FLECHA_DER){
			hablar("Ya est�s en el texto del mensaje, para cambiar al n�mero del " +
					"destinatario arrastr� el dedo hacia la izquierda. Hasta ahora el " +
					"mensaje que llev�s escrito es " + UtilesCadena.traducirCadenaParaLeer(cadenaSMS));
			return;
		}
		
		if (t == Tecla.OPCIONES) {
			setFoco(Foco.OPCIONES);
			opcionesEditor.seleccionarEn(0);
			hablar ("Abriendo las opciones, para seleccionarlas desliz� a " +
					"izquierda o derecha y acept� con una pulsaci�n corta. Est�s " +
					"en la primer opci�n " + opcionesEditor.getElementoSeleccionado());
		}
	}

	private void analizarTeclaPulsadaEnOpciones(Tecla t) {
		if (t == Tecla.FLECHA_IZQ){
			opcionesEditor.seleccionarAnterior();
			hablar(opcionesEditor.leerSelecci�n());
		}

		if (t == Tecla.FLECHA_DER){
			opcionesEditor.seleccionarSiguiente();
			hablar(opcionesEditor.leerSelecci�n());
		}
		
		if (t == Tecla.ENTER) { 
			ejecutarOpci�nMen�(opcionesEditor.getLugarSelecci�nActual());
		}
		
		if (t == Tecla.ESCAPE){
			setFoco(Foco.ESCRITURA);
			//se arma la cadena para cambiar el foco
			String cadena = "Cerrando las opciones. Est�s otra vez en ";
			if (getD�ndeEscribir() == EscribirEn.TEXTO_SMS){
				cadena += "el texto de tu mensaje";
				if (cadenaSMS.length() > 0)
					cadena += ". Llev�s escrito " + UtilesCadena.traducirCadenaParaLeer(cadenaSMS);
				else
					cadena += ". Todav�a no escribiste nada";
			}
			hablar(cadena);
		}
	}

	private void analizarTeclaPulsadaEnContactos(Tecla t) {
		if (swSePuedeEscribir(t)){
			escribirEnContactos(UtilesCadena.traducirTeclarParaEscribir(t));
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
		case ESCAPE:
		case BORRAR:
		case NINGUNA:
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