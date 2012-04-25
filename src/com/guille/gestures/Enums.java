package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;

public class Enums {
	
	public enum Gesture {
		ARRIBA_IZQUIERDA, ARRIBA, ARRIBA_DERECHA, IZQUIERDA, CENTRO, DERECHA, ABAJO_DERECHA, ABAJO, ABAJO_IZQUIERDA, ARRIBA_LEJOS, ABAJO_LEJOS, IZQUIERDA_LEJOS, 
		DERECHA_LEJOS, ARRIBA_DERECHA_LEJOS, ARRIBA_IZQUIERDA_LEJOS, ABAJO_DERECHA_LEJOS, ABAJO_IZQUIERDA_LEJOS
	}
	
	public enum Tecla {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, �, O, P, Q, R, S, T, U, V, W, X, Y, Z, �, �, �, �, �, �, NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, 
		NUM_7, NUM_8, NUM_9, BORRAR, ESPACIO, FLECHA_IZQ, FLECHA_DER, FLECHA_ARR, FLECHA_AB, OPCIONES, ENTER, ESCAPE, ENVIAR, NINGUNA,
		SYM_ASTERISCO, SYM_DI�RESIS, SYM_ABRE_ADMIRACI�N, SYM_CIERRA_ADMIRACI�N, SYM_ABRE_PREGUNTA, SYM_CIERRA_PREGUNTA, SYM_ABRE_LLAVE, SYM_CIERRA_LLAVE,
		SYM_ABRE_CORCHETES, SYM_CIERRA_CORCHETES, SYM_BARRA_DIAGONAL, SYM_BARRA_DIAGONAL_INVERTIDA, SYM_COMILLAS, SYM_NUMERAL, SYM_PESOS, SYM_PORCIENTO, 
		SYM_PUNTO_ELEVADO, SYM_PUNTO, SYM_COMA, SYM_PUNTO_Y_COMA, SYM_GUION, SYM_GUION_BAJO, SYM_MAYOR, SYM_MENOR, SYM_ABRE_PAR�NTESIS, SYM_CIERRA_PAR�NTESIS,
		SYM_IGUAL, SYM_BARRA_VERTICAL, SYM_ARROBA, SYM_AND, SYM_POTENCIA, SYM_NEGACI�N, SYM_EURO, SYM_AP�STROFO, SYM_AVOLADA, SYM_OVOLADA,
		SYM_CE_CERILLAS, SYM_DOS_PUNTOS
	}
	
	public enum TipoTecla {
		LETRAS, N�MEROS, S�MBOLOS, COMANDOS, LETRAS_ACENTUADAS
	}
	
	private List<Tecla> TeclaComando = new ArrayList<Tecla>();
	private List<Tecla> TeclaLetra = new ArrayList<Tecla>();
	private List<Tecla> TeclaLetraAcentuada = new ArrayList<Tecla>();
	private List<Tecla> TeclaN�mero = new ArrayList<Tecla>();
	private List<Tecla> TeclaS�mbolo = new ArrayList<Tecla>();	
			
	
	public List<Tecla> getTeclasComando() {
		return TeclaComando;
	}

	public List<Tecla> getTeclasLetra() {
		return TeclaLetra;
	}
	
	public List<Tecla> getTeclasLetraAcentuadas() {
		return TeclaLetraAcentuada;
	}

	public List<Tecla> getTeclasN�mero() {
		return TeclaN�mero;
	}

	public List<Tecla> getTeclasS�mbolo() {
		return TeclaS�mbolo;
	}

	public Enums(){
		cargarListas();
	}
	
