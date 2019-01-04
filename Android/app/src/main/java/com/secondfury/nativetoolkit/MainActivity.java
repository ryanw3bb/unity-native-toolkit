package com.secondfury.nativetoolkit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.util.Log;

public class MainActivity extends Activity {
	
	private static int PICK_IMAGE = 0;
	private static int CAPTURE_PHOTO = 1;
	private static int PICK_CONTACT = 2;
	
	private Uri imageUri;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	@Override
	protected void onCreate(Bundle b) 
	{
        super.onCreate(b);
        
        Bundle bundle = getIntent().getExtras();
        int action = bundle.getInt("action");
        
        switch(action)
        {
	        case 0:
	        	// pick photo from library
	        	Intent getContentIntent = FileUtils.createGetContentIntent("image/*");
	            Intent photoPickerIntent = Intent.createChooser(getContentIntent, "Select an image");
	            startActivityForResult(photoPickerIntent, PICK_IMAGE);
	    		break;
	    		
	        case 1:
                try
                {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }
                catch (Exception e)
                {
                    Log.w("Native Toolkit", "Camera Error");
                }

	        	// take camera shot
    		    imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	        	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	            startActivityForResult(cameraIntent, CAPTURE_PHOTO);
	            break;
	            
	        case 2:
	        	// pick contact
	        	Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
	        	pickContactIntent.setType(Phone.CONTENT_TYPE);
	            startActivityForResult(pickContactIntent, PICK_CONTACT);
	            break;
        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode != RESULT_OK)
		{
			if (requestCode == PICK_IMAGE)
			{
				UnityPlayer.UnitySendMessage("NativeToolkit", "OnPickImage", "Cancelled");
			}
			else if (requestCode == CAPTURE_PHOTO)
			{
				UnityPlayer.UnitySendMessage("NativeToolkit", "OnCameraFinished", "Cancelled");
			}
			else if (requestCode == PICK_CONTACT)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("cancelled", "true");
				String jsonString = new JSONObject(map).toString();
				UnityPlayer.UnitySendMessage("NativeToolkit", "OnPickContactFinished", jsonString);
			}
			
			this.finish();
			return;
		}
		
        if (requestCode == PICK_IMAGE)
        {
        	if(data != null)
        	{
        		try
				{
					imageUri = data.getData();
					String imagePath = FileUtils.getPath(this, imageUri);

					Log.w("Native Toolkit", "Image picked at location : " + imagePath);
					UnityPlayer.UnitySendMessage("NativeToolkit", "OnPickImage", imagePath);
				}
				catch (Exception e)
				{
					UnityPlayer.UnitySendMessage("NativeToolkit", "OnPickImage", "Error");
				}
        	}
        }
        else if (requestCode == CAPTURE_PHOTO)
        {
        	String imagePath = imageUri.getPath();
        	Image image = new Image();
         	image.Save(imagePath);
            
            Log.w("Native Toolkit", "Camera shot saved to location : " + imagePath);
            UnityPlayer.UnitySendMessage("NativeToolkit", "OnCameraFinished", imagePath);
        }
        else if (requestCode == PICK_CONTACT) 
        {
        	if(data != null)
        	{
	        	Uri	dataUri = data.getData();
	        	
	        	Map<String, String> map = new HashMap<String, String>();
	        	
	        	String[] projection = new String[] { Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER };
	        	
	        	// get display name and phone number
	            Cursor cursor =  getContentResolver().query(dataUri, projection, null, null, null);
	            if (cursor.moveToFirst()) 
	            {
	            	String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	            	map.put("name", name);
	            	
	            	String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	            	map.put("number", number);
	            }
	            
	            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
	            cursor.close();
	            
                // query for email
                Cursor emailCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,  
                			null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + Integer.parseInt(id), null, null);
                
                if (emailCursor.moveToFirst()) 
                {
                	String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                	map.put("email", email);
                }
                emailCursor.close();
                
                String jsonString = new JSONObject(map).toString();
                
            	UnityPlayer.UnitySendMessage("NativeToolkit", "OnPickContactFinished", jsonString);
        	}
        }
        
        this.finish();
	}
	
	// Create a file Uri for saving an image or video
	private static Uri getOutputMediaFileUri(int type)
	{
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	// Create a File for saving an image or video
	private static File getOutputMediaFile(int type)
	{
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Camera");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("Native Toolkit", "Failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
	    super.onSaveInstanceState(outState);
	    if (imageUri != null)
	        outState.putString("imageUri", imageUri.toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState.containsKey("imageUri"))
	    	imageUri = Uri.parse(savedInstanceState.getString("imageUri"));
	}
}