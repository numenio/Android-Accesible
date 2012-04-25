package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;

public class Enums {
	
	public enum Gesture {
		ARRIBA_IZQUIERDA, ARRIBA, ARRIBA_DERECHA, IZQUIERDA, CENTRO, DERECHA, ABAJO_DERECHA, ABAJO, ABAJO_IZQUIERDA, ARRIBA_LEJOS, ABAJO_LEJOS, IZQUIERDA_LEJOS, 
		DERECHA_LEJOS, ARRIBA_DERECHA_LEJOS, ARRIBA_IZQUIERDA_LEJOS, ABAJO_DERECHA_LEJOS, ABAJO_IZQUIERDA_LEJOS
	}
	
	public enum Tecla {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, Ñ, O, P, Q, R, S, T, U, V, W, X, Y, Z, Á, É, Í, Ó, Ú, Ü, NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, 
		NUM_7, NUM_8, NUM_9, BORRAR, ESPACIO, FLECHA_IZQ, FLECHA_DER, FLECHA_ARR, FLECHA_AB, OPCIONES, ENTER, ESCAPE, ENVIAR, NINGUNA,
		SYM_ASTERISCO, SYM_DIÉRESIS, SYM_ABRE_ADMIRACIÓN, SYM_CIERRA_ADMIRACIÓN, SYM_ABRE_PREGUNTA, SYM_CIERRA_PREGUNTA, SYM_ABRE_LLAVE, SYM_CIERRA_LLAVE,
		SYM_ABRE_CORCHETES, SYM_CIERRA_CORCHETES, SYM_BARRA_DIAGONAL, SYM_BARRA_DIAGONAL_INVERTIDA, SYM_COMILLAS, SYM_NUMERAL, SYM_PESOS, SYM_PORCIENTO, 
		SYM_PUNTO_ELEVADO, SYM_PUNTO, SYM_COMA, SYM_PUNTO_Y_COMA, SYM_GUION, SYM_GUION_BAJO, SYM_MAYOR, SYM_MENOR, SYM_ABRE_PARÉNTESIS, SYM_CIERRA_PARÉNTESIS,
		SYM_IGUAL, SYM_BARRA_VERTICAL, SYM_ARROBA, SYM_AND, SYM_POTENCIA, SYM_NEGACIÓN, SYM_EURO, SYM_APÓSTROFO, SYM_AVOLADA, SYM_OVOLADA,
		SYM_CE_CERILLAS, SYM_DOS_PUNTOS
	}
	
	public enum TipoTecla {
		LETRAS, NÚMEROS, SÍMBOLOS, COMANDOS, LETRAS_ACENTUADAS
	}
	
	private List<Tecla> TeclaComando = new ArrayList<Tecla>();
	private List<Tecla> TeclaLetra = new ArrayList<Tecla>();
	private List<Tecla> TeclaLetraAcentuada = new ArrayList<Tecla>();
	private List<Tecla> TeclaNúmero = new ArrayList<Tecla>();
	private List<Tecla> TeclaSímbolo = new ArrayList<Tecla>();	
			
	
	public List<Tecla> getTeclasComando() {
		return TeclaComando;
	}

	public List<Tecla> getTeclasLetra() {
		return TeclaLetra;
	}
	
	public List<Tecla> getTeclasLetraAcentuadas() {
		return TeclaLetraAcentuada;
	}

	public List<Tecla> getTeclasNúmero() {
		return TeclaNúmero;
	}

