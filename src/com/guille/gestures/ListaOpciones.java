package com.guille.gestures;

import java.util.List;

public class ListaOpciones extends Lista{
	
	public ListaOpciones(List<String> OpcionesMenú) {
		super(OpcionesMenú);
	}

	public String leerLugarSelecciónActual(){
		String respuesta = "";
		switch (índiceActual) {
		case 0:
			respuesta = "Primer opción";	
			break;
		case 1:
			respuesta = "Segunda opción";	
			break;
		case 2:
			respuesta = "Tercera opción";
			break;
		case 3:
			respuesta = "Cuarta opción";
			break;
		case 4:
			respuesta = "Quinta opción";
			break;
		case 5:
			respuesta = "Sexta opción";
			break;
		case 6:
			respuesta = "Séptima opción";
			break;
		case 7:
			respuesta = "Octava opción";
			break;
		case 8:
			respuesta = "Novena opción";
			break;
		case 9:
			respuesta = "Décima opción";
			break;
		}
		return respuesta;
	}
	
	public String leerSelección(){
		String cadena = "";
		cadena = this.leerLugarSelecciónActual() + ". " + listaElementos.get(índiceActual);
		return cadena;
	}

}
