package com.secondfury.nativetoolkit;

import java.io.File;
import java.util.ArrayList;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class EmailMultiAttachment {
	
	public EmailMultiAttachment(Activity activity, String to, String cc, String bcc, String subject, String message, String[] filePath)
	{
		Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(Intent.EXTRA_CC, new String[] { cc });
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[] { bcc });
       
        emailIntent.setType("image/*");
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        
        ArrayList<Uri> files = new ArrayList<Uri>();

        for(String path : filePath) 
        {
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            files.add(uri);
        }
        
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        
        UnityPlayer.currentActivity.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
	}
}