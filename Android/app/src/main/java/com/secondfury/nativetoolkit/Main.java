package com.secondfury.nativetoolkit;

import java.io.IOException;
import java.util.Locale;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Main extends UnityPlayerActivity {

	public static int addImageToGallery(String path) 
	{
		Log.w("Native Toolkit", "Add image to gallery");
		
		Image image = new Image();
		return image.Save(path);
	}
	
	public static void pickImageFromGallery()
	{
		Log.w("Native Toolkit", "Select image from gallery");
		try
		{
			Intent intent = new Intent(getUnityActivity(), MainActivity.class);
			intent.putExtra("action", 0);
			getUnityActivity().startActivity(intent);
		}
		catch (Exception e)
		{
			UnityPlayer.UnitySendMessage("NativeToolkit", "OnPickImage", "Error");
			e.printStackTrace();
		}
	}
	
	public static String getImageExifData(String filePath, String tag)
	{
		Log.w("Native Toolkit", "Get image exif data: " + tag + " " + filePath);
		
		ExifInterface exif = null;
		String exifData = "";
		boolean intTag = false;
        
		try 
        {
            exif = new ExifInterface(filePath);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
		
		String[] exifIntTags = new String[] { ExifInterface.TAG_FLASH, ExifInterface.TAG_ORIENTATION, 
				ExifInterface.TAG_WHITE_BALANCE, ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.TAG_IMAGE_WIDTH,
				ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_ALTITUDE };
		
		for(int i = 0; i < exifIntTags.length; i++)
		{
			if(tag == exifIntTags[i]) 
			{  
				exifData = String.valueOf(exif.getAttributeInt(tag, 0));
				intTag = true;
				break;
			}
		}
		
		if(!intTag) 
		{ 
			exifData = exif.getAttribute(tag); 
		}
		
		return exifData;
	}
	
	public static void takeCameraShot()
	{
		Log.w("Native Toolkit", "Open camera");
		
		Intent intent = new Intent(getUnityActivity(), MainActivity.class);
		intent.putExtra("action", 1);
        getUnityActivity().startActivity(intent);
	}
	
	public static void pickContact()
	{
		Log.w("Native Toolkit", "Pick contact");
		
		Intent intent = new Intent(getUnityActivity(), MainActivity.class);
		intent.putExtra("action", 2);
        getUnityActivity().startActivity(intent);
	}
	
	public static void sendEmail(String to, String cc, String bcc, String subject, String message, String filePath)
	{
		Log.w("Native Toolkit", "Send Email with attachment");
		
		new Email(getUnityActivity(), to, cc, bcc, subject, message, filePath);
	}
	
	public static void showConfirm(final String title, final String message, final String pos, final String neg)
	{
		Log.w("Native Toolkit", "Show dialog");
		
		getUnityActivity().runOnUiThread(new Runnable() {
			@Override
            public void run() {
            	Dialog dialog = new Dialog(getUnityActivity());
            	dialog.CreateConfirm(title, message, pos, neg);
            }
		});
	}
	
	public static void showAlert(final String title, final String message, final String pos)
	{
		Log.w("Native Toolkit", "Show alert");
		
		getUnityActivity().runOnUiThread(new Runnable() {
			@Override
            public void run() {
            	Dialog dialog = new Dialog(getUnityActivity());
            	dialog.CreateAlert(title, message, pos);
            }
		});
	}
	
	public static void rateThisApp(final String title, final String message, final String pos, final String neutral, final String neg)
	{
		Log.w("Native Toolkit", "Rate this App");
		
		getUnityActivity().runOnUiThread(new Runnable() {
			@Override
            public void run() {
            	Dialog dialog = new Dialog(getUnityActivity());
            	dialog.CreateRate(title, message, pos, neutral, neg);
            }
		});
	}
	
	public static String getLocale()
	{
		Log.w("Native Toolkit", "Get Locale");
		
		try {
	        TelephonyManager tm = (TelephonyManager) getUnityActivity().getSystemService(Context.TELEPHONY_SERVICE);
	        String simCountry = tm.getSimCountryIso();
	        if (simCountry != null && simCountry.length() == 2)
	            return simCountry;
	    }
	    catch (Exception e) { }
		
		Locale locale = Locale.getDefault();
		return locale.getCountry();
	}

	public static void startLocation()
	{
		Log.w("Native Toolkit", "Start Location");

		Location location = new Location();
		location.init(getUnityActivity());
	}

	public static double getLatitude()
	{
		return Location.LastLocation.getLatitude();
	}

	public static double getLongitude()
	{
		return Location.LastLocation.getLongitude();
	}
	
	public static void scheduleLocalNotification(String title, String message, int id, int delay, String sound, 
			boolean vibrate, String smallIcon, String largeIcon)
	{
		Log.w("Native Toolkit", "Schedule local notification: " + title);
		
		LocalNotification ln = new LocalNotification();
		ln.scheduleLocalNotification(getUnityActivity(), id, title, message, delay, sound, vibrate, smallIcon, largeIcon);
	}
	
	public static void clearLocalNotification(int id)
	{
		Log.w("Native Toolkit", "Clear local notification id #" + Integer.toString(id));
		
		LocalNotification ln = new LocalNotification();
		ln.clearLocalNotification(getUnityActivity(), id);
	}
	
	public static void clearAllLocalNotifications()
	{
		Log.w("Native Toolkit", "Clear all local notifications");
		
		LocalNotification ln = new LocalNotification();
		ln.clearAllLocalNotifications(getUnityActivity());
	}
	
	public static boolean wasLaunchedFromNotification()
	{		
		boolean fromNotification = getUnityActivity().getIntent().getBooleanExtra("fromNotification", false);
		Log.w("Native Toolkit", "Launched from notification : " + fromNotification);
		return fromNotification;
	}
	
	public static Activity getUnityActivity() 
	{
        return UnityPlayer.currentActivity;
    }
}