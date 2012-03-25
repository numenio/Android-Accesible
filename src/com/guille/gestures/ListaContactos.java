package com.guille.gestures;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

public class ListaContactos {
//	List<Contacto> misContactos = new ArrayList<Contacto>();
//	int �ndiceActual = 0;
//	public static final int PICK_CONTACT = 1;
//	
//	
//	protected void onCreate(Bundle savedInstanceState) 
//	{
//	  super.onCreate(savedInstanceState);       
//	  Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
//	  startActivityForResult(intentContact, PICK_CONTACT);
//	}//onCreate
//
//	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
//	{
//
//	  if (requestCode == PICK_CONTACT)
//	  {         
//	    misContactos = obtenerInfoContactos(intent);
//	  }
//	}//onActivityResult
//	
//	
//	protected List<Contacto> obtenerInfoContactos(Intent intent)
//	{
//		List<Contacto> misContactos = new ArrayList<Contacto>();
//
//		Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);      
//		while (cursor.moveToNext()) 
//		{
//			Contacto contacto = new Contacto();
//			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//			contacto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(
//					ContactsContract.Contacts.DISPLAY_NAME))); 
//
//			String hasPhone = cursor.getString(cursor.getColumnIndex(
//					ContactsContract.Contacts.HAS_PHONE_NUMBER));
//
//			if (hasPhone.equalsIgnoreCase("1")) //si tiene tel�fono
//			{
//				Cursor phones = getContentResolver().query(
//						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
//				List<String> tel�fonos = new ArrayList<String>();
//				while (phones.moveToNext()) 
//				{
//					tel�fonos.add(phones.getString(phones.getColumnIndex(
//							ContactsContract.CommonDataKinds.Phone.NUMBER)));
//				}
//				phones.close();
//				contacto.setTel�fonos(tel�fonos);
//			}
//
//			// Find Email Addresses
//			Cursor emails = getContentResolver().query(
//					ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
//					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
//			List<String> listaEmails = new ArrayList<String>();
//			while (emails.moveToNext()) 
//			{
//				listaEmails.add(emails.getString(emails.getColumnIndex(
//						ContactsContract.CommonDataKinds.Email.DATA)));
//			}
//			emails.close();
//			contacto.setEmails(listaEmails);
//
//			//			Cursor address = getContentResolver().query(
//			//					ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
//			//					null,
//			//					ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
//			//					null, null);
//			//	    while (address.moveToNext()) 
//			//	    { 
//			//	      // These are all private class variables, don't forget to create them.
//			//	      poBox      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
//			//	      street     = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
//			//	      city       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
//			//	      state      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
//			//	      postalCode = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
//			//	      country    = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
//			//	      type       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
//			//	    }  //address.moveToNext()
//			misContactos.add(contacto);
//		}  //while (cursor.moveToNext())        
//		cursor.close();
//		return misContactos;
//	}//getContactInfo
//	
//	
//	public List<Contacto> getTodosLosContactos() {
//		return misContactos;
//	}
//	
//	public int getCantContactos(){
//		return misContactos.size();
//	}
//	
//	public void seleccionarContactoEn(int nuevoLugarIndice){
//		if (nuevoLugarIndice <= getCantContactos())
//			�ndiceActual = nuevoLugarIndice;
//		else
//			�ndiceActual = getCantContactos();
//	}
//	
//	public int getLugarContactoActual(){
//		return �ndiceActual;
//	}
//	
//	public Contacto getContactoSeleccionado(){
//		return misContactos.get(�ndiceActual);
//	}
//	
//	public Contacto getContactoEn(int �ndiceContacto){
//		if (�ndiceContacto > getCantContactos()-1) �ndiceContacto = getCantContactos()-1;
//		if (�ndiceContacto < 0) �ndiceContacto = 0;
//		return misContactos.get(�ndiceContacto);
//	}
//	
//	public Contacto getElementoSeleccionadoEn(int lugarDelMen�){
//		if (lugarDelMen� < getCantContactos())
//			return misContactos.get(lugarDelMen�);
//		else
//			return misContactos.get(getCantContactos()-1);
//	}
//	
//	public void seleccionarSiguiente(){
//		�ndiceActual++;
//		if (�ndiceActual > getCantContactos()-1)
//			�ndiceActual = getCantContactos()-1;
//	}
//	
//	public void seleccionarAnterior(){
//		�ndiceActual--;
//		
//		if (�ndiceActual < 0)
//			�ndiceActual = 0;
//	}
}
