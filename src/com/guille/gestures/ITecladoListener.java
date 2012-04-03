package com.guille.gestures;

import com.guille.gestures.Enums.Tecla;

public interface ITecladoListener {
	public void onTeclaApretada (Tecla t);
	
	public void onTeclaMantienePresionada (Tecla t);
	
	public void onTeclaLiberada (Tecla t);
}
