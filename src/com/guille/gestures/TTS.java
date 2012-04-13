package com.guille.gestures;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import com.guille.gestures.MiApp;
import android.util.Log;

//public class TTS implements TextToSpeech.OnInitListener {
//	TextToSpeech voz;
//
//	public void onInit(int status) {
//		try {
//			//Locale loc = new Locale("es", "", "");
//			//if (voz.isLanguageAvailable)
//			//voz.setLanguage(loc);
//			voz.setLanguage(java.util.Locale.getDefault());
//			//voz.speak("hola mundo", TextToSpeech.QUEUE_FLUSH, null);
//			hablar("Iniciando la aplicación de mensajes");
//		} catch (Exception ex) {
//			Log.e("Aplicación", "Error en onInit: " + ex.getMessage());
//		}
//
//	}
//
//	public TTS(Context contexto) {
//		voz = new TextToSpeech(contexto, this);
//		voz.setLanguage(java.util.Locale.getDefault());
//		hablar("hola mundo");
//		cerrarVoz();
//	}
//
//	public void cerrarVoz() {
//		voz.shutdown();
//	}
//
//	public void hablar (String cadena) {
//		voz.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
//	}
//
//}

public class TTS implements TextToSpeech.OnInitListener {
	private TextToSpeech voz;
	MiApp aplic = null;
	private Context context;	     
	private static final TTS singleton = new TTS();

	public static TTS getInstance() {
		return singleton;
	}

	private TTS() {
		aplic = (MiApp) new Application();
		context = aplic.getApplicationContext();
		// Initialize text-to-speech. This is an asynchronous operation.
		// The OnInitListener (second argument) is called after initialization completes.
		voz = new TextToSpeech(context, this);
	}

	public void hablar(String cadena) {
		voz.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			voz.setLanguage(java.util.Locale.getDefault());
		} else {
			// Initialization failed.
			Log.e("Mi TTS", "Error inicializando el TTS");
		}
	}

	public void cerrar() {
		shutdown();
	}

	private void shutdown() {
		if (voz != null) {
			voz.stop();
			voz.shutdown();
			voz = null;
		}
	}

	public boolean estáHablando() {
		return voz!=null && voz.isSpeaking();
	}

}