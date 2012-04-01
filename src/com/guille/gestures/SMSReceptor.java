package com.guille.gestures;

//import java.util.Locale;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
//import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.widget.Toast;
 
public class SMSReceptor extends BroadcastReceiver
{
	//TextToSpeech voz2;
	SMSEditorTecladoGestures miClaseGestures = new SMSEditorTecladoGestures();
	
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                
                //se ve si el número ya está en los contactos
                String nombreContacto = "";//msgs[i].getOriginatingAddress();
                Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(msgs[i].getOriginatingAddress()));  
                Cursor cs = context.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME},
                		PhoneLookup.NUMBER + "='" + msgs[i].getOriginatingAddress() + "'",null,null);

                if(cs.getCount()>0)
                {
                 cs.moveToFirst();
                 nombreContacto = cs.getString(cs.getColumnIndex(PhoneLookup.DISPLAY_NAME));
                }
                
                if (nombreContacto.length() > 0) //si el número está en la agenda
                	str += "SMS de " + nombreContacto;
                else
                	str += "SMS del número " + msgs[i].getOriginatingAddress();
                
                str += ". El mensaje dice: ";
                str += msgs[i].getMessageBody().toString();
                str += "\n";        
            }
            //---display the new SMS message---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            //voz2.speak(str, TextToSpeech.QUEUE_FLUSH, null); //acá me tira un error
            //miClaseGestures.voz.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            ((MiApp)context.getApplicationContext()).hablar(str);
            
        }                         
    }
    
//    public void onInit(int status) {
//		Locale loc = new Locale("es", "", "");
//    	
//    	voz2.setLanguage(loc);
//    }
//    
//    @Override
//    protected void onDestroy(){
//    	super.onDestroy();
//    	voz.shutdown();
//    }
}