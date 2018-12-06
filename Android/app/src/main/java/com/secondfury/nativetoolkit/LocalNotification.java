package com.secondfury.nativetoolkit;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class LocalNotification extends BroadcastReceiver {

	private Context context;

    private String NOTIFICATION_CHANNEL_ID = "NotificationChannel";
    private CharSequence NOTIFICATION_CHANNEL_NAME = "MyChannel";

	@Override
	public void onReceive(Context context, Intent paramIntent) 
	{
		this.context = context;
		
		int id = paramIntent.getIntExtra("id", 0);
		String title = paramIntent.getStringExtra("title");
		String message = paramIntent.getStringExtra("message");
		String smallIcon = paramIntent.getStringExtra("smallIcon");
		String largeIcon = paramIntent.getStringExtra("largeIcon");
		String sound = paramIntent.getStringExtra("sound");
		boolean vibrate = paramIntent.getBooleanExtra("vibrate", false);
		
		Log.w("Native Toolkit", "Create local notification: " + title);
		
		String packageName = context.getPackageName();
		int smallIconId = context.getResources().getIdentifier(smallIcon, "drawable", packageName);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
		{
			int importance = NotificationManager.IMPORTANCE_LOW;
			NotificationChannel  notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
			notificationChannel.enableLights(true);
			notificationChannel.setLightColor(Color.BLUE);
			notificationChannel.enableVibration(true);
			notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
			notificationManager.createNotificationChannel(notificationChannel);
		}

		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(this.context, NOTIFICATION_CHANNEL_ID)
			    .setSmallIcon(smallIconId)
			    .setContentTitle(title)
			    .setContentText(message)
			    .setAutoCancel(true);
		
		if(largeIcon != "")
		{
			int largeIconId = context.getResources().getIdentifier(largeIcon, "drawable", packageName);
			Bitmap bmap = BitmapFactory.decodeResource(context.getResources(), largeIconId);
			mBuilder.setLargeIcon(bmap);
		}
		
		if(sound == "default_sound")
		{
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(soundUri);
		}
		else if(sound != "")
		{
			Uri soundUri = Uri.parse("android.resource://" + packageName + "/raw/" + sound);
			mBuilder.setSound(soundUri);
		}
		
		if(vibrate)
		{
			mBuilder.setVibrate(new long[] { 0, 1000 });
		}
		
		if(message.length() > 40)
		{
			mBuilder.setStyle(new NotificationCompat.BigTextStyle()
	        		.bigText(message));
		}
		
		Intent resultIntent = new Intent(context, LocalNotificationResult.class);
		resultIntent.putExtra("fromNotification", true);
		
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, id, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mBuilder.setContentIntent(resultPendingIntent);

		notificationManager.notify(id, mBuilder.build());
	}
	
	public void scheduleLocalNotification(Context context, int id, String title, String message, int delay, String sound, 
			boolean vibrate, String smallIcon, String largeIcon)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MINUTE, delay);
		 
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 
		Intent intent = new Intent(context, LocalNotification.class);
		intent.putExtra("id", id);
		intent.putExtra("title", title);
		intent.putExtra("message", message);
		intent.putExtra("smallIcon", smallIcon);
		intent.putExtra("largeIcon", largeIcon);
		intent.putExtra("sound", sound);
		intent.putExtra("vibrate", vibrate);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
		
		// saving to shared preferences so we can cancel later
		SharedPreferences prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE);
	    String pendingNotifications = prefs.getString("pending", "");
		Editor edit = prefs.edit();
	    
		if(pendingNotifications == "") 
			pendingNotifications = Integer.toString(id);
		else
			pendingNotifications += "," + Integer.toString(id);
			
		edit.putString("pending", pendingNotifications);
		edit.commit();
	}
	
	public void clearLocalNotification(Context context, int id)
	{
		// current notifications
		NotificationManager notificationManager =
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.cancel(id);
		
		// pending notifications
		Intent alarmIntent = new Intent(context, LocalNotification.class);
		
		if(PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT) != null)
		{
			PendingIntent pendingIntent = 
					PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);	
		}
	}
	
	public void clearAllLocalNotifications(Context context)
	{
		// current notifications
		NotificationManager notificationManager =
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.cancelAll();
		
		// pending notifications
		SharedPreferences prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE);
	    String pendingNotifications = prefs.getString("pending", "");
	    
	    if(pendingNotifications == "") return;
	    
	    String[] notifications = pendingNotifications.split(",");
	    Intent alarmIntent = new Intent(context, LocalNotification.class);
	    
	    for(int i = 0; i < notifications.length; i++)
	    {
	    	int id = Integer.parseInt(notifications[i]);
	    	
	    	if(PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT) != null)
			{
	    		Log.w("Native Toolkit", "Clear local notification id #" + id);
	    		
	    		PendingIntent pendingIntent = 
	    				PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    			
	    		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    		alarmManager.cancel(pendingIntent);
			}
	    }
	    
	    Editor edit = prefs.edit();
		edit.putString("pending", "");
		edit.commit();
	}
}