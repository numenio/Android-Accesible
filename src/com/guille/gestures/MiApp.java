package com.guille.gestures;

import java.util.Locale;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class MiApp extends Application implements TextToSpeech.OnInitListener {
	TextToSpeech voz;
	
	public void onInit(int status) {
		try {
			// TODO Auto-generated method stub
			Locale loc = new Locale("es", "", "");
			//if (voz.isLanguageAvailable)
			voz.setLanguage(loc);
			//voz.speak("hola mundo", TextToSpeech.QUEUE_FLUSH, null);
			hablar("hola mundo");
		} catch (Exception ex) {
			Log.e("Aplicación", "Error en onInit: " + ex.getMessage());
		}
		
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		voz = new TextToSpeech(this, this);
		hablar("hola mundo");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		voz.shutdown();
	}
	
	public void hablar (String cadena) {
		voz.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
	}
	
}
