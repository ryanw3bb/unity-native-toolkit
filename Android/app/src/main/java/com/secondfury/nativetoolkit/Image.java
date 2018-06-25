package com.secondfury.nativetoolkit;

import java.io.File;

import android.content.ContentValues;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

public class Image {
	
	public Image()
	{
		
	}
	
	public int Save(String path)
	{
		File file = new File(path);
		
		String ext = "";
		int i = path.lastIndexOf('.');
		if (i > 0)
		    ext = path.substring(i+1);
		
		if(file.exists()) 
		{	
			ContentValues values = new ContentValues();

		    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		    values.put(Images.Media.MIME_TYPE, "image/" + ext);
		    values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());

		    UnityPlayer.currentActivity.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
			
		    Log.w("Native Toolkit", "Content values written for file " + file.getAbsolutePath());
		    
		    return 1;
		}
		else
		{
			return 0;
		}
	}
}