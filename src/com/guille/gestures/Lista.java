package com.guille.gestures;

import java.util.List;

public class Lista {
	List<String> listaElementos;
	int �ndiceActual = 0;
	
	public Lista (List<String> OpcionesMen�) {
		listaElementos = OpcionesMen�;
	}
	
	public List<String> getTodosLosElementos() {
		return listaElementos;
	}
	
	public int getCantElementos(){
		return listaElementos.size();
	}
	
	public void seleccionarEn (int nuevoLugarIndice){
		if (nuevoLugarIndice <= getCantElementos())
			�ndiceActual = nuevoLugarIndice;
		else
			�ndiceActual = getCantElementos();
	}
	
	public int getLugarSelecci�nActual(){
		return �ndiceActual;
	}
	
	public String getElementoSeleccionado(){
		return listaElementos.get(�ndiceActual);
	}
	
	public String getElementoSeleccionadoEn(int lugarDelMen�){
		if (lugarDelMen� < getCantElementos())
			return listaElementos.get(lugarDelMen�);
		else
			return listaElementos.get(getCantElementos()-1);
	}
	
	public void seleccionarSiguiente(){
		�ndiceActual++;
		if (�ndiceActual > getCantElementos()-1)
			�ndiceActual = getCantElementos()-1;
	}
	
	public void seleccionarAnterior(){
		�ndiceActual--;
		
		if (�ndiceActual < 0)
			�ndiceActual = 0;
	}
}
