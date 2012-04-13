package com.guille.gestures;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class MiApp extends Application implements TextToSpeech.OnInitListener {
	TextToSpeech voz;
	
	public void onInit(int status) {
		try {
			//Locale loc = new Locale("es", "", "");
			//if (voz.isLanguageAvailable)
			//voz.setLanguage(loc);
			if (status == TextToSpeech.ERROR)
				Log.d("Resultado onInit", Integer.toString(status));
			
			voz.setLanguage(java.util.Locale.getDefault());
			hablar("Iniciando la aplicación de mensajes");
		} catch (Exception ex) {
			Log.e("Aplicación", "Error en onInit: " + ex.getMessage());
		}
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		voz = new TextToSpeech(this, this);
//		TTS voz = TTS.getInstance();
		hablar("Iniciando la aplicación de mensajes");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		voz.shutdown();
	}
	
	public void hablar (String cadena) {
		voz.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
	}
	
}
