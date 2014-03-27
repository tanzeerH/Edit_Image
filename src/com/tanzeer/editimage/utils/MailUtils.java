package com.tanzeer.editimage.utils;




import android.content.Context;
import android.content.Intent;
import android.net.Uri;



public class MailUtils {
	public static void sendMail(Context context,Uri imageUri){
		 Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
         emailIntent.setType("message/rfc822");
         emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"image");
         emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"please download");
         /*emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");  
         emailIntent.putExtra(android.content.Intent.EXTRA_BCC, ""); */
         
         emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    
         if (imageUri != null){
        	 emailIntent.setType("image/*"); 
        	 emailIntent.putExtra(Intent.EXTRA_STREAM,imageUri); 
        	
         }
        
         context.startActivity(emailIntent);
	}
	
	public static void contactSupport(Context context){
		 Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@mustachebashapp.com"});
        //emailIntent.setData(Uri.parse("mailto:support@mustachebashapp.com"));
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"extra subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"contact contact");
       /* emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");  
        emailIntent.putExtra(android.content.Intent.EXTRA_BCC, ""); */
        
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   
        /*if (imageUri != null){
       	 emailIntent.setType("image/*"); // 
       	 emailIntent.putExtra(Intent.EXTRA_STREAM,imageUri); 
        }*/
        
        context.startActivity(emailIntent);
	}
}