	public List<Tecla> getTeclasSímbolo() {
		return TeclaSímbolo;
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
		TeclaLetra.add(Tecla.Ñ); 
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
		
		TeclaLetraAcentuada.add(Tecla.Á);
		TeclaLetraAcentuada.add(Tecla.É);
		TeclaLetraAcentuada.add(Tecla.Í);
		TeclaLetraAcentuada.add(Tecla.Ó);
		TeclaLetraAcentuada.add(Tecla.Ú);
		TeclaLetraAcentuada.add(Tecla.Ü);
		
		TeclaNúmero.add(Tecla.NUM_0); 
		TeclaNúmero.add(Tecla.NUM_1); 
		TeclaNúmero.add(Tecla.NUM_2); 
		TeclaNúmero.add(Tecla.NUM_3); 
		TeclaNúmero.add(Tecla.NUM_4); 
		TeclaNúmero.add(Tecla.NUM_5); 
		
		TeclaNúmero.add(Tecla.NUM_6); 
		TeclaNúmero.add(Tecla.NUM_7); 
		TeclaNúmero.add(Tecla.NUM_8); 
		TeclaNúmero.add(Tecla.NUM_9);
		
		TeclaSímbolo.add(Tecla.SYM_DOS_PUNTOS);
		TeclaSímbolo.add(Tecla.SYM_CIERRA_ADMIRACIÓN);
		TeclaSímbolo.add(Tecla.SYM_CIERRA_PREGUNTA);
		TeclaSímbolo.add(Tecla.SYM_PUNTO);
		TeclaSímbolo.add(Tecla.SYM_COMA);
		TeclaSímbolo.add(Tecla.SYM_ABRE_PARÉNTESIS);
		
		TeclaSímbolo.add(Tecla.SYM_ASTERISCO);
		TeclaSímbolo.add(Tecla.SYM_DIÉRESIS);
		TeclaSímbolo.add(Tecla.SYM_ABRE_ADMIRACIÓN);
		TeclaSímbolo.add(Tecla.SYM_ABRE_PREGUNTA);
		TeclaSímbolo.add(Tecla.SYM_ABRE_LLAVE);
		TeclaSímbolo.add(Tecla.SYM_CIERRA_LLAVE);
		
		TeclaSímbolo.add(Tecla.SYM_ABRE_CORCHETES);
		TeclaSímbolo.add(Tecla.SYM_CIERRA_CORCHETES);
		TeclaSímbolo.add(Tecla.SYM_BARRA_DIAGONAL);
		TeclaSímbolo.add(Tecla.SYM_BARRA_DIAGONAL_INVERTIDA);
		TeclaSímbolo.add(Tecla.SYM_COMILLAS);
		TeclaSímbolo.add(Tecla.SYM_GUION_BAJO);
		
		TeclaSímbolo.add(Tecla.SYM_NUMERAL);
		TeclaSímbolo.add(Tecla.SYM_PESOS);
		TeclaSímbolo.add(Tecla.SYM_PORCIENTO); 
		TeclaSímbolo.add(Tecla.SYM_PUNTO_ELEVADO);
		TeclaSímbolo.add(Tecla.SYM_PUNTO_Y_COMA);
		TeclaSímbolo.add(Tecla.SYM_GUION);
		
		TeclaSímbolo.add(Tecla.SYM_MAYOR);
		TeclaSímbolo.add(Tecla.SYM_MENOR);
		TeclaSímbolo.add(Tecla.SYM_CIERRA_PARÉNTESIS);
		TeclaSímbolo.add(Tecla.SYM_IGUAL);
		TeclaSímbolo.add(Tecla.SYM_BARRA_VERTICAL);
		TeclaSímbolo.add(Tecla.SYM_ARROBA);
		
		TeclaSímbolo.add(Tecla.SYM_AND);
		TeclaSímbolo.add(Tecla.SYM_POTENCIA);
		TeclaSímbolo.add(Tecla.SYM_NEGACIÓN);
		TeclaSímbolo.add(Tecla.SYM_EURO);
		TeclaSímbolo.add(Tecla.SYM_APÓSTROFO);
		TeclaSímbolo.add(Tecla.SYM_AVOLADA);
		
		TeclaSímbolo.add(Tecla.SYM_OVOLADA);
		TeclaSímbolo.add(Tecla.SYM_CE_CERILLAS);
	}	
}


