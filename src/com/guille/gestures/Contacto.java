package com.guille.gestures;

import java.util.List;

public class Contacto implements Comparable<Contacto> {
	String nombre;
	List<String> teléfonos;
	List<String> emails;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<String> getTeléfonos() {
		return teléfonos;
	}
	public void setTeléfonos(List<String> teléfonos) {
		this.teléfonos = teléfonos;
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