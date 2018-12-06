package com.secondfury.nativetoolkit;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;

public class Email {
	
	public Email(Activity activity, String to, String cc, String bcc, String subject, String message, String filePath)
	{
        try
        {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        catch (Exception e)
        {
            Log.w("Native Toolkit", "Email Error");
        }

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/html");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message));
        emailIntent.putExtra(Intent.EXTRA_CC, new String[] { cc });
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[] { bcc });
        
        if(filePath != "")
        {
	        emailIntent.setType("image/*");
	        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	        
	        Uri uri = Uri.fromFile(new File(filePath));
	        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        
        activity.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
	}
}