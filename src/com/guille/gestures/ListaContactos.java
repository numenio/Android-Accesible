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
//	int índiceActual = 0;
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
//			if (hasPhone.equalsIgnoreCase("1")) //si tiene teléfono
//			{
//				Cursor phones = getContentResolver().query(
//						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
//				List<String> teléfonos = new ArrayList<String>();
//				while (phones.moveToNext()) 
//				{
//					teléfonos.add(phones.getString(phones.getColumnIndex(
//							ContactsContract.CommonDataKinds.Phone.NUMBER)));
//				}
//				phones.close();
//				contacto.setTeléfonos(teléfonos);
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
//			índiceActual = nuevoLugarIndice;
//		else
//			índiceActual = getCantContactos();
//	}
//	
//	public int getLugarContactoActual(){
//		return índiceActual;
//	}
//	
//	public Contacto getContactoSeleccionado(){
//		return misContactos.get(índiceActual);
//	}
//	
//	public Contacto getContactoEn(int índiceContacto){
//		if (índiceContacto > getCantContactos()-1) índiceContacto = getCantContactos()-1;
//		if (índiceContacto < 0) índiceContacto = 0;
//		return misContactos.get(índiceContacto);
//	}
//	
//	public Contacto getElementoSeleccionadoEn(int lugarDelMenú){
//		if (lugarDelMenú < getCantContactos())
//			return misContactos.get(lugarDelMenú);
//		else
//			return misContactos.get(getCantContactos()-1);
//	}
//	
//	public void seleccionarSiguiente(){
//		índiceActual++;
//		if (índiceActual > getCantContactos()-1)
//			índiceActual = getCantContactos()-1;
//	}
//	
//	public void seleccionarAnterior(){
//		índiceActual--;
//		
//		if (índiceActual < 0)
//			índiceActual = 0;
//	}
}
