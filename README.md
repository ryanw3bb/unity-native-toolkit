# Native Toolkit
Easily integrate native iOS & Android functionality into Unity projects.
* Pick images from camera roll
* Use device camera to take an image
* Save image to gallery
* Save screenshot to gallery
* Send email with attachments
* Retrieve a contact from contacts list
* Alert dialogs
* App rating
* Local notifications
* GPS data

## Usage
Add the files contained in Unity/Assets/ to your project then call the required static function from your code. For instance:
```csharp
public void OnSaveScreenshotPress()
{
	NativeToolkit.SaveScreenshot("MyScreenshot", "MyScreenshotFolder", "jpeg");
}
```

## Documentation
Detailed API documentation can be found here:
http://secondfury.com/nativetoolkit/

Android (Java) source located in Android/app/src/main/java/  
iOS (Obj-C) source located in Unity/Assets/Plugins/iOS/
