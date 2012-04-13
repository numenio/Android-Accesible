package com.guille.gestures;

import java.util.HashMap;
import com.guille.gestures.Enums.Tecla;

public class UtilesCadena {
	public static Boolean swEsNúmero (String carácter){
		//List<String> números = new ArrayList();
		String números = "0123456789";
		if (números.contains(carácter))
			return true;
		else
			return false;
	}

	public static Boolean swEsLetra (String carácter){
		//List<String> números = new ArrayList();
		String letras = "abcdefghijklmnñopqrstuvwxyzáéíóúü";
		if (letras.contains(carácter))
			return true;
		else
			return false;
	}
	
	public static Boolean swEsSímbolo (String carácter){
		//List<String> números = new ArrayList();
		String símbolos = "!\"·#$%&/()=?¿^*¨Çªº|@~€¬'][{}¡;:_,.-<> ";
		if (símbolos.contains(carácter))
			return true;
		else
			return false;
	}
	
	public static String traducirCarácterParaLeer (Tecla t){
		HashMap<Tecla, String> caracteres = new HashMap<Tecla, String>();
		caracteres.put(Tecla.A, "a");
		caracteres.put(Tecla.B, "be");
		caracteres.put(Tecla.C, "ce");
		caracteres.put(Tecla.D, "de");
		caracteres.put(Tecla.E, "e");
		caracteres.put(Tecla.F, "efe");
		caracteres.put(Tecla.G, "je");
		caracteres.put(Tecla.H, "ache");
		caracteres.put(Tecla.I, "i");
		caracteres.put(Tecla.J, "jota");
		caracteres.put(Tecla.K, "ka");
		caracteres.put(Tecla.L, "ele");
		caracteres.put(Tecla.M, "eme");
		caracteres.put(Tecla.N, "ene");
		caracteres.put(Tecla.Ñ, "eñe");
		caracteres.put(Tecla.O, "o");
		caracteres.put(Tecla.P, "pe");
		caracteres.put(Tecla.Q, "ku");
		caracteres.put(Tecla.R, "erre");
		caracteres.put(Tecla.S, "ese");
		caracteres.put(Tecla.T, "te");
		caracteres.put(Tecla.U, "u");
		caracteres.put(Tecla.V, "ve");
		caracteres.put(Tecla.W, "doble ve");
		caracteres.put(Tecla.X, "equis");
		caracteres.put(Tecla.Y, "i griega");
		caracteres.put(Tecla.Z, "zeta");
		caracteres.put(Tecla.NUM_0, "cero");
		caracteres.put(Tecla.NUM_1, "uno");
		caracteres.put(Tecla.NUM_2, "dos");
		caracteres.put(Tecla.NUM_3, "tres");
		caracteres.put(Tecla.NUM_4, "cuatro");
		caracteres.put(Tecla.NUM_5, "cinco");
		caracteres.put(Tecla.NUM_6, "seis");
		caracteres.put(Tecla.NUM_7, "siete");
		caracteres.put(Tecla.NUM_8, "ocho");
		caracteres.put(Tecla.NUM_9, "nueve");
		caracteres.put(Tecla.Á, "a con acento");
		caracteres.put(Tecla.É, "e con acento");
		caracteres.put(Tecla.Í, "i con acento");
		caracteres.put(Tecla.Ó, "o con acento");
		caracteres.put(Tecla.Ú, "u con acento");
		caracteres.put(Tecla.Ü, "u con diéresis");
		
		caracteres.put(Tecla.SYM_ABRE_ADMIRACIÓN, "abre admiración");
		caracteres.put(Tecla.SYM_ABRE_CORCHETES, "abre corchetes");
		caracteres.put(Tecla.SYM_ASTERISCO, "asterisco");
		caracteres.put(Tecla.SYM_CIERRA_ADMIRACIÓN, "cierra admiración");
		caracteres.put(Tecla.SYM_DIÉRESIS, "diéresis");
		caracteres.put(Tecla.SYM_ABRE_PREGUNTA, "abre pregunta");
		caracteres.put(Tecla.SYM_CIERRA_PREGUNTA, "cierra pregunta");
		caracteres.put(Tecla.SYM_ABRE_LLAVE, "abre llave");
		caracteres.put(Tecla.SYM_CIERRA_LLAVE, "cierra llave");
		caracteres.put(Tecla.SYM_BARRA_DIAGONAL, "barra diagonal");
		caracteres.put(Tecla.SYM_BARRA_DIAGONAL_INVERTIDA, "barra diagonal inversa");
		caracteres.put(Tecla.SYM_CIERRA_CORCHETES, "cierra corchetes");
		caracteres.put(Tecla.SYM_NUMERAL, "numeral");
		caracteres.put(Tecla.SYM_COMILLAS, "comillas");
		caracteres.put(Tecla.SYM_PESOS, "pesos");
		caracteres.put(Tecla.SYM_PORCIENTO, "porciento");
		caracteres.put(Tecla.SYM_PUNTO_ELEVADO, "punto elevado");
		caracteres.put(Tecla.SYM_PUNTO, "punto");
		caracteres.put(Tecla.SYM_CE_CERILLAS, "ce cerilla");
		caracteres.put(Tecla.SYM_OVOLADA, "o volada");
		caracteres.put(Tecla.SYM_AVOLADA, "a volada");
		caracteres.put(Tecla.SYM_COMA, "coma");
		caracteres.put(Tecla.SYM_PUNTO_Y_COMA, "punto y coma");
		caracteres.put(Tecla.SYM_GUION_BAJO, "guión bajo");
		caracteres.put(Tecla.SYM_GUION, "guión");
		caracteres.put(Tecla.SYM_MAYOR, "mayor");
		caracteres.put(Tecla.SYM_MENOR, "menor");
		caracteres.put(Tecla.SYM_ABRE_PARÉNTESIS, "abre paréntesis");
		caracteres.put(Tecla.SYM_CIERRA_PARÉNTESIS, "cierra paréntesis");
		caracteres.put(Tecla.SYM_BARRA_VERTICAL, "barra vertical");
		caracteres.put(Tecla.SYM_IGUAL, "igual");
		caracteres.put(Tecla.SYM_ARROBA, "arroba");
		caracteres.put(Tecla.SYM_POTENCIA, "potencia");
		caracteres.put(Tecla.SYM_AND, "andpersant");
		caracteres.put(Tecla.SYM_EURO, "euro");
		caracteres.put(Tecla.SYM_NEGACIÓN, "negación");
		caracteres.put(Tecla.SYM_APÓSTROFO, "apóstrofo");
		caracteres.put(Tecla.BORRAR, "borrar");
		caracteres.put(Tecla.ESPACIO, "espacio");
		caracteres.put(Tecla.FLECHA_IZQ, "flecha izquierda");
		caracteres.put(Tecla.FLECHA_DER, "flecha derecha");
		caracteres.put(Tecla.FLECHA_ARR, "flecha arriba");
		caracteres.put(Tecla.FLECHA_AB, "flecha abajo");
		caracteres.put(Tecla.OPCIONES, "abrir opciones");
		caracteres.put(Tecla.ENTER, "aceptar");
		caracteres.put(Tecla.ESCAPE, "cancelar");
		caracteres.put(Tecla.ENVIAR, "enviar");
		caracteres.put(Tecla.NINGUNA, "ninguna");
		
		String cadena = caracteres.get(t);
		if (cadena == null) cadena = "";
		return cadena;
	}
	
