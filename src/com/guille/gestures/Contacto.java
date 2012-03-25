package com.guille.gestures;

import java.util.List;

public class Contacto implements Comparable<Contacto> {
	String nombre;
	List<String> tel�fonos;
	List<String> emails;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<String> getTel�fonos() {
		return tel�fonos;
	}
	public void setTel�fonos(List<String> tel�fonos) {
		this.tel�fonos = tel�fonos;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	public int compareTo(Contacto cont) {
		return this.getNombre().compareToIgnoreCase(cont.getNombre());
	}	
}