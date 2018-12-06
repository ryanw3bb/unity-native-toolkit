package com.secondfury.nativetoolkit;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LocalNotificationResult extends Activity {
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		Log.w("Native Toolkit", "This is a notification result!");
		
		Intent intent = new Intent(this, UnityPlayerActivity.class);
		intent.putExtra("fromNotification", true);
		startActivity(intent);
		
		try
		{
			UnityPlayer.currentActivity.getIntent().putExtra("fromNotification", true);
		}
		catch(Exception e) { }
		
	    super.onCreate(bundle);
	}
}