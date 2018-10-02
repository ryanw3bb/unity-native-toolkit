using UnityEngine;
using UnityEngine.UI;

public class NativeToolkitExample : MonoBehaviour {

	public Text console;
	public Texture2D texture;

	string imagePath = "";

	void Start()
	{
        console.text += "\nLocation enabled: " + NativeToolkit.StartLocation();
        console.text += "\nDevice country: " + NativeToolkit.GetCountryCode();
        console.text += "\nLaunched from notification: " + NativeToolkit.WasLaunchedFromNotification();
	}
	
	void OnEnable ()
	{
		NativeToolkit.OnScreenshotSaved += ScreenshotSaved;	
		NativeToolkit.OnImageSaved += ImageSaved;
		NativeToolkit.OnImagePicked += ImagePicked;
		NativeToolkit.OnCameraShotComplete += CameraShotComplete;
		NativeToolkit.OnContactPicked += ContactPicked;
	}

	void OnDisable ()
	{
		NativeToolkit.OnScreenshotSaved -= ScreenshotSaved;	
		NativeToolkit.OnImageSaved -= ImageSaved;
		NativeToolkit.OnImagePicked -= ImagePicked;
		NativeToolkit.OnCameraShotComplete -= CameraShotComplete;
		NativeToolkit.OnContactPicked -= ContactPicked;
	}


	//=============================================================================
	// Button handlers
	//=============================================================================

	public void OnSaveScreenshotPress()
	{
		NativeToolkit.SaveScreenshot("MyScreenshot", "MyScreenshotFolder", "jpeg");
	}

	public void OnSaveImagePress()
	{
		NativeToolkit.SaveImage(texture, "MyImage", "png");
	}

	public void OnPickImagePress()
	{
		NativeToolkit.PickImage();
	}

	public void OnEmailSharePress()
	{
		NativeToolkit.SendEmail("Hello there", "<html><body><b>This is an email sent from my App!</b></body></html>", imagePath, "", "", "");
	}

	public void OnCameraPress()
	{
		NativeToolkit.TakeCameraShot();
	}

	public void OnPickContactPress()
	{
		NativeToolkit.PickContact();
	}

	public void OnShowAlertPress()
	{
		NativeToolkit.ShowAlert("Native Toolkit", "This is an alert dialog!", DialogFinished);
	}

	public void OnShowDialogPress()
	{
		NativeToolkit.ShowConfirm("Native Toolkit", "This is a confirm dialog!", DialogFinished);
	}

	public void OnLocalNotificationPress()
	{
		string message = "This is a local notification! This is a super long one to show how long we can make a notification. " +
						 "On Android this will appear as an extended notification.";
		NativeToolkit.ScheduleLocalNotification("Hello there", message, 1, 0, "sound_notification", true, "ic_notification", "ic_notification_large");
	}

	public void OnClearNotificationsPress()
	{
		NativeToolkit.ClearAllLocalNotifications ();
	}

	public void OnGetLocationPress()
	{
		console.text += "\nLongitude: " + NativeToolkit.GetLongitude ().ToString ();
		console.text += "\nLatitude: " + NativeToolkit.GetLatitude ().ToString ();
	}

	public void OnRateAppPress()
	{
		NativeToolkit.RateApp ("Rate This App", "Please take a moment to rate this App", "Rate Now", "Later", "No, Thanks", "343200656", AppRated);
	}

	//=============================================================================
	// Callbacks
	//=============================================================================

	void ScreenshotSaved(string path)
	{
		console.text += "\n" + "Screenshot saved to: " + path;
	}
	
	void ImageSaved(string path)
	{
		console.text += "\n" + texture.name + " saved to: " + path;
	}

	void ImagePicked(Texture2D img, string path)
	{
		imagePath = path;
		console.text += "\nImage picked at: " + imagePath;
		Destroy (img);
	}

	void CameraShotComplete(Texture2D img, string path)
	{
		imagePath = path;
		console.text += "\nCamera shot saved to: " + imagePath;
		Destroy (img);
	}

	void DialogFinished(bool result)
	{
		console.text += "\nDialog returned: " + result;
	}

	void AppRated(string result)
	{
		console.text += "\nRate this app result: " + result;
	}

	void ContactPicked(string name, string number, string email)
	{
		console.text +=  "\nContact Details:\nName:"+ name + ", number:" + number + ", email:" + email;
	}
}