package com.guille.gestures;

import java.util.List;

public class Lista {
	List<String> listaElementos;
	int índiceActual = 0;
	
	public Lista (List<String> OpcionesMenú) {
		listaElementos = OpcionesMenú;
	}
	
	public List<String> getTodosLosElementos() {
		return listaElementos;
	}
	
	public int getCantElementos(){
		return listaElementos.size();
	}
	
	public void seleccionarEn (int nuevoLugarIndice){
		if (nuevoLugarIndice <= getCantElementos())
			índiceActual = nuevoLugarIndice;
		else
			índiceActual = getCantElementos();
	}
	
	public int getLugarSelecciónActual(){
		return índiceActual;
	}
	
	public String getElementoSeleccionado(){
		return listaElementos.get(índiceActual);
	}
	
	public String getElementoSeleccionadoEn(int lugarDelMenú){
		if (lugarDelMenú < getCantElementos())
			return listaElementos.get(lugarDelMenú);
		else
			return listaElementos.get(getCantElementos()-1);
	}
	
	public void seleccionarSiguiente(){
		índiceActual++;
		if (índiceActual > getCantElementos()-1)
			índiceActual = getCantElementos()-1;
	}
	
	public void seleccionarAnterior(){
		índiceActual--;
		
		if (índiceActual < 0)
			índiceActual = 0;
	}
}
