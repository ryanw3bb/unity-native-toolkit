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
* GPS data (lat/long as double)

## Usage
Add the files contained in Unity/Assets/ to your project then call the required static function from your code. For instance:
```csharp
public void OnSaveScreenshotPress()
{
	NativeToolkit.SaveScreenshot("MyScreenshot", "MyScreenshotFolder", "jpeg");
}
```
For iOS builds make sure you have Scripting Backend set to IL2CPP and Architecture set to Universal in Unity Build Settings.

For Android builds Write Access needs to be set to External (SDCard) to allow saving of images.

## Documentation
Detailed API documentation can be found here:
http://secondfury.com/nativetoolkit/

Android (Java) source located in Android/app/src/main/java/  
iOS (Obj-C) source located in iOS/NativeToolkit/

If rebuilding the iOS library be sure to add the location of UnityAppController.h to Xcode header paths, it's normally located in a subfolder of the Unity install directory.
