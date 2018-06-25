package com.secondfury.nativetoolkit;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class Dialog {
	
	private Activity activity;
	
	public Dialog(Activity activity)
	{
		Log.w("Native Toolkit", "New dialog");
		
		this.activity = activity;
	}
	
	public void CreateConfirm(String title, String message, String pos, String neg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(pos,
                new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			UnityPlayer.UnitySendMessage("NativeToolkit", "OnDialogPress", "Yes");
            			dialog.cancel();
            		}
        });
        builder.setNegativeButton(neg,
                new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			UnityPlayer.UnitySendMessage("NativeToolkit", "OnDialogPress", "No");
            			dialog.cancel();
            		}
        });

        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void CreateAlert(String title, String message, String pos)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(pos,
                new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			UnityPlayer.UnitySendMessage("NativeToolkit", "OnDialogPress", "Yes");
            			dialog.cancel();
            		}
        });
        
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void CreateRate(String title, String message, String pos, String neutral, String neg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
		builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(pos,
                new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			UnityPlayer.UnitySendMessage("NativeToolkit", "OnRatePress", "2");
            			
            			final Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            			final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
            			if (activity.getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
            				activity.startActivity(rateAppIntent);
            			
            			dialog.cancel();
            		}
        });
        builder.setNeutralButton(neutral, 
        		new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			UnityPlayer.UnitySendMessage("NativeToolkit", "OnRatePress", "1");
            			dialog.cancel();
            		}
        });
        builder.setNegativeButton(neg,
                new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int id) {
            			UnityPlayer.UnitySendMessage("NativeToolkit", "OnRatePress", "0");
            			dialog.cancel();
            		}
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void CreateTimePicker()
	{
		// to do
	}
	
	public void CreateDatePicker()
	{
		// to do
	}
}