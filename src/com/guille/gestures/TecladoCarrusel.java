package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;

public class TecladoCarrusel {

	List<String> misTeclas = new ArrayList<String>();
	int índiceActual = 0;
	
	public TecladoCarrusel() {
		cargarTecladoAlfabético();
	}
	
	public void cambiarATecladoNumérico(){
		cargarTecladoNumérico();
		seleccionarTeclaEn(0);
	}
	
	public void cambiarATecladoAlfabético(){
		cargarTecladoAlfabético();
		seleccionarTeclaEn(0);
	}
	
	
	private void cargarTecladoNumérico(){
		misTeclas.clear();
		misTeclas.add("0");
		misTeclas.add("1");
		misTeclas.add("2");
		misTeclas.add("3");
		misTeclas.add("4");
		misTeclas.add("5");
		misTeclas.add("6");
		misTeclas.add("7");
		misTeclas.add("8");
		misTeclas.add("9");
		misTeclas.add("borrar");
		misTeclas.add("espacio");
	}


	private void cargarTecladoAlfabético() {
		misTeclas.clear();
		misTeclas.add("a");
		misTeclas.add("b");
		misTeclas.add("c");
		misTeclas.add("d");
		misTeclas.add("e");
		misTeclas.add("f");
		misTeclas.add("g");
		misTeclas.add("h");
		misTeclas.add("i");
		misTeclas.add("j");
		misTeclas.add("k");
		misTeclas.add("l");
		misTeclas.add("m");
		misTeclas.add("n");
		misTeclas.add("ñ");
		misTeclas.add("o");
		misTeclas.add("p");
		misTeclas.add("q");
		misTeclas.add("r");
		misTeclas.add("s");
		misTeclas.add("t");
		misTeclas.add("u");
		misTeclas.add("v");
		misTeclas.add("w");
		misTeclas.add("x");
		misTeclas.add("y");
		misTeclas.add("z");
		misTeclas.add("borrar");
		misTeclas.add("espacio");
	}
	
	

	public List<String> getTodoElTeclado() {
		return misTeclas;
	}

	public int getCantTeclas(){
		return misTeclas.size();
	}

	public void seleccionarTeclaEn(int nuevoLugarIndice){
		if (nuevoLugarIndice <= getCantTeclas())
			índiceActual = nuevoLugarIndice;
		else
			índiceActual = getCantTeclas();
	}

	public int getLugarTeclaActual(){
		return índiceActual;
	}

	public String getTeclaSeleccionada(){
		return misTeclas.get(índiceActual);
	}

	public String getContactoEn(int índiceContacto){
		if (índiceContacto > getCantTeclas()-1) índiceContacto = getCantTeclas()-1;
		if (índiceContacto < 0) índiceContacto = 0;
		return misTeclas.get(índiceContacto);
	}

	public String getTeclaSeleccionadoEn(int lugarDelTeclado){
		if (lugarDelTeclado < getCantTeclas())
			return misTeclas.get(lugarDelTeclado);
		else
			return misTeclas.get(getCantTeclas()-1);
	}

	public void seleccionarSiguiente(){
		índiceActual++;
		if (índiceActual > getCantTeclas()-1)
			índiceActual = getCantTeclas()-1;
	}

	public void seleccionarAnterior(){
		índiceActual--;

		if (índiceActual < 0)
			índiceActual = 0;
	}
} //CLASS TecladoCarrusel