	private void cargarListas(){
		TeclaComando.add(Tecla.BORRAR);
		TeclaComando.add(Tecla.ESPACIO);
		TeclaComando.add(Tecla.FLECHA_IZQ);
		TeclaComando.add(Tecla.FLECHA_DER);
		TeclaComando.add(Tecla.FLECHA_ARR);
		TeclaComando.add(Tecla.FLECHA_AB);
		
		TeclaComando.add(Tecla.OPCIONES); 
		TeclaComando.add(Tecla.ENTER);
		TeclaComando.add(Tecla.ESCAPE); 
		TeclaComando.add(Tecla.NINGUNA);
		
		TeclaLetra.add(Tecla.A); 
		TeclaLetra.add(Tecla.B); 
		TeclaLetra.add(Tecla.C); 
		TeclaLetra.add(Tecla.D); 
		TeclaLetra.add(Tecla.E); 
		TeclaLetra.add(Tecla.F); 
		
		TeclaLetra.add(Tecla.G); 
		TeclaLetra.add(Tecla.H); 
		TeclaLetra.add(Tecla.I); 
		TeclaLetra.add(Tecla.J); 
		TeclaLetra.add(Tecla.K); 
		TeclaLetra.add(Tecla.L); 
		
		TeclaLetra.add(Tecla.M); 
		TeclaLetra.add(Tecla.N); 
		TeclaLetra.add(Tecla.�); 
		TeclaLetra.add(Tecla.O); 
		TeclaLetra.add(Tecla.P); 
		TeclaLetra.add(Tecla.Q); 		
		
		TeclaLetra.add(Tecla.R); 
		TeclaLetra.add(Tecla.S); 
		TeclaLetra.add(Tecla.T); 
		TeclaLetra.add(Tecla.U); 								
		TeclaLetra.add(Tecla.V); 
		TeclaLetra.add(Tecla.W); 
		
		TeclaLetra.add(Tecla.X); 
		TeclaLetra.add(Tecla.Y); 
		TeclaLetra.add(Tecla.Z);
		
		TeclaLetraAcentuada.add(Tecla.�);
		TeclaLetraAcentuada.add(Tecla.�);
		TeclaLetraAcentuada.add(Tecla.�);
		TeclaLetraAcentuada.add(Tecla.�);
		TeclaLetraAcentuada.add(Tecla.�);
		TeclaLetraAcentuada.add(Tecla.�);
		
		TeclaN�mero.add(Tecla.NUM_0); 
		TeclaN�mero.add(Tecla.NUM_1); 
		TeclaN�mero.add(Tecla.NUM_2); 
		TeclaN�mero.add(Tecla.NUM_3); 
		TeclaN�mero.add(Tecla.NUM_4); 
		TeclaN�mero.add(Tecla.NUM_5); 
		
		TeclaN�mero.add(Tecla.NUM_6); 
		TeclaN�mero.add(Tecla.NUM_7); 
		TeclaN�mero.add(Tecla.NUM_8); 
		TeclaN�mero.add(Tecla.NUM_9);
		
		TeclaS�mbolo.add(Tecla.SYM_DOS_PUNTOS);
		TeclaS�mbolo.add(Tecla.SYM_CIERRA_ADMIRACI�N);
		TeclaS�mbolo.add(Tecla.SYM_CIERRA_PREGUNTA);
		TeclaS�mbolo.add(Tecla.SYM_PUNTO);
		TeclaS�mbolo.add(Tecla.SYM_COMA);
		TeclaS�mbolo.add(Tecla.SYM_ABRE_PAR�NTESIS);
		
		TeclaS�mbolo.add(Tecla.SYM_ASTERISCO);
		TeclaS�mbolo.add(Tecla.SYM_DI�RESIS);
		TeclaS�mbolo.add(Tecla.SYM_ABRE_ADMIRACI�N);
		TeclaS�mbolo.add(Tecla.SYM_ABRE_PREGUNTA);
		TeclaS�mbolo.add(Tecla.SYM_ABRE_LLAVE);
		TeclaS�mbolo.add(Tecla.SYM_CIERRA_LLAVE);
		
		TeclaS�mbolo.add(Tecla.SYM_ABRE_CORCHETES);
		TeclaS�mbolo.add(Tecla.SYM_CIERRA_CORCHETES);
		TeclaS�mbolo.add(Tecla.SYM_BARRA_DIAGONAL);
		TeclaS�mbolo.add(Tecla.SYM_BARRA_DIAGONAL_INVERTIDA);
		TeclaS�mbolo.add(Tecla.SYM_COMILLAS);
		TeclaS�mbolo.add(Tecla.SYM_GUION_BAJO);
		
		TeclaS�mbolo.add(Tecla.SYM_NUMERAL);
		TeclaS�mbolo.add(Tecla.SYM_PESOS);
		TeclaS�mbolo.add(Tecla.SYM_PORCIENTO); 
		TeclaS�mbolo.add(Tecla.SYM_PUNTO_ELEVADO);
		TeclaS�mbolo.add(Tecla.SYM_PUNTO_Y_COMA);
		TeclaS�mbolo.add(Tecla.SYM_GUION);
		
		TeclaS�mbolo.add(Tecla.SYM_MAYOR);
		TeclaS�mbolo.add(Tecla.SYM_MENOR);
		TeclaS�mbolo.add(Tecla.SYM_CIERRA_PAR�NTESIS);
		TeclaS�mbolo.add(Tecla.SYM_IGUAL);
		TeclaS�mbolo.add(Tecla.SYM_BARRA_VERTICAL);
		TeclaS�mbolo.add(Tecla.SYM_ARROBA);
		
		TeclaS�mbolo.add(Tecla.SYM_AND);
		TeclaS�mbolo.add(Tecla.SYM_POTENCIA);
		TeclaS�mbolo.add(Tecla.SYM_NEGACI�N);
		TeclaS�mbolo.add(Tecla.SYM_EURO);
		TeclaS�mbolo.add(Tecla.SYM_AP�STROFO);
		TeclaS�mbolo.add(Tecla.SYM_AVOLADA);
		
		TeclaS�mbolo.add(Tecla.SYM_OVOLADA);
		TeclaS�mbolo.add(Tecla.SYM_CE_CERILLAS);
	}	
}


