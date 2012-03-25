package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;

public class TecladoCarrusel {

	List<String> misTeclas = new ArrayList<String>();
	int �ndiceActual = 0;
	
	public TecladoCarrusel() {
		cargarTecladoAlfab�tico();
	}
	
	public void cambiarATecladoNum�rico(){
		cargarTecladoNum�rico();
		seleccionarTeclaEn(0);
	}
	
	public void cambiarATecladoAlfab�tico(){
		cargarTecladoAlfab�tico();
		seleccionarTeclaEn(0);
	}
	
	
	private void cargarTecladoNum�rico(){
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


	private void cargarTecladoAlfab�tico() {
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
		misTeclas.add("�");
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
			�ndiceActual = nuevoLugarIndice;
		else
			�ndiceActual = getCantTeclas();
	}

	public int getLugarTeclaActual(){
		return �ndiceActual;
	}

	public String getTeclaSeleccionada(){
		return misTeclas.get(�ndiceActual);
	}

	public String getContactoEn(int �ndiceContacto){
		if (�ndiceContacto > getCantTeclas()-1) �ndiceContacto = getCantTeclas()-1;
		if (�ndiceContacto < 0) �ndiceContacto = 0;
		return misTeclas.get(�ndiceContacto);
	}

	public String getTeclaSeleccionadoEn(int lugarDelTeclado){
		if (lugarDelTeclado < getCantTeclas())
			return misTeclas.get(lugarDelTeclado);
		else
			return misTeclas.get(getCantTeclas()-1);
	}

	public void seleccionarSiguiente(){
		�ndiceActual++;
		if (�ndiceActual > getCantTeclas()-1)
			�ndiceActual = getCantTeclas()-1;
	}

	public void seleccionarAnterior(){
		�ndiceActual--;

		if (�ndiceActual < 0)
			�ndiceActual = 0;
	}
} //CLASS TecladoCarrusel
