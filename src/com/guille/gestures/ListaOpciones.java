package com.guille.gestures;

import java.util.List;

public class ListaOpciones extends Lista{
	
	public ListaOpciones(List<String> OpcionesMen�) {
		super(OpcionesMen�);
	}

	public String leerLugarSelecci�nActual(){
		String respuesta = "";
		switch (�ndiceActual) {
		case 0:
			respuesta = "Primer opci�n";	
			break;
		case 1:
			respuesta = "Segunda opci�n";	
			break;
		case 2:
			respuesta = "Tercera opci�n";
			break;
		case 3:
			respuesta = "Cuarta opci�n";
			break;
		case 4:
			respuesta = "Quinta opci�n";
			break;
		case 5:
			respuesta = "Sexta opci�n";
			break;
		case 6:
			respuesta = "S�ptima opci�n";
			break;
		case 7:
			respuesta = "Octava opci�n";
			break;
		case 8:
			respuesta = "Novena opci�n";
			break;
		case 9:
			respuesta = "D�cima opci�n";
			break;
		}
		return respuesta;
	}
	
	public String leerSelecci�n(){
		String cadena = "";
		cadena = this.leerLugarSelecci�nActual() + ". " + listaElementos.get(�ndiceActual);
		return cadena;
	}

}