	public static String traducirCarácterParaEscribir (Tecla t){
		HashMap<Tecla, String> caracteres = new HashMap<Tecla, String>();
		caracteres.put(Tecla.A, "a");
		caracteres.put(Tecla.B, "b");
		caracteres.put(Tecla.C, "c");
		caracteres.put(Tecla.D, "d");
		caracteres.put(Tecla.E, "e");
		caracteres.put(Tecla.F, "f");
		caracteres.put(Tecla.G, "g");
		caracteres.put(Tecla.H, "h");
		caracteres.put(Tecla.I, "i");
		caracteres.put(Tecla.J, "j");
		caracteres.put(Tecla.K, "k");
		caracteres.put(Tecla.L, "l");
		caracteres.put(Tecla.M, "m");
		caracteres.put(Tecla.N, "n");
		caracteres.put(Tecla.Ñ, "ñ");
		caracteres.put(Tecla.O, "o");
		caracteres.put(Tecla.P, "p");
		caracteres.put(Tecla.Q, "q");
		caracteres.put(Tecla.R, "r");
		caracteres.put(Tecla.S, "s");
		caracteres.put(Tecla.T, "t");
		caracteres.put(Tecla.U, "u");
		caracteres.put(Tecla.V, "v");
		caracteres.put(Tecla.W, "w");
		caracteres.put(Tecla.X, "x");
		caracteres.put(Tecla.Y, "y");
		caracteres.put(Tecla.Z, "z");
		caracteres.put(Tecla.NUM_0, "0");
		caracteres.put(Tecla.NUM_1, "1");
		caracteres.put(Tecla.NUM_2, "2");
		caracteres.put(Tecla.NUM_3, "3");
		caracteres.put(Tecla.NUM_4, "4");
		caracteres.put(Tecla.NUM_5, "5");
		caracteres.put(Tecla.NUM_6, "6");
		caracteres.put(Tecla.NUM_7, "7");
		caracteres.put(Tecla.NUM_8, "8");
		caracteres.put(Tecla.NUM_9, "9");
		caracteres.put(Tecla.Á, "á");
		caracteres.put(Tecla.É, "é");
		caracteres.put(Tecla.Í, "í");
		caracteres.put(Tecla.Ó, "ó");
		caracteres.put(Tecla.Ú, "ú");
		caracteres.put(Tecla.Ü, "ü");
		caracteres.put(Tecla.SYM_ABRE_ADMIRACIÓN, "¡");
		caracteres.put(Tecla.SYM_ABRE_CORCHETES, "[");
		caracteres.put(Tecla.SYM_ASTERISCO, "*");
		caracteres.put(Tecla.SYM_CIERRA_ADMIRACIÓN, "!");
		caracteres.put(Tecla.SYM_DIÉRESIS, "¨");
		caracteres.put(Tecla.SYM_ABRE_PREGUNTA, "¿");
		caracteres.put(Tecla.SYM_CIERRA_PREGUNTA, "?");
		caracteres.put(Tecla.SYM_ABRE_LLAVE, "{");
		caracteres.put(Tecla.SYM_CIERRA_LLAVE, "}");
		caracteres.put(Tecla.SYM_BARRA_DIAGONAL, "/");
		caracteres.put(Tecla.SYM_BARRA_DIAGONAL_INVERTIDA, "\\");
		caracteres.put(Tecla.SYM_CIERRA_CORCHETES, "]");
		caracteres.put(Tecla.SYM_NUMERAL, "#");
		caracteres.put(Tecla.SYM_COMILLAS, "\"");
		caracteres.put(Tecla.SYM_PESOS, "$");
		caracteres.put(Tecla.SYM_PORCIENTO, "%");
		caracteres.put(Tecla.SYM_PUNTO_ELEVADO, "·");
		caracteres.put(Tecla.SYM_PUNTO, ".");
		caracteres.put(Tecla.SYM_CE_CERILLAS, "Ç");
		caracteres.put(Tecla.SYM_OVOLADA, "º");
		caracteres.put(Tecla.SYM_AVOLADA, "ª");
		caracteres.put(Tecla.SYM_COMA, ",");
		caracteres.put(Tecla.SYM_PUNTO_Y_COMA, ";");
		caracteres.put(Tecla.SYM_GUION_BAJO, "_");
		caracteres.put(Tecla.SYM_GUION, "-");
		caracteres.put(Tecla.SYM_MAYOR, ">");
		caracteres.put(Tecla.SYM_MENOR, "<");
		caracteres.put(Tecla.SYM_ABRE_PARÉNTESIS, "(");
		caracteres.put(Tecla.SYM_CIERRA_PARÉNTESIS, ")");
		caracteres.put(Tecla.SYM_BARRA_VERTICAL, "|");
		caracteres.put(Tecla.SYM_IGUAL, "=");
		caracteres.put(Tecla.SYM_ARROBA, "@");
		caracteres.put(Tecla.SYM_POTENCIA, "^");
		caracteres.put(Tecla.SYM_AND, "&");
		caracteres.put(Tecla.SYM_EURO, "€");
		caracteres.put(Tecla.SYM_NEGACIÓN, "~");
		caracteres.put(Tecla.SYM_APÓSTROFO, "'");
		caracteres.put(Tecla.BORRAR, "borrar");
		caracteres.put(Tecla.ESPACIO, "espacio");
		caracteres.put(Tecla.FLECHA_IZQ, "flecha izquierda");
		caracteres.put(Tecla.FLECHA_DER, "flecha derecha");
		caracteres.put(Tecla.FLECHA_ARR, "flecha arriba");
		caracteres.put(Tecla.FLECHA_AB, "flecha abajo");
		caracteres.put(Tecla.OPCIONES, "abrir opciones");
		caracteres.put(Tecla.ENTER, "aceptar");
		caracteres.put(Tecla.ESCAPE, "cancelar");
		caracteres.put(Tecla.ENVIAR, "enviar");
		caracteres.put(Tecla.NINGUNA, "ninguna");
		
		String cadena = caracteres.get(t);
		if (cadena == null) cadena = "";
		return cadena;
	} 
}
